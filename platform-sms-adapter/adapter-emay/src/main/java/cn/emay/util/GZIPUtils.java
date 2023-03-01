package cn.emay.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * GZIP 压缩工具
 * @author Frank
 *
 */
public class GZIPUtils {

	public static void main(String[] args) throws IOException {
		String sst = "hahahah";
		System.out.println(sst);
		System.out.println(System.currentTimeMillis());
		System.out.println("size:" + sst.length());
		byte[] bytes = sst.getBytes();
		System.out.println("length:" + bytes.length);
		System.out.println(System.currentTimeMillis());
		byte[] end = compress(bytes);
		System.out.println(System.currentTimeMillis());
		System.out.println("length:" + end.length);
		System.out.println(System.currentTimeMillis());
		byte[] start = decompress(end);
		System.out.println(System.currentTimeMillis());
		System.out.println("length:" + start.length);
		System.out.println(new String(start));
	}

	/**
	 * 数据压缩传输
	 * 
	 * @param is
	 * @param os
	 * @throws Exception
	 */
	public static void compressTransfe(byte[] bytes, OutputStream out) throws IOException {
		GZIPOutputStream gos = null;
		try {
			gos = new GZIPOutputStream(out);
			gos.write(bytes);
			gos.finish();
			gos.flush();
		} finally{
			if(gos != null){
				gos.close();
			}
		}
	}
	
	/**
	 * 数据压缩
	 * 
	 * @param is
	 * @param os
	 * @throws Exception
	 */
	public static byte[] compress(byte[] bytes) throws IOException {
		ByteArrayOutputStream out = null;
		GZIPOutputStream gos = null;
		try {
			out = new ByteArrayOutputStream();
			gos = new GZIPOutputStream(out);
			gos.write(bytes);
			gos.finish();
			gos.flush();
		} finally{
			if(gos != null){
				gos.close();
			}
			if(out != null){
				out.close();
			}
		}
		return out.toByteArray();
	}
	
	/**
	 * 数据解压
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static byte[] decompress(byte[] bytes) throws IOException {
		ByteArrayInputStream in = new ByteArrayInputStream(bytes);
		GZIPInputStream gin = new GZIPInputStream(in);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int count;
		byte data[] = new byte[1024];
		while ((count = gin.read(data, 0, 1024)) != -1) {
			out.write(data, 0, count);
		}
		out.flush();
		out.close();
		gin.close();
		return out.toByteArray();
	}

}
