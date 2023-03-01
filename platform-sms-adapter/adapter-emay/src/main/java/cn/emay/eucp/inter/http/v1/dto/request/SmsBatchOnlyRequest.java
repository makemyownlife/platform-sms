package cn.emay.eucp.inter.http.v1.dto.request;



/**
 * 批量短信发送参数
 * @author Frank
 *
 */
public class SmsBatchOnlyRequest extends SmsBaseRequest {

	private static final long serialVersionUID = 1L;

	/**
	 * 手机号与自定义SmsId
	 */
	private String[] mobiles;
	
	/**
	 * 短信内容
	 */
	private String content;
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String[] getMobiles() {
		return mobiles;
	}

	public void setMobiles(String[] mobiles) {
		this.mobiles = mobiles;
	}

}
