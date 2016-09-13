if (!window.Gotye) {
	var Gotye = {};
};
Gotye.Chat = (function(win) {
	//私聊用户对象id
	var _privateChatSendId = "";
	//私聊消息缓存
	var _privateChatMap = {};
    var timer;
	var imgs=[];
	var tempGift={};
	var Chat = function(token){
		this.liveChat = new Gotye.Live(token);
		//定时显示礼物
		timer=setInterval(function (){
   			showgf();
   		 },500);
		//计算礼品显示
		calculateGiftShow();
		this.login();
		this.bindSendmsg();
		this.bindPayGift();
		this.showHistory();
		this.bindGiftbtn();
		//注入表情
		win.toExpression('expressionBox').showHide('facebtn', 'expressionBox').sendFace('chat_div', 'expressionBox');
		bindPrivateClose();
	}
	Chat.prototype = {
		sendMsg:function(){
			var text = $("#chat_div").text();
			if(text.replace(/(^\s*)|(\s*$)/g,'') == ""){
				return;
			}
			var data = {
				"text" : text
			};
			var dataForShow = {};
			//表情
			dataForShow.text = win.replace_em(data.text);
			//昵称
			dataForShow.sendName = this.liveChat.getLoginUser().nickname;
			dataForShow.sendTimeStr = new Date().format("hh:mm:ss");

	        var headUrl=Tool.getCookie('headUrl');
	        //头像
	        if(headUrl){
	        	headUrl=headUrl.substring(1,headUrl.length-1);
	        	//发送消息可以有额外信息，格式为json字符串
		        data.extra = '{"headUrl":"'+headUrl+'"}';
		        dataForShow.headUrl = headUrl;
	        }

			dataForShow.isSelf=true;
			//如果是私聊
			if(_privateChatSendId != ""){
				data.recvId=_privateChatSendId;
				//添加私聊消息到缓存
				_pushPrivateChatMap(dataForShow);
				_appendPrivate("temp_text_msg", dataForShow);
			}else{
				_appendLi("temp_text_msg", dataForShow);
			}
			$("#chat_div").text("");
			this.liveChat.sendMsg(data, function(ack) {
				if (Gotye.Code.SUCCESS == ack.code) {
				}else{
					//消息发送失败
					if(_privateChatSendId != ""){
						_appendPrivate("temp_text_fail_event", data);
					}else{
						_appendLi("temp_text_fail_event", data);	
					}	
				}
			});
		},
		onMsg:function(data){
			if (!data)
			return;
			//消息类型为1表示文本消息
			if (data.type == 1) {
				data.sendTimeStr = new Date(data.sendTime).format("hh:mm:ss");
				data.text = win.replace_em(data.text);
				var dataForShow = $.extend(true, {}, data) ;
				if(dataForShow.extra){
					try{
						dataForShow.headUrl =  JSON.parse(dataForShow.extra).headUrl;
						dataForShow.role = JSON.parse(dataForShow.extra).role;//用户角色   2为主播,3为助理
					}catch(e){
					}
				}
				if(0 == data.chatType && data.sendId == _privateChatSendId){
					dataForShow.sendNameShow = dataForShow.sendName;
					_appendPrivate("temp_text_msg", dataForShow);
					_pushPrivateChatMap(data);
					dataForShow.sendNameShow = dataForShow.sendName + " 对 我 说";
					_appendLi("temp_text_msg", dataForShow);
				}else{
					if(0 == data.chatType ){
						_pushPrivateChatMap(data);
						dataForShow.sendNameShow = dataForShow.sendName + " 对 我 说";
					}
					_appendLi("temp_text_msg", dataForShow);
				}
			} 
			if(data.type == 3){
				if ("enter" == data.text&&!enterMap[data.sendId]) {
					enterMap[data.sendId] = true;
					_appendLi("temp_text_enter_event", data);
				}else{
					try{
						var jsonObj = $.parseJSON(data.text);
						if(jsonObj.notifyType == 4){//公告通知
							win.sysmsgshow(jsonObj.content);
							_appendLi("temp_text_notify", jsonObj);
						}
						if(jsonObj.notifyType == 5){//礼物
							var giftPicUrl = decodeURIComponent(jsonObj.giftPicUrl);
							data.giftName=jsonObj.giftName;
							data.giftPicUrl=giftPicUrl;
							data.sendName = jsonObj.nickName;
							_appendLi("temp_gift_msg", data);
							imgs.push(giftPicUrl);
							showgf();
						}
					}catch(e){
						Tool.log(e.message);
					}
				}
			}
		},
		//加载私聊
		loadPrivateChat:function(_this){
	       	if(this.liveChat.loginUser.account == $(_this).attr("data-sendId")){
        		return;
        	}
	       	showPrivateChatWin(_this);
	       	_privateChatSendId = $(_this).attr("data-sendId");
        	if(_privateChatMap[_privateChatSendId]){
        		//从缓存中取出私聊消息渲染到页面上
        		for(var i=0; i<_privateChatMap[_privateChatSendId].length; i++){
        			_privateChatMap[_privateChatSendId][i].sendNameShow = _privateChatMap[_privateChatSendId][i].sendName;
        			_appendPrivate("temp_text_msg", _privateChatMap[_privateChatSendId][i]);
        		}
        	}
		},
		//显示送礼框
		showPayGift:function(giftName,giftPrice,giftPicUrl){
			giftPicUrl = encodeURIComponent(giftPicUrl);
			tempGift.giftName = giftName;
			tempGift.giftPrice = giftPrice;
			tempGift.giftPicUrl = giftPicUrl;
			$("#giftBox").hide();
			//免费礼物直接发送
			if(giftPrice == 0){
				//发送送礼
				this.sendGift();
				return;
			}
			$(".dialog5").show();
			$("#forPayPriceH4").html("￥"+giftPrice);
		},
		login:function(){
			var self = this;
			this.liveChat.login("chat",function() {
				self.liveChat.onMsg(self.onMsg);
				self.liveChat.onForceLogout(function(){});
				self.liveChat.onKilled(function(){});
				self.getRoomNotice();
				self.sendEnterMsg();
			}, function(code) {
				if(1009 == code){
					var data = {};
					data.text = "您已经被管理员禁止登陆";
					_appendLi("temp_text_log", data);
				}
				if(1013 == code){
					var data = {};
					data.text = "达到最大在线人数,登陆失败";
					_appendLi("temp_text_log", data);
				}
			});
		},
		//发送欢迎通知
		sendEnterMsg:function(){
			var text = $("#chat_div").text();
			var data = {
					"text" : "enter"
			};
			data.type=3;
			var self=this;
			this.liveChat.sendMsg(data, function(ack) {
				if (Gotye.Code.SUCCESS == ack.code) {
					data.sendName = self.liveChat.getLoginUser().nickname;
					_appendLi("temp_text_enter_event", data);
				}
			});
		},
		//绑定发送消息事件
		bindSendmsg: function(){
			var self=this;
			$("#sendbtn").click(function(){
				self.sendMsg();
			});
		},
		//获取历史记录
		showHistory:function(){
			var data = {
					"index" : 0,
					"count" : 10,
					"roomId" : win.room.roomId,
					"msgType" : 1,
					"startTime":new Date().getTime()-3*60*60*1000
			}
			this.liveChat.getChatMsgs(data,function(entities,total){
				for(var i=0; i <  entities.length; i++){
					if(entities[i].msgType == 1){
						var data = {};
						data.text=Base64.decode(entities[i].msgCont)
						data.sendTime = entities[i].msgTime;
						data.account=entities[i].account;
						data.nickName=entities[i].nickName;
						data.sendTimeStr = new Date(data.sendTime).format("hh:mm:ss");
						data.text = win.replace_em(data.text);
						try{
							data.extra =  JSON.parse(Base64.decode(entities[i].extra)).data;
							data.headUrl =  JSON.parse(data.extra).headUrl;
							data.role = JSON.parse(data.extra).role;
						}catch(e){
						}
						_appendPre("temp_text_msg", data);
					}
				}
				setTimeout(function(){
					_msgBoxToBottom();
				},1000);
			});
		},
		//发送礼物
		sendGift:function(){
			var data = {};
			var text = '{"nickName":"'+ this.liveChat.getLoginUser().nickname  +'","giftName":"'+ tempGift.giftName + '","giftPicUrl":"' + tempGift.giftPicUrl + '","notifyType":"5"}';
			data.type = 3;//通知类型
			data.text = text;
			imgs.push(decodeURIComponent(tempGift.giftPicUrl));
			showgf();
			this.liveChat.sendMsg(data, function(ack) {
				if (Gotye.Code.SUCCESS == ack.code) {	
				}
			});
			data.giftName=tempGift.giftName;
			data.giftPicUrl=decodeURIComponent(tempGift.giftPicUrl);
			data.sendName = this.liveChat.getLoginUser().nickname;
			_appendLi("temp_gift_msg", data);
		},
		bindPayGift:function(){
			var self = this;
			$("#wxPayGift").click(function(){
				this.wxpayGift();
			});
		},
		//微信支付
		wxpayGift:function(){
			var data = {
					"amount":tempGift.giftPrice,
					"giftPicUrl":tempGift.giftPicUrl,
					"giftId":tempGift.giftId,
					"giftName":tempGift.giftName
			};
			$.post(path+"/wxjsapipay",data, 
				function(resp) {
				if(resp.jsapiAppid && resp.jsapiAppid != ""){
					var data2 = {
			            "appId": resp.jsapiAppid,
			            "timeStamp": resp.timeStamp,
			            "nonceStr": resp.nonceStr,
			            "package":  resp.jsapipackage,
			            "signType": resp.signType,
			            "paySign": resp.paySign
			        };
					//微信浏览器jssdk 支付 方法
			        WeixinJSBridge.invoke(
			                'getBrandWCPayRequest',
			                data2,
		                function (res) {
		                    WeixinJSBridge.log(res.err_msg);
		                }
			        );
				}
			});
		},
		//获取房间通知
		getRoomNotice:function(){
			this.liveChat.getRoomNotice({},function(content){
				if(content && content != ""){
					hasShowRoomNotice = true;
					sysmsgshow(content);
					var data = {};
					data.content = content;
					_appendLi("temp_text_notify", data);
				}
			},function(){});
		},
		//绑定发送礼物事件
		bindGiftbtn:function(){
			$("#giftbtn").click(function(){
				if($("#giftBox").is(":hidden")){
	    			$("#giftBox").show();
	    			$("expressionBox").hide();
	    		}else{
	    			$("#giftBox").hide();
	    		}
			});
			var self=this;
			$("#list_emotion").children("div").children("dd").click(function(){
				self.showPayGift($(this).attr("data-name"),$(this).attr("data-price"),$(this).attr("data-src"));
			});
		}
	}
	//显示礼物
    function showgf(){
		if(!imgs || imgs.length==0 || !$("#showgift").is(":hidden")){
			return;
		}
      	var oImg=document.createElement('img');
	  	var placeHold=document.getElementById("showgift");
	  	placeHold.innerHTML="";
	 	placeHold.appendChild(oImg);
	  	oImg.setAttribute('src',imgs[0]+"?time="+new Date().getTime());
	  	$("#showgift").addClass("hover");
		$("#showgift").css("display","block");
		$("#showgift").css("opacity", 1);
		setTimeout(function(){
	 		$("#showgift").fadeOut(1000);
	  	},3000);
	 	setTimeout(function(){
			$("#showgift").removeClass("hover");
			$("#showgift").css("display","none");
			placeHold.innerHTML="";
	  	},4000);
	  	imgs.splice(0,1);
	}
	//渲染页面，注入消息到页面
	function _appendLi(tempId, data) {
		var msgli = dom.ejs(tempId, {
			"data" : data
		})
		$("#msg-chat-list").append(msgli);
		_msgBoxToBottom();
	}
	function _appendPrivate(tempId,data){
		var msgli = dom.ejs(tempId, {
			"data" : data
		})
		$("#private_chat").append(msgli);
		_msgBoxToBottom();
	}
	function _appendPre(tempId, data){
		var msgli = dom.ejs(tempId, {
			"data" : data
		});
		$("#msg-chat-list").prepend(msgli);
	}
	//滚动条滚动到底部
	function _msgBoxToBottom() {
		setTimeout(function() {
			var msgBox = $(".msg-list")[1];
			if(msgBox){
				msgBox.scrollTop = msgBox.scrollHeight-10;
			}
			msgBox = $(".private_chat")[0];
			if(msgBox){
				msgBox.scrollTop = msgBox.scrollHeight-10;
			}
		}, 200);
	}
	//添加私聊消息到缓存
	function _pushPrivateChatMap(data){
		if(_privateChatSendId == ""){
			if(!_privateChatMap[data.sendId]){
				_privateChatMap[data.sendId] = new Array();	
			}
			_privateChatMap[data.sendId].push(data);
		}else{
			if(!_privateChatMap[_privateChatSendId]){
				_privateChatMap[_privateChatSendId] = new Array();	
			}
			_privateChatMap[_privateChatSendId].push(data);
		}
	}
	//显示私聊窗口
    function showPrivateChatWin(_this){
    	jQuery('#coverLayerempty2').fadeIn();
    	$('.textareaBox').attr('data-type','private');
    	 
    	jQuery('#private_chat_box').children().height($("#msg-chat-list").height()+$('.tabs').height()).css('display','block').stop(true,true).animate({'top':'0px'},500,function(){});
    	$('.private_chat').css("height",$(".private_chat_c").height()-$(".private_name").height());
    	$('.textareaBox').addClass('onprivate');
    	
    	$("#priavteName").html($(_this).attr("data-sendName").length>10?($(_this).attr("data-sendName").substr(0,10)+"..."):$(_this).attr("data-sendName"));
    	$("#private_chat").children().remove();
    }
    //显示送礼
	function calculateGiftShow(){
		var giftNum=0;
		$('#list_emotion').children('div').css("width",$(window).width());
		var divLength = $('#list_emotion').children('div').length;
		var divWidth = parseInt($('#list_emotion').children('div').css("width").replace("px",""));
		$("#list_emotion").css("width",divLength*divWidth+"px");
		var hammertime = new Hammer(document.getElementById("page_emotion"));
		//左划手势
	    hammertime.on("swipeleft", function (e) {
	    	if(giftNum < divLength-1){
				giftNum++;
				$("#list_emotion").css("transform","translate3d("+ (-giftNum*divWidth) +"px, 0px, 0px)");
				$("#nav_emotion").find("span").removeClass("on");
				$("#nav_emotion").find("span").eq(giftNum).addClass("on");
			}
	    });
	    //右划手势
	    hammertime.on("swiperight", function (e) {
	    	if(giftNum > 0){
				giftNum--;
				$("#list_emotion").css("transform","translate3d("+ (-giftNum*divWidth) +"px, 0px, 0px)");
				$("#nav_emotion").find("span").removeClass("on");
				$("#nav_emotion").find("span").eq(giftNum).addClass("on");
			}
	    });
		for(var i=0; i<divLength-1; i++){
			$("#nav_emotion").append("<span></span>");
		}
	}
	//绑定关闭私聊
	function bindPrivateClose(){
		$('#private_chat_box').on('click','.private_chat_c .private_name a',function(){//关闭私聊
			var that=this;
			$('.slider-ft').removeAttr('data-talkerid').removeAttr('data-alkername');
			
			jQuery(that).parents('.private_chat_c').stop(true,true).animate({'top':'100%'},500,function(){
				$(this).hide();
				jQuery('#coverLayerempty2').fadeOut();
			});
			
			$('.textareaBox').removeClass('onprivate');
			$('.textareaBox').attr('data-type','chat');
			_privateChatSendId = "";
		});
	}
	return Chat;
})(window);
