package com.gotye.live.model;

public class GetRoomResp {
	
	private String accessPath;
	
	private Integer runtime;
	
	private Long systime;
	
	private Short status;
	
	private Room entity;

	public String getAccessPath() {
		return accessPath;
	}

	public void setAccessPath(String accessPath) {
		this.accessPath = accessPath;
	}

	public Integer getRuntime() {
		return runtime;
	}

	public void setRuntime(Integer runtime) {
		this.runtime = runtime;
	}

	public Long getSystime() {
		return systime;
	}

	public void setSystime(Long systime) {
		this.systime = systime;
	}

	public Short getStatus() {
		return status;
	}

	public void setStatus(Short status) {
		this.status = status;
	}

	public Room getEntity() {
		return entity;
	}

	public void setEntity(Room entity) {
		this.entity = entity;
	}
}
