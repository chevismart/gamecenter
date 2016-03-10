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
    <title>充值</title>
    <meta name="viewport" content="width=device-width,initial-scale=1.0, maximum-scale=1.0,user-scalable=0">
    <meta http-equiv="content-type" content="text/html;charset=utf-8"/>
    <script type="application/javascript"
            src="http://wawaonline.net/corecenter/js/third-party/jquery-2.1.1.min.js"></script>
    <script type="application/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
    <%--<script type="text/javascript">--%>
    <%--function topup() {--%>
    <%--var order = "";//--%>
    <%--// alert(order);--%>
    <%--WeixinJSBridge.invoke('getBrandWCPayRequest', {--%>
    <%--"appId": "wxe89a9d2fa17df80f",--%>
    <%--"nonceStr": "125e18d4-ef04-40b1-be99-3bc3feb2328e",--%>
    <%--"package": "prepay_id=wx20150101143703b473cd24030845926993",--%>
    <%--"paySign": "9F36CB398AB366F393EB321F7122B8C7",--%>
    <%--"signType": "MD5",--%>
    <%--"timeStamp": "1420094192"--%>
    <%--}, function (res) {--%>
    <%--//WeixinJSBridge.log(res.err_msg);--%>
    <%--alert(res.err_code+ ":" + res.err_desc +":" + res.err_msg);--%>
    <%--});--%>
    <%--}--%>
    <%--</script>--%>
    <script type="text/javascript">

        function initApi(appId, timestamp, nonce, sign) {
            wx.config({
                debug: true, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
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
                        alert(res.err_code + res.err_desc + res.err_msg);
                    }
            );
        }

        function callpay() {

            jQuery.ajax({
                type: "post",
                async: false,
                url: "wechatOrder?chargeAmount="+ $("#topupAmount").val(),
                cache: false,
                success: function (json) {
                    //返回的数据用data.d获取内容
                    //alert("success");
                    alert(json)
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
                var params = $("#params").text();
                jsApiCall(params);
            }
        }
    </script>
</head>
<body>

<input name="topupAmount" id="topupAmount"/>
<a href="javascript:void(0)" onclick="callpay()">click me</a>

<div id="params">
    <s:text name="tempJsonStr"/>
</div>
<%--<s:form>--%>
<%--<s:text name="topupAmount" id="topupAmount"/>--%>
<%--<s:a href="javascript:void(0);" onclick="topup();"/>--%>
<%--</s:form>--%>
</body>
</html>
