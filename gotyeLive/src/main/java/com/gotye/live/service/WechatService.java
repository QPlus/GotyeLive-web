package com.gotye.live.service;

import java.io.IOException;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gotye.live.model.wechat.AccessToken;
import com.gotye.live.model.wechat.JsApiAccessToken;
import com.gotye.live.model.wechat.JsapiTicket;
import com.gotye.live.model.wechat.WeChatUserInfo;
import com.gotye.live.util.Config;
import com.gotye.live.util.HttpUtils;

/**
 * 微信公众号
 * @author xunbing
 *
 */
@Service
public class WechatService {
	
	private ObjectMapper mapper = new ObjectMapper();
	
	public AccessToken getAccessToken(String code) throws JsonParseException, JsonMappingException, IOException {
		
		String param = "appid=" + Config.getWxJsApiAppId() + "&secret=" + Config.getWxJsApiSecret() + "&code=" + code + "&grant_type=authorization_code";
		
		String respstr = HttpUtils.sendPost("https://api.weixin.qq.com/sns/oauth2/access_token", param);
		
		AccessToken accessToken = mapper.readValue(respstr, AccessToken.class);
		
		return accessToken;
	}
	
	public WeChatUserInfo getUserInfo(String accessToken, String openId) throws JsonParseException, JsonMappingException, IOException{
		
		String param = "access_token=" + accessToken + "&openid=" + openId + "&lang=zh_CN";
		
		String respstr = HttpUtils.sendPost("https://api.weixin.qq.com/sns/userinfo", param);
		
		WeChatUserInfo userInfo = mapper.readValue(respstr, WeChatUserInfo.class);
		
		return userInfo;	
	}
	
	public JsApiAccessToken getJsApiAccessToken() throws JsonParseException, JsonMappingException, IOException {
		
		String param = "grant_type=client_credential&appid="+Config.getWxJsApiAppId()+"&secret="+Config.getWxJsApiSecret();
		
		String respstr = HttpUtils.sendGet("https://api.weixin.qq.com/cgi-bin/token", param);
		
		JsApiAccessToken jsApiAccessToken = mapper.readValue(respstr, JsApiAccessToken.class);
		
		return jsApiAccessToken;
	}
	
	public JsapiTicket getJsapiTicket(String accessToken) throws JsonParseException, JsonMappingException, IOException{
		
		String param = "access_token=" + accessToken + "&type=jsapi";
		
		String respstr = HttpUtils.sendGet("https://api.weixin.qq.com/cgi-bin/ticket/getticket", param);
		
		JsapiTicket jsapiTicket = mapper.readValue(respstr, JsapiTicket.class);
		
		return jsapiTicket;
	}
}
