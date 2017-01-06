function createSimpleHighStock(element, url, seriesName, duration) {
    $.getJSON(url, function (returnData) {
        var seriesOptions = [];
        if (duration == "month") {
            seriesOptions = [{
                name: seriesName,
                data: returnData,
                tooltip: {
                    valueDecimals: 0
                },
                dataGrouping: {
                    approximation: "sum",
                    enabled: true,
                    forced: true,
                    units: [['month', [1]]]
                }
            }];
        }
        else if (duration == "week") {
            seriesOptions = [{
                name: seriesName,
                data: returnData,
                tooltip: {
                    valueDecimals: 0
                },
                dataGrouping: {
                    approximation: "sum",
                    enabled: true,
                    forced: true,
                    units: [['week', [1]]]
                }
            }];
        }
        else {
            seriesOptions = [{
                name: seriesName,
                data: returnData,
                tooltip: {
                    valueDecimals: 0
                }
            }];
        }
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

            title: {
                text: ''
            },
            navigator: {
                enabled: false
            },
            series: seriesOptions
        });
    });
}

function createHighStock(element, url, map, duration) {
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
        $.getJSON(url + map.get(name), function (data) {
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
                        var monthDays = getLastDay(Highcharts.dateFormat('%Y', new Date(this.x)), Highcharts.dateFormat('%m', new Date(this.x)));
                        var s = '<strong>' + Highcharts.dateFormat('%Y-%m', new Date(this.x)) + '</strong>';

                        var sortedPoints = this.points.sort(function (a, b) {
                            return ((a.y < b.y) ? -1 : ((a.y > b.y) ? 1 : 0));
                        });
                        sortedPoints.reverse();
                        $.each(sortedPoints, function (i, point) {
                            s += '<br/><span style="color:' + point.series.color + '"> ' + point.series.name + ' </span>: <b> ' + point.y + "(" + Math.floor(point.y / total * 100) + "%)" + " avg:" + Math.floor(point.y / monthDays);
                        });

                        return s;
                    },
                    valueDecimals: 0
                };
            }
            else if (duration == "week") {
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
                        var s = '<strong>' + Highcharts.dateFormat('%Y-%m-%d', new Date(this.x)) + '</strong>';

                        var sortedPoints = this.points.sort(function (a, b) {
                            return ((a.y < b.y) ? -1 : ((a.y > b.y) ? 1 : 0));
                        });
                        sortedPoints.reverse();
                        $.each(sortedPoints, function (i, point) {
                            s += '<br/><span style="color:' + point.series.color + '"> ' + point.series.name + ' </span>: <b> ' + point.y + "(" + Math.floor(point.y / total * 100) + "%)" + " avg:" + Math.floor(point.y / 7);
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
                        var s = '<strong>' + Highcharts.dateFormat('%Y-%m-%d', new Date(this.x)) + '</strong>';

                        var sortedPoints = this.points.sort(function (a, b) {
                            return ((a.y < b.y) ? -1 : ((a.y > b.y) ? 1 : 0));
                        });
                        sortedPoints.reverse();
                        $.each(sortedPoints, function (i, point) {
                            s += '<br/><span style="color:' + point.series.color + '"> ' + point.series.name + ' </span>: <b> ' + point.y + "(" + Math.floor(point.y / total * 100) + "%)";
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
function createTimeHighStock(element, url1, url2,url3, by, type,bookStatus) {
    var date = new Date();
    var dateformatted_to;
    var dateformatted_from;
    if(date.getMonth()==0){
        dateformatted_from=(date.getFullYear()-1) + "-" + date.getMonth() + "-"+(date.getDate()-1);
    }
    else{
        dateformatted_from = date.getFullYear() + "-" + date.getMonth() + "-"+(date.getDate()-1);
    }
    dateformatted_to = date.getFullYear() + "-" + (date.getMonth()+1) +"-"+(date.getDate()-1);
    var time;
    if(type.indexOf("---")>0){
        time=type;
    }else{
        time = dateformatted_from + "---" + dateformatted_to;
    }
    $.ajax({
        type:"get",
        url:url1+time,
        data:"",
        success:function () {
            $.ajax({
                type: "get",
                url: url2 + bookStatus,
                data: "",
                datatype: "json",
                success: function (returnData) {
                    var seriesOptions = [],
                        tooltipOptions = [];
                    var seriesCounter = 0;
                    var names = [];
                    var id=[];
                    returnData.forEach(function (element) {
                        names.push(element[0]);
                        id.push(element[2]);
                    });
                    /**
                     * Create the chart when all data is loaded
                     * @returns {undefined}
                     */
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
                        $.getJSON(url3 + id[i], function (data) {
                            if (by == "month") {
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
                                        var s = '<strong>' + Highcharts.dateFormat('%Y-%m', new Date(this.x)) + " " + total + '</strong>';

                                        var sortedPoints = this.points.sort(function (a, b) {
                                            return ((a.y < b.y) ? -1 : ((a.y > b.y) ? 1 : 0));
                                        });
                                        sortedPoints.reverse();
                                        $.each(sortedPoints, function (i, point) {
                                            s += '<br/><span style="color:' + point.series.color + '"> ' + point.series.name + ' </span>: <b> ' + point.y;
                                        });

                                        return s;
                                    },
                                    valueDecimals: 0
                                };
                            }
                            else if (by == "week") {
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
                                        var s = '<strong>' + Highcharts.dateFormat('%Y-%m-%d', new Date(this.x)) + '</strong>';

                                        var sortedPoints = this.points.sort(function (a, b) {
                                            return ((a.y < b.y) ? -1 : ((a.y > b.y) ? 1 : 0));
                                        });
                                        sortedPoints.reverse();
                                        $.each(sortedPoints, function (i, point) {
                                            s += '<br/><span style="color:' + point.series.color + '"> ' + point.series.name + ' </span>: <b> ' + point.y + "(" + Math.floor(point.y / total * 100) + "%)" + " avg:" + Math.floor(point.y / 7);
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
                                        var s = '<strong>' + Highcharts.dateFormat('%Y-%m-%d', new Date(this.x)) + " " + total.toFixed(2) + '</strong>';

                                        var sortedPoints = this.points.sort(function (a, b) {
                                            return ((a.y < b.y) ? -1 : ((a.y > b.y) ? 1 : 0));
                                        });
                                        sortedPoints.reverse();
                                        $.each(sortedPoints, function (i, point) {
                                            s += '<br/><span style="color:' + point.series.color + '"> ' + point.series.name + ' </span>: <b> ' + point.y.toFixed(2);
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

                },
                error: function (errorMsg) {
                    console.log(errorMsg);
                }
            });
        }
    });

}
function createComplexHighStock(element, url1, url2, by, type) {
    var date = new Date();
    var dateformatted_to;
    var dateformatted_from;
    if(date.getMonth()==0){
        dateformatted_from=(date.getFullYear()-1) + "-" + date.getMonth() + "-"+(date.getDate()-1);
    }
    else{
        dateformatted_from = date.getFullYear() + "-" + date.getMonth() + "-"+(date.getDate()-1);
    }
    dateformatted_to = date.getFullYear() + "-" + (date.getMonth()+1) +"-"+(date.getDate()-1);
    var time;
    if(type.indexOf("---")>0){
        time=type;
    }else{
        time = dateformatted_from + "---" + dateformatted_to;
    }
    $.ajax({
        type: "get",
        url: url1 + time,
        data: "",
        datatype: "json",
        success: function (returnData) {
            var seriesOptions = [],
                tooltipOptions = [];
            var seriesCounter = 0;
            var names = [];
            returnData.forEach(function (element) {
                names.push(element[0]);
            });
            /**
             * Create the chart when all data is loaded
             * @returns {undefined}
             */
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
                var condition = "";
                if (type == "all") {
                    condition = name;
                } else {
                    if(type.indexOf("---")>0){
                        name=name.replace("?","'");
                        condition=name+"---"+type;
                    }else{
                        condition = name + "---" + get_type_by(type);
                    }
                }
                $.getJSON(url2 + condition, function (data) {
                    if (by == "month") {
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
                                var s = '<strong>' + Highcharts.dateFormat('%Y-%m', new Date(this.x)) + " " + total + '</strong>';

                                var sortedPoints = this.points.sort(function (a, b) {
                                    return ((a.y < b.y) ? -1 : ((a.y > b.y) ? 1 : 0));
                                });
                                sortedPoints.reverse();
                                $.each(sortedPoints, function (i, point) {
                                    s += '<br/><span style="color:' + point.series.color + '"> ' + point.series.name + ' </span>: <b> ' + point.y;
                                });

                                return s;
                            },
                            valueDecimals: 0
                        };
                    }
                    else if (by == "week") {
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
                                var s = '<strong>' + Highcharts.dateFormat('%Y-%m-%d', new Date(this.x)) + '</strong>';

                                var sortedPoints = this.points.sort(function (a, b) {
                                    return ((a.y < b.y) ? -1 : ((a.y > b.y) ? 1 : 0));
                                });
                                sortedPoints.reverse();
                                $.each(sortedPoints, function (i, point) {
                                    s += '<br/><span style="color:' + point.series.color + '"> ' + point.series.name + ' </span>: <b> ' + point.y + "(" + Math.floor(point.y / total * 100) + "%)" + " avg:" + Math.floor(point.y / 7);
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
                                var s = '<strong>' + Highcharts.dateFormat('%Y-%m-%d', new Date(this.x)) + " " + total.toFixed(2) + '</strong>';

                                var sortedPoints = this.points.sort(function (a, b) {
                                    return ((a.y < b.y) ? -1 : ((a.y > b.y) ? 1 : 0));
                                });
                                sortedPoints.reverse();
                                $.each(sortedPoints, function (i, point) {
                                    s += '<br/><span style="color:' + point.series.color + '"> ' + point.series.name + ' </span>: <b> ' + point.y.toFixed(2);
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

        },
        error: function (errorMsg) {
            console.log(errorMsg);
        }
    });
}

