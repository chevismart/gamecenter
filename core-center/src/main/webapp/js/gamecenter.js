//var server = "http://alcock.gicp.net:8003";
//var server = "http://alcock.gicp.net:1314/game/fronter.php";
var server = "http://www.ruijunhao.com/test/fronter.php";
var gamecenter_server = "http://wawaonline.net/wechatOrder";
//var server = "http://www.chayeagin.com/fronter.php";
var dataType = "JSONP";
var token = "tokenStr";
var isReady = false;

function isTrue(value) {
    return value === "true";
}

function getTimeStamp() {
    return new Date().getTime();
}

function updateClientList(data) {
    $('#clientList').empty();
    $.each(data,
        function (index, content) {
            $('#clientList').append("<option value='" + content + "'>" + content + "</option>");
        });
}

function updateCounter(data) {
    var coin = data.COIN_QTY;
    var prize = data.PRIZE_QTY;
    var updateTime = data.COUNTER_QTY_TIMESTAMP;
    updateDeviceInfo("coin_qty", coin);
    updateDeviceInfo("prize_qty", prize);

}

function resetCounterResult(data, groupId, mac) {
    var isCoinReset = isTrue(data.COIN_RESET);
    var isPrizeReset = isTrue(data.PRIZE_RESET);
    queryCounterQty(groupId, mac, isCoinReset, isPrizeReset);
    alert("Reset Counter Successfully!");
}

function updatePowerStatus(data) {
    var pwdStatus = isTrue(data.POWER_STATUS);
    var updateTime = data.POWER_STATUS_UPDATE_TIME;
    var pwdStatusStr = pwdStatus ? "ON" : "OFF";
    updateDeviceInfo("power_status", pwdStatusStr);
}

function processTopUpResult(data, coinQty) {
    var isSuccess = isTrue(data.TOP_UP_RESULT);
    if (isSuccess) {
        var refNum = data.TOP_UP_REFERENCE_ID;
        alert(refNum + " top up " + coinQty + " successfully!");
    } else {
        alert("Top up fails");
    }
}

function selectedDeviceChanged(groupId) {
    var mac = getSelectedDevice();
    updateDeviceInfo("device_name", mac);
    updateDeviceInfo("mac_info", mac);
    queryCounterQty(groupId, mac, true, true);
    queryPowerStatus(groupId, mac);
}

function updateDeviceInfo(type, value) {
    var infoText = $('.' + type + '').find('.val');
    infoText.text(value);
}

function getSelectedDevice() {
    return $('#clientList').val();
}

function getClientList(groupId) {

    $.ajax({
        type: "GET",
        url: server,
        data: {
            CENTER_ID: groupId,
            TOKEN: token,
            DATA_TYPE: dataType,
            REQ_TYPE: "CLIENT_LIST",
            TIMESTAMP: getTimeStamp()
        },
        dataType: dataType,
        jsonp: "jsonpCallback",
        beforeSend: setHeader,
        success: function (data) {
            isReady = true;
            updateClientList(data);
            selectedDeviceChanged('00000000');
        },
        error: function () {
            alert('fail');
        }

    });

    function setHeader(xhr) {

        xhr.setRequestHeader('Access-Control-Allow-Origin', '*');
    }

}

function queryCounterStatus(groupId, mac) {

    $.ajax({
        type: "GET",
        url: server,
        data: {
            CENTER_ID: groupId,
            TOKEN: token,
            DATA_TYPE: dataType,
            REQ_TYPE: "COUNTER_STATUS",
            MAC: mac,
            COIN_STATUS: true,
            PRIZE_STATUS: true
        },
        dataType: dataType,
        jsonp: "jsonpCallback",
        success: function (data) {
            $.each(data,
                function (index, content) {
                    alert(content);
                });
        },
        error: function () {
            alert('fail');
        }

    });

}

function switchCounterStatus(groupId, mac, switcher) {

    $.ajax({
        type: "GET",
        url: server,
        data: {
            CENTER_ID: groupId,
            TOKEN: token,
            DATA_TYPE: dataType,
            REQ_TYPE: "COUNTER_SWITCH",
            MAC: mac,
            COUNTER_SWITCH: switcher
        },
        dataType: dataType,
        jsonp: "jsonpCallback",
        success: function (data) {
            $.each(data,
                function (index, content) {
                    alert(content);
                });
        },
        error: function () {
            alert('fail');
        }

    });

}

function queryCounterQty(groupId, mac, isCoin, isPrize) {

    $.ajax({
        type: "GET",
        url: server,
        data: {
            CENTER_ID: groupId,
            TOKEN: token,
            DATA_TYPE: dataType,
            REQ_TYPE: "COUNTER_QTY",
            MAC: mac,
            COIN_QTY: isCoin,
            PRIZE_QTY: isPrize
        },
        dataType: dataType,
        jsonp: "jsonpCallback",
        success: function (data) {
            updateCounter(data);
        },
        error: function () {
            alert('fail');
        }

    });

}

function resetCounter(groupId, mac, resetCoin, resetPrize) {

    $.ajax({
        type: "GET",
        url: server,
        data: {
            CENTER_ID: groupId,
            TOKEN: token,
            DATA_TYPE: dataType,
            REQ_TYPE: "COUNTER_RESET",
            MAC: mac,
            COIN_RESET: resetCoin,
            PRIZE_RESET: resetPrize
        },
        dataType: dataType,
        jsonp: "jsonpCallback",
        success: function (data) {
            resetCounterResult(data, groupId, mac);
        },
        error: function () {
            alert('fail');
        }

    });

}


function topUp(groupId, mac, refId, coinQty) {

    $.ajax({
        type: "GET",
        url: server,
        data: {
            CENTER_ID: groupId,
            TOKEN: token,
            DATA_TYPE: dataType,
            REQ_TYPE: "TOP_UP",
            MAC: mac,
            TOP_UP_REFERENCE_ID: refId,
            TOP_UP_COIN_QTY: coinQty
        },
        dataType: dataType,
        jsonp: "jsonpCallback",
        success: function (data) {
            processTopUpResult(data, coinQty)
        },
        error: function () {
            alert('Top up fail!');
        }

    });

}


function queryPowerStatus(groupId, mac) {

    $.ajax({
        type: "GET",
        url: server,
        data: {
            CENTER_ID: groupId,
            TOKEN: token,
            DATA_TYPE: dataType,
            REQ_TYPE: "POWER_STATUS",
            MAC: mac
        },
        dataType: dataType,
        jsonp: "jsonpCallback",
        success: function (data) {
            updatePowerStatus(data);
        },
        error: function () {
            alert('fail');
        }

    });

}

function switchPowerStatus(groupId, mac, isPowerOn) {

    $.ajax({
        type: "GET",
        url: server,
        data: {
            CENTER_ID: groupId,
            TOKEN: token,
            DATA_TYPE: dataType,
            REQ_TYPE: "POWER_CONTROL",
            MAC: mac,
            POWER_SWITCHER: isPowerOn
        },
        dataType: dataType,
        jsonp: "jsonpCallback",
        success: function (data) {
            updatePowerStatus(data);
        },
        error: function () {
            alert('fail');
        }

    });

}

function charge(charge_amount) {
    $.ajax({
        type: "POST",
        url: gamecenter_server,
        data: {
            CHARGE_NUM: charge_amount
        },
        dataType: dataType,
        jsonp: "jsonpCallback",
        success: function (data) {
            alert("充值成功！");
        },
        error: function () {
            alert('Charge failure!');
        }
    });
}