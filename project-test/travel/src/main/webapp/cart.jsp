<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>购物车列表</title>
</head>
<body>
<!--引入头部-->
<%@include file="header.jsp" %>
<div class="container">
    <c:choose>
        <c:when test="${not empty cart.cartItemMap}">
            <div class="row">
                <div style="margin:0 auto; margin-top:20px">
                    <div style="font-weight: bold;font-size: 15px;margin-bottom: 10px">商品数量：${cart.cartNum}</div>
                    <table class="table">
                        <tbody>
                        <tr bgcolor="#f5f5f5" class="table-bordered">
                            <th>图片</th>
                            <th>商品</th>
                            <th>价格</th>
                            <th>数量</th>
                            <th>小计</th>
                            <th>操作</th>
                        </tr>
                        <c:forEach items="${cart.cartItemMap}" var="entry">
                            <tr class="table-bordered">
                                <td width="180" width="40%">
                                    <input type="hidden" name="id" value="22">
                                    <img src="${pageContext.request.contextPath}/${entry.value.route.rimage}"
                                         width="170"
                                         height="100">
                                </td>
                                <td width="30%">
                                    <a href="${pageContext.request.contextPath}/routeServlet?action=findByDetail&rid=${entry.value.route.rid}"
                                       target="_blank">${entry.value.route.rname}</a>
                                </td>
                                <td width="10%">
                                    ￥${entry.value.route.price}
                                </td>
                                <td width="14%">
                                    ×${entry.value.num}
                                </td>
                                <td width="15%">
                                    <span class="subtotal">￥${entry.value.itemTotal}</span>
                                </td>
                                <td>
                                    <a href="javascript:deleteCartItem('${entry.value.route.rid}')"
                                       class="delete">删除</a>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
                <div>
                    <div style="text-align:right;">
                        商品金额: <strong style="color:#ff6600;">￥${cart.cartTotal}元</strong>
                    </div>
                    <div style="text-align:right;margin-top:10px;margin-bottom:10px;">
                        <a href="${pageContext.request.contextPath}/orderServlet?action=preOrder">
                            <input type="button" width="100" value="结算" name="submit" border="0" style="background-color: #ea4a36;
						height:45px;width:120px;color:white;font-size: 15px">
                        </a>
                    </div>
                </div>
            </div>
        </c:when>
        <c:otherwise>
            <div class="row" style="margin: 100px 200px;text-align: center">
                <p align="center" style="font-size: 20px;color: blue">购物车中空空如也,快去寻找自己喜欢的旅游路线吧</p>
            </div>
        </c:otherwise>
    </c:choose>
</div>
<!--引入尾部-->
<%@include file="footer.jsp" %>
<script>
    //删除功能
    function deleteCartItem(rid) {
        if (confirm("您确定要删除该条线路吗?")) {
            location.href = "${pageContext.request.contextPath}/cartServlet?action=delete&rid=" + rid;
        }
    }
</script>
</body>
</html>
