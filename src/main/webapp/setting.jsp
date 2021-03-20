<%-- 
    Document   : setting
    Created on : Jan 16, 2021, 8:36:15 PM
    Author     : yee_j
--%>

<%@page import="classPackage.user"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    user u = (user) session.getAttribute("user");
    if (u == null) {
        response.sendRedirect("login.jsp");
    }
%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Setting | Preferences</title>
        <link href="css/sideBar.css" rel="stylesheet" type="text/css">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
        <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>

        <style>
            .personal{
                display:block;
                position: relative;
                top:50px;
                width : 520px;
                height: 280px;
                margin: 0px auto;
                padding: 10px;
                box-shadow: 6px 6px 29px -4px rgba(0,0,0,0.10);
            } 
            label{
                font-size: 15px;
            }
            .name{
                margin-left: auto;
                margin-right : auto;
                margin-top: 10px;
                width: 100%;
            }
            .fname{
                float : left;
                width : 48%;
            }
            .lname{
                float : right;
                width : 48%;
            }

            h3{
                margin: 10px;
            }
            hr{
                margin-top: 0px ;
            }
            .preference{
                display:block;
                width : 520px;
                height: 280px;
                margin : 50px auto;
                padding: 10px;
                box-shadow: 6px 6px 29px -4px rgba(0,0,0,0.10);
            }

            .preference p{
                display: inline-block;
                font-size: 15px;
                float: left;
                margin: auto;
            }

            .preference label{
                float:right;
                display: inline-block;
            }

            .switch {
                position: relative;
                display: inline-block;
                width: 50px;
                height: 28px;
            }

            .switch input { 
                opacity: 0;
                width: 0;
                height: 0;
            }

            .slider {
                position: absolute;
                cursor: pointer;
                top: 0;
                left: 0;
                right: 0;
                bottom: 0;
                background-color: #ccc;
                -webkit-transition: .4s;
                transition: .4s;
            }

            .slider:before {
                position: absolute;
                content: "";
                height: 20px;
                width: 20px;
                left: 4px;
                bottom: 4px;
                background-color: white;
                -webkit-transition: .4s;
                transition: .4s;
            }

            input:checked + .slider {
                background-color: #2196F3;
            }

            input:focus + .slider {
                box-shadow: 0 0 1px #2196F3;
            }

            input:checked + .slider:before {
                -webkit-transform: translateX(20px);
                -ms-transform: translateX(20px);
                transform: translateX(20px);
            }

            .option{
                border: black solid 1px;
                height: 30px;
                padding:auto;
            }

            .passwordForm{
                background : rgba(0,0,0,0.6);
                width: 100%;
                height: 100%;
                position : absolute;
                top : 0;
                display : none;
                justify-content: center;
                align-items: center;
                z-index: 2;
            }

            .formContent h3{
                text-align: center;
                padding-left: 10px;
                font-weight: bold;
            }

            .formContent span{
                text-align: center;
                position : absolute;
                top: -12px;
                right : -12px;
                background-color: white;
                height: 20px;
                width: 20px;
                font-size: 20px;
                border-radius: 50%;
                box-shadow: 6px 6px 29px -4px rgba(0,0,0,0.75);
            }

            .formContent label{
                display: block;
                text-align: left;
            }

            .formContent input{
                text-align: left;
                margin-bottom: 10px;
            }

            .formContent button{
                width: 100%;
            }

        </style>

    </head>
    <body>
        <div class="sidenav" id="mySidenav" style="width:0px;">
            <a href="dashboardServlet">My Files</a>
            <a href="shareFilesServlet">Share With Me</a>
            <a href="#">Setting</a>
            <hr style="border-bottom : #000000 1px solid; width: 80%;">            
            <a href="logoutServlet">Logout</a>
        </div>
        <div class="main" id="main" style="margin-left: 0px;">
            <div class="barIcon">
                <span style="font-size:25px;cursor:pointer;margin-right: 10px;" onclick="navBar()">&#9776;</span>
            </div>
            <div class="personal">
                <h3 style="text-align:center;">Personal Information</h3>
                <hr>
                <label>Email Address</label>
                <input type="text" class="form-control form-control" value="<c:if test="${user != null}"><c:out value="${user.getEmail()}" /></c:if>" readonly/>
                    <div class="name">
                        <label class="fname">First Name</label>
                        <label class="lname">Last Name</label>
                        <input type="text" class="form-control form-control fname" value="<c:if test="${user != null}"><c:out value="${user.getFirstName()}" /></c:if>" readonly/>
                    <input type="text" class="form-control form-control lname" value="<c:if test="${user != null}"><c:out value="${user.getLastName()}" /></c:if>" readonly/>
                </div>
                <button class="btn btn-danger" style="margin-top: 20px; float: right;" onclick="showPasswordContainer()">Change Password</button>
            </div>

            <div class="passwordForm">
                <div class="formContent" style="width:30%; height:auto;">
                    <h3>Change Password</h3>
                    <hr>
                    <label>Old Password</label>
                    <input type="password" class="form-control form-control" id="opwd"/>
                    <label>New Password</label>
                    <input type="password" class="form-control form-control" id="npwd"/>
                    <label>Confirm New Password</label>
                    <input type="password" class="form-control form-control" id="cnpwd"/>
                    <hr>
                    <p style="text-align: left;" id="errorMsg"></p>
                    <button type="button" class="btn btn-light" onclick="changePassword()">Change Password</button>
                    <span class="fa disable-select" id="closeBtn" onclick="closePasswordForm()">&#xf00d;</span>
                </div>
            </div>

        </div>
    </body>
