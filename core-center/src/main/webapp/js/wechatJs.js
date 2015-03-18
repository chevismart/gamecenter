/**
 * wechat api 封装（前提：引用jweixin-1.0.0.js）
 */
function config(appId, nonceStr, timestamp, signature, apiList) {
    wx.config({
        debug: true, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
        appId: appId, // 必填，公众号的唯一标识
        timestamp: timestamp, // 必填，生成签名的时间戳
        nonceStr: nonceStr, // 必填，生成签名的随机串
        signature: signature,// 必填，签名，见附录1
        jsApiList: apiList // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
    });
}
function scan(appId, nonceStr, timestamp, signature) {
    var apiList = ['scanQRCode'];
    config(appId, nonceStr, timestamp, signature, apiList);
}