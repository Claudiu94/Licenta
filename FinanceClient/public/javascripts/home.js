var dataUrl = "http://localhost:8080/history?symbol=";
var titleValues = {
    "GC=F": "Gold (USD)",
    "SI=F": "Silver (USD)",
    "CL=F": "Oil(price per baril in USD)",
    "USDEUR=X": "USD to EUR Exchange Rate History",
    "EURUSD=X": "EUR to USD Exchange Rate History",
    "USDRON=X": "USD to RON Exchange Rate History",
    "RONUSD=X": "RON to USD Exchange Rate History",
    "EURRON=X": "EUR to RON Exchange Rate History",
    "RONEUR=X": "RON to EUR Exchange Rate History"
}

$(document).ready(function(){
	var localObj = JSON.parse($("#myLocalDataObj").val());

    createUrlAndRender("GC=F");

	$(".table-row").click(function(e){
		var id = e.target.parentNode.id;
		var name = e.target.parentNode.children[1].innerHTML;
		var url = "http://localhost:3000/details?symbol=" + id + "&name=" + name;
		window.location.href = url;
    });
});

function createUrlAndRender(symbol) {
    // var oilURL1 = "https://l1-query.finance.yahoo.com/v8/finance/chart/CL=F?period2="
    // var oilURL2 = Math.floor(Date.now() / 1000) + "&period1=1496448000&interval=1h&indicators=quote&includeTimestamps=true&includePrePost=true&events=div|split|earn&corsDomain=finance.yahoo.com"
    // $.getJSON("jsonfiles/oil.json", function(json) {
    //     var timestamps = json.chart.result[0].timestamp;
    //     var values = json.chart.result[0].indicators.quote[0].high;
    //     var finalValues = [];
    //     var defaultValue;

    //     for (i = 0; i < values.length; i++) {
    //         if(values[i] != null || values[i] != "") {
    //             defaultValue = values[i];
    //             break;
    //         }
    //     }
    //     for (i = 0; i < values.length; i++) {
    //         finalValues[i] = [];
    //         finalValues[i][0] = timestamps[i] * 1000;
    //         if (values[i] == null || values[i] == "") {
    //             finalValues[i][1] = defaultValue
    //         }
    //         else {
    //             finalValues[i][1] = values[i];
    //             defaultValue = values[i];
    //         }
    //     }
    //     console.log(defaultValue)
    //     oilChart(finalValues);
    // });
    
    var oilURL = dataUrl + symbol;

    $.getJSON(oilURL, function (data) {
        renderChart(data, symbol);
    });
}

function addMenuButtons() {
    Highcharts.getOptions().exporting.buttons.contextButton.menuItems.push(
        {
            text: 'Gold',
            onclick: function () {
                createUrlAndRender("GC=F")
            }
        },

        {
            text: 'Silver',
            onclick: function () {
                createUrlAndRender("SI=F")
            }
        },

        {
            text: 'Oil',
            onclick: function () {
                createUrlAndRender("CL=F")
            }
        },

        {
            text: 'USD/EUR',
            onclick: function () {
                createUrlAndRender("USDEUR=X")
            }
        },

        {
            text: 'EUR/USD',
            onclick: function () {
                createUrlAndRender("EURUSD=X")
            }
        },

        {
            text: 'USD/RON',
            onclick: function () {
                createUrlAndRender("USDRON=X")
            }
        },

        {
            text: 'RON/USD',
            onclick: function () {
                createUrlAndRender("RONUSD=X")
            }
        },

        {
            text: 'EUR/RON',
            onclick: function () {
                createUrlAndRender("EURRON=X")
            }
        },

        {
            text: 'RON/EUR',
            onclick: function () {
                createUrlAndRender("RONEUR=X")
            }
        },

    );
}

function renderChart(data, symbol) {
    // console.log(titleValues[symbol]);
    Highcharts.stockChart('container', {

        events: {
            load: addMenuButtons()
        },

        rangeSelector: {
            selected: 1
        },

        title: {
            text: titleValues[symbol]
        },

        yAxis: {
            title: {
                text: titleValues[symbol]
            },
        },

        series: [{
            name: symbol,
            data: data,
            tooltip: {
                valueDecimals: 4
            }
        }]
    });
   
}

function exchangeRateDefaultRender() {

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
    });
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
