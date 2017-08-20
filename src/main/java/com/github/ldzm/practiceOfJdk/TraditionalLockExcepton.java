package com.github.ldzm.practiceOfJdk;

public class TraditionalLockExcepton implements Runnable {

	private int i = 0;

	public void run(){
		// TODO Auto-generated method stub
		synchronized (this) {
			while (true) {
				try {
					i++;
					Thread.sleep(1000);
					System.out.println(Thread.currentThread().getName() + "i=" + i);
					if (i == 2) {
						Integer.parseInt("a");
					}
				} catch (Exception e) {
					System.out.println("Log info i = " + i);
					// 运行时异常不需要显示抛出（throws）
				    throw new RuntimeException();
				}
			}
		}
	}
	
	public static void main(String[] args) {
		Thread thread = new Thread(new TraditionalLockExcepton());
		
		thread.start();
		
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}