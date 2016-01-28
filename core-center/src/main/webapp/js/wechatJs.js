/**
 * wechat api 封装（前提：引用jweixin-1.0.0.js）
 */
var applicationId;

function config(appId, nonceStr, timestamp, signature, apiList, coins) {

    wx.config({
        debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
        appId: appId, // 必填，公众号的唯一标识
        timestamp: timestamp, // 必填，生成签名的时间戳
        nonceStr: nonceStr, // 必填，生成签名的随机串
        signature: signature,// 必填，签名，见附录1
        jsApiList: apiList // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
    });
    wx.ready(function () {
        // config信息验证后会执行ready方法，所有接口调用都必须在config接口获得结果之后，config是一个客户端的异步操作，所以如果需要在页面加载时就调用相关接口，则须把相关接口放在ready函数中调用来确保正确执行。对于用户触发时才调用的接口，则可以直接调用，不需要放在ready函数中。
        wx.scanQRCode({
            needResult: 1, // 默认为0，扫描结果由微信处理，1则直接返回扫描结果，
            scanType: ["qrCode", "barCode"], // 可以指定扫二维码还是一维码，默认二者都有
            success: function (res) {
                var result = res.resultStr; // 当needResult 为 1 时，扫码返回的结果
                redirectToTopup(locateTheDevice(result),coins);
            }
        });
    });
    wx.error(function (res) {
        alert("something goes wrong: " + res.errMsg);
    });
}
function scan(appId, nonceStr, timestamp, signature, coins) {
    applicationId = appId;
    var apiList = ['scanQRCode'];
    config(appId, nonceStr, timestamp, signature, apiList, coins);
}

function getAppId() {
    return applicationId;
}

function locateTheDevice(fullPath) {
    return fullPath.substring(fullPath.indexOf('#') + 1);
}

function redirectToTopup(deviceId, coins) {
    var timestamp = Date.parse(new Date());
    //var urls = "https://open.weixin.qq.com/connect/oauth2/authorize?" +
    //    "appid=" + appId + "&redirect_uri=http://wawaonline.net/corecenter/topup&response_type=code&scope=snsapi_base&state=appid:" + appId + ",deviceid:" + device + "#wechat_redirect";
    var fixedUrl = "http://wawaonline.net/corecenter/topup?appid=" + getAppId() + "&deviceid=" + deviceId + "&topupCoins=" + coins + "&timestamp=" + timestamp;
    window.location = fixedUrl;
}

function tryPlay(openId){
    var topupUrl = "http://wawaonline.net/corecenter/tryPlay"

}