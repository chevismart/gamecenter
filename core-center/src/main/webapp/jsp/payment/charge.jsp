<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <title>代币充值</title>
    <meta name="viewport" content="width=device-width,initial-scale=1.0, maximum-scale=1.0,user-scalable=0">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="http://wawaonline.net/corecenter/css/weui.min.css"/>
    <link rel="stylesheet" href="http://wawaonline.net/corecenter/css/style.css"/>
    <script type="application/javascript"
            src="http://wawaonline.net/corecenter/js/third-party/jquery-2.1.1.min.js"></script>
    <script type="application/javascript" src="http://wawaonline.net/corecenter/js/charge.js"></script>
    <script type="application/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
    <script src="http://apps.bdimg.com/libs/angular.js/1.4.6/angular.min.js"></script>

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
</body>
</html>
