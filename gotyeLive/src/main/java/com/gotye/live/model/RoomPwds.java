package com.gotye.live.model;

public class RoomPwds{

	private String anchorPwd;// 主播密码
	private String userPwd;// 用户登录密码
	private String assistPwd;// 助理密码

	public String getAnchorPwd() {
		return anchorPwd;
	}

	public void setAnchorPwd(String anchorPwd) {
		this.anchorPwd = anchorPwd;
	}

	public String getUserPwd() {
		return userPwd;
	}

	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}

	public String getAssistPwd() {
		return assistPwd;
	}

	public void setAssistPwd(String assistPwd) {
		this.assistPwd = assistPwd;
	}

}
