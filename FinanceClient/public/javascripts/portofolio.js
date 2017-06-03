var username = null;
var dataUrl = "http://localhost:8080/portofolio?name=";
var portofoliosUrl= "http://localhost:8080/portofolioNames?name=";

$(document).ready(function() {
    username = document.getElementById("user");

    if (username != null) {
        dataUrl += username.innerHTML;
        portofoliosUrl += username.innerHTML;
    }

    $.getJSON(portofoliosUrl, function(data) {
        addPortofolios(data);
    })

    $('.button1').click(function() {
        var txt = $('.button1').text();

        if (txt == 'New Portofolio') {
            $('.pname').css("display", "inline-block");
            $('.button1').html('Create Portofolio');
        }
        else {
            $('.pname').css("display", "none");
            $('.button1').html('New Portofolio');
        }

    })

    $('.select-style').change(function() {
        getPortofolioData();
    })
});

function plot(data) {
    Highcharts.chart('container', {
        chart: {
            plotBackgroundColor: null,
            plotBorderWidth: null,
            plotShadow: false,
            type: 'pie'
        },
        title: {
            text: 'My Portofolio'
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
                    style: {
                        color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
                    }
                }
            }
        },
        // series: [{
        //     name: 'Brands',
        //     colorByPoint: true,
        //     data: [{
        //         name: 'Microsoft Internet Explorer',
        //         y: 56.33
        //     }, {
        //         name: 'Chrome',
        //         y: 24.03,
        //         sliced: true,
        //         selected: true
        //     }, {for(obj in data) {}
        //         name: 'Firefox',
        //         y: 10.38
        //     }, {
        //         name: 'Safari',
        //         y: 4.77
        //     }, {
        //         name: 'Opera',
        //         y: 0.91
        //     }, {
        //         name: 'Proprietary or Undetectable',
        //         y: 0.2
        //     }]
        // }]
        series: [{
            name: 'My Portofolio',
            colorByPoint: true,
            data: data
        }]
    });
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
    console.log(processedData)
    var allData = processedData[0].concat(processedData[1]);
    var tbody = $('.results').children().eq(1);
    var postLink = "http://localhost:8080/sellBuyShares";

    $('.results').children().eq(1).empty()
    // console.log(username.innerHTML);
    if (username != null)
        postLink = postLink + "/" + username.innerHTML;
    else
        postLink = postLink + "/Claudiu_94";    

    for (i in allData) {
        var form = "<form method=\"post\" action=\"" + postLink
        + "\"><input type=\"text\" name=\"sharesNumber\" class=\"search-str\" value=\"\"/>"
        + "<input type=\"hidden\" name=\"symbol\" value=\"" + allData[i].name
        + "\"><button type=\"submit\" name=\"type\" value=\"buy\">Buy</button>"
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
        + "<button class=\"move\" name=\"type\" value=\"move\">Move</button>"
        + "</td></tr>"
        tbody.append(row);
    }
    $(".search-str").keyup(function() {
        var nrShares = parseInt($(document.activeElement).val(), 10);
        var parent = $(document.activeElement).parent();
        var existingShares = parseInt(parent.parent().parent().children()[4].firstChild.data, 10);
        var sellButton = parent.parent().children()[0][3];

        if (!Number.isNaN(nrShares) && nrShares > 0 && existingShares >= nrShares) {
            $(sellButton).prop("disabled", false);
        }
        else {
            $(sellButton).prop("disabled", true);
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
    $('.select-style').append(htmlText);

    getPortofolioData();
}

function getPortofolioData() {
    var dataUrl1 = dataUrl;

    if ($('.select-style').val() != null) {
        dataUrl1 += "&portofolio=" + $('.select-style').val();
    }
    console.log(dataUrl1)
    $.getJSON(dataUrl1, function (data) {
        var processedData = retreiveData(data.sharesList);
        plot(processedData[0]);
        populateTable(processedData);        
    });
}