var url = 'http://localhost:8080/notifications?user=';
var notifications;

$(document).ready(function(){
	var url = window.location.href;
	var page = url.substring(url.lastIndexOf('/') + 1)

	if (page.indexOf("?") > 0)
		page = page.substring(0, page.indexOf("?"));
	page = "\"#" + page + "\"";
	// console.log(page)
	$(document).find(".selected-item").removeClass("selected-item");
	$(eval(page)).addClass("selected-item");
	checkNotifications();
	$(".fa-globe").click(function() {
		showNotifications();
	})
});

function checkNotifications() {
	 username = document.getElementById("user");
    if (username != null) {
    	var finalUrl = url + username.innerText;
    	$.getJSON(finalUrl, function(data) {
    		notifications = data;
    		var nr = data.length;
    		$(".fa-globe").append("<span class=\"num\">" + nr
    			+ "</span>");
    	})
    }
}

function showNotifications() {
	var message = "";
	
	for(var i in notifications) {
		var notification = notifications[i];
		message = message + "Notification for "+ notification.company
					+ "(" + notification.symbol
					+ "). Price(" + notification.currentPrice
					+ "$) is " + notification.message
					+ " than $" + notification.price + "(current price).\n";
	}

	alert(message);
}