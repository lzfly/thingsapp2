package com.smartdevice.mode;

public enum HttpError {

	WRONG_PARAM_ERROR(1001, "操作失败");
	
	private int code = 0;
	private String info;
	
	HttpError(int code, String info){
		this.code = code;
		this.info = info;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
	
	
}
