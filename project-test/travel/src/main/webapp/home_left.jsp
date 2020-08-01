<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta charset="utf-8">
</head>
<body>
<!--左侧列表-->
<div class="yui3-u-1-6 list">

    <div class="person-info">
        <div class="person-photo" style="text-align: center">
            <c:if test="${empty currentUser.pic}">
                <img src="${pageContext.request.contextPath}/pic/girl.png" width="160px" class="img-circle" alt="">
            </c:if>
            <c:if test="${not empty currentUser.pic}">
            <img src="${pageContext.request.contextPath}/${currentUser.pic}" width="160px" class="img-circle" alt="">
            </c:if>
        </div>
        <div class="clearfix"></div>
    </div>
    <div class="list-items">
        <dl>
            <dt><i>·</i> 设置</dt>
            <dd><a href="${pageContext.request.contextPath}/userServlet?action=userInfo">个人信息</a></dd>
            <dd><a href="${pageContext.request.contextPath}/addressServlet?action=findAllByUid">地址管理</a></dd>
        </dl>
        <dl>
            <dt><i>·</i> 订单中心</dt>
            <dd><a href="${pageContext.request.contextPath}/orderServlet?action=findByPage">我的订单</a></dd>
            <dd><a href="${pageContext.request.contextPath}/orderServlet?action=findByPage&state=0">待付款</a></dd>
            <dd><a href="${pageContext.request.contextPath}/orderServlet?action=findByPage&state=1">待发货</a></dd>
            <dd><a href="javascript:">待收货</a></dd>
            <dd><a href="javascript:">待评价</a></dd>
        </dl>
        <dl>
            <dt><i>·</i> 我的中心</dt>
            <dd><a href="${pageContext.request.contextPath}/my_favorite.jsp">我的收藏</a></dd>
            <dd><a href="javascript:">我的足迹</a></dd>
        </dl>
        <dl>
            <dt><i>·</i> 物流消息</dt>
        </dl>

    </div>
</div>
</body>
</html>
