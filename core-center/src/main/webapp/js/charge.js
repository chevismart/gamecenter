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

function callpay(coin){
    var coinQty;
    if(coin) coinQty = coin;
    else coinQty = $("#topupAmount").val();
    raiseReq(coinQty, "ATM001");
}


function raiseReq(coin, device) {

    jQuery.ajax({
        type: "post",
        async: false,
        url: "wechatOrder?chargeAmount="+ coin+ "&deviceId=ATM001",//+ device,
        cache: false,
        success: function (json) {
            //返回的数据用data.d获取内容
            //alert("success");
            parameters = eval("("+json+")");
        },
        error: function (err) {
            alert("error: "+err);
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

function updatePay(coin){
    $(".chargeAmount .weui_btn").html("支付"+coin+"元购买"+coin+"个代币");
    $(".chargeAmount .weui_btn").attr("coin", coin);
}

function payIt(){
    var coin = $(".chargeAmount .weui_btn").attr("coin");
    if(coin){
        callpay(coin);
    }
}