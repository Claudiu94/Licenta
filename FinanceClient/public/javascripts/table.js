$(document).ready(function() {
	$(".search").keyup(function () {
		var searchTerm = $(".search").val();
		var listItem = $('.results tbody').children('tr');
		var searchSplit = searchTerm.replace(/ /g, "'):containsi('")
		
		$.extend($.expr[':'], {'containsi': function(elem, i, match, array){
					return (elem.textContent || elem.innerText || '').toLowerCase().indexOf((match[3] || "").toLowerCase()) >= 0;
			}
		});
			
		$(".results tbody tr").not(":containsi('" + searchSplit + "')").each(function(e){
			$(this).attr('visible','false');
		});

		$(".results tbody tr:containsi('" + searchSplit + "')").each(function(e){
			$(this).attr('visible','true');
		});

		var jobCount = $('.results tbody tr[visible="true"]').length;
			$('.counter').text(jobCount + ' item');

		if(jobCount == '0') {$('.no-result').show();}
		else {$('.no-result').hide();}
	});

	$(".table-row").click(function(e){
		var id = e.target.parentNode.id;
		var name = e.target.parentNode.children[1].innerHTML;
		var url = "http://localhost:3000/details?symbol=" + id + "&name=" + name;
		console.log(url);
		window.location.href = url;
    });
});