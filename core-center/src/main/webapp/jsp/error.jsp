<%--
  Created by IntelliJ IDEA.
  User: Chevis
  Date: 2014/12/8
  Time: 21:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>错误</title>
    <meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=0">
    <script type="text/javascript"
            src="http://wawaonline.net/corecenter/js/third-party/jquery-2.1.1.min.js"></script>
    <link rel="stylesheet" href="http://wawaonline.net/corecenter/css/weui.min.css"/>
</head>
<body>

<div class="weui_msg">
    <div class="weui_icon_area"><i class="weui_icon_warn weui_icon_msg"></i></div>
    <div class="weui_text_area">
        <h2 class="weui_msg_title">操作失败</h2>
        <p class="weui_msg_desc">请重试或联系我们</p>
    </div>
    <div class="weui_opr_area">
        <p class="weui_btn_area">
            <a href="http://wawaonline.net/corecenter/pocket" class="weui_btn weui_btn_warn">确定</a>
            <%--<a href="javascript:void(0)" id="cancel" class="weui_btn weui_btn_default">取消</a>--%>
        </p>
    </div>
    <div class="weui_extra_area">
        <%--<a href="">查看详情</a>--%>
    </div>
</div>
</body>
</html>
