$(document).ready(function() {
	var url = decodeURIComponent(window.location.href);
	var from = url.substring(url.indexOf("from=") + 5, url.indexOf("&"));
	var shares = url.substring(url.indexOf("shares=") + 7, url.lastIndexOf("&"));
	var symbol = url.substring(url.lastIndexOf("=") + 1, url.length);
	var html = "<p>Move " + shares + " "
				+ symbol + " shares from " + from
				+" to: </p><select class="select-style"></select>";
	
	$('#content').append(html);
	// console.log(html)
});