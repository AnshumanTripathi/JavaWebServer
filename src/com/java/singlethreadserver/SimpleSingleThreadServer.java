package com.java.singlethreadserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleSingleThreadServer implements Runnable {

	private int serverPort = 8080;
	// private Thread current = null;
	private ServerSocket serverSocket = null;
	private boolean isServerStopped = false;

	public SimpleSingleThreadServer(int serverPort) {
		this.serverPort = serverPort;
	}

	@Override
	public void run() {
		openServerSocket();
		// synchronized (this) {
		// this.current = Thread.currentThread();
		// }
		while (!this.isServerStopped) {
			Socket client = null;
			try {
				client = this.serverSocket.accept();
			} catch (IOException e) {
				if (isServerStopped) {
					System.err.println("Server is Closed!");
				} else {
					System.err.println("IOException Occured while processing client request\n" + e.getMessage());
					e.printStackTrace();
				}
			}
			processClientRequest(client);
		}

	}

	public void openServerSocket() {
		try {
			this.serverSocket = new ServerSocket(this.serverPort);
			System.out.println("Server Started! Listening on Port " + this.serverPort);
		} catch (IOException e) {
			System.out.println("Exception Occured while starting server!\n" + e.getMessage());
		}
	}

	private void processClientRequest(Socket client) {
		try {
			InputStreamReader isr = new InputStreamReader(client.getInputStream());
			BufferedReader buff = new BufferedReader(isr);
			String line = buff.readLine();
			while (!line.isEmpty()) {
				System.out.println(line);
				line = buff.readLine();
			}
			Thread.sleep(10000);
			OutputStream output = client.getOutputStream();
			String outputHttpHeader = "HTTP/1.1 200 OK \n\n";
			String data = "This is Single threaded server";
			output.write((outputHttpHeader + data).getBytes("UTF-8"));
			isr.close();
			output.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void closeServer() {
		this.isServerStopped = true;
		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			System.out.println("Server Stopped!");
		}
	}

}
