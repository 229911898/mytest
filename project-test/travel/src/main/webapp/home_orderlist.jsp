<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="css/webbase.css">
    <link rel="stylesheet" href="css/pages-seckillOrder.css">
    <title>我的订单</title>

</head>
<body>
<!--引入头部-->
<%@include file="header.jsp"%>
<div class="container-fluid">
    <!--header-->
    <div id="account">
        <div class="py-container">
            <div class="yui3-g home">
                <!--左侧列表-->
                <%@include file="home_left.jsp"%>
                <!--右侧主内容-->
                <div class="yui3-u-5-6 order-pay">
                    <div class="body">
                        <div class="table-title">
                            <table class="sui-table  order-table">
                                <tr>
                                    <thead>
                                    <th width="18%">图片</th>
                                    <th width="15%">商品</th>
                                    <th width="15%">单价</th>
                                    <th width="10%">数量</th>
                                    <th width="8%">商品操作</th>
                                    <th width="10%">实付款</th>
                                    </thead>
                                </tr>
                            </table>
                        </div>
                        <c:choose>
                            <c:when test="${not empty pb.list}">
                                <c:forEach items="${pb.list}" var="order">
                                    <div class="order-detail">
                                        <div class="orders">
                                            <!--order1-->
                                            <div class="choose-title">
                                                <label>
                                                    <span>${order.ordertime}　订单编号：${order.oid}  店铺：${order.orderItemList[0].route.seller.sname} <a>和我联系</a></span>
                                                </label>
                                                <a class="sui-btn btn-info share-btn">分享</a>
                                            </div>
                                            <table class="sui-table table-bordered order-datatable">
                                                <tbody>
                                                <c:forEach items="${order.orderItemList}" var="orderItem">
                                                    <tr>
                                                        <td width="15%">
                                                            <div class="typographic">
                                                                <img src="${pageContext.request.contextPath}/${orderItem.route.rimage}" width="150" height="80">

                                                            </div>
                                                        </td>
                                                        <td width="35%">
                                                            <div>
                                                                <a href="${pageContext.request.contextPath}/routeServlet?action=findByDetail&rid=${orderItem.route.rid}" class="block-text">${orderItem.route.rname}</a>
                                                            </div>
                                                        </td>
                                                        <td width="5%" class="center">
                                                            <ul class="unstyled">
                                                                <li>¥${orderItem.route.price}</li>
                                                            </ul>
                                                        </td>
                                                        <td width="5%" class="center">${orderItem.num}</td>
                                                        <td width="8%" class="center">
                                                            售后服务
                                                        </td>
                                                        <td width="10%" class="center" >
                                                            <ul class="unstyled">
                                                                <li>¥${orderItem.subtotal}</li>
                                                            </ul>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                                </tbody>
                                            </table>
                                        </div>
                                        <div class="clearfix"></div>
                                    </div>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <div class="order-detail">
                                    <p align="center" style="font-size: 20px;color: blue">你还未添加任何订单,快去下单吧</p>
                                </div>
                            </c:otherwise>
                        </c:choose>

                        <div style="margin-top: 50px">
                            <div class="page_num_inf">
                                <i></i> 共
                                <span>${pb.totalPage}</span>页<span>${pb.totalCount}</span>条
                            </div>
                            <div class="pageNum">
                                <ul>
                                    <c:if test="${pb.curPage>1}">
                                        <li><a href="${pageContext.request.contextPath}/orderServlet?action=findByPage&state=${state}">首页</a></li>
                                        <li class="threeword"><a href="${pageContext.request.contextPath}/orderServlet?action=findByPage&state=${state}&curPage=${pb.curPage-1}">上一页</a></li>
                                    </c:if>
                                    <c:forEach begin="${pb.begin}" end="${pb.end}" var="page">
                                        <c:if test="${pb.curPage==page}">
                                            <li class="curPage"><a href="${pageContext.request.contextPath}/orderServlet?action=findByPage&state=${state}&curPage=${page}">${page}</a></li>
                                        </c:if>
                                        <c:if test="${pb.curPage!=page}">
                                            <li><a href="${pageContext.request.contextPath}/orderServlet?action=findByPage&state=${state}&curPage=${page}">${page}</a></li>
                                        </c:if>
                                    </c:forEach>

                                    <c:if test="${pb.curPage<pb.totalPage}">
                                        <li class="threeword"><a href="${pageContext.request.contextPath}/orderServlet?action=findByPage&state=${state}&curPage=${pb.curPage+1}">下一页</a></li>
                                        <li class="threeword"><a href="${pageContext.request.contextPath}/orderServlet?action=findByPage&state=${state}&curPage=${pb.totalPage}">末页</a></li>
                                    </c:if>
                                </ul>
                            </div>
                        </div>
                    </div>

                </div>
            </div>
        </div>
    </div>
</div>
</div>
<!--引入尾部-->
<%@include file="footer.jsp"%>
<script type="text/javascript" src="js/plugins/citypicker/distpicker.data.js"></script>
<script type="text/javascript" src="js/plugins/citypicker/distpicker.js"></script>
</body>
</html>
