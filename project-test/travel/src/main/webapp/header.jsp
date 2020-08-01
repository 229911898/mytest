<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--bootstrap--%>
<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/common.css">
<script src="${pageContext.request.contextPath}/js/jquery-3.3.1.js"></script>
<script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
<script src="${pageContext.request.contextPath}/js/getParameter.js"></script>

<!-- 头部 start -->
<header id="header">
    <%--广告--%>
    <div class="top_banner">
        <img src="${pageContext.request.contextPath}/images/top_banner.jpg" alt="">
    </div>
    <%--右侧按钮--%>
    <div class="shortcut">
        <c:if test="${empty currentUser}">
            <!-- 未登录状态 -->
            <div class="login_out">
                <a id="loginBtn" data-toggle="modal" data-target="#loginModel" style="cursor: pointer;">登录</a>
                <a href="register.jsp" style="cursor: pointer;">注册</a>
            </div>
        </c:if>
        <c:if test="${not empty currentUser}">
            <!-- 登录状态 -->
            <div class="login">
                <span>欢迎回来，${currentUser.username}</span>
                <a href="${pageContext.request.contextPath}/userServlet?action=userInfo" class="collection">个人中心</a>
                <a href="${pageContext.request.contextPath}/cartServlet?action=findAll" class="collection">购物车</a>
                <a href="${pageContext.request.contextPath}/userServlet?action=logout">退出</a>
            </div>
        </c:if>
    </div>
    <%--搜索框--%>
    <div class="header_wrap">
        <div class="topbar">
            <div class="logo">
                <a href="www.itcast.cn"><img src="${pageContext.request.contextPath}/images/logo.jpg" alt=""></a>
            </div>
            <div class="search">
                <input id="rname" name="rname" type="text" placeholder="请输入路线名称" class="search_input" value="${rname}"
                       autocomplete="off">
                <a href="javascript:void(0);" onclick="searchClick()" class="search-button">搜索</a>
            </div>
            <div class="hottel">
                <div class="hot_pic">
                    <img src="${pageContext.request.contextPath}/images/hot_tel.jpg" alt="">
                </div>
                <div class="hot_tel">
                    <p class="hot_time">客服热线(9:00-6:00)</p>
                    <p class="hot_num">400-618-9090</p>
                </div>
            </div>
        </div>
    </div>
</header>
<!-- 头部 end -->
<!-- 首页导航 -->
<div class="navitem">
    <ul class="nav" id="categoryUI">
        <li class="nav-active" id="indexLi"><a href="index.jsp">首页</a></li>
        <li><a href="favorite_rank.jsp">收藏排行榜</a></li>
    </ul>
</div>
<!-- 登录模态框 -->
<div class="modal fade" id="loginModel" tabindex="-1" role="dialog" aria-labelledby="loginModelLable">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <%--头部--%>
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title" id="loginModelLable">
                    <ul id="myTab" class="nav nav-tabs" style="width: auto">
                        <li class="active">
                            <a href="#pwdReg" data-toggle="tab">
                                密码登录
                            </a>
                        </li>
                        <li><a href="#telReg" data-toggle="tab">短信登录</a></li>
                    </ul>
                    <span id="loginErrorMsg" style="color: red;"></span>
                </h4>

            </div>
            <%--内容--%>
            <div id="myTabContent" class="tab-content">
                <%--密码登录--%>
                <div class="tab-pane fade in active" id="pwdReg">
                    <form id="pwdLoginForm" action="#" method="post">
                        <input type="hidden" name="action" value="pwdLogin">
                        <div class="modal-body">
                            <div class="form-group">
                                <label>用户名</label>
                                <input type="text" class="form-control" id="login_username" name="username"
                                       placeholder="请输入用户名">
                            </div>
                            <div class="form-group">
                                <label>密码</label>
                                <input type="password" class="form-control" id="login_password" name="password"
                                       placeholder="请输入密码">
                            </div>
                        </div>
                        <div class="modal-footer">
                            <span id="pwdLoginSpan" style="color:red"></span>
                            <input type="reset" class="btn btn-primary" value="重置">
                            <input type="button" id="pwdLogin" class="btn btn-primary" value="登录"/>
                        </div>
                    </form>
                </div>
                <%--短信登录--%>
                <div class="tab-pane fade" id="telReg">
                    <form id="telLoginForm" method="post" action="#">
                        <input type="hidden" name="action" value="telLogin">
                        <div class="modal-body">
                            <div class="form-group">
                                <label>手机号</label>
                                <input type="text" class="form-control" name="telephone" id="login_telephone"
                                       placeholder="请输入手机号">
                            </div>
                            <div class="form-group">
                                <label>手机验证码</label>
                                <input type="text" class="form-control" id="login_check" name="smsCode"
                                       placeholder="请输入手机验证码">
                            </div>
                            <input id="login_sendSmsCode" value="发送手机验证码" class="btn btn-link"/>
                        </div>
                        <div class="modal-footer">
                            <span id="telLoginSpan" style="color:red"></span>
                            <input type="reset" class="btn btn-primary" value="重置">
                            <input type="button" class="btn btn-primary" id="telLogin" value="登录"/>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
