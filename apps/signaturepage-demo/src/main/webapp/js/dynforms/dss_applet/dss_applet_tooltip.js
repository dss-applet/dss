function dssInitTooltip(){
	$(document).tooltip({content:function(){
		var e = $(this);
		if (e.hasClass('dss_cert_info')){
			return e.attr('title').replace(/|/g,'<br\/>');
		} else {
			return e.attr('title');
		}
	}});
}
function dssFormatTooltip(tip){
	return tip.replace(/<br\/>/g, '|');
}