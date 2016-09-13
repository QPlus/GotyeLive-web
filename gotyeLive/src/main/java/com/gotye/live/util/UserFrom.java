package com.gotye.live.util;

public enum UserFrom {

	gotye((byte)1),
	//微信开发者平台
	qq((byte)2),
	//微信公众号
	wechat((byte)3);
	
	private Byte val;
	
	private UserFrom(Byte val){
		this.val = val;
	}
	public Byte getVal() {
		return val;
	}
}
