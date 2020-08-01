<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/webbase.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/pages-seckillOrder.css">
    <title>地址管理</title>

</head>
<body>
<!--引入头部-->
<%@include file="header.jsp" %>
<div class="container-fluid">
    <!--header-->
    <div id="account">
        <div class="py-container">
            <div class="yui3-g home">
                <!--左侧列表-->
                <%@include file="home_left.jsp" %>
                <!--右侧主内容-->
                <div class="yui3-u-5-6 order-pay">
                    <div class="body userAddress">
                        <div class="address-title">
                            <span class="title">地址管理</span>
                            <a data-toggle="modal" data-target="#addressModel" data-keyboard="false"
                               class="sui-btn  btn-info add-new" id="save">添加新地址</a>
                            <span class="clearfix"></span>
                        </div>
                        <div class="address-detail">
                            <table class="sui-table table-bordered">
                                <thead>
                                <tr>
                                    <th>姓名</th>
                                    <th>地址</th>
                                    <th>联系电话</th>
                                    <th>操作</th>
                                </tr>
                                </thead>
                                <tbody>

                                <c:choose>
                                    <c:when test="${not empty list}">
                                        <c:forEach items="${list}" var="address">
                                            <tr>
                                                <td>${address.contact}</td>
                                                <td>${address.address}</td>
                                                <td>${address.telephone}</td>
                                                <td>
                                                    <a data-toggle="modal" data-target="#addressModel" class="update"
                                                       name="${address.aid}">编辑</a>
                                                    <a href="javascript:deleteAddress('${address.aid}')">删除</a>
                                                    <a href="#" class="isDefault" name="${address.aid}">设为默认</a>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <tr>
                                            <td align="center" colspan="4" style="color: blue;font-size: 20px">地址栏空空如也,快去添加您的收货地址吧</td>
                                        </tr>
                                    </c:otherwise>
                                </c:choose>
                                </tbody>
                            </table>
                        </div>

                        <!-- 地址模态框 -->
                        <div class="modal fade" id="addressModel" tabindex="-1" role="dialog"
                             aria-labelledby="loginModelLable">
                            <div class="modal-dialog" role="document">
                                <div class="modal-content">
                                    <%-- 新增地址--%>
                                    <div class="tab-pane fade in active">
                                        <form id="xxxx" action="${pageContext.request.contextPath}/addressServlet"
                                              method="post">
                                            <input type="hidden" name="aid" id="aid">
                                            <div class="modal-body">
                                                <div class="form-group">
                                                    <label>姓名</label><span id="check01"></span>
                                                    <input type="text" class="form-control" name="contact" id="contact"
                                                           placeholder="姓名">
                                                </div>
                                                <div class="form-group">
                                                    <label>地址</label><span id="check02"></span>
                                                    <input type="text" class="form-control" name="address" id="address"
                                                           placeholder="请输入地址">
                                                </div>
                                                <div class="form-group">
                                                    <label>联系电话</label><span id="check03"></span>
                                                    <input type="text" class="form-control" name="telephone"
                                                           id="telephone"
                                                           placeholder="联系电话">
                                                </div>
                                            </div>
                                            <div class="modal-footer">
                                                <input type="button" class="btn btn-default" data-dismiss="modal"
                                                       value="关闭">
                                                <input type="button" class="btn btn-primary" id="submit" value="保存"/>
                                            </div>
                                        </form>
                                    </div>
                                </div>
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
<%@include file="footer.jsp" %>
</body>
<script>
    //页面加载事件
    $(function () {
        //给所有编辑标签绑定点击事件
        $(".update").click(function () {
            //获取当前address对象的aid
            let aid = $(this).attr("name");
            //异步请求根据aid获取address对象
            $.ajax({
                url: "${pageContext.request.contextPath}/addressServlet",
                type: "post",
                data: {"action": "findByAid", "aid": aid},
                success: function (response) {
                    //数据回显
                    $("#aid").val(response.aid);
                    $("#contact").val(response.contact);
                    $("#address").val(response.address);
                    $("#telephone").val(response.telephone);
                },
                dataType: "json"
            })
        });

        //为新增标签绑定点击事件,清除模态框中的数据
        $("#save").click(function () {
            $("#aid").val("");
            $("#contact").val("");
            $("#address").val("");
            $("#telephone").val("");
        });

        //为保存标签绑定点击事件
        $("#submit").click(function () {
            let flag = true;

            //表单校验
            //检验联系人是否为空
            let contact = $("#contact").val().trim();
            if (contact == "") {
                $("#check01").html("姓名不能为空").css("color", "red");
                flag = false;
            } else {
                $("#check01").empty();
            }

            //检验地址是否符合要求(不能小于6个字符)
            let address = $("#address").val().trim();
            if (address.length < 6) {
                $("#check02").html("地址长度不能低于6个字符").css("color", "red");
                flag = false;
            } else {
                $("#check02").empty();
            }

            //检验手机号码格式是否正确
            let telephone = $("#telephone").val();
            if (!/^1[3578]\d{9}$/.test(telephone)) {
                $("#check03").html("请输入正确的手机号码").css("color", "red");
                flag = false;
            } else {
                $("#check03").empty();
            }

            //获取隐藏域aid的值
            let aid = $("#aid").val();
            if (flag) {
                //校验通过,判断aid的值是否为空
                if (aid == "") {
                    //为空,说明是新增地址,调用保存功能
                    location.href = "${pageContext.request.contextPath}/addressServlet?action=save&contact=" + contact + "&address=" + address + "&telephone=" + telephone;
                } else {
                    //不为空,说明是编辑地址,调用更新功能
                    location.href = "${pageContext.request.contextPath}/addressServlet?action=update&contact=" + contact + "&address=" + address + "&telephone=" + telephone + "&aid=" + aid;
                }
            }
        });

        //给所有设为默认标签绑定点击事件
        $(".isDefault").click(function () {
            //获取当前address对象的aid
            let aid = $(this).attr("name");
            //异步请求根据aid获取address对象
            $.ajax({
                url: "${pageContext.request.contextPath}/addressServlet",
                type: "post",
                data: {"action": "isDefault", "aid": aid},
                success: function (response) {
                    alert(response.message);
                },
                dataType: "json"
            })
        });
    });

    //删除功能
    function deleteAddress(aid) {
        if (confirm("您确定要删除该条地址信息吗?")) {
            location.href = "${pageContext.request.contextPath}/addressServlet?action=delete&aid=" + aid;
        }
    }
</script>
</html>
