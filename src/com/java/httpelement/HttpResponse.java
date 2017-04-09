package com.java.httpelement;

/*
 * Author: Anshuman Tripathi
 * 
 * HTTP Request which can be used with the servers
 */


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
	private static final String protocol = "HTTP/1.1";
	private static Map<String, String> headers = new HashMap<String, String>();
	private static String statusCode;
	private static byte[] body;

	public HttpResponse(String statusCode) {
		HttpResponse.statusCode = statusCode;
		headers.put("Date", new Date().toString());
	}

	//Generate Response with HTML File
	public HttpResponse fileResponse(File f) {
		if (f.isFile()) {
			try {
				// Read file
				FileInputStream fs = new FileInputStream(f);
				int lengthOfFileInBytes = fs.available();
				body = new byte[lengthOfFileInBytes];
				fs.read(body);
				fs.close();

				setContentLength(lengthOfFileInBytes);
				if (f.getName().endsWith("html") || f.getName().endsWith("htm")) {
					setContentType(ContentType.HTML);
				} else {
					//If file is not HTML type
					setContentType(ContentType.TEXT);
				}
			} catch (FileNotFoundException e) {
				System.err.println("File to respond not found");
				e.printStackTrace();
			} catch (IOException e) {
				System.err.println("Error Ocured while reading file\n" + e.getMessage());
				e.printStackTrace();
			}
			return this;
		} else {
			return new HttpResponse(StatusCode.NOT_IMPLEMENTED)
					.defaultHTMLResponse("<html><body>File not found!</body></html>");
		}
	}

	//Generate HTTP response using HTML string 
	public HttpResponse defaultHTMLResponse(String msg) {
		setContentLength(msg.getBytes().length);
		setContentType(ContentType.HTML);
		body = msg.getBytes();
		return this;
	}

	public void setContentLength(long value) {
		headers.put("Content-Length", String.valueOf(value));
	}

	public void setContentType(String value) {
		headers.put("Content-Type", value);
	}

	//HTTP response status codes
	public static class StatusCode {
		public static final String OK = "200 OK";
		public static final String NOT_FOUND = "404 Not Found";
		public static final String NOT_IMPLEMENTED = "501 Server Error";
	}

	//HTTP content type supported by the server
	public static class ContentType {
		public static final String TEXT = "text/plain";
		public static final String HTML = "text/html";
	}
	
	
	@Override
	public String toString() {
		String httpStringResponse = protocol+" "+ statusCode + "\n";
		
		for(String key: headers.keySet()){
			httpStringResponse +=  key +": " + headers.get(key) + "\n";
		}
		httpStringResponse += "\n"; 
		if(body!=null){
			httpStringResponse += new String(body);
		}
		
		return httpStringResponse;
		
	}
}