</html>

<script>
    function navBar() {
        if (document.getElementById("mySidenav").style.width !== "0px") {
            document.getElementById("mySidenav").style.width = "0px";
            document.getElementById("main").style.marginLeft = "0px";
            document.getElementById("preview").style.marginLeft = "10px";
        } else {
            document.getElementById("mySidenav").style.width = "180px";
            document.getElementById("main").style.marginLeft = "180px";
            document.getElementById("preview").style.marginLeft = "190px";
        }
    }

    function showPasswordContainer() {
        document.querySelector(".passwordForm").style.display = "flex";
    }

    function closePasswordForm() {
        document.querySelector(".passwordForm").style.display = "none";
        document.getElementById("opwd").value = "";
        document.getElementById("npwd").value = "";
        document.getElementById("cnpwd").value = "";
        document.getElementById("errorMsg").innerHTML = "";
    }

    function changePassword() {
        var pswReq = /^(?=.*\d)(?=.*[a-zA-Z]).{7,15}$/;
        var opwd = document.getElementById("opwd");
        var npwd = document.getElementById("npwd");
        var cnpwd = document.getElementById("cnpwd");

        var validation = false;

        if (opwd.value !== "") {
            if (npwd.value !== "") {
                if (cnpwd.value !== "") {
                    if (npwd.value === cnpwd.value) {
                        if (npwd.value.match(pswReq)) {
                            validation = true;
                        } else {
                            document.getElementById("errorMsg").style.color = "red";
                            document.getElementById("errorMsg").innerHTML = "Password length must between 7-17 characters, must contain both numeric digits and letter.";
                        }
                    } else {
                        cnpwd.classList.add("borderColor");
                        document.getElementById("errorMsg").style.color = "red";
                        document.getElementById("errorMsg").innerHTML = "New password and confirmed password must be same";
                    }
                } else {
                    document.getElementById("errorMsg").style.color = "red";
                    document.getElementById("errorMsg").innerHTML = "Please fill in the confirmed new password";
                }
            } else {
                document.getElementById("errorMsg").style.color = "red";
                document.getElementById("errorMsg").innerHTML = "Please fill in the new password";
            }
        } else {
            document.getElementById("errorMsg").style.color = "red";
            document.getElementById("errorMsg").innerHTML = "Please fill in the old password";
        }

        if (validation) {
            $.ajax({
                type: 'post',
                url: 'changePassword',
                data: {npwd: npwd.value, opwd: opwd.value},
                success: function (result) {
                    if (result === "1") {
                        document.getElementById("errorMsg").style.color = "green";
                        document.getElementById("errorMsg").innerHTML = "Password change successfully, you can now login with your new password.";
                    } else if (result === "2") {
                        document.getElementById("errorMsg").style.color = "red";
                        document.getElementById("errorMsg").innerHTML = "Incorrect password";
                    } else {
                        document.getElementById("errorMsg").style.color = "red";
                        document.getElementById("errorMsg").innerHTML = "Unknown error occur, please try again later.";
                    }
                }
            });
        }

    }
</script>
