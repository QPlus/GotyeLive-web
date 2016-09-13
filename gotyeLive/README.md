# 亲加直播web观看端
## 概述

此项目开源是为了帮助用户快捷集成亲加直播web端，亲加直播web观看端是以亲加直
播SDK为基础开发的一套开源项目，用户可以在此基础上修改完成自己的观看页面。
主播端请到[亲加直播后台](https://livevip.com.cn/live/admin2/)打开。


## 功能模块
```
1.登陆(后台配置登陆类型)
    a.微信登陆
    b.游客登陆
2.视频观看
    a.pc观看
    b.手机观看
    c.观看视频人数，视频播放状态监听
3.聊天功能
	a.房间聊天
	b.私聊
	c.历史消息
4.课件功能
5.送礼品
    a.微信支付
```

## 编译环境

    1.jdk1.6以上
    2.maven打包

## 功能配置
    修改配置文件config.properties

### 亲加账号配置
```
GOTYE_ACCESS_SECRET=  //请到直播管理后台查看
GOTYE_EMAIL=  //亲加后台账号
GOTYE_PASSWORD= //亲加后台密码
通过房间账号密码 获取房间密码，生成房间token 
```
### 配置登陆类型
```
<!-- 1-微信登陆 2-游客登陆 -->
GOTYE_LOGTYE_TYPE = 2

当配置为游客登陆时会 自动生成昵称-生成token-登陆
当配置为微信登陆时在微信浏览器下会进行微信授权-生成token-登陆
```

### 配置微信登陆
```

GOTYE_WX_JSAPI_APPID=  //微信appId
GOTYE_WX_JSAPI_SECRET=  //微信appsecret

微信公众平台https://mp.weixin.qq.com/
微信网页授权文档https://mp.weixin.qq.com/wiki/4/9ac2e7b1f1d22e9e57260f6553822520.html

```
### 配置支付功能
```
 BEECLOUD_APPID = //BeeCloud应用 appId
 BEECLOUD_TEST_SECRET= //BeeCloud应用 testSecret
 BEECLOUD_APP_SECRET= //BeeCloud应用 appSecret
 BEECLOUD_MASTER_SECRET= //BeeCloud应用 masterSecret
 BEECLOUD_SANDBOX= 设置sandbox属性为true，开启测试模式
 
生产模式testSecret可为null
测试模式appSecret、masterSecret可为null
 
支付功能使用的是BeeCloud开源项目，可以支持多种支付方式，这里只使用了微信作为示例代码.其它支付方式请见https://beecloud.cn官方文档
```

## 关键代码

	页面路径：main\webapp\live\live.jsp
	
### 聊天模块###

	<!-- 聊天 -->
    <section class="msg-chat swiper-slide show" id="infor-tab10">
     	<div class="showgift" id="showgift"></div>
     	<div class="loadImg" style="display:none;">
     		<img src="http://162.gotlive.com.cn:80/share/h5/images/loading.gif">
     	</div>
     	<ul class="msg-list chat allow-roll" id="msg-chat-list" style="height: 333.063px;">
	 	</ul>
	</section>

	chat = new Gotye.Chat(token);//聊天

### 课件模块

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

	loadCourseware();//加载课件
	
### 视频模块

    <!-- 视频容器div -->
    <div class="video" id="videoPlayer2" >
    
	loadPlayer(token);//加载视频

### 主播介绍


	$("#anchorIntr").html(entity.anchorDesc);//主播介绍

## 打包发布

    1.使用maven打包发布到容器中,比如tomcat
    2.打开观看页面 http://{basePath}/live/{roomid}
        basePath: 为项目域名和端口
        roomId：为亲加后台创建的房间的id

## demo页面

    http://150.gotlive.com.cn/gotyeLive/live/215147

## Q&A

### 为什么有些浏览器会全屏播放
全屏播放与手机有关，目前js库无法解决。但是亲加js有集成微信sdk，能在微信中打开时保证大部分情况不会全屏，如果用户使用自己域名，必须找微信官方申请白名单方可生效。