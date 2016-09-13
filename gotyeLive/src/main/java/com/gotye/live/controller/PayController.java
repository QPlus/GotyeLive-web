package com.gotye.live.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.beecloud.BCCache;
import cn.beecloud.BCEumeration.PAY_CHANNEL;
import cn.beecloud.BCPay;
import cn.beecloud.BCUtil;
import cn.beecloud.BeeCloud;
import cn.beecloud.bean.BCException;
import cn.beecloud.bean.BCOrder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gotye.live.model.SendMsgReq;
import com.gotye.live.util.ApiCall;
import com.gotye.live.util.Config;
import com.gotye.live.util.CookiesUtil;

@Controller
public class PayController {

	 private String apiUrl = "https://livevip.com.cn/liveApi";
	
	 private ObjectMapper mapper = new ObjectMapper();
	   
	   static {
		   //初始化beecloud
		   BeeCloud.registerApp(Config.getBeeCloudAppId(),Config.getBeeCloudTestSecret(),Config.getBeeCloudAppSecret(),Config.getBeeCloudMasterSecret());
		   if(Config.getBeeCloudSandbox().equals("true")){
			   //测试模式
			   BeeCloud.setSandbox(true); 
		   }
	   }
		
	   boolean verify(String sign, String text, String key, String input_charset) {
	        text = text + key;
	        String mysign = DigestUtils.md5Hex(getContentBytes(text, input_charset));
	        long timeDifference = System.currentTimeMillis() - Long.valueOf(key);
	        if (mysign.equals(sign) && timeDifference <= 300000) {
	            return true;
	        } else {
	            return false;
	        }
	    }

	   byte[] getContentBytes(String content, String charset) {
	       if (charset == null || "".equals(charset)) {
	           return content.getBytes();
	       }
	       try {
	           return content.getBytes(charset);
	       } catch (UnsupportedEncodingException e) {
	           throw new RuntimeException("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + charset);
	       }
	   }
	   
	   boolean verifySign(String sign, String timestamp) {
	       return verify(sign, BCCache.getAppID() + BCCache.getAppSecret(),
	               timestamp, "UTF-8");
	   }
	   
	   /**
	    * 支付完成回调
	    * @param request
	    * @param response
	    * @throws ServletException
	    * @throws IOException
	    */
	   @RequestMapping(method = RequestMethod.POST, value = "/pay/payover")
	   public void payOver(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		    StringBuffer json = new StringBuffer();
		    String line = null;
		    try {
		        BufferedReader reader = request.getReader();
		        while ((line = reader.readLine()) != null) {
		            json.append(line);
		        }
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		    JSONObject jsonObj = JSONObject.fromObject(json.toString());
		    String sign = jsonObj.getString("sign");
		    String timestamp = jsonObj.getString("timestamp");
		    boolean status = verifySign(sign, timestamp);
		    if (status) { //验证成功
		    	JSONObject optionalJson = JSONObject.fromObject(jsonObj.getString("optional"));
		  		try{
			    	String giftId = optionalJson.getString("giftId");
			    	if(!StringUtils.isEmpty(giftId)){
			    		//发送消息通知聊天页面
			           sendAdminMsg("{\"nickName\":\""+ optionalJson.getString("nickName")  +"\","+"\"giftName\":\""+ (optionalJson.getString("giftName")) + "\",\"giftPicUrl\":\"" + optionalJson.getString("giftPicUrl") + "\",\"notifyType\":\"5\"}",optionalJson.getString("token"));
			           response.getWriter().println("success");
			           return;
		    		}
		    	}
		  		catch(Exception e){
				}
		    } else { //验证失败
		    	response.getWriter().println("fail");
		    }
	   }
	   
	   	/**
	   	 * beecloud微信支付
	   	 * @param role
	   	 * @param req
	   	 * @param resp
	   	 * @param map
	   	 * @return
	   	 * @throws Exception
	   	 */
	    @RequestMapping(method = RequestMethod.POST, value = "/live/wxjsapipay/{roomId}")
		public @ResponseBody Map<String, Object> wxjsapipay(@PathVariable Long roomId,HttpServletRequest req, HttpServletResponse resp, ModelMap map) throws Exception {
			Map<String, Object> result = new HashMap<String, Object>();
			Map<String,Object> optional = new HashMap<String,Object>();
			String giftId = req.getParameter("giftId");
			if(!StringUtils.isEmpty(giftId)){
				optional.put("giftId", giftId);
			}
			String giftName = req.getParameter("giftName");
			if(!StringUtils.isEmpty(giftName)){
				optional.put("giftName", giftName);
			}
			String giftPicUrl = req.getParameter("giftPicUrl");
			if(!StringUtils.isEmpty(giftPicUrl)){
				optional.put("giftPicUrl", giftPicUrl);
			}
			String token = CookiesUtil.getCookie(req, "room_token_"+roomId);
			optional.put("token", token);
			String billNo = BCUtil.generateRandomUUIDPure();
			Float amountF = Float.valueOf(req.getParameter("amount"))*100; 
			Integer amount = amountF.intValue();
			BCOrder bcOrder = new BCOrder(PAY_CHANNEL.WX_JSAPI, amount, billNo, "亲加视频直播打赏");
			bcOrder.setBillTimeout(360);
			String openId = CookiesUtil.getCookie(req, "weChatOpenId");
			if(StringUtils.isEmpty(openId)){
				openId = req.getParameter("openId");
			}
			bcOrder.setOpenId(openId);
			bcOrder.setOptional(optional);
			try {
			    bcOrder = BCPay.startBCPay(bcOrder);
			    Map<String, String> WxJSAPIMap = bcOrder.getWxJSAPIMap();
			    result.put("jsapiAppid", WxJSAPIMap.get("appId").toString());
			    result.put("timeStamp", WxJSAPIMap.get("timeStamp").toString());
			    result.put("nonceStr", WxJSAPIMap.get("nonceStr").toString());
			    result.put("jsapipackage", WxJSAPIMap.get("package").toString());
			    result.put("signType", WxJSAPIMap.get("signType").toString());
			    result.put("paySign", WxJSAPIMap.get("paySign").toString());
			} catch (BCException e) {
				e.printStackTrace();
			}
			return result;
		}
	   
	    //发送房间消息
	   private void sendAdminMsg(String text, String token){
			String url = apiUrl + "/SendMsg";
	        SendMsgReq req = new SendMsgReq();
	        //消息类容
			req.setText(text);
			//通知类型消息
			req.setType((short) 3);
			//发送者账号
			req.setSender("admin");
			String reqJson;
			try {
				reqJson = mapper.writeValueAsString(req);
				Map<String, String> headers = new HashMap<String, String>();
				headers.put("Authorization", token);
		        ApiCall.post(url, reqJson, headers);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
	  }
}
