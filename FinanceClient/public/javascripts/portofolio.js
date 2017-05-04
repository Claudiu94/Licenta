
$(document).ready(function() {
    $.getJSON('http://localhost:8080/portofolio', function (data) {
        var processedData = retreiveData(data.shareList);
        plot(processedData[0]);
        populateTable(processedData);        
    });
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
            pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
        },
        plotOptions: {
            pie: {
                allowPointSelect: true,
                cursor: 'pointer',
                dataLabels: {
                    enabled: true,
                    format: '<b>{point.name}</b>: {point.percentage:.1f} %',
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
    var allData = processedData[0].concat(processedData[1]);
    var tbody = $('.results').children().eq(1);
    var form =     "<form method=\"post\" action=\"http://localhost:8080/sellBuyShares\">"
      + "<input type=\"text\" name=\"search-str\" id=\"search-str\" value=\"\"/>"
      + "<button type=\"submit\" name=\"buy\" value=\"buy\">Buy</button>"
      + "<button type=\"submit\" name=\"sell\" value=\"sell\">Sell</button>"
    + "</form>"

    for (i in allData) {
        console.log(allData[i]);
        var row = "<tr id="
                + allData[i].name
                + "class=\"table-row\">"
                + "<th class=\"row\">" + allData[i].name
                + "</th><td id=\"cname\">" + allData[i].companyName
                + "</td><td id=\"price\">" + allData[i].price
                + "</td><td id=\"currency\">" + allData[i].currency
                + "</td><td id=\"shares\">" + allData[i].shares
                + "</td><td id=\"buySell\">" + form
                + "</td></tr>"
        tbody.append(row);
    }


}