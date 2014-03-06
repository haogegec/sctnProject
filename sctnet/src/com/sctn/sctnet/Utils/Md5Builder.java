package com.sctn.sctnet.Utils;

import java.security.*;

public class Md5Builder {

	public static String getMd5(String s) {
		try {
			MessageDigest messagedigest = MessageDigest.getInstance("MD5");
			messagedigest.update(s.getBytes("UTF8"));
			byte abyte0[] = messagedigest.digest();
			String s1 = "";
			for (int i = 0; i < abyte0.length; i++) {
				String s2 = toHexString(0xff & abyte0[i] | 0xffffff00)
						.substring(6);
				s1 = s1 + s2;
			}

			return s1;
		} catch (Exception _ex) {
			return null;
		}
	}

	public static String toHexString(int i) {
		String str = toUnsignedString(i, 4);
		return str;
	}

	private static String toUnsignedString(int i, int shift) {
		char[] buf = new char[32];
		int charPos = 32;
		int radix = 1 << shift;
		int mask = radix - 1;
		do {
			buf[--charPos] = digits[i & mask];
			i >>>= shift;
		} while (i != 0);

		return new String(buf, charPos, (32 - charPos));
	}

	final static char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
			'9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l',
			'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y',
			'z' };

	public static void main(String[] args) {
		System.out.println(getMd5("c12e01f2a13ff5587e1e9e4aedb8242d"));
	}

}
