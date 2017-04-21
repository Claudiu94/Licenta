var express = require('express');
var request = require('request');
var router = express.Router();
var username = {};

/* GET home page. */
router.get('/', function(req, res, next) {
	console.log("enters in /!!");
	var params = req.query;
	
	if (typeof params.username !== 'undefined' && params.username !== null)
		req.session.username = params.username;
	else if (typeof params.err !== 'undefined' && params.err !== null)
		req.session.err= params.err;
	
	console.log(req.session);
	res.redirect('home');
});

router.get('/home', function(req, res, next) {
	console.log("In /home");
	request('http://localhost:8080/stocks', function (error, response, body) {
    	if (!error && response.statusCode == 200) {
    		var data = JSON.parse(body);
    		console.log(data)
       	 	console.log(data.companies) // Print the google web page.
        	res.render('home', {'session': req.session, 'companies': data.companies});
     	}
     	else {
     		res.render('home', {'session': req.session});
     	}
	})
});

router.get('/account', function(req, res, next) {
	res.render('account', {'session': req.session})
});

router.get('/details', function(req, res, next) {
	res.render('details', {'session': req.session})
});

router.post('/account', function(req, res, next) {
	console.log(Object.keys(req.body).length)
	if (Object.keys(req.body).length) {
		console.log(req.body);
		res.render('home', {'session': req.session});
	}
});

module.exports = router;
