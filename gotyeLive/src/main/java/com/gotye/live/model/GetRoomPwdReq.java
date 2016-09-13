package com.gotye.live.model;

public class GetRoomPwdReq {
	
	private Short role;

	private Long roomId;

	public Short getRole() {
		return role;
	}

	public void setRole(Short role) {
		this.role = role;
	}

	public Long getRoomId() {
		return roomId;
	}

	public void setRoomId(Long roomId) {
		this.roomId = roomId;
	}
}
