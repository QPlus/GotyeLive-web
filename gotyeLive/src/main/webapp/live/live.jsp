<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
        <title>
          	  亲加直播
        </title>
        <meta name="keywords" content="">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta name="viewport" content="width=device-width, initial-scale=1.0,user-scalable=no,maximum-scale=1.0">
        <link href="${_path_}/images/ico.png" rel="shortcut icon">
        <link href="${_path_}/css/style.css" rel="stylesheet">
        <link rel="stylesheet" type="text/css" href="${_path_}/css/reset.css">
        <script type="text/javascript">
    	</script>
    	<!-- 模板 -->
    	<script src="${_path_}/js/ejs.js"></script>
    	<!-- 表情 -->
    	<script src="${_path_}/js/expression.js"></script>
    	<script src="${_path_}/js/jquery-1.11.1.js"></script>
    	<script src="${_path_}/js/util.js"></script>
    	<!-- 页面初始化 -->
    	<script src="${_path_}/js/winInit.js"></script>
    	<!-- h5监听手势滑动等事件 -->
    	<script src="${_path_}/js/hammer.min.js"></script>
    	<script src="${_path_}/js/jquery.hammer.js"></script>
    	
    	<!-- 聊天模块 -->
    	<script src="${_path_}/js/model/chat.js"></script>
    	<!-- 课件模块  -->
    	<script src="${_path_}/js/model/courseware.js"></script>
    	<!-- 视频模块 -->
    	<script src="${_path_}/js/model/play.js"></script>
    	<!-- 亲加视频直播js库 -->
		<script src="http://medias.livevip.com.cn/web_lib/js/live.player_2.0.js"></script> 
    </head>
    <body>
        <div data-role="page" id="pageone" class="container">
            <div class="header">
                <ul>
                    <li class="lo">
                        <p>
                            在线<span class="fans-num">0人</span>
                        </p>
                    </li>
                    <li class="lu">
                        <p id="roomName2">
                        </p>
                    </li>
                    <li class="ig">
                        <a href="#">
                            <i class="ico01">
                            </i>
                        </a>
                        <div class="ab">
                            <div class="audio">
                                <a href="#">
                                    <i class="ico02">
                                    </i>
                                    <p>
                                                                                                          纯语音
                                    </p>
                                </a>
                            </div>
                        </div>
                    </li>
                </ul>
            </div>
            <div class="video" id="videoPlayer" style="display:none;">
            	<div  class="video-container">
            		<video id="liveVideoWindow" controls autoplay poster="data:image/gif,AAAA" width="100%" height="100%" style="display: block;margin-top:0px;  background-size:100%; width:100%; height:100%;" webkit-playsinline preload src="">
	                </video>
            	</div>
                
                <div id="notStartedCover" class="no_video" style="display: block;">
					<div>
						<i></i>
						<p style="transform-origin: 0px 0px 0px; opacity: 1; transform: scale(1, 1);">亲，直播还没开始呢！</p>
					</div>
				</div>
            </div>
            <!-- 视频容器div -->
            <div class="video" id="videoPlayer2" >
            </div>
            <div class="chat-plate">
                <div class="tabs" id="activity_tab">
                	 <ul class="display-box fastclick">
                	 <li class="flex chatLi active" data-tab="infor-tab10"> 
				   		<a href="javascript:void(0);">互动消息</a>
					 </li>
					 <li class="flex" data-tab="infor-tab81"> 
				   		<a href="javascript:void(0);">课件</a>
					 </li>
					 <li class="flex" data-tab="infor-tab52"> 
				   		<a href="javascript:void(0);">主播介绍</a>
					 </li>
					</ul>
                </div>
                <div class="system_messages display-box" id="sys_msg">
                    <div>
                        <i></i>
                        <em class="line"></em>
                    </div>
                    <p class="flex"><span class="">欢迎大家参加本次直播活动！</span></p>
                </div>
                <!--私聊-->
                <div class="nomine_tips" id="private_msg"></div>
                <div id="private_chat_box">
                    <div class="private_chat_c">
                        <div class="private_name display-box">
                            <p class="flex">与“<span id="priavteName">Jean</span>”私聊中</p>
                            <a>
                                <i></i>
                            </a>
                        </div>
                        <div class="private_chat allow-roll">
                        	  <ul class="msg-list chat" id="private_chat">
                              </ul>
                        </div>       
                    </div>
                </div>
                <div class="chatBox" style="padding-top:37px;">
                    <div id="tabs-container" class="swiper-container">
                      <div class="swiper-wrapper" id="content1">
                      	<!-- 聊天 -->
                         <section class="msg-chat swiper-slide show" id="infor-tab10">
	                         <div class="showgift" id="showgift"></div>
	                         <div class="loadImg" style="display:none;">
	                         	<img src="http://162.gotlive.com.cn:80/share/h5/images/loading.gif">
	                         </div>
	                         <ul class="msg-list chat allow-roll" id="msg-chat-list" style="height: 333.063px;">
							 </ul>
						 </section>
						 <!-- 课件 -->
						 <section class="swiper-slide docbox" id="infor-tab81">
						 	<div class="default-kj">
						 		<iframe name="iframeId" id="iframeId" class="iframeId" src="" scrolling="yes" style="border: none; width: 100%; height: 100%; display: none;" frameborder="0"></iframe>
						 		<p id="kjp" style=""><img src="${_path_}/images/kj.png"></p>
						 	</div>
						 	<div class="doc_bg"></div>
						 	<div class="doc_big" id="doc_big" onclick="docbigClick();"><a href="javascript:void(0);"><i></i></a></div>
						 	<div class="doc_refresh" id="doc_refresh"><a href="javascript:_loadmod();"><i></i></a></div>
						 </section>
						 <!-- 主播介绍 -->
						 <section id="infor-tab52" class="videoBox1 swiper-slide">
						 	<div class="intr-cont" id="anchorIntr"></div>
						 </section>
                	  </div>
                          <nav id="chat">
                              <div class="display-box">
                              	  <div class="chat-input flex border-box">
                                      <div class="face_div"><a class="smilebtn" id="facebtn" href="javascript:void(0);"></a></div>
                                      <div id="chat_div" class="chattxt" contenteditable="true"></div>
                                      <div class="send_div"><a class="sendbtn" id="sendbtn">发送</a></div>
                                   </div>
                                  <a id="giftbtn" class="gift-icon" href="javascript:void(0);"></a>
                              </div>
                              
                              <div id="expressionBox" style="display:none;">
                                  <ul>
                                  </ul>
                              </div>
                              <div id="giftBox" style="display:none;">
                    			<p>礼品区</p>
                        		<div class="page_emotion box_swipe" id="page_emotion">
                           			 <dl id="list_emotion" class="list_emotion pt_10" style="list-style: none; transition-duration: 500ms; transform: translate3d(0px, 0px, 0px);">
                           			 <div style="vertical-align: top; float: left; width: 375px;">
                           			   	<dd data-name="小花" data-price='0' data-src="${_path_}/images/gift/gift_5.png">
											<img src="${_path_}/images/gift/gift_5.png"><span class="diamond"><i></i>免费</span>
									 	</dd>
                           			    <dd data-name="摩托车" data-price='0' data-src="${_path_}/images/gift/gift_4.png">
											<img src="${_path_}/images/gift/gift_4.png"><span class="diamond"><i></i>免费</span>
									 	</dd>
                           			    <dd data-name="手捧花" data-price='0' data-src="${_path_}/images/gift/gift_3.png">
											<img src="${_path_}/images/gift/gift_3.png"><span class="diamond"><i></i>免费</span>
									 	</dd>
                           			 	<dd data-name="砖石" data-price='0' data-src="${_path_}/images/gift/gift_2.png">
											<img src="${_path_}/images/gift/gift_2.png"><span class="diamond"><i></i>免费</span>
									 	</dd>
                           			   	<dd data-name="跑车" data-price='0.01' data-src="${_path_}/images/gift/gift_1.png">
											<img src="${_path_}/images/gift/gift_1.png"><span class="diamond"><i></i>0.01</span>
									 	</dd>
									</div>
                             		</dl><!-- 礼物框 -->
                             		<dt>
                                	<ol id="nav_emotion" class="nav_emotion">
                                    <span class="on"></span>
                               		</ol>
                           	    	</dt>
                        		</div>
                   			 </div>
                          </nav>
                    </div>
                  <div class="dialogBox">
                    <section id="identification" class="dialog2">
                        <div class="loginWay">
                            <h4>
                                	通过第三方授权登录
                            </h4>
                            <ul class="ulList2">
                                <li>
                                    <a href="javascript:void(0);" id="weChatLoginBtn">
                                        <img src="${_path_}/images/weixin.png">
                                    </a>
                                </li>
                                <li>
                                    <a href="javascript:void(0);" id="qqLoginBtn">
                                        <img src="${_path_}/images/qq.png">
                                    </a>
                                </li>
                            </ul>
                        </div>
                        <ul class="ulList1">
                            <li class="li-or">
                            </li>
                            <li>
                                <input type="text" class="input" placeholder="请输入昵称" id="nickName">
                            </li>
                            <li>
                                <a href="javascript:void(0);" class="button" id="login">
                                   	 游客登录
                                </a>
                            </li>
                        </ul>
                        <div class="closeBtn" id="closeLogin">
                        </div>
                    </section>
                    
                    <section id="qrCode" class="dialog4">
                        <div class="dialog4-con">
                            <p class="closeBtn pointer" id="closeBtnP">
                            </p>
                            <div id="payDiv">
                            </div>
                            <h2>
                                                                                    微信扫一扫支付
                            </h2>
                        </div>
                    </section>
                   <section id="ptBox02" class="dialog5">
           			 	<div class="exceptional_box">
            				<h4 id="forPayPriceH4"></h4>
               		 		<a href="javascript:void(0);" id="wxPayGift">
                 	 	 	支付
                			</a>
           		 		</div>
            			<div class="close_btn1" onclick="$('#ptBox02').hide();"></div>
        			</section>
                </div>
            </div>
      	</div>
	</div>
        <script type="tmpl" id="temp_text_enter_event">
				<li class="system_msg">
                      <a class="leftHead" href="javascript:void(0);"><img src="${_path_}/images/system_img.png"></a>
                      <div class="leftChat">
                      <i></i>
                      <p><span class="name">系统消息:</span>欢迎<font>"<&=data.sendName&>"</font>进入频道</p>
                      </div>
                  	  <div class="clearfix"></div>
                </li>	
			</script>
		<!-- 通知  -->
		<script type="tmpl" id="temp_text_notify">
			<li class="system_msg">
                <a class="leftHead" href="javascript:void(0);"><img src="${_path_}/images/system_img.png"></a>
                <div class="leftChat">
                <i></i>
                <p><span class="name">系统消息:</span><&=data.content&></p>
                </div>
                <div class="clearfix"></div>
              </li>	
		  </script>
			
			<script type="tmpl" id="temp_text_fail_event">
				<li class="reward">
                    <p>
                                                                 消息 "<&=data.text&>" 发送失败
                    </p>
                </li>
		    </script>
		    
		    <!-- 日志模板  -->
			<script type="tmpl" id="temp_text_log">
				<li class="reward">
                      <p style="color:red;">
                     	<&=data.text&>      
                      </p>
               </li>
			</script>
			
			<!-- 礼物显示模板  -->
			<script type="tmpl" id="temp_gift">
                <div style="vertical-align: top; float:left;">
                <& for(var i=0,tl = list.length,tr;i < tl;i++){ &>
       				 <& tr = list[i]; &>
					<dd onclick="showPayGift(<&=tr.giftId&>,'<&=tr.giftName&>','<&=tr.giftPrice&>','<&=tr.giftPicUrl&>');">
						<img src="<&=tr.giftPicUrl&>" ><span class="diamond"><&if(tr.giftPrice==0){&>免费<&}else{&><i></i><&=tr.giftPrice&> <&}&> </span></dd>
					</dd>
 				<& } &>               
                 </div>
			</script>
			
			<!-- 礼物 消息模板  -->
		    <script type="tmpl" id="temp_gift_msg">
				<li class="system_msg">
                     <a class="leftHead" href="javascript:void(0);"><img src="${_path_}/images/system_img.png"></a>
                     <div class="leftChat">
                     <i></i>
                     <p><span class="name">系统消息:</span><font><&=data.sendName&></font>送了<img src="<&=data.giftPicUrl&>"></p>
                     </div>
                     <div class="clearfix"></div>
                 </li>
		    </script>
			<!-- 消息模板 -->
			<script type="tmpl" id="temp_text_msg">
				<&if(data.isSelf){&>
					<li class="privatechat">
				<&}else{&>
					<li class="privatechat" data-sendId="<&=data.sendId?data.sendId:data.account&>" data-sendName="<&=data.sendName?data.sendName:data.nickName&>" onclick="chat.loadPrivateChat(this);">
				<&}&>
                    <a class="leftHead" href="javascript:void(0);"><&if(data.headUrl){&><img src="<&=data.headUrl&>"/><&}else{&><img src="${_path_}/images/hd_img.png" /><&}&></a>
                          <div class="leftChat">
                              <i></i>
                              <p>
								<&if(data.role==2||data.role==3){&>
									<span class="name" style="color:#3dc379">
								<&}else{&>
									<span class="name">
								<&}&>
								<&if(data.sendNameShow){&>
									<&=data.sendNameShow&>
								<&}else if(data.sendName){&>
									<&=data.sendName&>
								<&}else{&>
									<&=data.nickName&>
								<&}&>
								<&if(data.role==2){&>
									(主播)
								  	<&}else if(data.role==3){&>
									 (助理)
								  	<&}&>:
								</span><&=data.text&></p>
                           </div>
                    <div class="clearfix"></div>
                 </li>
			</script>
			<script type="tmpl" id="temp_activity_tab">
				<li class='<&if(data.tagType == 1){&>flex chatLi<&}else{&> flex<&}&>' data-tab="infor-tab<&=data.index&>"> 
				   <a href="javascript:void(0);">
                        <&=data.title&>
                   </a>
				</li>
			</script>
        <script type="text/javascript">
	   		var token = Tool.getCookie("${token_key}");
	   		var live = new Gotye.Live(token);
	   		var wxJsApiAppId = "${wxJsApiAppId}";
	   		var path = "${_path_}";
	   		var roomId = "${roomId}"; 
	   		var room;
	   		var chat;
	        $(function(){
	        	//获取房间信息
				live.getRoom({},function(entity){
		   			if(entity){
		   				room = entity;
		   				chat = new Gotye.Chat(token);//聊天
		   				$("#anchorIntr").html(entity.anchorDesc);//主播介绍
		   				loadPlayer(token);//加载视频
		   				loadCourseware();//加载课件
		   			}else{
		   				Tool.log("room is empty!");
		   			}
		   		},
		   		function(){
		   			Tool.log("get room fail!");
		   		});
	         });
        </script>
    </body>
    </html>