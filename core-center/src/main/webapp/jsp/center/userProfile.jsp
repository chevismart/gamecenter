<%--
  Created by IntelliJ IDEA.
  User: Chevis
  Date: 2014/12/20
  Time: 1:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <meta name="viewport" content="width=device-width,initial-scale=1.0, maximum-scale=1.0,user-scalable=0">
    <script type="text/javascript"
            src="http://alcock.gicp.net:8888/corecenter/js/third-party/jquery-2.1.1.min.js"></script>
    <script type="text/javascript" src="http://alcock.gicp.net:8888/corecenter/js/gamecenter.js"></script>
    <script type="text/javascript" src="http://alcock.gicp.net:8888/corecenter/js/test.js"></script>
    <script type="text/javascript">
        function deviceList() {
            getClientList("00000000");
        }
        function counterStatus() {
            queryCounterStatus("00000000", "accf233b95f6");
        }
        function counterRecord() {
            queryCounterQty("00000000", "accf233b95f6", true, true);
        }
        function resetRecord() {
            resetCounter("00000000", "accf233b95f6", true, false);
        }
        function turnOn() {
            switchPowerStatus("00000000", "accf233b95f6", true);
        }
        function turnOff() {
            switchPowerStatus("00000000", "accf233b95f6", false);
        }
        function topup1() {
            topUp("00000000", "accf233b95f6", "ABCDEF0000", 1);
        }
        function topup5() {
            topUp("00000000", "accf233b95f6", "ABCDEF0000", 5);
        }
        function powerStatus() {
            queryPowerStatus("00000000", "accf233b95f6");
        }
        function enableCounter() {
            switchCounterStatus("00000000", "accf233b95f6", "true");
        }
        function disableCounter() {
            switchCounterStatus("00000000", "accf233b95f6", "false")
        }

        function chargeMoney() {
            var amount = $("#chargeSelect").val();
            window.open('<s:text name="userProfile.accessInfo.chargeURL"/>?chargeAmount=' + amount);
        }
    </script>

    <style>
        * {
            font-family: arial;
        }

        body {
            background: #e6e6e6;
            padding: 0px;
            margin: 0px;
        }

        .focus {
            text-decoration: none;
        }

        .main .title {
            display: inline-block;
            width: 100%;
            position: relative;
            top: 50%;
            margin-top: -155px;
            text-align: center;
        }

        .main .title h1 {
            color: #333;
            font-size: 25px;
            font-weight: normal;
        }

        .main .lanchBtn {
            display: inline-block;
            padding: 5px;
            left: 50%;
            margin-left: -40px;
            top: 50%;
            margin-top: 40px;
            position: relative;
            border-radius: 70px;
            background: #eeeeee;
            border: 1px solid #d2d2d2;
            box-shadow: 0px 1px 2px rgba(255, 255, 255, 0.6);
        }

        .main .lanchBtn a {
            display: inline-block;
            width: 70px;
            height: 70px;
            line-height: 70px;
            color: #333;
            text-decoration: none;
            text-align: center;
            border-radius: 70px;
            font-size: 40px;
            background: -webkit-gradient(linear,
            left top, left bottom, from(#fff), to(#eee));
            box-shadow: 0px 1px 1px rgba(0, 0, 0, 0.3);
        }

        .product {
            height: 100%;
            width: 100%;
            display: none;
        }

        .main {
            /*display:none;*/
        }

        .user {
            position: absolute;
            margin: 10px;

        }

        .user .headphoto {
            float: left;
            display: inline-block;
            background: url('<s:text name="userProfile.userImgUrl"/>') center center no-repeat;
            width: 50px;
            height: 50px;
            background-size: 50px 50px;
            border-radius: 50px;
            border: 2px solid #fff;
            box-shadow: 0px 1px 2px rgba(0, 0, 0, 0.5);
        }

        .user .username {
            float: left;
            display: inline-block;
            height: 54px;
            line-height: 54px;
            margin-left: 5px;
        }

        .main {
            background: url('<s:if test="userProfile.isFollowed==true">http://alcock.gicp.net:8888/corecenter/images/bg.jpg</s:if>') center center no-repeat;
            width: 100%;
            height: 100%;
            background-size: 320px 640px;
        }

        .product {
            width: 100%;
            height: 100%;
            display: none;
        }
    </style>

    <title>疯狂娃娃世界</title>
</head>
<body>
<div class="main">
    <div class="user">
        <div class="headphoto">
        </div>
        <div class="username">
            <s:text name="userProfile.displayName"/>
        </div>
    </div>
    <div class="title">
        <s:if test="userProfile.isFollowed==false">
            <s:a href="http://mp.weixin.qq.com/s?__biz=MzA5NzAwOTE5MA==&mid=201512829&idx=1&sn=7dfc26347eac047d6494212e2d49ef3f#rd"
                 cssClass="focus">一键关注</s:a>
            <%--<a href="http://mp.weixin.qq.com/s?__biz=MzA5NzAwOTE5MA==&mid=201512829&idx=1&sn=7dfc26347eac047d6494212e2d49ef3f#rd"--%>
            <%--class="focus">一键关注</a>--%>
        </s:if><s:else>
        谢谢您的关注！
    </s:else>
        <br>
        <%--<s:a href="/wechatOrder">充值</s:a>--%>
        <select id="chargeSelect">
            <option value="1">1元</option>
            <option value="5">5元</option>
            <option value="10">10元 - 送1币</option>
            <option value="20">20元 - 送2币</option>
        </select>
        <h1>
            荔园3楼 - <b> <s:text name="userProfile.deviceId"/></b>号机...
        </h1>
    </div>


    <div id="lanchBtn" class="lanchBtn">
        <%--<s:if test="userProfile.isFollowed==true">--%>
        <a href="javascript:void(0);">
            充
        </a>
        <%--</s:if>--%>
    </div>
</div>
<div class="product">
</div>

</body>
<script>
    $("#lanchBtn").click(function () {
        chargeMoney();
    });

</script>
</html>
