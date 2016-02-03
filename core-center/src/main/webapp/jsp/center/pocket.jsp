<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="s" uri="/struts-tags" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=0">
    <link rel="stylesheet" href="http://wawaonline.net/corecenter/css/weui.min.css"/>
    <link rel="stylesheet" href="http://wawaonline.net/corecenter/css/style.css"/>
    <script src="http://code.jquery.com/jquery-1.8.3.min.js"></script>
    <script type="text/javascript" src="http://wawaonline.net/corecenter/js/wechatJs.js"></script>
    <script type="text/javascript" src="http://wawaonline.net/corecenter/js/pocket.js"></script>
    <script type="text/javascript" src="http://wawaonline.net/corecenter/js/third-party/jweixin-1.0.0.js"></script>
    <title>我的钱包</title>
    <script type="application/javascript">
        var coinQty = new Number(getWallet());
        $(document).ready(function () {
            hideInvalidOption(coinQty);
            $(".lanchBtn").find("a").click(function () {
                        var coin = $("#coinQty").val();
                        var appId = '${wechatJsConfig.appId}';
                        var nonceStr = '${wechatJsConfig.nonceStr}';
                        var timestamp = '${wechatJsConfig.timestamp}';
                        var signature = '${wechatJsConfig.signature}';
                        scan(appId, nonceStr, timestamp, signature, coin);
                    }
            );
        });

    </script>
    <style>
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

        .main {
            background: url('<s:if test="userProfile.isFollowed==true">http://wawaonline.net:80/corecenter/images/bg.jpg</s:if>') center center no-repeat;
            width: 100%;
            height: 100%;
            background-size: 320px 640px;
        }

        .main .title {
            display: inline-block;
            width: 100%;
            position: relative;
            top: 50%;
            margin-top: 80px;
            text-align: center;
        }

        .main .lanchBtn {
            display: inline-block;
            padding: 5px;
            left: 50%;
            margin-left: -40px;
            top: 50%;
            margin-top: 50px;
            position: relative;
            border-radius: 70px;
            background: #eeeeee;
            border: 1px solid #d2d2d2;
            box-shadow: 0px 1px 2px rgba(255, 255, 255, 0.6);
        }

        #selectCoin {
            margin-top: 150px;
        }

        /*SELECT W/DOWN-ARROW*/
        select#coinQty
        {
            width                    : 80pt;
            height                   : 40pt;
            line-height              : 40pt;
            padding-right            : 20pt;
            text-indent              : 4pt;
            text-align               : left;
            vertical-align           : middle;
            box-shadow               : inset 0 0 3px #606060;
            border                   : 1px solid #acacac;
            -moz-border-radius       : 6px;
            -webkit-border-radius    : 6px;
            border-radius            : 6px;
            -webkit-appearance       : none;
            -moz-appearance          : none;
            appearance               : none;
            font-family              : Arial,  Calibri, Tahoma, Verdana;
            font-size                : 18pt;
            font-weight              : 500;
            color                    : #000099;
            cursor                   : pointer;
            outline                  : none;
        }
        select#coinQty option
        {
            padding             : 4px 10px 4px 10px;
            font-size           : 11pt;
            font-weight         : normal;
        }
        select#coinQty option[selected]{ font-weight:bold}
        select#coinQty option:nth-child(even) { background-color:#f5f5f5; }
        select#coinQty:hover {font-weight: 700;}
        select#coinQty:focus {box-shadow: inset 0 0 5px #000099; font-weight: 600;}

        /*LABEL FOR SELECT*/
        label#lblSelect{ position: relative; display: inline-block;}
        /*DOWNWARD ARROW (25bc)*/
        label#lblSelect::after
        {
            content                 : "\25bc";
            position                : absolute;
            top                     : 0;
            right                   : 0;
            bottom                  : 0;
            width                   : 20pt;
            line-height             : 40pt;
            vertical-align          : middle;
            text-align              : center;
            background              : #000099;
            color                   : #FF0000;
            -moz-border-radius       : 0 6px 6px 0;
            -webkit-border-radius    : 0 6px 6px 0;
            border-radius           : 0 6px 6px 0;
            pointer-events          : none;
        }
    </style>
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
        <h2>钱包余额：${wallet} 币</h2><br>

    </s:else>
        <br>

        <div id="selectCoin">
            <h3>请扫描售币机上二维码加币</h3>
            <select id="coinQty">
                <option value="1">1个币</option>
                <option value="2">2个币</option>
                <option value="3">3个币</option>
                <option value="4">4个币</option>
                <option value="5">5个币</option>
                <option value="6">6个币</option>
                <option value="7">7个币</option>
                <option value="8">8个币</option>
                <option value="9">9个币</option>
                <option value="10">10个币</option>
            </select>
        </div>

    </div>


    <div id="lanchBtn" class="lanchBtn">
        <%--<s:if test="userProfile.isFollowed==true">--%>
        <a href="javascript:void(0);">
            加
        </a>
        <%--</s:if>--%>
    </div>
</div>
<div class="product">
</div>


</body>
</html>