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

function timeConverter(UNIX_timestamp) {
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
