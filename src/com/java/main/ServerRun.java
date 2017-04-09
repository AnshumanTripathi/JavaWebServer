package com.java.main;

import com.java.multithreadserver.MultithreadedServer;

public class ServerRun {
	public static void main(String[] args) {
		MultithreadedServer multi = new MultithreadedServer(8080,10);
		new Thread(multi).start();
		
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		multi.closeServer();
	}
}
