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
    <script type="application/javascript" src="http://wawaonline.net/corecenter/js/wechatJs.js"></script>
    <script type="application/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
    <script src="http://apps.bdimg.com/libs/angular.js/1.4.6/angular.min.js"></script>

    <script type="application/javascript">
        var appId = '${wechatJsConfig.appId}';
        var nonceStr = '${wechatJsConfig.nonceStr}';
        var timestamp = '${wechatJsConfig.timestamp}';
        var signature = '${wechatJsConfig.signature}';
    </script>
</head>
<body>
<div class="charge_main">
    <div class="weui_panel_bd">
        <div class="weui_media_box weui_media_text">
            <div class="title">
                <h2 class="weui_media_title">欢迎使用微信支付购买娃娃机代币</h2>
            </div>
            <div class="deviceInfo">
                当前设备:
                <span><b>ATM001</b></span>
            </div>
        </div>
        <div class="chargeList">
            <ul>
                <li onclick="updatePay(5)"><a href="#" class="weui_btn weui_btn_plain_default chargeOption">5 币</a></li>
                <li onclick="updatePay(10)"><a href="#" class="weui_btn weui_btn_plain_default chargeOption">10 币</a>
                </li>
                <li onclick="updatePay(20)"><a href="#" class="weui_btn weui_btn_plain_default chargeOption">20 币</a>
                </li>
                <li onclick="updatePay(50)"><a href="#" class="weui_btn weui_btn_plain_default chargeOption">50 币</a>
                </li>
            </ul>
        </div>

        <div class="chargeAmount">
            <a href="#" id="payButton" class="weui_btn weui_btn_default weui_btn_disabled"
               onclick="payIt()">选择购买代币数量</a>
        </div>

        <div class="declaration">
            <section>
                <h3>温馨提示：</h3>
                <p>代币售出恕不退回，不设找零。</p>
            </section>

        </div>
    </div>
</div>

<div class="weui_dialog_alert inactive">
    <div class="weui_mask"></div>
    <div class="weui_dialog">
        <div class="weui_dialog_hd"><strong class="weui_dialog_title">疯狂的娃娃机提示你</strong></div>
        <div class="weui_dialog_bd">请扫描售币机上的二维码继续购币！</div>
        <div class="weui_dialog_ft">
            <a href="javascript:void(0)" class="weui_btn_dialog primary" onclick="closeAlert()">确定</a>
        </div>
    </div>
</div>
</body>
</html>
