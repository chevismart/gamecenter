<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Insert title here</title>
</head>
<body>
<script type="text/javascript" src="http://wawaonline.net/corecenter/js/third-party/jquery-2.1.1.min.js"></script>
<script type="text/javascript" src="http://wawaonline.net/corecenter/js/third-party/jweixin-1.0.0.js"></script>
<script type="text/javascript" src="http://wawaonline.net/corecenter/js/wechatJs.js"></script>
<div id="appId">${wechatJsConfig.appId}</div>
<div id="noncStr">${wechatJsConfig.nonceStr}</div>
<div id="timestamp">${wechatJsConfig.timestamp}</div>
<div id="signature">${wechatJsConfig.signature}</div>

<script type="text/javascript">
    var jq = jQuery.noConflict();
    var appId = jq("#appId").text();
    var nonceStr = jq("#noncStr").text();
    var timestamp = jq("#timestamp").text();
    var signature = jq("#signature").text();
    scan(appId, nonceStr, timestamp, signature);
</script>
</body>
 
</html>