<script>
    //发送异步请求获取导航条列表所有数据
    $.ajax({
        url:"${pageContext.request.contextPath}/categoryServlet",
        type:"post",
        data:"action=findAll",
        success:function (response) {
            //遍历json数据,展示导航条信息
            for (let c of response) {
                $("#indexLi").after("<li><a href='${pageContext.request.contextPath}/routeServlet?action=findByPage&cid="+c.cid+"'>"+c.cname+"</a></li>")
            }
        },
        dataType:"json"
    });

    //页面加载事件
    $(function () {
        //绑定点击事件
        $("#pwdLogin").click(function () {
            //先判断用户名和密码输入是否为空
            let username = $("#login_username").val();
            let password = $("#login_password").val();
            if (username == "" || password == "") {
                $("#pwdLoginSpan").html("用户名和密码不能为空");
                return;
            }

            //发送异步请求校验用户名和密码
            $.ajax({
                url: "${pageContext.request.contextPath}/userServlet",
                type: "post",
                data: $("#pwdLoginForm").serialize(),
                success: function (response) {
                    if (response.success) {
                        location.reload();
                    } else {
                        $("#pwdLoginSpan").html(response.message);
                    }
                },
                dataType: "json"
            });
        });

        //绑定点击事件
        $("#login_sendSmsCode").click(function () {
            //先判断手机号码格式是否正确
            let telephone = $("#login_telephone").val();
            if (!/^1[3578]\d{9}$/.test(telephone)) {
                alert("请输入正确的手机号码");
                return;
            }

            //发送异步请求获取短信验证码
            $.ajax({
                url: "${pageContext.request.contextPath}/userServlet",
                type: "post",
                data: {"action": "sendSmsCode", "telephone": telephone},
                success: function (response) {
                    alert(response.message);
                },
                dataType: "json"
            });

            //调用计时器
            countDownLogin(this);
        });

        //为发送短信验证码设置60秒计时器
        function countDownLogin(obj) {
            let count=60;
            let id=setInterval(function () {
                count--;
                $(obj).prop("disabled", true);
                $(obj).val(count+"秒后可重新发送验证码");

                if (count == 0) {
                    clearInterval(id);
                    $(obj).prop("disabled", false);
                    $(obj).val("重新发送验证码");
                }
            },1000);
        }

        //绑定点击事件
        $("#telLogin").click(function () {
            //先判断手机号和验证码输入是否为空
            let telephone = $("#login_telephone").val();
            let smsCode = $("#login_check").val();
            if (telephone == "" || smsCode == "") {
                $("#telLoginSpan").html("手机号和验证码不能为空");
                return;
            }

            //发送异步请求校验用户名和密码
            $.ajax({
                url: "${pageContext.request.contextPath}/userServlet",
                type: "post",
                data: $("#telLoginForm").serialize(),
                success: function (response) {
                    if (response.success) {
                        location.reload();
                    } else {
                        $("#telLoginSpan").html(response.message);
                    }
                },
                dataType: "json"
            });
        });
    });

    //搜索框点击事件
    function searchClick() {
        let rname = $("#rname").val().trim();
        //判断搜索框是否为空
        if (rname == "") {
            //为空,刷新当前页面
            location.reload();
        } else {
            //不为空则发送分页查询请求
            location.href = "${pageContext.request.contextPath}/routeServlet?action=findByPage&cid=${cid}&rname=" + rname;
        }
    }
</script>