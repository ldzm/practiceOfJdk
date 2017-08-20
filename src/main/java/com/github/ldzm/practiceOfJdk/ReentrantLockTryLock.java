package com.github.ldzm.practiceOfJdk;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

//限时等待锁
public class ReentrantLockTryLock implements Runnable {

	public static ReentrantLock lock = new ReentrantLock();

	public static void main(String[] args) {
		ReentrantLockTryLock reentrantLockTryLock = new ReentrantLockTryLock();
		Thread t1 = new Thread(reentrantLockTryLock);
		Thread t2 = new Thread(reentrantLockTryLock);
		t1.start();
		t2.start();
		
		ReentrantLockAndTryLock reentrantLockAndTryLock = new ReentrantLockAndTryLock(1);
		ReentrantLockAndTryLock reentrantLockAndTryLock2 = new ReentrantLockAndTryLock(2);
		Thread t3 = new Thread(reentrantLockAndTryLock);
		Thread t4 = new Thread(reentrantLockAndTryLock2);
		t3.start();
		t4.start();
		
	}

	public void run() {
		// 接收两个参数一个表示等待时常一个表示计时单位。
		// 由于占用锁的线程会持有锁的时常长达35毫秒,
		// 而另一个线程无法在30毫秒的等带时间获取锁 请求锁失败
		try {
			if (lock.tryLock(30, TimeUnit.MILLISECONDS)) {
				System.out.println(Thread.currentThread().getId() + " get lock successfully");
				Thread.sleep(35);
			} else {

				System.out.println(Thread.currentThread().getId() + " get lock failed");
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			if (lock.isHeldByCurrentThread())
				lock.unlock();
		}
	}
}

class ReentrantLockAndTryLock implements Runnable {
	
	public static ReentrantLock lock1 = new ReentrantLock();
	public static ReentrantLock lock2 = new ReentrantLock();
	private int lock;
	
	
	public ReentrantLockAndTryLock(int lock) {
		this.lock = lock;
	}

	// 问题：线程1永远获取lock1，线程2永远获取lock2
	// 线程1想要执行lock2中的操作无法执行，这是死锁吗？？？？？？
	public void run() {
		// TODO Auto-generated method stub
		if(lock == 1) {
			while(true) {
				if(lock1.tryLock()) {
					try {
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if (lock2.tryLock()) {
							try {
								
							} finally{
								System.out.println(Thread.currentThread().getId() + " exit. 1 1");
								lock2.unlock();
							}
						}
					} finally {
						System.out.println(Thread.currentThread().getId() + " exit. 1 2");
						lock1.unlock();
					}
				}
			}
		} else {
			while(true) {
				if(lock2.tryLock()) {
					try {
						try {
							Thread.sleep(300);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if (lock1.tryLock()) {
							try {
								
							} finally{
								System.out.println(Thread.currentThread().getId() + " exit. 2 1");
								lock1.unlock();
							}
						}
					} finally {
						System.out.println(Thread.currentThread().getId() + " exit. 2 2");
						lock2.unlock();
					}
				}
			}
		}
	}
}

















