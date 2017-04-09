package com.java.multithreadserver;

/*
 * Author: Anshuman Tripathi
 * 
 * Worker Threads that run in thread pool and handle http request response.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import com.java.httpelement.HttpRequest;
import com.java.httpelement.HttpResponse;
import com.java.httpelement.HttpResponse.StatusCode;

public class ServerWorkerThread implements Runnable {
	Socket client = null;

	public ServerWorkerThread(Socket client) {
		this.client = client;
	}

	@Override
	public void run() {
		try {
			HttpRequest request = HttpRequest.parseRequest(client.getInputStream());
			if(request != null){
				if(request.getMethod().equals("GET")){	//Server Accepts get request.
					System.out.println("Sending Response!");
//					File type Server handling files as given in root for the server
//					File f = new File(ServerCtx.getInstance().getServerRoot() + request.getPath());
					
					//Return HTTP response with status  Code
					HttpResponse response = new HttpResponse(StatusCode.OK).
							defaultHTMLResponse("<html><body>Response</body></html>"); 
					sendResponse(response);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//Return response to the HTTP request
	public void sendResponse(HttpResponse response){
		try {
			PrintWriter writer = new PrintWriter(this.client.getOutputStream());
			System.out.println("Reponse: \n"+response.toString());
			writer.write(response.toString());
			writer.flush();		// Keeps writing output stream thread safe
		} catch (IOException e) {
			System.err.println("Error Occured while sending repsonse.\n"+e.getMessage());
			e.printStackTrace();
		}
	}
}
