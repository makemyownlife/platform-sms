package cn.emay.util;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/**
 * json util 
 * 
 * @author 东旭
 *
 */
public class JsonHelper {

	private static Map<String,Gson> gsons = new HashMap<String, Gson>();

	private static String DEFAULT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
	
	static {
		gsons.put(DEFAULT_DATE_PATTERN, createGson(DEFAULT_DATE_PATTERN));
	}
	
	private static Gson createGson(String datePattern){
		return  new GsonBuilder().setDateFormat(datePattern).disableHtmlEscaping().serializeNulls().create();
	}
	
	public static Gson getGson() {
		return gsons.get(DEFAULT_DATE_PATTERN);
	}

	public static Gson getGson(String datePattern) {
		Gson gson = gsons.get(datePattern);
		if(gson == null){
			gson = createGson(datePattern);
			gsons.put(datePattern, gson);
		}
		return gson;
	}
	
	public static GsonBuilder newGsonBuilder() {
		return new GsonBuilder();
	}

	/**
	 * 将对象转换为json串
	 * 
	 * @param obj
	 * @return
	 */
	public static String toJsonString(Object obj) {
		if (obj == null) {
			return null;
		}
		return getGson().toJson(obj);
	}

	/**
	 * 将对象转换为json串，自定义日期转换规则
	 * 
	 * @param obj
	 * @param datePattern
	 * @return
	 */
	public static String toJsonString(Object obj, String datePattern) {
		if (obj == null) {
			return null;
		}
		return getGson(datePattern).toJson(obj);
	}

	/**
	 * 将json串转换为对象
	 * 
	 * @param clazz
	 * @param jsonString
	 * @return
	 */
	public static <T> T fromJson(Class<T> clazz, String jsonString) {
		if (jsonString == null) {
			return null;
		}
		return getGson().fromJson(jsonString, clazz);
	}
	
	/**
	 * 将json串转换为对象
	 * 
	 * @Type type
	 * @param jsonString
	 * @return
	 */
	public static <T> T fromJson(TypeToken<T> token, String jsonString) {
		if (jsonString == null) {
			return null;
		}
		return getGson().fromJson(jsonString, token.getType());
	}
	
	/**
	 * 将json串转换为对象
	 * 
	 * @Type type
	 * @param jsonString
	 * @return
	 */
	public static <T> T fromJson(TypeToken<T> token, String jsonString, String datePattern) {
		if (jsonString == null) {
			return null;
		}
		return getGson(datePattern).fromJson(jsonString, token.getType());
	}

	/**
	 * 将json串转换为对象
	 * 
	 * @param clazz
	 * @param jsonString
	 * @return
	 */
	public static <T> T fromJson(Class<T> clazz, String jsonString, String datePattern) {
		if (jsonString == null) {
			return null;
		}
		return getGson(datePattern).fromJson(jsonString, clazz);
	}

}
