<%--
  Created by IntelliJ IDEA.
  User: Chevis
  Date: 2014/12/25
  Time: 22:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <title>代币充值</title>
    <meta name="viewport" content="width=device-width,initial-scale=1.0, maximum-scale=1.0,user-scalable=0">
    <meta http-equiv="content-type" content="text/html;charset=utf-8"/>
    <link rel="stylesheet" href="http://wawaonline.net/corecenter/css/weui.min.css"/>
    <link rel="stylesheet" href="http://wawaonline.net/corecenter/css/style.css"/>
    <script type="application/javascript"
            src="http://wawaonline.net/corecenter/js/third-party/jquery-2.1.1.min.js"></script>
    <script type="application/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
    <script type="text/javascript">

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
    </script>
</head>
<body>
<div class="charge_main">
    <div class="title">
        <h2>欢迎使用微信支付购买娃娃机代币</h2>
    </div>
    <div class="chargeList">
        <ul>
            <li onclick="updatePay(5)"><a href="#" class="weui_btn weui_btn_plain_default chargeOption">5 币</a></li>
            <li onclick="updatePay(10)"><a href="#" class="weui_btn weui_btn_plain_default chargeOption">10 币</a></li>
            <li onclick="updatePay(20)"><a href="#" class="weui_btn weui_btn_plain_default chargeOption">20 币</a></li>
            <li onclick="updatePay(50)"><a href="#" class="weui_btn weui_btn_plain_default chargeOption">50 币</a></li>
        </ul>

    </div>

    <div class="chargeAmount">
        <a href="#" id="payButton" class="weui_btn weui_btn_primary" onclick="payIt()">选择购买代币数量</a>
        <%--<input name="topupAmount" id="topupAmount"/>--%>
        <%--<a href="javascript:void(0)" onclick="callpay()">click me</a>--%>
    </div>
</div>

<%--<s:form>--%>
<%--<s:text name="topupAmount" id="topupAmount"/>--%>
<%--<s:a href="javascript:void(0);" onclick="topup();"/>--%>
<%--</s:form>--%>
</body>
</html>
