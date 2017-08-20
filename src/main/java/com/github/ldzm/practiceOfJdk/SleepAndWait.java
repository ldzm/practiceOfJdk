package com.github.ldzm.practiceOfJdk;

public class SleepAndWait {

	public static void main(String[] args) {

		new SleepAndWait().testWait();
	}

	/**
	 * wait 函数抛出的异常 
	 * 
	 * IllegalMonitorStateException - if the current thread is not
	 * the owner of the object's monitor.
	 * 
	 * InterruptedException - if any thread interrupted the current thread
	 * before or while the current thread was waiting for a notification. The
	 * interrupted status of the current thread is cleared when this exception
	 * is thrown.
	 */
	public void testWait() {

		Integer integer = new Integer(1);
		synchronized (integer) {
			try {

				// new Integer(1).wait(); // throw IllegalMonitorStateException
				integer.wait();

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("getNum");
	}
	/**
	 * sleep 抛出的异常
    * @throws  IllegalArgumentException
    *          if the value of {@code millis} is negative
    *
    * @throws  InterruptedException
    *          if any thread has interrupted the current thread. The
    *          <i>interrupted status</i> of the current thread is
    *          cleared when this exception is thrown.
    */
	public void testSleep() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
