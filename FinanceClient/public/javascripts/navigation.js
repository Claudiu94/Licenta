$(document).ready(function(){
	var url = window.location.href;
	var page = url.substring(url.lastIndexOf('/') + 1)

	if (page.indexOf("?") > 0)
		page = page.substring(0, page.indexOf("?"));
	page = "\"#" + page + "\"";
	// console.log(page)
	$(document).find(".selected-item").removeClass("selected-item")
	$(eval(page)).addClass("selected-item")
});