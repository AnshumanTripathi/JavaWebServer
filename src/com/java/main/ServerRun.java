package com.java.main;

import com.java.multithreadserver.MultithreadedServer;

public class ServerRun {
	public static void main(String[] args) {
		MultithreadedServer multi = new MultithreadedServer(8080);
		new Thread(multi).start();
		
		try {
			Thread.sleep(100000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		multi.closeServer();
	}
}
