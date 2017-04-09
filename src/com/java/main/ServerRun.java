package com.java.main;

/*
 * Author: Anshuman Tripathi
 * 
 * Main execution class or the server
 */

import com.java.multithreadserver.MultithreadedServer;

public class ServerRun {
	
	public static MultithreadedServer multi = null;
	
	public static void main(String[] args) {
		if (args.length == 0 || args[0].equals("-help") || args[0].equals("-h") || args.length > 3) {
			System.out.println("Usage:java -jar JavaServer.jar <port> <root> <thread pool size>");
		} else {
			//Start Server
			int port = Integer.parseInt(args[0]);
			String root = args[1];
			if (args[2] != null) {
				int threadPoolSize = Integer.parseInt(args[2]);
				multi = new MultithreadedServer(port, root, threadPoolSize);
			} else {
				multi = new MultithreadedServer(port, "root");
			}

			new Thread(multi).start();
			
			//Register Shutdown Hook for server shutdown using cmd+C
			Runtime.getRuntime().addShutdownHook(new Thread(){
				public void run(){
					multi.closeServer();
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
		}

	}
}
