package com.example.enums;

public enum QBQrCodePayReqPayPassEnum {

	TYPE_WX("1", "微信"), 
	TYPE_ZFB("2", "支付宝"), 
	;
	
	private String code;
	private String desc;

	private QBQrCodePayReqPayPassEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public String getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}

	public static String getDesc(String code) {
		QBQrCodePayReqPayPassEnum[] enums = values();
		for (QBQrCodePayReqPayPassEnum e : enums) {
			if (e.getCode().equals(code)) {
				return e.getDesc();
			}
		}
		return null;
	}
}
