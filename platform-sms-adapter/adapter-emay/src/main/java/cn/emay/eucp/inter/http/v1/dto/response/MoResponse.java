package cn.emay.eucp.inter.http.v1.dto.response;

import java.io.Serializable;

/**
 * 上行数据
 * @author Frank
 *
 */
public class MoResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	private String mobile;// 手机号

	private String extendedCode; // 扩展码

	private String content;// 内容

	private String moTime;// 手机上行时间
	
	public MoResponse(){
		
	}
	
	public MoResponse(String mobile,String extendedCode,String content,String moTime){
		this.mobile = mobile;
		this.extendedCode = extendedCode;
		this.content = content;
		this.moTime = moTime;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getExtendedCode() {
		return extendedCode;
	}

	public void setExtendedCode(String extendedCode) {
		this.extendedCode = extendedCode;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getMoTime() {
		return moTime;
	}

	public void setMoTime(String moTime) {
		this.moTime = moTime;
	}

}
