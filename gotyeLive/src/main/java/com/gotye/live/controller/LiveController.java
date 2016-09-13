package com.gotye.live.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gotye.live.model.AccessTokenReq;
import com.gotye.live.model.AccessTokenResp;
import com.gotye.live.model.GetRoomPwdReq;
import com.gotye.live.model.GetRoomPwdResp;
import com.gotye.live.model.wechat.AccessToken;
import com.gotye.live.model.wechat.WeChatUserInfo;
import com.gotye.live.namegenerate.NameLib;
import com.gotye.live.service.WechatService;
import com.gotye.live.util.ApiCall;
import com.gotye.live.util.Config;
import com.gotye.live.util.CookiesUtil;
import com.gotye.live.util.LIVE_ROLE;

@Controller
public class LiveController {

	private String apiUrl = "https://livevip.com.cn/liveApi";

	private ObjectMapper mapper = new ObjectMapper();
	
	@Autowired
	private WechatService wechatService;
	
	private static AccessTokenResp appAccessToken=null;
	
	/**
	 * 页面进入
	 * @param req
	 * @param resp
	 * @param map
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/live/{roomId}")
	public String liveOnlineEnter(@PathVariable Long roomId,HttpServletRequest req, HttpServletResponse resp, ModelMap map) throws IOException {
		String path = req.getScheme()+"://"+req.getServerName() + ":" + req.getServerPort()+ req.getContextPath() + "/live";
		req.setAttribute("_path_", path);
		req.setAttribute("roomId", roomId);
		
		//微信appid
		req.setAttribute("wxJsApiAppId", Config.getWxJsApiAppId());
		
		String tokenKey = "room_token_"+roomId;
		//页面通过tokenkey从cookie中获得token
		req.setAttribute("token_key",tokenKey);
		String token_key_time = "room_token_" + roomId+"_time";
		
		String tokenTimeStr = CookiesUtil.getCookie(req, token_key_time);
		String token = CookiesUtil.getCookie(req, tokenKey);
		
		//如果cookie中已经存在token
		if (StringUtils.isNotEmpty(token) && StringUtils.isNotEmpty(tokenTimeStr)) {
			Long tokenTime = Long.parseLong(tokenTimeStr);
			//如果token未过期
			if((System.currentTimeMillis() - tokenTime) < 24 * 60 * 60 * 1000){
				return "live/live";
			}
		}
		//如果配置为微信登陆
		if(isWexin(req) && "1".equals(Config.getLoginType())){
			//微信登陆授权
			String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="+Config.getWxJsApiAppId()+"&redirect_uri=" + URLEncoder.encode(path.replace(":80","")+"/wechat/"+roomId,"UTF-8") + "&response_type=code&scope=snsapi_userinfo&state=weChatState2#wechat_redirect";
			resp.sendRedirect(url);
			return null;
		}
		token = accesstoken("","",roomId);
		CookiesUtil.setCookie(resp, "room_token_"+roomId, token);
		CookiesUtil.setCookie(resp, "room_token_" + roomId+"_time", new Date().getTime()+"");
		return "live/live";
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/live/wechat/{roomId}")
	public String wechat(@PathVariable Long roomId,HttpServletRequest req, HttpServletResponse resp, ModelMap map) throws JsonProcessingException {
		String code = req.getParameter("code");
		String nickName = "";
		String openId = "";
		if (!StringUtils.isEmpty(code)) {
			try {
				AccessToken accessToken = wechatService.getAccessToken(code);
				if (accessToken.getAccess_token() != null) {
					//获取微信用户信息
					WeChatUserInfo userInfo = wechatService.getUserInfo(accessToken.getAccess_token(), accessToken.getOpenid());
					// 确保授权之后不再重复授权
					CookiesUtil.setCookie(resp, "iswechatauth", "1");
					CookiesUtil.setCookie(resp, "weChatOpenId", userInfo.getOpenid());
					CookiesUtil.setCookie(resp, "headUrl", userInfo.getHeadimgurl());
					nickName = userInfo.getNickname();
					openId = userInfo.getOpenid();
					String token = accesstoken(nickName,openId,roomId);
					CookiesUtil.setCookie(resp, "room_token_"+roomId, token);
					CookiesUtil.setCookie(resp, "room_token_" + roomId+"_time", new Date().getTime()+"");
				}
			} catch (Exception e) {
				CookiesUtil.setCookie(resp, "iswechatauth", "2");
				e.printStackTrace();
			}
		} else {
			// 用户取消授权
			CookiesUtil.setCookie(resp, "iswechatauth", "2");
		}
		return "redirect:/" + "live/"+roomId;
	}
	
	
	/**
	 * 获取token
	 * @return
	 */
	private String accesstoken(String nickName,String account,Long roomId){
		String token = "";
		try {
			
			String url = apiUrl + "/AccessToken";
			String reqJson = "";
			String respStr = "";
			AccessTokenReq req = new AccessTokenReq();
			AccessTokenResp accessTokenResp;
			//获取app级别的token
			if(null == appAccessToken || System.currentTimeMillis() - appAccessToken.getSystime() > appAccessToken.getExpiresIn()*1000){
				req.setScope("app");
				req.setUserName(Config.getEmail());
				req.setPassword(Config.getPassword());
				reqJson = mapper.writeValueAsString(req);
				respStr = ApiCall.post(url, reqJson, null);
				accessTokenResp = mapper.readValue(respStr, AccessTokenResp.class);
				appAccessToken = accessTokenResp;
			}
			token = appAccessToken.getAccessToken();
			
			//获取房间密码
			url = apiUrl + "/GetRoomPwd";
			GetRoomPwdReq getRoomPwdReq = new GetRoomPwdReq();
			getRoomPwdReq.setRole(LIVE_ROLE.visitor.getValue());
			getRoomPwdReq.setRoomId(roomId);
			reqJson = mapper.writeValueAsString(getRoomPwdReq);
			Map<String, String> headers = new HashMap<String, String>();
			headers.put("Authorization", token);
			respStr = ApiCall.post(url, reqJson, headers);
			GetRoomPwdResp getRoomPwdResp = mapper.readValue(respStr, GetRoomPwdResp.class);
			String roomPassword = getRoomPwdResp.getEntity().getUserPwd();
			
			//获取房间级别的token
			url = apiUrl + "/AccessToken";
			req = new AccessTokenReq();
			req.setAccount(StringUtils.isEmpty(account)?UUID.randomUUID().toString().replace("-", ""):account);
			req.setNickName(StringUtils.isEmpty(nickName)?NameLib.generateName():nickName);
			req.setRoomId(roomId);
			req.setPassword(roomPassword);
			req.setScope("room");
			String secretKey = DigestUtils.md5Hex(roomId+roomPassword+Config.getAccessSecret()).toString();
			req.setSecretKey(secretKey);
			reqJson = mapper.writeValueAsString(req);
			respStr = ApiCall.post(url, reqJson, null);
			accessTokenResp = mapper.readValue(respStr, AccessTokenResp.class);
			token = accessTokenResp.getAccessToken();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return token;
	}
	
	/**
	 * 微信浏览器
	 * @param req
	 * @return
	 */
	private Boolean isWexin(HttpServletRequest req){
		String ua = req.getHeader("user-agent").toLowerCase();
	    if (ua.indexOf("micromessenger") > 0) {// 是微信浏览器
	        return true; 
	    }else{
	    	return false;
	    }
	}
}
