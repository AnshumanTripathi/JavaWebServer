package com.java.multithreadserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MultithreadedServer implements Runnable {

	private ServerSocket serverSocket = null;
	private int serverPort;
	private boolean isClosed = false;

	public MultithreadedServer(int serverPort) {
		this.serverPort = serverPort;
	}

	@Override
	public void run() {
		startServer();
		while (!isClosed()) {
			Socket client = null;
			try {
				client = this.serverSocket.accept();
			} catch (IOException e) {
				if (this.isClosed) {
					System.out.println("Server Closed!");
				} else {
					System.out.println("Exception Occured with client thread!\n" + e.getMessage());
					e.printStackTrace();
				}
			}
			if (client != null) {
				new Thread(new ServerWorkerThread(client)).start();
			}
		}
	}

	private boolean isClosed() {
		return this.isClosed;
	}

	private void startServer() {
		try {
			this.serverSocket = new ServerSocket(this.serverPort);
			System.out.println("Mulithreaded Server Started on port "+this.serverPort);
		} catch (IOException e) {
			System.err.println("Exception Occured while starting server!\n" + e.getMessage());
		}
	}

	public synchronized void closeServer() {
		this.isClosed = true;
		try {
			this.serverSocket.close();
		} catch (IOException e) {
			System.err.println("Exception Occured while closing Server!\n" + e.getMessage());
		}
	}
}
