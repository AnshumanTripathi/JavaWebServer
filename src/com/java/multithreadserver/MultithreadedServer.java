package com.java.multithreadserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.java.serverctx.ServerCtx;

public class MultithreadedServer implements Runnable {

	private ServerSocket serverSocket = null;
	private boolean isClosed = false;
	private ExecutorService threadPool;

	public MultithreadedServer(int serverPort, int threadPoolSize) {
		this.threadPool = Executors.newFixedThreadPool(threadPoolSize);
		ServerCtx.getInstance().setPort(serverPort);
	}

	public MultithreadedServer(int serverPort, String root, int threadPoolSize) {
		this.threadPool = Executors.newFixedThreadPool(threadPoolSize);
		ServerCtx.getInstance().setServerRoot(root);
		ServerCtx.getInstance().setPort(serverPort);
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
				this.threadPool.execute(new ServerWorkerThread(client));
			}
		}
		System.out.println("Closing thread pool.");
		this.threadPool.shutdown();
	}

	private boolean isClosed() {
		return this.isClosed;
	}

	private void startServer() {
		try {
			this.serverSocket = new ServerSocket(ServerCtx.getInstance().getPort());
			System.out.println("Mulithreaded Server Started on port "+ServerCtx.getInstance().getPort());
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
