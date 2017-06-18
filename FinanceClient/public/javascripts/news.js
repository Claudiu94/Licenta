var cachedData;
var idx;

$(document).ready(function(){
	var owl = $('.owl-carousel');
	owl.owlCarousel({
		items:5,
		loop:true,
		margin:20,
		autoplay:true,
		autoplayTimeout:2000,
		autoplayHoverPause:true
	});

	$.ajax({
    	xhrFields: {
        	withCredentials: true
    	},
    	crossDomain:true,
    	url: "https://www.google.com/finance/market_news"
	}).done(function (data) {
		var hrefs = $(data).find('.name');
		cachedData = hrefs;

		for (i = 0; i < 5; i++) {
			var id = ".item" + (i + 1);
			$(id).html($(hrefs[i]).html());
		}
		idx = 4;

		owl.on('changed.owl.carousel', function(e) {
			if (idx >= cachedData.length - 1) {
				idx = 0;
			}
			else {
				idx += 1;
			}
    		var id = ".item" + (idx + 1);
			$(id).html($(cachedData[idx]).html());
  		});
	});
});

