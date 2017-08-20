package com.github.ldzm.practiceOfJdk;

/**
 * 线程中断并不会使线程立即退出，而是给线程发送一个通知。 告知目标线程需要退出，
 * 而目标线程接到通知后会如何处理，完全由目标线程决定。
 * 
 * @author ldzm
 *
 */
public class ThreadAndInterrupt {

	public static void main(String[] args) {

		// test1();
		test2();
	}

	public static void test1() {
		Thread t1 = new Thread() {
			@Override
			public void run() {
				while (true) {
					// 处理中断的代码
					if (Thread.currentThread().isInterrupted()) {
						break;
					}

					System.out.println("t1 is runing...");
				}

			}
		};
		t1.start();
		try {
			// Causes the currently executing thread to sleep
			// sleep是静态方法，不属于任何对象，使用t1.sleep(1000)并不是让t1线程休眠1s
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 虽然中断了线程但是如果没有做中断处理逻辑，
		// 即使被置上中断状态，这个中断也不会发生作用，只是做上一个标记。
		t1.interrupt();
	}

	public static void test2() {
		Thread t2 = new Thread() {
			@Override
			public void run() {
				while (true) {


					// 处理中断的代码
					if (Thread.currentThread().isInterrupted()) {
						System.out.println("Interrupt");
						break;
					}
					System.out.println("t2 is running...");
					
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						System.out.println("Sleeping");
						
						// Thread.sleep()方法会让当前线程休眠一段时间，
						// 并抛出一个InterruptedException.这个异常不是运行时异常，
						// 必须捕获并处理它。如果当前线程正在休眠时被中断就会抛出这个异常。
						// 并且会清除中断标记。所以在必要的时候我们必须在异常的处理中重新设置中断标记。
						Thread.currentThread().interrupt();
						e.printStackTrace();
					}
				}
			}
		};
		t2.start();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 虽然中断了线程但是如果没有做中断处理逻辑，
		// 即使被置上中断状态，这个中断也不会发生作用，只是做上一个标记。
		t2.interrupt();
	}
}
