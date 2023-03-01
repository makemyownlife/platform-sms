package cn.emay.eucp.inter.http.v1.dto.response;

import java.io.Serializable;

public class ResponseData<T>  implements Serializable {

	private static final long serialVersionUID = 1L;

	private String code;
	
	private T data;
	
	public ResponseData(String code,T data){
		this.code = code;
		this.data = data;
	}

	public ResponseData(){
		
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
	
	
	
}
