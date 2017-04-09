package com.java.multithreadserver;

/*
 * Author: Anshuman Tripathi 
 * 
 * Implementation of Multithreaded Thread Pooled Server.
 * MultithreadedServer uses FixedThreadPool from Java's Executor Service.
 */

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

	public MultithreadedServer(int serverPort, String serverRoot) {
		ServerCtx.getInstance().setPort(serverPort);
		ServerCtx.getInstance().setServerRoot(serverRoot);
		this.threadPool = Executors.newFixedThreadPool(10); //Default thread pool size is 10 if none is provided
	}

	public MultithreadedServer(int serverPort, String root, int threadPoolSize) {
		this.threadPool = Executors.newFixedThreadPool(threadPoolSize);
		ServerCtx.getInstance().setServerRoot(root);
		ServerCtx.getInstance().setPort(serverPort);
	}
	
	
	@Override
	public void run() {
		startServer();  	//Initalize Server Socket
		while (!isClosed()) {
			Socket client = null;
			try {
				client = this.serverSocket.accept(); // Accept Client Sockets 
			} catch (IOException e) {
				if (this.isClosed) {	// When server is turned off and thread runs asynchronously 
					System.out.println("\nServer Closed!");
				} else {
					System.out.println("Exception Occured with client thread!\n" + e.getMessage());
					e.printStackTrace();
				}
			}
			if (client != null) {
				this.threadPool.execute(new ServerWorkerThread(client));	//Execute Tasks in thread pool
			}
		}
		System.out.println("Closing thread pool."); 
		
		//All tasks completed, close thread pool
		this.threadPool.shutdown();
		closeServer();
	}

	// Server State
	private boolean isClosed() {
		return this.isClosed;
	}

	private void startServer() {
		try {
			this.serverSocket = new ServerSocket(ServerCtx.getInstance().getPort()); // Start Server Socket at given port
			System.out.println("Mulithreaded Server Started on port "+ServerCtx.getInstance().getPort());
		} catch (IOException e) {
			System.err.println("Exception Occured while starting server!\n" + e.getMessage());
		}
	}

	//Thread safe closeServer, if other threads try to close server
	public synchronized void closeServer() {
		this.isClosed = true;
		try {
			this.serverSocket.close();
		} catch (IOException e) {
			System.err.println("Exception Occured while closing Server!\n" + e.getMessage());
		} catch (NullPointerException e) { //If server is closed too fast
			try {
				System.err.println("Server Socket not responding. Waiting for Server Socket to respond.");
				Thread.sleep(100);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
	}
}
