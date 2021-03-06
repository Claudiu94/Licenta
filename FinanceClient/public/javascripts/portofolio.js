var username = null;
var dataUrl = "http://localhost:8080/portofolio?name=";
var portofoliosUrl= "http://localhost:8080/portofolioNames?name=";

$(document).ready(function() {
    username = document.getElementById("user");
    if (username != null) {
        dataUrl += username.innerText;
        portofoliosUrl += username.innerText;
    }
    console.log(portofoliosUrl);
    $.getJSON(portofoliosUrl, function(data) {
        addPortofolios(data);
    })

    $('.create-btn').click(function() {
        var txt = $('.create-btn').text();

        if (txt == 'New Portofolio') {
            $('.pname').css("display", "inline-block");
            $('.create-btn').html('Create Portofolio');
        }
        else {
            var url = "http://localhost:8080/create-portofolio?user="
            if (username != null)
                url += username.innerText;
            url += "&portofolioName=" + $('.pname').val();

            $.getJSON(url, function(data) {
                addPortofolios(data);
            })
            $('.pname').css("display", "none");
            $('.create-btn').html('New Portofolio');
        }

    })

    $('.select-style').change(function() {
        getPortofolioData();
    })

    $('.delete-btn').click(function() {
        // deletePortofolio();
    })
});

function deletePortofolio() {
    var url = "http://localhost:8080/delete-portofolio?user="
    
    if (username != null)
        url += username.innerText;

    url += "&portofolioName=" + $('.select-style').val();
    $.getJSON(url, function(data) {
        location.reload();
    })
}

function plot(data) {

    Highcharts.chart('container', {
        events: {
            load: addTheme()
        },
        chart: {
            plotBackgroundColor: null,
            plotBorderWidth: null,
            plotShadow: false,
            type: 'pie'
        },
        title: {
            text: $('.select-style').val()
        },
        tooltip: {
            pointFormat: 'Percentage: <b>{point.percentage:.1f}%</b> <br/> Price per share: <b>{point.price}</b>'
        },
        plotOptions: {
            pie: {
                allowPointSelect: true,
                cursor: 'pointer',
                dataLabels: {
                    enabled: true,
                    format: '<b>{point.companyName}</b>: {point.percentage:.1f} %',
                    // style: {
                    //     color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
                    // }
                },
                showInLegend: true
            }
        },
        series: [{
            name: 'My Portofolio',
            colorByPoint: true,
            data: data
        }]
    });
}

function addTheme() {
        Highcharts.theme = {
        colors: ['#f45b5b', '#8085e9', '#8d4654', '#7798BF', '#aaeeee', '#ff0066', '#eeaaee',
            '#55BF3B', '#DF5353', '#7798BF', '#aaeeee'],
        chart: {
            backgroundColor: null,
            style: {
                fontFamily: 'Signika, serif'
            }
        },
        title: {
            style: {
                color: 'black',
                fontSize: '16px',
                fontWeight: 'bold'
            }
        },
        subtitle: {
            style: {
                color: 'black'
            }
        },
        tooltip: {
            borderWidth: 0
        },
        legend: {
            itemStyle: {
                fontWeight: 'bold',
                fontSize: '13px'
            }
        },
        xAxis: {
            labels: {
                style: {
                    color: '#6e6e70'
                }
            }
        },
        yAxis: {
            labels: {
                style: {
                    color: '#6e6e70'
                }
            }
        },
        plotOptions: {
            series: {
                shadow: true
            },
            candlestick: {
                lineColor: '#404048'
            },
            map: {
                shadow: false
            }
        },

        // Highstock specific
        navigator: {
            xAxis: {
                gridLineColor: '#D0D0D8'
            }
        },
        rangeSelector: {
            buttonTheme: {
                fill: 'white',
                stroke: '#C0C0C8',
                'stroke-width': 1,
                states: {
                    select: {
                        fill: '#D0D0D8'
                    }
                }
            }
        },
        scrollbar: {
            trackBorderColor: '#C0C0C8'
        },

        // General
        background2: '#E0E0E8'

    };

    // Apply the theme
    Highcharts.setOptions(Highcharts.theme);
}

