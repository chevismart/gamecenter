function initApi(appId, timestamp, nonce, sign) {
    wx.config({
        debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
        appId: appId, // 必填，公众号的唯一标识
        timestamp: timestamp, // 必填，生成签名的时间戳
        nonceStr: nonce, // 必填，生成签名的随机串
        signature: sign,// 必填，签名，见附录1
        jsApiList: ['chooseWXPay'] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
    });
}

//调用微信JS api 支付
function jsApiCall(params) {
    WeixinJSBridge.invoke(
        'getBrandWCPayRequest', params,
        function (res) {
            WeixinJSBridge.log(res.err_msg);
//                        alert(res.err_code + res.err_desc + res.err_msg);
        }
    );
}

var parameters;

function callpay(coin, device) {
    var coinQty;
    if (coin) coinQty = coin;
    else coinQty = $("#topupAmount").val();
    //scanQrcode(coinQty);
    raiseReq(coinQty, device);
}


function raiseReq(coin, device) {

    jQuery.ajax({
        type: "post",
        async: false,
        url: "wechatOrder?chargeAmount=" + coin + "&deviceId=ATM001",//+ device,
        cache: false,
        success: function (json) {
            //返回的数据用data.d获取内容
            //alert("success");
            parameters = eval("(" + json + ")");
        },
        error: function (err) {
            alert("error: " + err);
        }
    });

    if (typeof WeixinJSBridge == "undefined") {
        if (document.addEventListener) {
            document.addEventListener('WeixinJSBridgeReady', jsApiCall, false);
        } else if (document.attachEvent) {
            document.attachEvent('WeixinJSBridgeReady', jsApiCall);
            document.attachEvent('onWeixinJSBridgeReady', jsApiCall);
        }
    } else {
        jsApiCall(parameters);
    }
}

function updatePay(coin) {
    $(".chargeAmount .weui_btn").html("支付" + coin + "元购买" + coin + "个代币");
    $(".chargeAmount .weui_btn").attr("coin", coin);
    if (!!$(".chargeAmount .weui_btn").hasClass("weui_btn_disabled")) {
        $(".chargeAmount .weui_btn").removeClass("weui_btn_disabled");
        $(".chargeAmount .weui_btn").removeClass("weui_btn_default");
        $(".chargeAmount .weui_btn").addClass("weui_btn_primary");
    }
}

function payIt() {
    var coin = $(".chargeAmount .weui_btn").attr("coin");
    if (coin) {
        $(".weui_dialog_alert").removeClass("inactive");
    }
}

function scanQrcode(callback){
    return scanDeviceInfo(appId, nonceStr, timestamp, signature, callback);
}

function closeAlert(){
    $(".weui_dialog_alert").addClass("inactive");
    scanQrcode(function(result){
        var device;
        var params = result.split("#")[1].split("&");

        for(var i = 0;i < params.length; i++) {
            if(params[i].split("=")[0] === 'deviceid'){
                device = params[i].split("=")[1];
                callpay(coin, device);
            }
        }
        if(!device){
            alert("找不到设备,请重新扫描！");
        }
    });
}