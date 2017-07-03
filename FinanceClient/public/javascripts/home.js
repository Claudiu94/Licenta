var dataUrl = "http://localhost:8080/history?symbol=";
var titleValues = {
    "GC=F": "Gold price in USD/oz (1oz = 28.35g)",
    "SI=F": "Silver, price in USD/oz (1oz = 28.35g)",
    "CL=F": "Oil (price in USD/baril)",
    "USDEUR=X": "USD to EUR Exchange Rate History",
    "EURUSD=X": "EUR to USD Exchange Rate History",
    "USDRON=X": "USD to RON Exchange Rate History",
    "RONUSD=X": "RON to USD Exchange Rate History",
    "EURRON=X": "EUR to RON Exchange Rate History",
    "RONEUR=X": "RON to EUR Exchange Rate History"
};

var yAxisValues = {
    "GC=F": "USD",
    "SI=F": "USD",
    "CL=F": "USD",
    "USDEUR=X": "EUR",
    "EURUSD=X": "USD",
    "USDRON=X": "RON",
    "RONUSD=X": "USD",
    "EURRON=X": "RON",
    "RONEUR=X": "EUR"
};

var currencyValues = {
    "GC=F": "$",
    "SI=F": "$",
    "CL=F": "$",
    "USDEUR=X": "€",
    "EURUSD=X": "$",
    "USDRON=X": "LEI",
    "RONUSD=X": "$",
    "EURRON=X": "LEI",
    "RONEUR=X": "€"
};

var buttonsAdded = false;

$(document).ready(function(){
	var localObj = JSON.parse($("#myLocalDataObj").val());

    createUrlAndRender("USDEUR=X");

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
    
    if (!buttonsAdded) {
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
            }

        );
        buttonsAdded = true;
    }
}

function renderChart(data, symbol) {
    // console.log(titleValues[symbol]);

    var startDate = new Date(data[data.length - 1][0]), // Get year of last data point
            minRate = data[data.length - 1][1],
            maxRate = minRate,
            startPeriod,
            date,
            rate,
            index;
        // console.log(data);
    startDate.setMonth(startDate.getMonth() - 24); // 2 years before last data point
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
                text: yAxisValues[symbol]
            },
            labels: {
                    formatter: function () {
                        if (currencyValues[symbol] == "LEI") {
                            return this.value + "LEI";
                        }
                        else
                            return currencyValues[symbol] + this.value;
                    }
            },
            plotLines: [{
                value: minRate,
                color: 'green',
                dashStyle: 'shortdash',
                width: 2,
                label: {
                    text: 'Minimum value'
                }
            }, {
                value: maxRate,
                color: 'red',
                dashStyle: 'shortdash',
                width: 2,
                label: {
                    text: 'Maximum value'
                }
            }]
        },

        series: [{
            name: symbol,
            data: data,
            type: 'area',
            threshold: null,
            tooltip: {
                valueDecimals: 4
            },

             fillColor: {
                linearGradient: {
                    x1: 0,
                    y1: 0,
                    x2: 0,
                    y2: 1
                },
                stops: [
                    [0, Highcharts.getOptions().colors[0]],
                    [1, Highcharts.Color(Highcharts.getOptions().colors[0]).setOpacity(0).get('rgba')]
                ]
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
                    text: 'USD'
                },
                labels: {
                    formatter: function () {
                        return '$' + this.value;
                    }
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
