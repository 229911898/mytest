<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/webbase.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/pages-weixinpay.css">
    <script src="${pageContext.request.contextPath}/js/qrious.js"></script>
    <title>微信支付</title>

</head>
<body>
<!--引入头部-->
<%@include file="header.jsp" %>
<div class="container-fluid">
    <div class="cart py-container">
        <!--主内容-->
        <div class="checkout py-container  pay">
            <div class="checkout-tit">
                <h4 class="fl tit-txt"><span class="success-icon"></span><span
                        class="success-info">订单提交成功，请您及时付款！订单号：${order.oid}</span></h4>
                <span class="fr"><em class="sui-lead">应付金额：</em><em class="orange money">￥${order.total}</em>元</span>
                <div class="clearfix"></div>
            </div>
            <div class="checkout-steps">
                <div class="fl weixin">微信支付</div>
                <div class="fl sao">
                    <p class="red" style="padding-bottom: 40px"></p>
                    <div class="fl code">
                        <img alt="" id="qrious">
                        <script>
                            new QRious({
                                element: document.getElementById("qrious"),
                                level: "L",
                                size: 300,
                                value: "${payUrl}"
                            })
                        </script>
                        <div class="saosao">
                            <p>请使用微信扫一扫</p>
                            <p>扫描二维码支付</p>
                        </div>
                    </div>
                    <div class="fl"
                         style="background:url(./img/phone-bg.png) no-repeat;width:350px;height:400px;margin-left:40px">
                    </div>
                </div>
                <div class="clearfix"></div>
            </div>
        </div>

    </div>
</div>
<!--引入尾部-->
<%@include file="footer.jsp" %>
<script>
    //设置定时器,每隔5秒发送异步请求查询支付状态
    let id=setInterval(function () {
        $.ajax({
            url:"${pageContext.request.contextPath}/payServlet",
            type:"post",
            data:{"action":"isPay","oid":"${order.oid}"},
            success:function (response) {
                if (response.success) {
                    //已支付则发送支付成功请求
                    location.href="${pageContext.request.contextPath}/payServlet?action=successPay&oid=${order.oid}";
                }
            },
            dataType:"json"
        })
    },5000);

    //设置一个一次性定时器,十分钟后如果用户还未支付,则发送支付失败请求
    setTimeout(function () {
        clearInterval(id);
        location.href="${pageContext.request.contextPath}/payServlet?action=failPay&oid=${order.oid}";
    },600000)
</script>
</body>
</html>
