//公告显示
var sys_time_show;
function sysmsgshow(content) {
	if (sys_time_show) {
		clearTimeout(sys_time_show);
		$('#sys_msg').css({
			'visibility': 'hidden',
			'top': '0'
		}).find('span').removeClass('marquee');
	}
	$('#sys_msg').css({
		'visibility': 'visible',
		'top': '37px'
	}).find('span').html(content);
	$('#sys_msg').find('span').addClass('marquee');
	sys_time_show = setTimeout(function() {
		$('#sys_msg').animate({
			'top': '0px'
		}, 500, function() {
			$('#sys_msg').css('visibility', 'hidden').find('span').removeClass('marquee');
		});
	}, 12000);
}
var h1,h2,h3;
$(function(){
	$(window).resize(function() {
        var contHeight = $(window).height();
        var videoHeight = $(window).width() * 9 / 16;
        $("#pageone").css("height", contHeight);
        if(isPhone()){
        	$(".video").css("height", videoHeight);
        }else{
        	$(".video").css("height", "360px");
        	videoHeight=360;
        }
        $(".chat-plate").css("height", contHeight - 28 - videoHeight);
        var tabsHeight = (contHeight - 28 - videoHeight) - $(".tabs").height();
        $(".chatBox").css("height", tabsHeight);
        $("#msg-chat-list").css("height", tabsHeight - $("#chat").innerHeight());
    }).resize();
	h1=$(".video").height();
	h2=$(".chat-plate").height();
	h3=$(".chatBox").height();
	$(".tabs li").click(function(){
		$(this).addClass("active").siblings("li").removeClass("active");
		$("#"+$(this).data("tab")).addClass("show").siblings("section").removeClass("show");
		if($(this).hasClass("chatLi")){
			$("#chat").show();
		}else{
			$("#chat").hide();
		}
	});
});