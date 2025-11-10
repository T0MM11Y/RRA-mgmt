package com.twm.mgmt.utils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.Security;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESUtil {

	private static final String AES_ALGORITHM = "AES";

	private static final Charset UTF_8 = StandardCharsets.UTF_8;

	public static String encryptStr(String keyStr, String str) throws Exception {
		Key aesKey = new SecretKeySpec(keyStr.getBytes(), AES_ALGORITHM);
		Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, aesKey);
		byte[] byteCipherText = cipher.doFinal(str.getBytes());
		return Base64.getEncoder().encodeToString(byteCipherText);
	}

	public static String decryptStr(String keyStr, String encryptedStr) throws Exception {
		Key aesKey = new SecretKeySpec(keyStr.getBytes(), AES_ALGORITHM);
		byte[] cipherText = Base64.getDecoder().decode(encryptedStr);
		Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, aesKey);
		byte[] decryptedText = cipher.doFinal(cipherText);
		return new String(decryptedText);
	}

	/**
	 * AES-256 加密
	 * 
	 * @param str
	 * @param key
	 * @param iv
	 * @return
	 * @throws Exception
	 */
	public static String encryptStr(String str, String key, String iv) throws Exception {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");

		cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key.getBytes(UTF_8), AES_ALGORITHM), new IvParameterSpec(iv.getBytes(UTF_8)));

		byte[] encryptData = cipher.doFinal(str.getBytes(UTF_8));

		return Base64.getEncoder().encodeToString(encryptData);
	}

	/**
	 * AES-256解密
	 * 
	 * @param str
	 * @param key
	 * @param iv
	 * @return
	 * @throws Exception
	 */
	public static String decryptStr(String str, String key, String iv) throws Exception {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");

		cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key.getBytes(UTF_8), AES_ALGORITHM), new IvParameterSpec(iv.getBytes(UTF_8)));

		byte[] decrypt = cipher.doFinal(Base64.getDecoder().decode(str));

		return new String(decrypt, UTF_8);
	}

}
