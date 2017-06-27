var seriesOptions = [];
var url = window.location.href;
var sym = getParameterByName("symbol", url);
var name = getParameterByName("name", url);
var dataUrl = "http://localhost:8080/history?symbol=";
var seriesCounter = 0;
var symbols = [sym];
var username = null;

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
            seriesOptions[i] = {
                name: sym,
                data: data,
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

            // As we're loading the data asynchronously, we don't know what order it will arrive. So
            // we keep a counter and create the chart when all the data is loaded.
            seriesCounter += 1;
            var smaData = calculateSMA(data);
            i += 1;
            // console.log(data)
            // console.log(data.length)

            seriesOptions[i] = {
                name: 'Simple Moving Average',
                data: smaData,
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

            createChart();
        });
    });
}

function calculateSMA(data) {
    var smaData = [];
    var sum = 0;
    for (i = 0; i < 9; i++) {
        sum += data[i][1];
        smaData[i] = data[i];
    }

    smaData[8] = []
    smaData[8][0] = data[8][0];
    smaData[8][1] = sum / 9;

    for (i = 9; i < data.length; i++) {
        sum = sum - data[i - 9][1] + data[i][1];
        smaData[i] = [];
        smaData[i][0] = data[i][0];
        smaData[i][1] = sum / 9;
    }
    
    // console.log(smaData)
    // console.log(smaData.length)
    
    return smaData;
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
                    return (this.value > 0 ? ' + ' : '') + this.value + '%';
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
                compare: 'percent',
                showInNavigator: true
            }
        },

        tooltip: {
            pointFormat: '<span style="color:{series.color}">{series.name}</span>: <b>{point.y}</b> ({point.change}%)<br/>',
            valueDecimals: 2,
            split: true,
            style: {fontSize: '11pt'}
        },

        series: seriesOptions
    });
}

// https://www.highcharts.com/samples/data/jsonp.php?filename=aapl-c.json&callback=?
