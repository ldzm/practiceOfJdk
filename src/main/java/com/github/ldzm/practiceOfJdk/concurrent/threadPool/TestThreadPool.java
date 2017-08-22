package com.github.ldzm.practiceOfJdk.concurrent.threadPool;

//测试线程池
public class TestThreadPool {
	public static void main(String[] args) {
		// 创建3个线程的线程池
		ThreadPool t = ThreadPool.getThreadPool(3);
		t.execute(new Runnable[] { new Task(), new Task(), new Task(), new Task() });
		t.execute(new Runnable[] { new Task(), new Task(), new Task() });
		t.execute(new Runnable[] { new Task(), new Task(), new Task() });
		
		System.out.println("Main方法中调用destroy()前的输出" + t);
		t.destroy();// 所有线程都执行完成才destory
		System.out.println("Main方法中调用destroy()后的输出" + t);
	}

	// 任务类
	static class Task implements Runnable {
		private static volatile int i = 1;

		public synchronized static int increaseI() {
			i++;
			
			return i;
		}
		
		@Override
		public void run() {// 执行任务

			// 如果异常导致线程池无法正常退出
			// int a = 1 / 0;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("任务" + increaseI() + " 完成");
		}
	}
}
