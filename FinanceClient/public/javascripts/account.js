console.log("Script");
$(document).ready(function(){
  $('.message a').click(function(){
    console.log("in function");
    $('form').animate({height: "toggle", opacity: "toggle"}, "slow");
  });
});