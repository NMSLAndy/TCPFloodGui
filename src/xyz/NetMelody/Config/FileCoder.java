package xyz.NetMelody.Config;

public class FileCoder {

	public String encrypt(String input) {
		return this.xorToCrypt(input);
	}

	public String decrypt(String input) {
		return this.xorToCrypt(input);
	}

	private String xorToCrypt(String input) {
		char[] chars = input.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			chars[i] = (char) (chars[i] ^ (((2 << 64) ^ 4 >> (32 & 2)) * 16));
		}
		String crypted = String.valueOf(chars);
		return crypted;
	}

}
