package com.github.ldzm.practiceOfJdk;

import java.util.Arrays;

public class TestHashCode {

	public static void main(String[] args) {
		
		System.out.println(Boolean.hashCode(true));
		System.out.println(Boolean.hashCode(false));
		System.out.println(Byte.hashCode((byte)100));
		System.out.println(Character.hashCode('F'));
		System.out.println(Short.hashCode((short)100));
		System.out.println(Integer.hashCode(100));
		System.out.println(Long.hashCode(100L));
		System.out.println(Float.hashCode(100.0f));
		System.out.println(Double.hashCode(100.0));
		System.err.println("hello".hashCode());
	}
}

class TestHash {
	private int num;
	private String string;
	private char[] chs;
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(chs);
		result = prime * result + num;
		result = prime * result + ((string == null) ? 0 : string.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TestHash other = (TestHash) obj;
		if (!Arrays.equals(chs, other.chs))
			return false;
		if (num != other.num)
			return false;
		if (string == null) {
			if (other.string != null)
				return false;
		} else if (!string.equals(other.string))
			return false;
		return true;
	}
}