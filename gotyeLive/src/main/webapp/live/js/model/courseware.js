function loadCourseware(){
	//加载课件页面
	live.getClientUrls({},function(data){
		if(data){
			$("#iframeId").attr("src",data.modVisitorShareUrl);
		}else{
			Tool.log("getClientUrls fail!");
		}
	},function(){
		Tool.log("getClientUrls fail!");
	});
}

//课件全屏
function docbigClick(event){
	var doc_itime;
	setTimeout(function(){
		if($(window).scrollTop()!=0){
			$(window).scrollTop(0);
		}
		
	},100);
	 event.stopPropagation();
	 $(this).toggleClass("on");
	 if($(this).hasClass("on")){
		 $(".video").addClass("videotop");
		 $(".header").css({'display':"none"});
		 $(".tabs").css({'display':"none"});
		 $("#sys_msg").css({'display':"none"});
		 $(".video").animate({'height':0},500);
		 jQuery('.chat-plate').addClass('ontop').animate({'height':$(window).height()+"px"},{
			duration: 500,complete:function(){setTimeout(function(){
			if($(window).scrollTop()!=0){
				$(window).scrollTop(0);
			}
		},0);}
		});
		$(".chatBox").animate({'height':$(window).height()+"px"},500).css("padding-top",0);	
	 }else{
		 $(".video").removeClass("videotop");
		 $(".header").css({'display':"block"});
		 $(".tabs").css({'display':"block"});
		 $("#sys_msg").css({'display':"block"});
		 $(".video").animate({'height':h1},500);
		 jQuery('.chat-plate').removeClass('ontop').animate({'height':h2},{
			duration: 500,complete:function(){setTimeout(function(){
			if($(window).scrollTop()!=0){
				$(window).scrollTop(0);
			}
			},0);}
			});
			$(".chatBox").animate({'height':h3},500).css("padding-top",36);	
	 	}
 }


