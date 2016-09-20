Highcharts.setOptions({global: {useUTC: false}});
function createPieHighChart(element, map) {
    var colors = Highcharts.getOptions().colors;
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
