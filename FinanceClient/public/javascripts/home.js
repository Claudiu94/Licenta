$(document).ready(function(){
	var localObj = JSON.parse($("#myLocalDataObj").val());

	$.getJSON('https://www.highcharts.com/samples/data/jsonp.php?filename=usdeur.json&callback=?', function (data) {

    var startDate = new Date(data[data.length - 1][0]), // Get year of last data point
        minRate = 1,
        maxRate = 0,
        startPeriod,
        date,
        rate,
        index;
    // console.log(data);
    startDate.setMonth(startDate.getMonth() - 3); // a quarter of a year before last data point
    startPeriod = Date.UTC(startDate.getFullYear(), startDate.getMonth(), startDate.getDate());

    for (index = data.length - 1; index >= 0; index = index - 1) {
        date = data[index][0]; // data[i][0] is date
        rate = data[index][1]; // data[i][1] is exchange rate
        if (date < startPeriod) {
            break; // stop measuring highs and lows
        }
        if (rate > maxRate) {
            maxRate = rate;
        }
        if (rate < minRate) {
            minRate = rate;
        }
    }
    // exchangeRate(data, minRate, maxRate);
    renderOilChart();
    
})

	$(".table-row").click(function(e){
		var id = e.target.parentNode.id;
		var name = e.target.parentNode.children[1].innerHTML;
		var url = "http://localhost:3000/details?symbol=" + id + "&name=" + name;
		window.location.href = url;
    });
});

function exchangeRate(data, minRate, maxRate) {
    console.log(data);
    // Create the chart
    Highcharts.stockChart('container', {

        rangeSelector: {
            selected: 1
        },

        title: {
            text: 'USD to EUR exchange rate'
        },

        yAxis: {
            title: {
                text: 'Exchange rate'
            },
            plotLines: [{
                value: minRate,
                color: 'green',
                dashStyle: 'shortdash',
                width: 2,
                label: {
                    text: 'Last quarter minimum'
                }
            }, {
                value: maxRate,
                color: 'red',
                dashStyle: 'shortdash',
                width: 2,
                label: {
                    text: 'Last quarter maximum'
                }
            }]
        },

        series: [{
            name: 'USD to EUR',
            data: data,
            tooltip: {
                valueDecimals: 4
            }
        }]
    })
}

function renderOilChart() {
    var oilURL1 = "https://l1-query.finance.yahoo.com/v8/finance/chart/CL=F?period2="
    var oilURL2 = Math.floor(Date.now() / 1000) + "&period1=1496448000&interval=1h&indicators=quote&includeTimestamps=true&includePrePost=true&events=div|split|earn&corsDomain=finance.yahoo.com"
    var oilURL = oilURL1 + oilURL2;
    $.getJSON("jsonfiles/oil.json", function(json) {
        var timestamps = json.chart.result[0].timestamp;
        var values = json.chart.result[0].indicators.quote[0].high;
        var finalValues = [];
        var defaultValue;

        for (i = 0; i < values.length; i++) {
            if(values[i] != null || values[i] != "") {
                defaultValue = values[i];
                break;
            }
        }
        for (i = 0; i < values.length; i++) {
            finalValues[i] = [];
            finalValues[i][0] = timestamps[i] * 1000;
            if (values[i] == null || values[i] == "") {
                finalValues[i][1] = defaultValue
            }
            else {
                finalValues[i][1] = values[i];
                defaultValue = values[i];
            }
        }
        console.log(defaultValue)
        oilChart(finalValues);
    });
}


function oilChart(data) {
   Highcharts.stockChart('container', {

        rangeSelector: {
            selected: 1
        },

        title: {
            text: 'Oil price(per baril)'
        },

        yAxis: {
            title: {
                text: 'Oil price'
            },
        },

        series: [{
            name: 'Price per baril',
            data: data,
            tooltip: {
                valueDecimals: 4
            }
        }]
    })
}

function readTextFile(file, callback) {
    var rawFile = new XMLHttpRequest();
    rawFile.overrideMimeType("application/json");
    rawFile.open("GET", file, true);
    rawFile.onreadystatechange = function() {
        if (rawFile.readyState === 4 && rawFile.status == "200") {
            callback(rawFile.responseText);
        }
    }
    rawFile.send(null);
}
