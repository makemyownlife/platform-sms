package cn.emay.sdk.core.dto.cache;

public class UrlDTO {

	public String appId;

	public String url;

	public UrlDTO() {

	}

	public UrlDTO(String appId, String url) {
		this.appId = appId;
		this.url = url;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
