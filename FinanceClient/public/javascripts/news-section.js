$(document).ready(function(){
	// $.get('https://news.google.com/news/section?ned=us&topic=b', function(data) {
	// 	console.log($(data).find('.section-stream'))
	// 	// $('#content').html($(data).find('.section-stream').html())
	// })

	$.ajax({
    	xhrFields: {
        	withCredentials: true
    	},
    	crossDomain:true,
    	url: "https://www.google.com/finance/market_news"
	}).done(function (data) {
   		// console.log($(data).find('#news-main').html());
   		$('#content').html($(data).find('#news-main').html())
	});
});