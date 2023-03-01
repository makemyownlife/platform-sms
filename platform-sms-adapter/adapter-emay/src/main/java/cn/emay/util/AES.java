package cn.emay.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES加解密工具
 * 
 * @author Frank
 *
 */
public class AES {
	
	public final static String ALGORITHM_AEPP = "AES/ECB/PKCS5Padding";

	/**
	 * AES加密
	 * 
	 * @param content
	 *            内容
	 * @param password
	 *            密钥
	 * @param algorithm
	 *            算法
	 * @return 加密后数据
	 */
	public static byte[] encrypt(byte[] content, byte[] password, String algorithm) {
		if (content == null || password == null)
			return null;
		try {
			Cipher cipher = null;
			if (algorithm.endsWith("PKCS7Padding")) {
				cipher = Cipher.getInstance(algorithm, "BC");
			} else {
				cipher = Cipher.getInstance(algorithm);
			}
			cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(password, "AES"));
			return cipher.doFinal(content);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * AES解密
	 * 
	 * @param content
	 *            加密内容
	 * @param password
	 *            密钥
	 * @param algorithm
	 *            算法
	 * @return 解密后数据
	 */
	public static byte[] decrypt(byte[] content, byte[] password, String algorithm) {
		if (content == null || password == null)
			return null;
		try {
			Cipher cipher = null;
			if (algorithm.endsWith("PKCS7Padding")) {
				cipher = Cipher.getInstance(algorithm, "BC");
			} else {
				cipher = Cipher.getInstance(algorithm);
			}
			cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(password, "AES"));
			byte[] bytes = cipher.doFinal(content);
			return bytes;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * AES加密
	 * 
	 * @param content
	 *            内容
	 * @param password
	 *            密钥
	 * @param algorithm
	 *            算法
	 * @param ivStr
	 *            向量
	 * @return 加密后数据
	 */
	public static byte[] encrypt(byte[] content, byte[] password, byte[] ivStr, String algorithm) {
		if (content == null || password == null)
			return null;
		try {
			Cipher cipher = null;
			if (algorithm.endsWith("PKCS7Padding")) {
				cipher = Cipher.getInstance(algorithm, "BC");
			} else {
				cipher = Cipher.getInstance(algorithm);
			}
			IvParameterSpec iv = new IvParameterSpec(ivStr);
			cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(password, "AES"), iv);
			return cipher.doFinal(content);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * AES解密
	 * 
	 * @param content
	 *            加密内容
	 * @param password
	 *            密钥
	 * @param algorithm
	 *            算法
	 * @param ivStr
	 *            向量
	 * @return 解密后数据
	 */
	public static byte[] decrypt(byte[] content, byte[] password, byte[] ivStr, String algorithm) {
		if (content == null || password == null)
			return null;
		try {
			Cipher cipher = null;
			if (algorithm.endsWith("PKCS7Padding")) {
				cipher = Cipher.getInstance(algorithm, "BC");
			} else {
				cipher = Cipher.getInstance(algorithm);
			}
			IvParameterSpec iv = new IvParameterSpec(ivStr);
			cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(password, "AES"), iv);
			byte[] bytes = cipher.doFinal(content);
			return bytes;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
