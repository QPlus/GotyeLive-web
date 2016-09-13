package com.gotye.live.model;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Room{
	private Long roomId;
	@JsonIgnore
	private String roomKey;
	@JsonIgnore
	private Long appUserId;
	private String roomName;
	@JsonIgnore
	private Byte enableRecordFlag;
	@JsonIgnore
	private Byte permanentPlayFlag;
	private Long startPlayTime;
	private Long stopPlayTime;

	@JsonIgnore
	private String anchorPwd;// 主播密码
	@JsonIgnore
	private String userPwd;// 用户登录密码
	@JsonIgnore
	private String assistPwd;// 助理密码
	private String anchorDesc;// 主播描述
	private String contentDesc;// 内容描述
	private Byte isImRoom = 0;
	private Date dateCreate;
	private Date dateUpdate;

	@JsonIgnore
	private Byte enableLogoFlag;
	@JsonIgnore
	private String logoId;

	private String thirdRoomId; // 三方roomId. string类型

	private Long imRoomId;

	private Integer maxOnlineNum; // 允许最大人数
	/** 新加字段 **/
	private String firstIndustry; // 所属行业，大分类
	private String secondIndustry;// 所属行业，小分类
	private String creator; // 创建人
	private Short status; // 0-未开始 1-已删除 2-正在直播
	private Integer onlineNum;// 在线人数

	@JsonIgnore
	private List<String> attaInfo;

	public Long getRoomId() {
		return roomId;
	}

	public void setRoomId(Long roomId) {
		this.roomId = roomId;
	}

	public String getRoomKey() {
		return roomKey;
	}

	public void setRoomKey(String roomKey) {
		this.roomKey = roomKey;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public Byte getEnableRecordFlag() {
		return enableRecordFlag;
	}

	public void setEnableRecordFlag(Byte enableRecordFlag) {
		this.enableRecordFlag = enableRecordFlag;
	}

	public Byte getPermanentPlayFlag() {
		return permanentPlayFlag;
	}

	public void setPermanentPlayFlag(Byte permanentPlayFlag) {
		this.permanentPlayFlag = permanentPlayFlag;
	}

	public Long getStartPlayTime() {
		return startPlayTime;
	}

	public void setStartPlayTime(Long startPlayTime) {
		this.startPlayTime = startPlayTime;
	}

	public Long getStopPlayTime() {
		return stopPlayTime;
	}

	public void setStopPlayTime(Long stopPlayTime) {
		this.stopPlayTime = stopPlayTime;
	}

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

	public String getAnchorDesc() {
		return anchorDesc;
	}

	public void setAnchorDesc(String anchorDesc) {
		this.anchorDesc = anchorDesc;
	}

	public String getContentDesc() {
		return contentDesc;
	}

	public void setContentDesc(String contentDesc) {
		this.contentDesc = contentDesc;
	}

	public List<String> getAttaInfo() {
		return attaInfo;
	}

	public void setAttaInfo(List<String> attaInfo) {
		this.attaInfo = attaInfo;
	}

	public Long getAppUserId() {
		return appUserId;
	}

	public void setAppUserId(Long appUserId) {
		this.appUserId = appUserId;
	}

	public void setStartPlayDate(Date startPlayDate) {
		this.startPlayTime = startPlayDate.getTime();
	}

	public void setStopPlayDate(Date stopPlayDate) {
		this.stopPlayTime = stopPlayDate.getTime();
	}

	@JsonIgnore
	public Date getStartPlayDate() {
		if (startPlayTime != null && startPlayTime > 0) {
			return new Date(startPlayTime);
		}
		return null;
	}

	@JsonIgnore
	public Date getStopPlayDate() {
		if (stopPlayTime != null && stopPlayTime > 0) {
			return new Date(stopPlayTime);
		}
		return null;
	}

	public Date getDateCreate() {
		return dateCreate;
	}

	public void setDateCreate(Date dateCreate) {
		this.dateCreate = dateCreate;
	}

	public Date getDateUpdate() {
		return dateUpdate;
	}

	public void setDateUpdate(Date dateUpdate) {
		this.dateUpdate = dateUpdate;
	}

	public String getThirdRoomId() {
		return thirdRoomId;
	}

	public void setThirdRoomId(String thirdRoomId) {
		this.thirdRoomId = thirdRoomId;
	}

	public Byte getIsImRoom() {
		return isImRoom;
	}

	public void setIsImRoom(Byte isImRoom) {
		this.isImRoom = isImRoom;
	}

	public Long getImRoomId() {
		return imRoomId;
	}

	public void setImRoomId(Long imRoomId) {
		this.imRoomId = imRoomId;
	}

	public Integer getMaxOnlineNum() {
		return maxOnlineNum;
	}

	public void setMaxOnlineNum(Integer maxOnlineNum) {
		this.maxOnlineNum = maxOnlineNum;
	}

	/***************************** 新加字段 *************************/
	public String getFirstIndustry() {
		return firstIndustry;
	}

	public void setFirstIndustry(String firstIndustry) {
		this.firstIndustry = firstIndustry;
	}

	public String getSecondIndustry() {
		return secondIndustry;
	}

	public void setSecondIndustry(String secondIndustry) {
		this.secondIndustry = secondIndustry;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Short getStatus() {
		return status;
	}

	public void setStatus(Short status) {
		this.status = status;
	}

	public Integer getOnlineNum() {
		return onlineNum;
	}

	public void setOnlineNum(Integer onlineNum) {
		this.onlineNum = onlineNum;
	}

}
