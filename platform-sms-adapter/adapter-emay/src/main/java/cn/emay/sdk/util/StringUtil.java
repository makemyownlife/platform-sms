package cn.emay.sdk.util;

public class StringUtil {

	/**
	 * 判断是否为空
	 * 
	 * @param val
	 * @return
	 */
	public static boolean isEmpty(String val) {
		if (val == null || (val.trim()).length() == 0 || "null".equals(val.trim())) {
			return true;
		} else {
			return false;
		}
	}

}
