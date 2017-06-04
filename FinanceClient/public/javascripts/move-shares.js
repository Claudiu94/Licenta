var username = null;

$(document).ready(function() {
	var url = decodeURIComponent(window.location.href);
	var from = url.substring(url.indexOf("from=") + 5, url.indexOf("&"));
	var shares = url.substring(url.indexOf("shares=") + 7, url.lastIndexOf("&"));
	var symbol = url.substring(url.lastIndexOf("=") + 1, url.length);
	var html = "<p>Move " + shares + " "
				+ symbol + " shares from " + from
				+" to: </p><select class=\"select-style\"></select>"
				+ "<button class=\"button1 button-class move\">Move</button>";
	
	$('#content').append(html);

	var portofoliosUrl= "http://localhost:8080/portofolioNames?name=";
	username = document.getElementById("user");

    if (username != null) {
        portofoliosUrl += username.innerHTML;
    }

	$.getJSON(portofoliosUrl, function(data) {
        addPortofolios(data, from, shares, symbol);
    })
});

function addPortofolios(portofolios, from, shares, symbol) {
    var htmlText = "";

    for (i in portofolios) {
    	if (portofolios[i] != from)
	        htmlText += "<option>" + portofolios[i]
	        + "</option>\n";
    }
    $('.select-style').empty();
    $('.select-style').append(htmlText);

    $('.move').click(function() {
    	var url = "http://localhost:8080/move-shares?name=";
    	if (username != null)
    		url += username.innerHTML + "&from=" + from + "&to=" + $('.select-style').val()
    		+ "&symbol=" + symbol + "&shares=" + shares;

    	// console.log(url);
    	$.getJSON(url, function(data) {
    		console.log(data);
    		var redirectUrl = window.location.href;
    		redirectUrl = redirectUrl.substring(0, redirectUrl.lastIndexOf("/"));

    		if (data === true) {
    			redirectUrl += "/portofolio";
    		}
    		else {
    			redirectUrl += "/error";
    		}
    		// console.log(redirectUrl);
    		window.location.replace(redirectUrl);
    	})
    })
}