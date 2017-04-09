package com.java.multithreadserver;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import com.java.httpelement.HttpRequest;
import com.java.httpelement.HttpResponse;
import com.java.httpelement.HttpResponse.StatusCode;
import com.java.serverctx.ServerCtx;

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
				if(request.getMethod().equals("GET")){
					File f = new File(ServerCtx.getInstance().getServerRoot() + request.getPath());
					HttpResponse response = new HttpResponse(StatusCode.OK).fileResponse(f);
					sendResponse(response);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendResponse(HttpResponse response){
		try {
			PrintWriter writer = new PrintWriter(this.client.getOutputStream());
			writer.write(response.toString());
			writer.flush();
		} catch (IOException e) {
			System.err.println("Error Occured while sending repsonse.\n"+e.getMessage());
			e.printStackTrace();
		}
	}
}
