var seriesOptions = [], smaData = [], bbUpperBand = [], bbLowerBand = [];
var url = window.location.href;
var sym = getParameterByName("symbol", url);
var name = getParameterByName("name", url);
var dataUrl = "http://localhost:8080/history?symbol=";
var seriesCounter = 0;
var symbols = [sym];
var username = null;
var buttonsAdded = false;
var sData, sma, upbb, lbb;

$(document).ready(function() {
    var localObj = JSON.parse($("#myLocalDataObj").val());
    
    if (sym == null) {
        symbols = ["AAPL"];
        name = "Apple Inc.";
        sym = "AAPL";
    }
    // $.getJSON(dataUrl, function (data) {
    //     // Create the chart
    //     Highcharts.stockChart('container', {


    //         rangeSelector: {
    //             selected: 1
    //         },

    //         title: {
    //             text: name + ' Stock Price'
    //         },

    //         series: [{
    //             name: sym + ' Stock Price',
    //             data: data,
    //             marker: {
    //                 enabled: true,
    //                 radius: 3
    //             },
    //             shadow: true,
    //             tooltip: {
    //                 valueDecimals: 2
    //             }
    //         }]
    //     });
    // });
    plot();
    username = document.getElementById("user");

    $('.create-btn').click(function() {
        $('.set-part').css('display', 'inline-block');
        $('.create-btn').css('display', 'none');

    })

    $('.cancel-btn').click(function() {
        $('.set-part').css('display', 'none');
        $('.create-btn').css('display', 'inline-block');
    })

    $('.set-btn').click(function() {
        var url = "http://localhost:8080/create-notification?user="
        var type = $('.select-style').val() == 'smaller' ? -1 : 1;

        if (username != null) {
            url += username.innerText;
            url += "&type=" + type + "&symbol=" + sym + "&price=" + $('.text').val();
            console.log(url)
            $.getJSON(url, function(data) {
                if (data == true)
                    $('.alert-info').show();
            })
        }

        $('.set-part').css('display', 'none');
        $('.create-btn').css('display', 'inline-block');
    })
});

function plot() {
    $.each(symbols, function (i, sym) {
        $.getJSON(dataUrl + sym, function (data) {
            sData = {
                name: sym,
                color: '#f90000',
                data: data,
                type: 'area',
                threshold: null,
                // fillColor: 'rgba(249, 0, 0, 0.2)',
                marker: {
                    enabled: true,
                    radius: 3
                },
                shadow: true,
                tooltip: {
                    valueDecimals: 2,
                    style: {fontSize: '11pt'}
                },

                fillColor: {
                    linearGradient: {
                        x1: 0,
                        y1: 0,
                        x2: 0,
                        y2: 1
                    },
                    stops: [
                        [0, Highcharts.getOptions().colors[1]],
                        [1, Highcharts.Color(Highcharts.getOptions().colors[1]).setOpacity(0).get('rgba')]
                    ]
                }
            };

            // As we're loading the data asynchronously, we don't know what order it will arrive. So
            // we keep a counter and create the chart when all the data is loaded.
            seriesCounter += 1;
            calculateSMA(data);
            calculateBB(data);

            sma = {
                name: 'Simple Moving Average',
                data: smaData,
                color: '#4292f4',
                marker: {
                    enabled: true,
                    radius: 3
                },
                shadow: true,
                tooltip: {
                    valueDecimals: 2,
                    style: {fontSize: '11pt'}
                },
            };

            lbb = {
                name: 'Lower BB',
                color: '#41f485',
                data: bbLowerBand,
                marker: {
                    enabled: true,
                    radius: 3
                },
                shadow: true,
                tooltip: {
                    valueDecimals: 2,
                    style: {fontSize: '11pt'}
                }
            };

            upbb = {
                name: 'Upper BB',
                color: '#f4b241',
                data: bbUpperBand,
                marker: {
                    enabled: true,
                    radius: 3
                },
                shadow: true,
                tooltip: {
                    valueDecimals: 2,
                    style: {fontSize: '11pt'}
                }
            };

            seriesOptions = [sData, sma]

            createChart();
        });
    });
}

function calculateSMA(data) {
    var sum = 0;
    for (i = 0; i < 20; i++) {
        sum += data[i][1];
        smaData[i] = data[i];
    }

    smaData[19] = []
    smaData[19][0] = data[19][0];
    smaData[19][1] = sum / 20;

    for (i = 20; i < data.length; i++) {
        sum = sum - data[i - 20][1] + data[i][1];
        smaData[i] = [];
        smaData[i][0] = data[i][0];
        smaData[i][1] = sum / 20;
    }
}

function calculateBB(data) {
    var j = 0;
    for(i = 0; i < data.length; i++) {
        bbLowerBand[j] = [];
        bbUpperBand[j] = [];
        bbLowerBand[j][0] = data[i][0]
        bbUpperBand[j][0] = data[i][0]

        var diff = Math.abs(data[i][1] - smaData[i][1]) * 2;
        bbLowerBand[j][1] = smaData[i][1] - diff;
        bbUpperBand[j][1] = smaData[i][1] + diff;
        j++;
    }
}

function getParameterByName(name, url) {
    if (!url) url = window.location.href;
    name = name.replace(/[\[\]]/g, "\\$&");
    var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}

function createChart() {

    var chart = Highcharts.stockChart('container', {

        events: {
            load: addMenuButtons()
        },

        rangeSelector: {
            selected: 4
        },

        title: {
            text: name + ' Stock Price'
        },

        xAxis: {
            labels: {
                style: {fontSize: '11pt'}
            }
        },

        yAxis: {
            labels: {
                formatter: function () {
                    return '$' + this.value;
                }
            },
            plotLines: [{
                value: 0,
                width: 2,
                color: 'silver'
            }]
        },

        plotOptions: {
            series: {
                // compare: 'percent',
                showInNavigator: true,
            }
        },

        tooltip: {
            // pointFormat: '<span style="color:{series.color}">{series.name}</span>: <b>{point.y}</b> ({point.change}%)<br/>',
            valueDecimals: 2,
            split: true,
            style: {fontSize: '11pt'}
        },

        series: seriesOptions
    });
}


function addMenuButtons() {
    if (!buttonsAdded) {
        Highcharts.getOptions().exporting.buttons.contextButton.menuItems.push(
            {
                text: 'Simple Moving Average',
                onclick: function () {
                    if (seriesOptions.length == 3 || seriesOptions.length == 1) {
                        seriesOptions.push(sma)
                    }
                    else {
                        var index = seriesOptions.indexOf(sma)
                        seriesOptions = removeAtIndex(index, seriesOptions);
                        // console.log(seriesOptions);
                    }
                    createChart();
                }
            },

            {
                text: 'Bollinger Bands',
                onclick: function () {
                    if (seriesOptions.length <= 2) {
                        seriesOptions.push(lbb);
                        seriesOptions.push(upbb);
                    }
                    else {
                        var index = seriesOptions.indexOf(lbb);
                        seriesOptions = removeAtIndex(index, seriesOptions);
                        seriesOptions = removeAtIndex(index, seriesOptions);
                    }
                    createChart();
                }
            }

        );
        buttonsAdded = true;
    }
}

function removeAtIndex(index, data) {
    var  j = 0, newData = [];

    for (i = 0; i < data.length; i++) 
       if (i != index) {
            newData[j] = data[i];
            j++;
        }
    return newData;
}
// https://www.highcharts.com/samples/data/jsonp.php?filename=aapl-c.json&callback=?
