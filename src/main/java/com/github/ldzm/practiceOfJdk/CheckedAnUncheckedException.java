package com.github.ldzm.practiceOfJdk;

import java.io.IOException;

public class CheckedAnUncheckedException {

	public void testCheckedException() throws IOException {
		
		// 已检查异常必须捕获或者抛出
		throw new IOException("Checked Exception.");
		
		// 猜测，除RuntimeException外，Exception下的子类都是已检查异常必捕获或者抛出
		//throw new InterruptedException();
	}
	
	public void testUncheckedException() throws Exception{
		throw new RuntimeException("Unchecked Exception.");
	}
	
	public void testException() throws Exception {
		// Exception 默认是已检查异常必须抛出
		throw new Exception("Unchecked Exception.");
	}
	
	public static void main(String[] args) {
		CheckedAnUncheckedException checkedAnUncheckedException = new CheckedAnUncheckedException();
		
		try {
			checkedAnUncheckedException.testCheckedException();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}
		
		System.out.println("after Checked Exception.");
		
		// 可以捕获也可以不捕获，如果不捕获异常，后面的“after Unchecked Exception”无法输出，因为程序终止
		try {
			checkedAnUncheckedException.testUncheckedException();
		} catch(Exception e) {
		}
		
		System.out.println("after Unchecked Exception.");
		
		try {
			checkedAnUncheckedException.testException();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}
		
		System.out.println("after Exception.");
	}
}
