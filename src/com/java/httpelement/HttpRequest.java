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

	private static Map<String, String> headers = new HashMap<String, String>(); // Can
																				// be
																				// replaced
																				// in
																				// with
																				// a
																				// ConcurrentHashMap
																				// for
																				// further
																				// thread
																				// safety
	private static String method;
	private static String protocol;
	private static String path;
	private static List<String> body = new ArrayList<String>();

	private HttpRequest() {
	}

	// Parse and return HTTP Reponse from Client input stream
	public static HttpRequest parseRequest(InputStream in) throws IOException {
		HttpRequest request = new HttpRequest();
		BufferedReader buff = new BufferedReader(new InputStreamReader(in));
		String line = buff.readLine();

		// Check for protocol, HTTP method, and the url path
		String[] data = line.split(" ");

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
			line = buff.readLine();
			System.out.println(line);

			// Read all the header data and add it to the HashMap
			String[] headerData = line.split(": ");
			if (headerData.length != 2) {
				//Host contains "HostName:Port"
				if (headerData[0].equals("Host")) {
					System.out.println("Found host");
					headerData[1] = headerData[1] + ":" + headerData[2];
				}
				//The end of HTTP headers
				if (headerData[0].equals("")) {
					return request;
				} else {
					System.out.println("Header not parsed: " + headerData[0]);
					throw new IOException("Cannot Parse Headers. ");
				}
			}
			headers.put(headerData[0], headerData[1]);
		}
		
		//Read body of the the http request if present
		while (buff.ready()) {
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
		for (String key : headers.keySet()) {
			httpStringRequest += key + ": " + headers.get(key);
		}
		if (body != null && body.size() > 0) {
			for (String value : body)
				httpStringRequest += value + "\n";
		}
		return httpStringRequest;
	}

}
