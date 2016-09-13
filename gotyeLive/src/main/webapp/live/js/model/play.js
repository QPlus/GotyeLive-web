//加载视频
function loadPlayer(token){
	var opts = {
	        "width": "100%",//视频宽度
	        "height": "100%",//视频高度
	        "bg":""//视频封面
	};
	var player = new Gotye.Player(token,opts);
	//人数变化
	player.onPlayUserChange(function(count,vircount){//count为真实人数，vircount为虚拟人数
		$(".fans-num").html(vircount+"人");
	});
	//直播状态变化
	player.onPubStatusChange(function(status){
		if(status==0){
			_noVideo();
		}else{
			_videoLoad();
		}
	});
	//人数达到上限
	player.onUserUpperLimit(function(){
		alert("达到最大观看人数上限！");
	});
	//视频元素渲染到页面
	player.appendTo("videoPlayer2");
}

function _videoLoad(){
	$("#videoPlayer2").show();
	$("#videoPlayer").hide();
	$("#kjp").hide();
	$("#iframeId").show();
}

function _noVideo(){
	$("#videoPlayer2").hide();
	$("#videoPlayer").show();
	$("#kjp").show();
	$("#iframeId").hide();
}