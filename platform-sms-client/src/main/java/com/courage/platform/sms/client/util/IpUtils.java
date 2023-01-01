package com.courage.platform.sms.client.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class IpUtils {

	private static final Logger logger = LoggerFactory.getLogger(IpUtils.class);

	public static final String LOCAL_IP = getLocalIp();
	public static final String HOST_NAME = getLocalHostName();
	
	private static final String REGEX = "((25[0-5]|2[0-4]\\d|1\\d{2}|0?[1-9]\\d|0?0?\\d)\\.){3}(25[0-5]|2[0-4]\\d|1\\d{2}|0?[1-9]\\d|0?0?\\d)";
	
	private static final Pattern PATTERN = Pattern.compile(REGEX);
	
	private static final int ONE = 1;
	
	private static final int TWO = 2;
	
	private static final int THREE = 3;
	
	private static final int FOUR = 4;
	
	private static final int ZERO = 0;
	
	private static final int ONE_BYTE_LENGTH = 8;
	
	private static final int TWO_BYTE_LENGTH = 16;
	
	private static final int THREE_BYTE_LENGTH = 24;
    private static final Long MAX_ADDRESS_NUM = 4294967295L;
    private static final int OX00FFFFFF = 0x00ffffff;
    private static final int OX0000FFFF = 0x0000ffff;
    private static final int OX000000FF = 0x000000ff;

    private static final Long A_LOCAL_START = 167772160L;
    private static final Long A_LOCAL_END = 184549375L;
    private static final Long B_LOCAL_START = 2886729728L;
    private static final Long B_LOCAL_END = 2887778303L;
    private static final Long C_LOCAL_START = 3232235520L;
    private static final Long C_LOCAL_END = 3232301055L;

    private static Pattern p = Pattern.compile("(?<=//|)((\\w)+\\.)+\\w+");

    private IpUtils(){}

	/**
	 * 根据点分式IP地址返回十进制IP数值
	 *
	 */
	public static long ipToLongNum(String ip) {
		if (checkIpStr(ip)) {
			String[] ipStrArray = ip.split("\\.");
			Long[] ipLongArray = new Long[FOUR];
			for (int i = 0; i < ipStrArray.length; i++) {
				ipLongArray[i] = Long.parseLong(ipStrArray[i]);
			}
			return (ipLongArray[ZERO] << THREE_BYTE_LENGTH) + (ipLongArray[ONE] << TWO_BYTE_LENGTH) + (ipLongArray[TWO] << ONE_BYTE_LENGTH) + ipLongArray[THREE];
		} else {
			throw new IllegalArgumentException("点分式IP地址格式错误！");
		}
	}
	
	/**
	 * 根据十进制IP数值返回点分式IP地址
	 *
	 * @Author : yongwang.chen
	 * @Date   : 7:57 PM 24/05/2018
	 * @param addrNum 十进制IP数值
	 * @return 点分式IP地址
	 */
	public static String longNumToIp(Long addrNum) {
		if (addrNum >= 0L && addrNum <= MAX_ADDRESS_NUM) {
			Long[] ipLongArray = new Long[FOUR];
			ipLongArray[ZERO] = addrNum >> THREE_BYTE_LENGTH;
			ipLongArray[ONE] = (addrNum & OX00FFFFFF) >> TWO_BYTE_LENGTH;
			ipLongArray[TWO] = (addrNum & OX0000FFFF) >> ONE_BYTE_LENGTH;
			ipLongArray[THREE] = addrNum & OX000000FF;
			return StringUtils.join(ipLongArray, '.');
		} else {
			throw new IllegalArgumentException("十进制IP数值超出IP范围！");
		}
	}

	/**
	 * 获取本机ip地址
	 * 此方法为重量级的方法，不要频繁调用
	 * @Author : yongwang.chen
	 * @Date   : 7:57 PM 24/05/2018
	 * @return
	 */
	private static String getLocalIp() {

        try{
            //根据网卡取本机配置的IP
            Enumeration<NetworkInterface> netInterfaces=NetworkInterface.getNetworkInterfaces();
            String ip = null;
            a: while(netInterfaces.hasMoreElements()){
                NetworkInterface ni=netInterfaces.nextElement();
                Enumeration<InetAddress> ips = ni.getInetAddresses();
                while (ips.hasMoreElements()) {
                    InetAddress ipObj = ips.nextElement();
                    if (ipObj.isSiteLocalAddress()) {
                        ip =  ipObj.getHostAddress();
                        break a;
                    }
                }
            }
            return ip;
        }catch (Exception e){
            logger.error("", e);
            return null;
        }
	}

	/**
	 * 获取本地机器名
	 * 此方法为重量级的方法，不要频繁调用
	 * 一般耗时在百毫秒，缓存使用
	 * @Author : yongwang.chen
	 * @Date   : 7:57 PM 24/05/2018
	 * @return
	 */
	private static String getLocalHostName() {

        String hostName = null ;
        try {
            hostName = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            logger.error("get hostname error", e);
        }
        logger.info("get local hostName ：" + hostName );

        return hostName ;
	}
	
	/**
	 * 判断点分式IPV4格式是否正确
	 * @Author : yongwang.chen
	 * @Date   : 7:57 PM 24/05/2018
	 * @since 1.0
	 * @param ipAddress
	 * @return
	 */
	public static boolean checkIpStr(String ipAddress) {
	      Matcher m = PATTERN.matcher(ipAddress);
	      return m.matches();  
	}
	
	/**
	 * 判断点分式IP地址是否是内网IP
	 * @Date   : 7:57 PM 24/05/2018
	 * @since 3.2.9
	 * @param ipAddress
	 * @return
	 */
	public static boolean isLocalIp(String ipAddress) {
		if (!checkIpStr(ipAddress)) {
			return false;
		} else {
			Long ip = ipToLongNum(ipAddress);
			boolean isLocalA = (ip >= A_LOCAL_START && ip <= A_LOCAL_END);
			boolean isLocalB = (ip >= B_LOCAL_START && ip <= B_LOCAL_END);
			boolean isLocalC = (ip >= C_LOCAL_START && ip <= C_LOCAL_END);
			if (isLocalA || isLocalB || isLocalC) {
				return true;
			}
			return false;
		}
	}

    /**
     * 从url中抽取ip地址
     * http://1.1.1.1:8080/s/t/index.html
     * https://1.1.1.1:8080/s/t/index.html
     * @param url
     * @return
     */
    public static String extractIp(String url){
        if (StringUtils.isBlank(url)) {
            return null;
        }
        String remoteServiceIp = null;

        Matcher m = p.matcher(url.toString());
        if(m.find()){
            remoteServiceIp = m.group();
        }
        return remoteServiceIp;
    }
}