function retreiveData(rawData) {
    var processedData = [], j = 0, processedData1 = [], k = 0;
    for(i in rawData) {
        if (rawData[i].shares > 0) {
            processedData[j] = rawData[i];
            j++;
        } else {
            processedData1[k] = rawData[i];
            k++;
        }
    }
    return [processedData, processedData1];
}

function populateTable(processedData) {
    // console.log(processedData)
    var allData = processedData[0].concat(processedData[1]);
    var tbody = $('.results').children().eq(1);
    var postLink = "http://localhost:8080/sellBuyShares";

    $('.results').children().eq(1).empty()
    // console.log(username.innerText);
    if (username != null)
        postLink = postLink + "/" + username.innerText;
    else
        postLink = postLink + "/Claudiu_94";    

    for (i in allData) {
        var form = "<form method=\"post\" action=\"" + postLink
        + "\"><input type=\"text\" name=\"sharesNumber\" class=\"search-str\" value=\"\"/>"
        + "<input type=\"hidden\" name=\"symbol\" value=\"" + allData[i].name
        + "\"><button type=\"submit\" name=\"type\" value=\"buy\" disabled>Buy</button>"
        + "<button type=\"submit\" name=\"type\" value=\"sell\" disabled>Sell</button>"
        + "<input type=\"hidden\" name=\"portofolio\" value=\"" + $('.select-style').val()
        + "\"</input></form>"
        var row = "<tr id=\""
        + allData[i].name
        + "\" class=\"table-row\">"
        + "<th class=\"row\">" + allData[i].name
        + "</th><td id=\"cname\">" + allData[i].companyName
        + "</td><td id=\"price\">" + allData[i].price
        + "</td><td id=\"currency\">" + allData[i].currency
        + "</td><td id=\"shares\">" + allData[i].shares
        + "</td><td id=\"buySell\">" + form
        + "<button class=\"move\" name=\"type\" value=\"move\" disabled>Move</button>"
        + "</td></tr>"
        tbody.append(row);
    }
    $(".search-str").keyup(function() {
        var nrShares = parseInt($(document.activeElement).val(), 10);
        var parent = $(document.activeElement).parent();
        var existingShares = parseInt(parent.parent().parent().children()[4].firstChild.data, 10);
        var sellButton = parent.parent().children()[0][3];
        var moveButton = parent.parent().children()[1];
        var buyButton = parent.parent().children()[0][2];

        if (!Number.isNaN(nrShares) && nrShares > 0) {
            $(buyButton).prop("disabled", false);
        }
        else {
            $(buyButton).prop("disabled", true);
        }

        if (!Number.isNaN(nrShares) && nrShares > 0 && existingShares >= nrShares) {
            $(sellButton).prop("disabled", false);
            $(moveButton).prop("disabled", false);
        }
        else {
            $(sellButton).prop("disabled", true);
            $(moveButton).prop("disabled", true);
        }
    });

    $('.move').click(function() {
        var redirectUrl = window.location.href;
        var shares = $(document.activeElement).parent().parent().children()[5].children[0];
        var sym = $(document.activeElement).parent().parent()[0];
        
        sym = $(sym).attr('id');
        shares = $(shares)[0][0].value

        redirectUrl = redirectUrl.substring(0, redirectUrl.lastIndexOf("/"));
        redirectUrl += "/move-shares?from=" + $('.select-style').val() + "&shares=" + shares +"&sym=" + sym;
        window.location.replace(redirectUrl);
    })
}

function addPortofolios(portofolios) {
    var htmlText = "";

    for (i in portofolios) {
        htmlText += "<option>" + portofolios[i]
        + "</option>\n";
    }
    $('.select-style').empty();
    $('.select-style').append(htmlText);

    getPortofolioData();
}

function getPortofolioData() {
    var dataUrl1 = dataUrl;

    if ($('.select-style').val() != null) {
        dataUrl1 += "&portofolio=" + $('.select-style').val();
    }

    $.getJSON(dataUrl1, function (data) {
        var processedData = retreiveData(data.sharesList);
        plot(processedData[0]);
        populateTable(processedData);        
    });
}