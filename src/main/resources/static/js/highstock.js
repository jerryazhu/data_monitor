function createHighStock(element, url, map, duration){
    var seriesOptions = [],
        tooltipOptions = "",
        seriesCounter = 0,
        names = map.keys();
    function createChart() {
        element.highcharts('StockChart', {
            xAxis: {
                type: 'datetime',
                dateTimeLabelFormats: {
                    second: '%Y-%m-%d<br/>%H:%M:%S',
                    minute: '%Y-%m-%d<br/>%H:%M',
                    hour: '%Y-%m-%d<br/>%H:%M',
                    day: '%Y<br/>%m-%d',
                    week: '%Y<br/>%m-%d',
                    month: '%Y-%m',
                    year: '%Y'
                }
            },
            rangeSelector: {
                selected: 4
            },
            navigator: {
                enabled: false
            },
            tooltip: tooltipOptions,

            series: seriesOptions
        });
    }

    $.each(names, function (i, name) {
        $.getJSON(url+map.get(name), function (data) {
            if (duration == "month") {
                seriesOptions[i] = {
                    name: name,
                    data: data,
                    dataGrouping: {
                        approximation: "sum",
                        enabled: true,
                        forced: true,
                        units: [['month', [1]]]
                    }
                };
                tooltipOptions = {
                    formatter: function () {
                        var total = 0;
                        $.each(this.points, function (i, point) {
                            total = total + point.y;
                        });
                        var monthDays = getLastDay(Highcharts.dateFormat('%Y', new Date(this.x)),Highcharts.dateFormat('%m', new Date(this.x)));
                        var s = '<strong>' + Highcharts.dateFormat('%Y-%m', new Date(this.x))  + '</strong>';

                        var sortedPoints = this.points.sort(function (a, b) {
                            return ((a.y < b.y) ? -1 : ((a.y > b.y) ? 1 : 0));
                        });
                        sortedPoints.reverse();
                        $.each(sortedPoints, function (i, point) {
                            s += '<br/><span style="color:' + point.series.color + '"> ' + point.series.name + ' </span>: <b> ' + point.y +"("+Math.floor(point.y/total*100)+"%)" + " avg:" + Math.floor(point.y/monthDays) ;
                        });

                        return s;
                    },
                    valueDecimals: 0
                };
            }
            else if(duration == "week") {
                seriesOptions[i] = {
                    name: name,
                    data: data,
                    dataGrouping: {
                        approximation: "sum",
                        enabled: true,
                        forced: true,
                        units: [['week', [1]]]
                    }
                };
                tooltipOptions = {
                    formatter: function () {
                        var total = 0;
                        $.each(this.points, function (i, point) {
                            total = total + point.y;
                        });
                        var s = '<strong>' + Highcharts.dateFormat('%Y-%m-%d', new Date(this.x))  + '</strong>';

                        var sortedPoints = this.points.sort(function (a, b) {
                            return ((a.y < b.y) ? -1 : ((a.y > b.y) ? 1 : 0));
                        });
                        sortedPoints.reverse();
                        $.each(sortedPoints, function (i, point) {
                            s += '<br/><span style="color:' + point.series.color + '"> ' + point.series.name + ' </span>: <b> ' + point.y +"("+Math.floor(point.y/total*100)+"%)" + " avg:" + Math.floor(point.y/7) ;
                        });

                        return s;
                    },
                    valueDecimals: 0
                };
            }
            else {
                seriesOptions[i] = {
                    name: name,
                    data: data
                };
                tooltipOptions = {
                    formatter: function () {
                        var total = 0;
                        $.each(this.points, function (i, point) {
                            total = total + point.y;
                        });
                        var s = '<strong>' + Highcharts.dateFormat('%Y-%m-%d', new Date(this.x))  + '</strong>';

                        var sortedPoints = this.points.sort(function (a, b) {
                            return ((a.y < b.y) ? -1 : ((a.y > b.y) ? 1 : 0));
                        });
                        sortedPoints.reverse();
                        $.each(sortedPoints, function (i, point) {
                            s += '<br/><span style="color:' + point.series.color + '"> ' + point.series.name + ' </span>: <b> ' + point.y +"("+Math.floor(point.y/total*100)+"%)";
                        });

                        return s;
                    },
                    valueDecimals: 0
                };
            }
            // As we're loading the data asynchronously, we don't know what order it will arrive. So
            // we keep a counter and create the chart when all the data is loaded.
            seriesCounter += 1;

            if (seriesCounter === names.length) {
                createChart();
            }
        });
    });
}
