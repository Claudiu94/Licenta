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
});
