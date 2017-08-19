package com.github.ldzm.practiceOfJdk;

import java.util.LinkedList;
import java.util.List;

public class UseLinkedListAsStackAndHeap {

	public static void main(String[] args) {
		final int NUM = 10;
		
		// 头插头删实现栈
		LinkedList<Integer> stack = new LinkedList<Integer>();
		for(int i = 0; i < NUM; i++) {
			stack.addFirst(i);
		}
		while(!stack.isEmpty()) {
			System.out.print(stack.removeFirst());
		}
		
		System.out.println();
		
		// 头插尾删实队列或者尾插头删
		LinkedList<Integer> queue = new LinkedList<Integer>();
		for(int i = 0; i < NUM; i++) {
			queue.addFirst(i);
		}
		while(!queue.isEmpty()) {
			System.out.print(queue.removeLast());
		}
		
	}
}
