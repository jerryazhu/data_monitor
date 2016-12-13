if (!Array.prototype.shuffle) {
    Array.prototype.shuffle = function () {
        for (var j, x, i = this.length; i; j = parseInt(Math.random() * i), x = this[--i], this[i] = this[j], this[j] = x);
        return this;
    };
}

function round(number, roundDigit) {
    if (number >= 0) {
        return parseInt((number * Math.pow(10, roundDigit) + 0.5)) / Math.pow(10, roundDigit);
    }
    else {
        number1 = -number;
        var tempNumber = parseInt((number1 * Math.pow(10, roundDigit) + 0.5)) / Math.pow(10, roundDigit);
        return -tempNumber;
    }
}

function getLastDay(year, month) {
    var new_year = year;
    var new_month = month++;
    if (month > 12) {
        new_month -= 12;
        new_year++;
    }
    var new_date = new Date(new_year, new_month, 1);
    return (new Date(new_date.getTime() - 1000 * 60 * 60 * 24)).getDate();
}
function get_type_by(selected) {
    var type = "";
    if (selected == "所有") {
        type = "all";
    }
    else if (selected == "其它") {
        type = "0";
    }
    else if (selected == "青少年") {
        type = "1";
    }
    else if (selected == "商务") {
        type = "2";
    }
    else if (selected == "应试") {
        type = "3";
    }
    else {
        type = "4";
    }
    return type;
}
function unixToDate(UNIX_timestamp) {
    var a = new Date(UNIX_timestamp * 1000);
    var year = a.getFullYear();
    var month = a.getMonth() + 1;
    var day = a.getDate();
    var hour = a.getHours();
    var min = a.getMinutes();
    var sec = a.getSeconds();
    month = (month < 10 ? "0" : "") + month;
    day = (day < 10 ? "0" : "") + day;
    hour = (hour < 10 ? "0" : "") + hour;
    min = (min < 10 ? "0" : "") + min;
    sec = (sec < 10 ? "0" : "") + sec;
    return year + '-' + month + '-' + day + ' ' + hour + ':' + min + ':' + sec;
}
function unixToDay(UNIX_timestamp) {
    var a = new Date(UNIX_timestamp * 1000);
    var year = a.getFullYear();
    var month = a.getMonth() + 1;
    var day = a.getDate();
    month = (month < 10 ? "0" : "") + month;
    day = (day < 10 ? "0" : "") + day;
    return year + '-' + month + '-' + day;
}
function dateToUnix(dateString) {
    if (dateString.length == 0) {
        return "";
    }
    return Date.parse(dateString) / 1000;
}
