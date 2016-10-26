Highcharts.setOptions({global: {useUTC: false}});
function createSimpleColumnHighcharts(element, url, data, remark) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    $.ajax({
        type: "get",
        url: encodeURI(url + data),
        data: "",
        datatype: "json",
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        success: function (returnData) {
            $(element).highcharts({
                chart: {
                    type: 'column'
                },
                title: {
                    text: data
                },
                xAxis: {
                    type: 'category'
                },
                yAxis: {},
                legend: {
                    enabled: false
                },
                plotOptions: {
                    series: {
                        borderWidth: 0,
                        dataLabels: {
                            enabled: true,
                            format: '{point.y:.0f}'
                        }
                    }
                },

                tooltip: {
                    headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
                    pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>{point.y:.0f}</b><br/>'
                },

                series: [{
                    name: remark,
                    colorByPoint: true,
                    data: returnData
                }]
            });
        },
        error: function (errorMsg) {
            console.log(errorMsg);
        }
    });
}
function createColumnHighcharts(element, url, data, type) {
    $.ajax({
        type: "get",
        url: url + data,
        data: "",
        datatype: "json",
        success: function (returnData) {
            var JSONObject = returnData;
            $(element).highcharts({
                chart: {
                    type: 'column'
                },
                title: {
                    text: data.slice(0, 23) + type
                },
                xAxis: {
                    categories: JSONObject.cityNames
                },
                yAxis: {
                    min: 0,
                    title: {
                        text: ''
                    },
                    stackLabels: {
                        enabled: true,
                        style: {
                            fontWeight: 'bold',
                            color: (Highcharts.theme && Highcharts.theme.textColor) || 'gray'
                        }
                    }
                },
                tooltip: {
                    formatter: function () {
                        var total = 0;
                        $.each(this.points, function (i, point) {
                            total = total + point.y;
                        });
                        var s = '<strong>' + this.x + '总计 ' + total + '</strong>';

                        var sortedPoints = this.points.sort(function (a, b) {
                            return ((a.y < b.y) ? -1 : ((a.y > b.y) ? 1 : 0));
                        });
                        sortedPoints.reverse();
                        $.each(sortedPoints, function (i, point) {
                            if (point.y != 0) {
                                s += '<br/><span style="color:' + point.series.color + '"> ' + point.series.name + ' </span>: <b> ' + point.y + " (" + (point.y / total * 100).toFixed(0) + '%)';
                            }
                        });

                        return s;
                    },
                    shared: true
                },
                series: [{
                    name: '其它',
                    data: JSONObject.type0
                }, {
                    name: '青少年',
                    data: JSONObject.type1
                }, {
                    name: '商务',
                    data: JSONObject.type2
                }, {
                    name: '应试',
                    data: JSONObject.type3
                }, {
                    name: '综合',
                    data: JSONObject.type4
                }]

            });
        },
        error: function (errorMsg) {
            console.log(errorMsg);
        }
    });

}
function creteSimplePieHighChart(element, map) {
    var colors;
    if (map.size > 5) {
        colors = Highcharts.getOptions().colors.slice(0);
    }
    else {
        colors = Highcharts.getOptions().colors.slice(0);
        colors.shuffle();
    }
    var categories = [];
    var data = [];
    var index = 0;
    map.forEach(function (value, key) {
        if (key != "firstLevelSeriesName") {
            categories.push(key);
            var subData = {
                y: value.get("y"),
                color: colors[index],
                drilldown: {
                    name: value.get("drilldownName"),
                    categories: value.get("drilldownCategories"),
                    data: value.get("drilldownData"),
                    color: colors[index]
                }
            };
            data.push(subData);
            index++;
        }
    });

    var firstLevelData = [],
        i,
        j,
        dataLen = data.length,
        drillDataLen,
        brightness;

    for (i = 0; i < dataLen; i += 1) {
        firstLevelData.push({
            name: categories[i],
            y: data[i].y,
            color: data[i].color
        });
        drillDataLen = data[i].drilldown.data.length;
        for (j = 0; j < drillDataLen; j += 1) {
            brightness = 0.2 - (j / drillDataLen) / 5;
        }
    }

    element.highcharts({
        chart: {
            type: 'pie',
            options3d: {
                enabled: true,
                alpha: 45
            }
        },
        title: {
            text: ''
        },
        subtitle: {
            text: ''
        },
        yAxis: {
            title: {
                text: ''
            }
        },
        plotOptions: {
            pie: {
                innerSize: 100,
                depth: 45,
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
        tooltip: {
            pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
        },
        series: [{
            name: map.get("firstLevelSeriesName"),
            data: firstLevelData,
            size: '80%',
            dataLabels: {
                formatter: function () {
                    return this.y > 5 ? this.point.name : null;
                }
            }
        }]
    });
}
function createPieHighChart(element, map) {
    var colors;
    if (map.size > 5) {
        colors = Highcharts.getOptions().colors.slice(0);
    }
    else {
        colors = Highcharts.getOptions().colors.slice(0);
        colors.shuffle();
    }
    var categories = [];
    var data = [];
    var index = 0;
    map.forEach(function (value, key) {
        if (key != "firstLevelSeriesName" && key != "secondLevelSeriesName") {
            categories.push(key);
            var subData = {
                y: value.get("y"),
                color: colors[index],
                drilldown: {
                    name: value.get("drilldownName"),
                    categories: value.get("drilldownCategories"),
                    data: value.get("drilldownData"),
                    color: colors[index]
                }
            };
            data.push(subData);
            index++;
        }
    });

    var firstLevelData = [],
        secondLevelData = [],
        i,
        j,
        dataLen = data.length,
        drillDataLen,
        brightness;

    for (i = 0; i < dataLen; i += 1) {
        firstLevelData.push({
            name: categories[i],
            y: data[i].y,
            color: data[i].color
        });

        drillDataLen = data[i].drilldown.data.length;
        for (j = 0; j < drillDataLen; j += 1) {
            brightness = 0.2 - (j / drillDataLen) / 5;
            secondLevelData.push({
                name: data[i].drilldown.categories[j],
                y: data[i].drilldown.data[j],
                color: Highcharts.Color(data[i].color).brighten(brightness).get()
            });
        }
    }

    element.highcharts({
        chart: {
            type: 'pie'
        },
        title: {
            text: ''
        },
        subtitle: {
            text: ''
        },
        yAxis: {
            title: {
                text: ''
            }
        },
        plotOptions: {
            pie: {
                shadow: false,
                center: ['50%', '50%']
            }
        },
        tooltip: {
            valueSuffix: '%'
        },
        series: [{
            name: map.get("firstLevelSeriesName"),
            data: firstLevelData,
            size: '60%',
            dataLabels: {
                formatter: function () {
                    return this.y > 5 ? this.point.name : null;
                },
                color: '#ffffff',
                distance: -30
            }
        }, {
            name: map.get("secondLevelSeriesName"),
            data: secondLevelData,
            size: '80%',
            innerSize: '60%',
            dataLabels: {
                formatter: function () {
                    // display only if larger than 1
                    return this.y > 1 ? '<b>' + this.point.name + ':</b> ' + this.y + '%' : null;
                }
            }
        }]
    });

}

function createScatterHighChart(element, data) {
    element.highcharts({
        chart: {
            type: 'scatter',
            zoomType: 'xy'
        },
        title: {
            text: ''
        },
        subtitle: {
            text: ''
        },
        xAxis: {
            type: 'datetime',
            title: {
                enabled: false,
                text: ''
            },
            dateTimeLabelFormats: {
                second: '%H:%M'
            },
            startOnTick: false,
            endOnTick: true,
            showLastLabel: false
        },
        yAxis: {
            title: {
                text: ''
            }
        },
        tooltip: {
            formatter: function () {
                return Highcharts.dateFormat('%Y-%m-%d %H:%M',
                        new Date(this.x))
                    + '<br>' + this.y;
            }
        },
        plotOptions: {
            scatter: {
                marker: {
                    radius: 5,
                    states: {
                        hover: {
                            enabled: true,
                            lineColor: 'rgb(100,100,100)'
                        }
                    }
                },
                states: {
                    hover: {
                        marker: {
                            enabled: false
                        }
                    }
                }
            }
        },
        series: [{
            name: '',
            showInLegend: false,
            color: 'rgba(223, 83, 83, .5)',
            data: data
        }]
    });
}
