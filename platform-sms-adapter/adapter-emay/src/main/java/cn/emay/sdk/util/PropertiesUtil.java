package cn.emay.sdk.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 配置文件读取工具
 * 
 * @author 东旭
 *
 */
public class PropertiesUtil {

	/**
	 * 获取单一参数的值
	 * 
	 * @param key
	 * @param propertiesFilePath
	 * @return
	 */
	public static String getProperty(String key, String propertiesClassPath) {
		Properties properties = getProperties(propertiesClassPath);
		if (properties == null)
			return null;
		return properties.getProperty(key);
	}

	/**
	 * 获取单一参数的数值
	 * 
	 * @param key
	 * @param propertiesClassPath
	 * @param defaultValue
	 * @return
	 */
	public static int getIntProperty(String key, String propertiesClassPath, int defaultValue) {
		Properties properties = getProperties(propertiesClassPath);
		if (properties == null || !properties.containsKey(key))
			return defaultValue;
		int value = defaultValue;
		try {
			value = Integer.valueOf(properties.getProperty(key));
		} catch (Exception e) {
		}
		return value;
	}

	/**
	 * 获取单一参数的数值
	 * 
	 * @param key
	 * @param propertiesClassPath
	 * @param defaultValue
	 * @return
	 */
	public static long getLongProperty(String key, String propertiesClassPath, long defaultValue) {
		Properties properties = getProperties(propertiesClassPath);
		if (properties == null || !properties.containsKey(key))
			return defaultValue;
		long value = defaultValue;
		try {
			value = Long.valueOf(properties.getProperty(key));
		} catch (Exception e) {
		}
		return value;
	}

	/**
	 * 获取单一参数的布尔值
	 * 
	 * @param key
	 * @param propertiesClassPath
	 * @param defaultValue
	 * @return
	 */
	public static boolean getBooleanProperty(String key, String propertiesClassPath, boolean defaultValue) {
		Properties properties = getProperties(propertiesClassPath);
		if (properties == null || !properties.containsKey(key))
			return defaultValue;
		String valuep = properties.getProperty(key);
		if (valuep.equalsIgnoreCase("true") || valuep.equalsIgnoreCase("on") || valuep.equalsIgnoreCase("1"))
			return true;
		if (valuep.equalsIgnoreCase("false") || valuep.equalsIgnoreCase("off") || valuep.equalsIgnoreCase("0"))
			return false;
		return defaultValue;
	}

	public static float getFloatProperty(String key, String propertiesClassPath, float defaultValue) {
		Properties properties = getProperties(propertiesClassPath);
		if (properties == null || !properties.containsKey(key))
			return defaultValue;
		float value = defaultValue;
		try {
			value = Float.valueOf(properties.getProperty(key));
		} catch (Exception e) {
		}
		return value;
	}

	public static Date getDateProperty(String key, String propertiesClassPath, String format, Date defaultValue) {
		Properties properties = getProperties(propertiesClassPath);
		if (properties == null || !properties.containsKey(key))
			return defaultValue;
		Date date = DateUtil.parseDate(properties.getProperty(key), format);
		if (date == null)
			date = defaultValue;
		return date;
	}

	public static BigDecimal getBigDecimalProperty(String key, String propertiesClassPath, String format, BigDecimal defaultValue) {
		Properties properties = getProperties(propertiesClassPath);
		if (properties == null || !properties.containsKey(key))
			return defaultValue;
		BigDecimal value = defaultValue;
		try {
			value = new BigDecimal(properties.getProperty(key));
		} catch (Exception e) {
		}
		return value;
	}

	/**
	 * 获取批量参数的值
	 * 
	 * @param propertiesFilePath
	 * @return
	 */
	public static Map<String, String> getPropertys(String propertiesClassPath) {
		Map<String, String> map = new ConcurrentHashMap<String, String>();
		Properties properties = getProperties(propertiesClassPath);
		if (properties == null) {
			return map;
		}
		for (String name : properties.stringPropertyNames()) {
			map.put(name, properties.getProperty(name));
		}
		return map;
	}

	/**
	 * 获取原生的Properties
	 * 
	 * @Title: getProperties
	 * @Description: 获取原生的Properties
	 * @param propertiesFilePath
	 * @return Properties
	 */
	public static Properties getProperties(String propertiesClassPath) {
		Properties properties = new Properties();
		InputStream in = null;
		try {
			in = PropertiesUtil.class.getClassLoader().getResourceAsStream(propertiesClassPath);
			if(in != null){
				properties.load(in);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return properties;
	}

	/**
	 * 获取原生的Properties
	 * 
	 * @Title: getProperties
	 * @Description: 获取原生的Properties
	 * @param propertiesFilePath
	 * @return Properties
	 */
	public static Properties getPropertiesByFile(File file) {
		Properties properties = new Properties();
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			if(fis != null){
				properties.load(fis);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return properties;
	}

	/**
	 * 获取原生的Properties
	 * 
	 * @Title: getProperties
	 * @Description: 获取原生的Properties
	 * @param propertiesFilePath
	 * @return Properties
	 */
	public static Properties getPropertiesByFile(String propertiesFilePath) {
		Properties properties = new Properties();
		FileInputStream fis = null;
		try {
			File file = new File(propertiesFilePath);
			fis = new FileInputStream(file);
			if(fis != null){
				properties.load(fis);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return properties;
	}

	/**
	 * 获取单一参数的值
	 * 
	 * @param key
	 * @param propertiesFilePath
	 * @return
	 */
	public static String getPropertyByFile(String key, String propertiesFilePath) {
		Properties properties = getPropertiesByFile(propertiesFilePath);
		if (properties == null) {
			return null;
		}
		return properties.getProperty(key);
	}

	/**
	 * 获取批量参数的值
	 * 
	 * @param propertiesFilePath
	 * @return
	 */
	public static Map<String, String> getPropertysByFile(String propertiesFilePath) {
		Map<String, String> map = new ConcurrentHashMap<String, String>();
		Properties properties = getPropertiesByFile(propertiesFilePath);
		if (properties == null) {
			return map;
		}
		for (String name : properties.stringPropertyNames()) {
			map.put(name, properties.getProperty(name));
		}
		return map;
	}

}
