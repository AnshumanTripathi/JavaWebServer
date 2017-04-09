package com.java.httpelement;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpRequest {

	private static Map<String, String> headers = new HashMap<String, String>();
	private static String method;
	private static String protocol;
	private static String path;
	private static List<String> body = new ArrayList<String>(); 
	
	private HttpRequest() {
	}

	public static HttpRequest parseRequest(InputStream in) throws IOException {
		HttpRequest request = new HttpRequest();
		BufferedReader buff = new BufferedReader(new InputStreamReader(in));
		String line = buff.readLine();

		String[] data = line.split("");

		if (data.length != 3) {
			throw new IOException("Cannot Parse HTTP Request.");
		}

		if (!data[2].startsWith("HTTP/")) {
			throw new IOException("Server Accepts only HTTP requests.");
		}

		setMethod(data[0]);
		setPath(data[1]);
		setProtocol(data[2]);

		while (line != null && !line.isEmpty()) {
			System.out.println(line);
			line = buff.readLine();
			String[] headerData = line.split(":");
			if (headerData.length != 2) {
				throw new IOException("Cannot Parse Headers.");
			}
			headers.put(headerData[0], headerData[1]);
		}
		while(buff.ready()){
			line = buff.readLine();
			body.add(line);
		}
		return request;
	}

	public static Map<String, String> getHeaders() {
		return headers;
	}

	public static String getMethod() {
		return method;
	}

	public static void setMethod(String method) {
		HttpRequest.method = method;
	}

	public static String getProtocol() {
		return protocol;
	}

	public static void setProtocol(String protocol) {
		HttpRequest.protocol = protocol;
	}

	public static String getPath() {
		return path;
	}

	public static void setPath(String path) {
		HttpRequest.path = path;
	}
	
	@Override
	public String toString() {
		String httpStringRequest = method + "\n" + path + "\n" + protocol;
		for(String key: headers.keySet()){
			httpStringRequest += key + ": " + headers.get(key);
		}
		if(body != null && body.size() > 0){
			for(String value: body)
			httpStringRequest += value + "\n";
		}
		return httpStringRequest;
	}

}
