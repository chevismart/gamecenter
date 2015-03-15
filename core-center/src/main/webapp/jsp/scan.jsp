<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Insert title here</title>
</head>
<body>
<script type="text/javascript" src="http://wawaonline.net:8080/testweb/js/third-party/jquery-2.1.1.min.js"></script>
<script type="text/javascript" src="http://wawaonline.net:8080/testweb/js/third-party/jweixin-1.0.0.js"></script>
<script type="text/javascript" src="http://wawaonline.net:8080/testweb/js/wechatJs.js"></script>
<script type="text/javascript">
    var appId = ${wechatJsConfig.appId};
    var nonceStr = ${wechatJsConfig.nonceStr};
    var timestamp = ${wechatJsConfig.timestamp};
    var signature = ${wechatJsConfig.signature};
    scan(appId, nonceStr, timestamp, signature);
</script>
</body>
</html>