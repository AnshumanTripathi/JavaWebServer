package com.java.serverctx;

/*
 * Author: Anshuman Tripathi
 * 
 * Server Contextual Data that can be used during Server runtime
 */
public class ServerCtx {
	private static ServerCtx instance = null;
	private static Object threadSafe = new Object(); // Object that helps achieve singleton thread safety
	private String serverRoot;
	private int port;
	
	private ServerCtx(){}
	
	public static ServerCtx getInstance(){
		if(instance == null){
			synchronized (threadSafe) {
				if(instance == null)
					instance = new ServerCtx();
					return instance;
			}
		}
		return instance;
	}

	public String getServerRoot() {
		return serverRoot;
	}

	public void setServerRoot(String serverRoot) {
		this.serverRoot = serverRoot;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	
	
}
