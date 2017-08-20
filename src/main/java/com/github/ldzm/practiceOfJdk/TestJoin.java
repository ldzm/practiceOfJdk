package com.github.ldzm.practiceOfJdk;

public class TestJoin {
	
	// i 每次修改都会写入内存
	public volatile static int i = 0;
	
	public static class IncreaseThread extends Thread {
		@Override
		public void run() {
			for (i = 0; i < 1000; i++) {
			}
		}
	}

	public static void main(String[] args) {
		
		IncreaseThread increaseThread = new IncreaseThread();
		increaseThread.start();
		try {
			// 在主函数中如果没有increaseThread.join()，那么得到的i可能是一个非常小的数或者是0
			// 因为increaseThread还没有开始执行主线程就已经执行完毕了。
			// 但是在加入at.join()表示主线程愿意等待increaseThread执行完毕之后，再执行主线程。

			increaseThread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(i);
	}
}
