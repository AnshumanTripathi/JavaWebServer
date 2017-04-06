package com.java.threadpool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ThreadPool {

	private BlockingQueue<Runnable> taskQueue;
	private List<PoolThread> threads = null;
	private Boolean stopped = false;

	public ThreadPool(int poolSize) {
		PoolCtx.getInstance().setPoolSize(poolSize);
		this.taskQueue = new LinkedBlockingQueue<Runnable>(poolSize);
		PoolCtx.getInstance().setTaskQueue(taskQueue);
		this.threads = new ArrayList<PoolThread>(poolSize);
		
		for(int i=0;i<threads.size();i++){
			threads.get(i).run();
		}
	}

	public synchronized void execute(Runnable task) {
		if (isStopped()) {
			throw new IllegalStateException("Thread Pool has Stopped");
		}
		try {
			taskQueue.put(task);
		} catch (InterruptedException e) {
			if (isStopped()) {
				System.err.println("Server has stopped");
			} else {
				System.err.println("Exception occured while executing task in thread pool.\n"+e.getMessage());
				e.printStackTrace();
			}
		}

	}

	private Boolean isStopped() {
		return stopped;
	}

}
