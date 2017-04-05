package com.java.multithreadserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class ServerWorkerThread implements Runnable {
	Socket client = null;

	public ServerWorkerThread(Socket client) {
		this.client = client;
	}

	@Override
	public void run() {
		try {
			InputStreamReader isr = new InputStreamReader(this.client.getInputStream());
			BufferedReader buff = new BufferedReader(isr);
			String line = buff.readLine();
			while (!line.isEmpty()) {
				System.out.println(line);
				line = buff.readLine();
			}
			Thread.sleep(10000);
			OutputStream output = this.client.getOutputStream();
			String outputHttpHeader = "HTTP/1.1 200 OK \n\n";
			String data = "This is Multi-Threaded server";
			output.write((outputHttpHeader + data).getBytes("UTF-8"));
			isr.close();
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
