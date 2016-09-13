package com.gotye.live.util;

public enum  LIVE_ROLE {
	/**
	 * 后台管理端
	 */
	admin(1),
	/**
	 * 发布端，主播端
	 */
	publisher(2),
	/**
	 * 助理
	 */
	assistant(3),
	/**
	 * 观看端。学生端
	 */
	visitor(4),
	/**
	 * 来自IM的观看端
	 */
	imvisitor(5),
	/**
	 * 虚拟token
	 */
	virtual(6);
	
	private short value;

	private LIVE_ROLE(Integer val) {
		this.value = val.shortValue();
	}

	public short getValue() {
		return value;
	}

	public void setValue(short value) {
		this.value = value;
	}
	
	
}
