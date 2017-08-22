package com.github.ldzm.practiceOfJdk.concurrent.threadPool;

import java.util.LinkedList;
import java.util.List;

/**
 * 代码来自博客：http://blog.csdn.net/touch_2011/article/details/6914468
 * 感谢博主“会画原型写需求的程序员”的无私奉献
 * 
 * 热心网友的回复： 
 * 1. 在构造器中不应该启动线程，这样会造成this溢出，可以定义一个start方法。
 * 2. 线程池与客户端是典型的生产者--消费者关系，可以考虑使用BlockingQueue来代替LinkedList，就不用去wait notify了。
 * 3. 单例模式没用同步。
 * 4. 如4楼说的那样，关闭消费者的同时要关闭生产者，线程池关闭就不应该允许客户端再提交任务
 * 线程池类，线程管理器：创建线程，执行任务，销毁线程，获取线程基本信息
 */
public final class ThreadPool {
	// 线程池中默认线程的个数为5
	private static int threadNums = 5;
	// 运行的线程
	private WorkThread[] runningThreads;
	// 已经处理的任务
	private static volatile int finishedTasks = 0;
	// 任务队列，作为一个缓冲,List线程不安全
	private List<Runnable> taskQueue = new LinkedList<Runnable>();
	private static ThreadPool threadPool;

	// 创建具有默认线程个数的线程池
	private ThreadPool() {
		this(5);
	}

	// 创建线程池,worker_num为线程池中工作线程的个数
	private ThreadPool(int threadNums) {
		ThreadPool.threadNums = threadNums;
		runningThreads = new WorkThread[threadNums];
		for (int i = 0; i < threadNums; i++) {
			runningThreads[i] = new WorkThread();
			runningThreads[i].start();// 开启线程池中的线程
		}
	}

	// 单态模式，获得一个默认线程个数的线程池
	public static ThreadPool getThreadPool() {
		return getThreadPool(ThreadPool.threadNums);
	}

	// 单态模式，获得一个指定线程个数的线程池,worker_num(>0)为线程池中工作线程的个数
	// worker_num<=0创建默认的工作线程个数
	public static ThreadPool getThreadPool(int threadNums) {
		if (threadNums <= 0)
			threadNums = ThreadPool.threadNums;
		if (threadPool == null)
			threadPool = new ThreadPool(threadNums);
		return threadPool;
	}

	// 执行任务,其实只是把任务加入任务队列，什么时候执行由线程池管理器决定
	public void execute(Runnable task) {
		synchronized (taskQueue) {
			taskQueue.add(task);
			taskQueue.notify();
		}
	}

	// 批量执行任务,其实只是把任务加入任务队列，什么时候执行由线程池管理器决定
	public void execute(Runnable[] task) {
		synchronized (taskQueue) {
			for (Runnable t : task)
				taskQueue.add(t);
			taskQueue.notify();
		}
	}

	// 批量执行任务,其实只是把任务加入任务队列，什么时候执行由线程池管理器决定
	public void execute(List<Runnable> task) {
		synchronized (taskQueue) {
			for (Runnable t : task)
				taskQueue.add(t);
			taskQueue.notify();
		}
	}

	// 销毁线程池,该方法保证在所有任务都完成的情况下才销毁所有线程，否则等待任务完成才销毁
	public void destroy() {
		while (!taskQueue.isEmpty()) {// 如果还有任务没执行完成，就先睡会吧
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		// 工作线程停止工作，且置为null
		for (int i = 0; i < threadNums; i++) {
			runningThreads[i].stopWorker();
			runningThreads[i] = null;
		}
		threadPool=null;
		taskQueue.clear();// 清空任务队列
	}
	
	// 评论的代码 ：线程池销毁的方法，没有考虑到taskQueue的同步问题。
	// 如果在你判断 taskQueue.isEmpty（）成功了，在你即将销毁工作线程时，
	// 有别的线程将任务加入到taskQueue里，怎么办？
	public void destroy2() {
		synchronized (taskQueue) {
			while (!taskQueue.isEmpty()) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			for (int i = 0; i < threadNums; i++) {
				runningThreads[i].stopWorker();
				runningThreads[i] = null;
			}
			threadPool = null;
			taskQueue.clear();
		}
	}

	// 返回工作线程的个数
	public int getWorkThreadNumber() {
		return threadNums;
	}

	// 返回已完成任务的个数,这里的已完成是只出了任务队列的任务个数，可能该任务并没有实际执行完成
	public int getFinishedTasknumber() {
		return finishedTasks;
	}

	// 返回任务队列的长度，即还没处理的任务个数
	public int getWaitTasknumber() {
		return taskQueue.size();
	}

	// 覆盖toString方法，返回线程池信息：工作线程个数和已完成任务个数
	@Override
	public String toString() {
		return "WorkThread number:" + threadNums + "  finished task number:"
				+ finishedTasks + "  wait task number:" + getWaitTasknumber();
	}

	/**
	 * 内部类，工作线程
	 */
	private class WorkThread extends Thread {
		// 该工作线程是否有效，用于结束该工作线程
		private boolean isRunning = true;

		/*
		 * 如果任务队列不空，则取出任务执行，若任务队列空，则等待
		 */
		@Override
		public void run() {
			Runnable r = null;
			while (isRunning) {// 注意，若线程无效则自然结束run方法，该线程就没用了
				synchronized (taskQueue) {
					System.out.println("TaskQueue size: " + taskQueue.size());
					while (isRunning && taskQueue.isEmpty()) {// 队列为空
						try {
							taskQueue.wait(20);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					if (!taskQueue.isEmpty())
						r = taskQueue.remove(0);// 取出任务
				}
				if (r != null) {
					// run的方法要进行一些异常捕获，不然任务中出现异常的话，会导致整个线程池无法使用功能
					try {
						r.run();// 执行任务, 进行异常捕获
					} catch (Exception e) {
						e.printStackTrace();
					}
					//r.run();
				}
				finishedTasks++;
				// System.out.println("Finished Tasks:" + finishedTasks);
				r = null;
			}
		}

		// 停止工作，让该线程自然执行完run方法，自然结束
		public void stopWorker() {
			// 评论的问题：还有你的stopWorker方法，你只是把isRunning = false ，
			// 但是已经释放锁的工作线程 没有被唤醒 将永远得不到锁，将会一直被阻塞。
			isRunning = false;
		}
	}
}
