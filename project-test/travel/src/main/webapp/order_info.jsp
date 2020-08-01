<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/webbase.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/pages-getOrderInfo.css">

    <title>结算页</title>

</head>
<body>
<!--引入头部-->
<%@include file="header.jsp" %>

<div class="container-fluid">
    <form action="${pageContext.request.contextPath}/orderServlet" method="post" onsubmit="return checkForm()">
        <input type="hidden" name="action" value="subOrder"/>
        <!-- 头部 end -->
        <div class="cart py-container">
            <!--主内容-->
            <div class="checkout py-container">
                <div class="step-tit">

                    <h4 style="font-weight: bold">填写并核对订单信息</h4>
                </div>
                <div class="checkout-steps">
                    <!--收件人信息-->
                    <div class="step-tit">
                        <h5>收件人信息</h5>
                    </div>
                    <div class="step-cont">
                        <div class="addressInfo">
                            <ul class="addr-detail">
                                <li class="addr-item">
                                    <div>
                                        <c:choose>
                                            <c:when test="${not empty list}">
                                                <c:forEach items="${list}" var="address">
                                                    <div class="con address">
                                                        <input type="radio" name="addressId" value="${address.aid}" <c:if test="${address.isdefault=='1'}">checked="checked"</c:if>/> ${address.contact}
                                                            ${address.address} <span>${address.telephone}</span>
                                                    </div>
                                                </c:forEach>
                                                <div class="con address">
                                                    我要修改收货地址,<a href="${pageContext.request.contextPath}/addressServlet?action=findAllByUid">点击此处</a>快速前往地址管理页面
                                                </div>
                                            </c:when>
                                            <c:otherwise>
                                            <div class="con address">
                                                您还未添加任何地址信息,<a href="${pageContext.request.contextPath}/addressServlet?action=findAllByUid">点击此处</a>快速前往地址管理页面
                                            </div>
                                            </c:otherwise>
                                        </c:choose>
                                        <div class="clearfix"></div>
                                    </div>
                                </li>
                            </ul>
                        </div>
                        <div class="hr"></div>

                    </div>
                    <div class="hr"></div>
                    <!--支付和送货-->
                    <div class="payshipInfo">
                        <div class="step-tit">
                            <h5>支付方式</h5>
                        </div>
                        <div class="step-cont">
                            <ul class="payType">
                                <li class="selected">微信付款</li>
                                <li >支付宝付款</li>
                                <li >银联付款</li>
                            </ul>
                        </div>
                        <div class="hr"></div>
                        <div class="step-tit">
                            <h5>送货清单</h5>
                        </div>
                        <div class="step-cont">
                            <ul class="send-detail">
                                <li>
                                    <div class="sendGoods">
                                            <c:forEach items="${cart.cartItemMap}" var="entry">
                                                <ul class="yui3-g">
                                                <li class="yui3-u-1-6">
                                                    <span><img src="${pageContext.request.contextPath}/${entry.value.route.rimage}"/></span>
                                                    </li>
                                                <li class="yui3-u-7-12">
                                                    <div class="desc">${entry.value.route.rname}</div>
                                                    <div class="seven">7天无理由退货</div>
                                                </li>
                                                <li class="yui3-u-1-12">
                                                    <div class="price">￥${entry.value.route.price}</div>
                                                </li>
                                                <li class="yui3-u-1-12">
                                                    <div class="num">X${entry.value.num}</div>
                                                </li>
                                                <li class="yui3-u-1-12">
                                                    <div class="exit">有货</div>
                                                </li>
                                                </ul>
                                            </c:forEach>
                                    </div>
                                </li>
                                <li></li>
                                <li></li>
                            </ul>
                        </div>
                        <div class="hr"></div>
                    </div>
                </div>
            </div>
            <div class="clearfix trade">
                <div class="fc-price">
                    <span class="number">${cart.cartNum}</span>件商品，应付金额:　<span class="price">¥${cart.cartTotal}</span>
                </div>
            </div>
            <div class="submit">
                <span style="font-size: 20px" id="submitInfo"></span>
                <button class="sui-btn btn-danger btn-xlarge" id="submit">提交订单</button>
            </div>
        </div>
    </form>
</div>
<!-- 底部栏位 -->
<!--引入尾部-->
<%@include file="footer.jsp" %>
<script src="js/getOrderInfo.js"></script>
<script>
    //表单提交事件
    function checkForm() {
        let flag=true;
        //获取地址单选框的value值
        let address = $("input[type='radio']:checked").val();
        if (!address) {
            //value值未定义,说明没有选择收货地址
            $("#submitInfo").html("请选择您的收货地址").css("color", "blue");
            flag=false;
        }

        //获取支付单选框的文本值
        let payType = $(".payType li[class=selected]").html();
        if (!payType) {
            //文本值未定义,说明没有选择付款方式
            $("#submitInfo").html("请选择您的付款方式").css("color", "blue");
            flag=false;
        }

        return flag;
    }

</script>
</body>
</html>
