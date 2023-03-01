package cn.emay.util.http;

/**
 * https 参数
 * 
 * @author Frank
 *
 */
public class HttpsParams {

	private String password;// 密钥库密钥
	private String keyStorePath;// 密钥库文件地址
	private String trustStorePath;// 信任库文件地址
	private String algorithm;// 指定交换数字证书的加密标准:JKS

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getKeyStorePath() {
		return keyStorePath;
	}

	public void setKeyStorePath(String keyStorePath) {
		this.keyStorePath = keyStorePath;
	}

	public String getTrustStorePath() {
		return trustStorePath;
	}

	public void setTrustStorePath(String trustStorePath) {
		this.trustStorePath = trustStorePath;
	}

	public String getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}

}
