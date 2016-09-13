(function(DOC) {
	this.dom = {
		quote : window.JSON
				&& JSON.stringify
				|| String.quote
				|| function(str) {
					str = str.replace(/[\x00-\x1f\\]/g, function(chr) {
						var special = metaObject[chr];
						return special ? special : '\\u'
								+ ('0000' + chr.charCodeAt(0).toString(16))
										.slice(-4);
					});
					return '"' + str.replace(/"/g, '\\"') + '"';
				}

	};
	if (!String.prototype.trim) {
		String.prototype.trim = function() {
			return this.replace(/^[\s\xa0]+|[\s\xa0]+$/g, '');
		};
	};
	var metaObject = {
		'\b' : '\\b',
		'\t' : '\\t',
		'\n' : '\\n',
		'\f' : '\\f',
		'\r' : '\\r',
		'\\' : '\\\\'
	}, _startOfHTML = "\t__views.push(", _endOfHTML = ");\n", sRight = "&>", rLeft = /\s*<&\s*/, rRight = /\s*&>\s*/;
	var ejs2 = dom.ejs = function(id, data) {
		if (!ejs2[id]) {
			var rleft = rLeft, rright = rRight, sright = sRight, startOfHTML = _startOfHTML, endOfHTML = _endOfHTML, str, logic, el = DOC
					.getElementById(id);
			if (!el)
				throw "can not find the target element";
			str = el.text;
			var arr = str.trim().split(rleft), temp = [ "var __views = [];\n" ], i = 0, n = arr.length, els, segment;
			while (i < n) {//逐行分析，以防歧义
				segment = arr[i++];
				els = segment.split(rright);
				if (~segment.indexOf(sright)) {//这里不使用els.length === 2是为了避开IE的split bug
					switch (els[0].charAt(0)) {
					case "="://处理后台返回的变量（输出到页面的);
						logic = els[0].substring(1);
						temp.push(startOfHTML, logic, endOfHTML);
						break;
					case "#"://处理注释
						break;
					default://处理逻辑
						logic = els[0];
						temp.push(logic, "\n");
					}
					//处理静态HTML片断
					els[1]
							&& temp.push(startOfHTML, dom.quote.call(null,
									els[1]), endOfHTML);//转义
				} else {
					//处理静态HTML片断
					temp.push(startOfHTML, dom.quote.call(null, els[0]),
							endOfHTML);
				}
			}
			var keys = [], values = [];
			for (i in data) {
				keys.push(i);
				values.push(data[i]);
			}
			keys.push(temp.concat(" return __views.join('');").join(""))
			try{
				return (ejs2[id] = Function.apply(0, keys)).apply(0, values);
			}catch (e) {
				if(console){
					console.warn(e.message);
				}
				return "";
			}
		}
		var vals = []
		for (i in data) {
			vals.push(data[i]);
		}
		try{
			return ejs2[id].apply(0, vals);
		}catch (e) {
			if(console){
				console.warn(e.message);
			}
			return "";
		}
	};
})(document);