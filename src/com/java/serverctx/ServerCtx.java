package com.java.serverctx;

public class ServerCtx {
	private static ServerCtx instance = null;
	private String serverRoot;
	private int port;
	
	private ServerCtx(){}
	
	public static ServerCtx getInstance(){
		if(instance == null){
			synchronized (instance) {
				if(instance == null)
					return new ServerCtx();
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
