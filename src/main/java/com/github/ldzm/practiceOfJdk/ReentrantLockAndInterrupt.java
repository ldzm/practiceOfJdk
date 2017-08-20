package com.github.ldzm.practiceOfJdk;

import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockAndInterrupt implements Runnable {

	private int lock;
	// 创建重入锁
	private static ReentrantLock lock1 = new ReentrantLock();
	private static ReentrantLock lock2 = new ReentrantLock();

	public ReentrantLockAndInterrupt(int lock) {
		this.lock = lock;
	}

	public static void main(String[] args) throws InterruptedException {
		ReentrantLockAndInterrupt r1 = new ReentrantLockAndInterrupt(1);
		ReentrantLockAndInterrupt r2 = new ReentrantLockAndInterrupt(2);
		Thread t1 = new Thread(r1);
		Thread t2 = new Thread(r2);
		t1.start();
		t2.start();
		Thread.sleep(2000);
		// 中断一个线程
		t2.interrupt();

	}

	public void run() {
		try {
			if (lock == 1) {
				// 如果当前线程未中断则获取锁
				lock1.lockInterruptibly();
				Thread.sleep(2000);
				lock2.lockInterruptibly();
			} else {
				lock2.lockInterruptibly();
				Thread.sleep(1000);
				lock1.lockInterruptibly();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			// 查询当前线程是否持有锁
			if (lock1.isHeldByCurrentThread())
				lock1.unlock();
			if (lock2.isHeldByCurrentThread())
				lock2.unlock();
			System.out.println(Thread.currentThread().getId() + ":线程退出");
		}
	}
}
