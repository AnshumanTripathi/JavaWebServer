package com.java.threadpool;

/*
 * Author: Anshuman Tripathi
 * 
 * Implementation of PoolThreads that run in the thread pool and dequeue tasks from the taskQueue
 * 
 */

import java.util.concurrent.BlockingQueue;

public class PoolThread implements Runnable {
	private Boolean isStopped = false;
	private BlockingQueue<Runnable> queue = null;

	public PoolThread() {
		this.queue = PoolCtx.getInstance().getTaskQueue();
	}

	@Override
	public void run() {
		while (!isStopped()) {
			try {
				Runnable r = this.queue.remove();
				r.run();
			} catch (Exception e) {
				if (isStopped()) {
					System.err.println("Server Stopped!");
				} else {
					System.err.println("Exception Occured with thread execution.\n" + e.getMessage());
					System.err.println("Trying to keep the thread pool alive.");
				}
			}
		}
	}

	public Boolean isStopped() {
		return isStopped;
	}

	public synchronized void stopPoolThread() {
		this.isStopped = true;
		try {
			this.wait();
		} catch (InterruptedException e) {
			System.err.println("Exception Occured while Stopping thread pool.\n"+e.getMessage());
			e.printStackTrace();
		}
	}
}
