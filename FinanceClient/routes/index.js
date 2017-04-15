var express = require('express');
var router = express.Router();

/* GET home page. */
router.get('/', function(req, res, next) {
	res.render('index');
});

router.get('/home', function(req, res, next) {
	res.render('index')
});

router.get('/account', function(req, res, next) {
	res.render('account')
});

router.post('/account', function(req, res, next) {
	console.log(Object.keys(req.body).length)
	if (Object.keys(req.body).length) {
		console.log(req.body);
		res.render('index');
	}
});

module.exports = router;
