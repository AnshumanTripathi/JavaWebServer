package com.java.threadpool;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class PoolCtx {
	private static PoolCtx instance = null;
	private static BlockingQueue<Runnable> taskQueue;
	private int poolSize;

	private PoolCtx() {
	}
	
	public static PoolCtx getInstance(){
		if(instance == null){
			synchronized (taskQueue) {
				if(instance == null){
					return new PoolCtx();
				}
			}
		}
		return instance;
	}

	public BlockingQueue<Runnable> getTaskQueue() {
		return PoolCtx.taskQueue;
	}

	public void setTaskQueue(BlockingQueue<Runnable> taskQueue) {
		taskQueue = new LinkedBlockingQueue<Runnable>(this.poolSize);
		PoolCtx.taskQueue = taskQueue;
	}

	public int getPoolSize() {
		return this.poolSize;
	}

	public void setPoolSize(int poolSize) {
		this.poolSize = poolSize;
	}
	
}
