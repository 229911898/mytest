<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8">
    <title>注册</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/register.css">
</head>
<body>
<!--引入头部-->
<%@include file="header.jsp" %>
<!-- 头部 end -->
<div class="rg_layout">
    <div class="rg_form clearfix">
        <%--左侧--%>
        <div class="rg_form_left">
            <p>新用户注册</p>
            <p>USER REGISTER</p>
        </div>
        <div class="rg_form_center">
            <!--注册表单-->
            <form id="registerForm" action="${pageContext.request.contextPath}/userServlet" method="post"
                  onsubmit="return checkForm()">
                <!--提交处理请求的标识符-->
                <input type="hidden" name="action" value="register">
                <table style="margin-top: 25px;width: 558px">
                    <tr>
                        <td class="td_left">
                            <label for="username">用户名</label>
                        </td>
                        <td class="td_right">
                            <input type="text" id="username" name="username" placeholder="请输入账号">
                            <span id="userInfo" style="font-size:10px"></span>
                        </td>
                    </tr>
                    <tr>
                        <td class="td_left">
                            <label for="telephone">手机号</label>
                        </td>
                        <td class="td_right">
                            <input type="text" id="telephone" name="telephone" placeholder="请输入您的11位手机号">
                            <span id="telephoneInfo" style="font-size:10px"></span>
                        </td>
                    </tr>
                    <tr>
                        <td class="td_left">
                            <label for="password">密码</label>
                        </td>
                        <td class="td_right">
                            <input type="password" id="password" name="password" placeholder="请输入6-12位密码">
                            <span id="passwordInfo" style="font-size:10px"></span>
                        </td>
                    </tr>
                    <tr>
                        <td class="td_left">
                            <label for="smsCode">验证码</label>
                        </td>
                        <td class="td_right check">
                            <input type="text" id="smsCode" name="smsCode" class="check" placeholder="请输入验证码">
                            <input id="sendSmsCode" value="发送手机验证码" class="btn btn-link"/>
                            <span id="smsCodeInfo" style="font-size:10px"></span>
                        </td>
                    </tr>
                    <tr>
                        <td class="td_left">
                        </td>
                        <td class="td_right check">
                            <input type="submit" class="submit" value="注册">
                            <span id="msg" style="color: red;">${resultInfo.message}</span>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
        <%--右侧--%>
        <div class="rg_form_right">
            <p>
                已有账号？
                <a href="javascript:$('#loginBtn').click()">立即登录</a>
            </p>
        </div>
    </div>
</div>
<!--引入尾部-->
<%@include file="footer.jsp" %>

<script>
    //页面加载事件
    $(function () {
        //用户名异步验证
        $("#username").blur(function () {
            let username = $(this).val().trim();
            //先判断用户名是否为空
            if (username == "") {
                $("#userInfo").html("用户名不能为空").css("color", "red");
                return;
            }

            //发送异步请求验证用户名是否已存在
            $.ajax({
                url: "${pageContext.request.contextPath}/userServlet",
                type: "post",
                data: {"action": "findByUsername", "username": username},
                success: function (response) {
                    if (response.success) {
                        $("#userInfo").html("此用户名太受欢迎，请更换一个").css("color", "red");
                    } else {
                        $("#userInfo").html("√").css("color", "green");
                    }
                },
                dataType: "json"
            });
        });

        //手机号异步验证
        $("#telephone").blur(function () {
            let telephone = $(this).val();
            //先判断手机号码格式是否正确
            if (!/^1[3578]\d{9}$/.test(telephone)) {
                $("#telephoneInfo").html("请输入正确的手机号码").css("color", "red");
                return;
            }

            //发送异步请求验证手机号码是否已存在
            $.ajax({
                url: "${pageContext.request.contextPath}/userServlet",
                type: "post",
                data: {"action": "findByTelephone", "telephone": telephone},
                success: function (response) {
                    if (response.success) {
                        $("#telephoneInfo").html("该手机号码已注册").css("color", "red");
                    } else {
                        $("#telephoneInfo").html("√").css("color", "green");
                    }
                },
                dataType: "json"
            });

        });

        //验证密码格式
        $("#password").blur(function () {
            let password = $(this).val();
            if (!/^\w{6,12}$/.test(password)) {
                $("#passwordInfo").html("密码仅限数字,字母和下划线_").css("color", "red");
            } else {
                $("#passwordInfo").html("√").css("color", "green");
            }
        });

        //验证短信验证码格式
        $("#smsCode").blur(function () {
            let smsCode = $(this).val();
            if (!/^\d{6}$/.test(smsCode)) {
                $("#smsCodeInfo").html("请输入正确的验证码").css("color", "red");
            } else {
                $("#smsCodeInfo").html("√").css("color", "green");
            }
        });

        //异步发送验证码
        $("#sendSmsCode").click(function () {
            //先判断手机号码格式是否正确
            let telephone = $("#telephone").val();
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
            countDown(this);
        });
    });

    //为发送短信验证码设置60秒计时器
    function countDown(obj) {
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

    //提交表单事件
    function checkForm() {
        //判断所有字段是否为空
        if ($("#username").val() == "" || $("#telephone").val() == "" || $("#password").val() == "" || $("#smsCode").val() == "") {
            alert("必填字段不能为空");
            return false;
        }

        //判断所有字段格式是否正确
        if ((!/^1[3578]\d{9}$/.test($("#telephone").val())) || (!/^\w{6,12}$/.test($("#password").val())) || (!/^\d{6}$/.test($("#smsCode").val()))) {
            alert("必填字段格式不正确");
            return false;
        }

        //判断用户名和手机号码是否已存在
        if ($("#userInfo").html() != "√" || $("#telephoneInfo").html() != "√") {
            alert("用户名或手机号码已注册");
            return false;
        }

        return true;
    }
</script>
</body>
</html>
