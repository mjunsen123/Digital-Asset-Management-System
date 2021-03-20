<%-- 
    Document   : login
    Created on : Nov 18, 2020, 2:00:54 PM
    Author     : yee_j
--%>
<%@page import="classPackage.user"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<% 
    user u = (user)session.getAttribute("user");
    if (u != null) {
        response.sendRedirect("dashboard.jsp");
    }
%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
        <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
        <title>PAVE - Login</title>
        <style>
            .center{
                position: absolute;
                top: 50%;
                left: 50%;
                -moz-transform: translateX(-50%) translateY(-50%);
                -webkit-transform: translateX(-50%) translateY(-50%);
                transform: translateX(-50%) translateY(-50%);
                width : 500px;
                height: 550px;
            } 
            button{
                width : 100%;
            }
            img{
                display: block;
                margin-left: auto;
                margin-right: auto;
                width: 50%;
                margin-bottom: 15px;
            }
            .name{
                margin-right: 10px;
                margin-bottom : 20px;
                margin-left: auto;
                margin-right : auto;
                width: 98%;
            }
            label{
                text-align: right;
                margin-right:15px;
                float:left;
            }
            input{
                margin-bottom : 10px;   
            }

            a{
                color: #F3451A;
                cursor: pointer;
            }

            .dialog{
                background : rgba(0,0,0,0.6);
                width: 100%;
                height: 100%;
                position : absolute;
                top : 0;
                display : none;
                justify-content: center;
                align-items: center;
                -webkit-animation-name: animatebottom;
                -webkit-animation-duration: 1s;
                animation-name: animatebottom;
                animation-duration: 1s
            }

            .dialogContent{
                height: auto;
                width: 530px;
                background-color: white;
                border-radius: 5px;
                padding :20px;
                padding-bottom: 30px;
                position: relative;
                text-align: center;
            }

            .dialog span{
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

            .dialog span:hover{
                cursor: pointer;
            }
            
            .loading{
                background : rgba(0,0,0,0.6);
                width: 100%;
                height: 100%;
                position : absolute;
                top : 0;
                display : none;
                justify-content: center;
                align-items: center;
            }

            .loader {
                border: 16px solid #f3f3f3;
                border-radius: 50%;
                border-top: 16px solid red;
                border-right: 16px solid green;
                border-bottom: 16px solid blue;
                border-left: 16px solid white;
                width: 100px;
                height: 100px;
                -webkit-animation: spin 2s linear infinite;
                animation: spin 2s linear infinite;
            }

            /* Safari */
            @-webkit-keyframes spin {
                0% { -webkit-transform: rotate(0deg); }
                100% { -webkit-transform: rotate(360deg); }
            }

            @keyframes spin {
                0% { transform: rotate(0deg); }
                100% { transform: rotate(360deg); }
            }

        </style>
    </head>
    <body>
        <div class="center" name="loginForm">
            <img src="img/PAVElogo.png" alt="alt"/>
            <label for="email">Email Address</label>
            <input type="email" id="email" placeholder="Email Address" class="form-control form-control" required="required"/>
            <label for="password">Password</label>
            <input type="password" id="pwd" placeholder="Password" class="form-control form-control"  required="required"/>
            <p style="color: red;" id="errorMsg"></p>
            <hr>
            <button class="btn btn-primary" onclick="login()" id="loginbtn">Login</button>
            <a href="register.jsp" class="btn btn-info" style="margin-top:15px; width: 100%">Register</a>
            <p style="margin-top: 15px; text-align: center;">Forgot your password? <a onclick="resetDialog()">Click here</a></p>     
        </div>
        <div class="dialog">
            <div class="dialogContent">
                <p style="font-size : 20px; text-align: left; margin-bottom: 0px;">Enter your email address and we will send you a link to reset your password.</p><hr>
                    <input type="email" id="resetEmail" name="resetEmail" placeholder="Email Address" class="form-control form-control" required="required"/>
                    <p style="color: green;" id="resetMessage"></p>
                    <button class="btn btn-danger" style="width:100%; margin-top:5px;" onclick="reset()" id="resetButton">Reset Password</button>
                <span class="fa disable-select" id="closeBtn" onclick="closeDialog()">&#xf00d;</span>
            </div>
        </div>
        <div class="loading" >
            <div class="loader"></div>
        </div>
    </body>
</html>

<script>
    
    function reset() {        
        var email = document.getElementById("resetEmail").value;
        var validation = false;
        var req = /^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/;
        if (email.match(req)) {
            validation = true;
        } else {
            document.getElementById("resetMessage").style.color = "red";
            document.getElementById("resetMessage").innerHTML = "Invalid Email format.";
        }
        if (validation) {
            document.querySelector(".loading").style.display = "flex";
            $.ajax({
                type: 'post',
                url: 'forgetPasswordServlet',
                data: {resetEmail: email},
                success: function(){
                    document.querySelector(".loading").style.display = "none";
                    document.getElementById("resetButton").disabled = true;
                    document.getElementById("resetMessage").style.color = "green";
                    document.getElementById("resetMessage").innerHTML = "We have e-mailed your password reset link!";
                }
            });
        }
    }
    
    function resetDialog() {
        document.querySelector(".dialog").style.display = "flex";
    }
    
    function closeDialog() {
        document.querySelector(".dialog").style.display = "none";
        document.getElementById("resetMessage").innerHTML = "";
        document.getElementById("resetEmail").value = "";
        document.getElementById("resetButton").disabled = false;
    }
    
    var passwordEnter = document.getElementById("pwd");
    passwordEnter.addEventListener("keyup", function (event) {
        if (event.keyCode === 13) {
            event.preventDefault();
            document.getElementById("loginbtn").click();
        }
    });
    
    function login() {
        var email = document.getElementById("email").value;
        var password = document.getElementById("pwd").value;
        var validation = false;
        var mailformat = /^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/;
        var pwdreq = /^(?=.*\d)(?=.*[a-zA-Z]).{8,16}$/;
        if (email.match(mailformat) && password.match(pwdreq))
        {
                validation = true;
        }
        
        if (validation) {
            $.ajax({
                type: 'post',
                url: 'loginServlet',
                data: {email: email, pwd: password},
                success: function (result) {
                    document.getElementById("errorMsg").innerHTML = "";
                    if (result === "2") {
                        document.location.href = 'dashboardServlet';
                    } else if (result === "1") {
                        document.getElementById("errorMsg").innerHTML = "Email not verified";
                    } else {
                        document.getElementById("errorMsg").innerHTML = "The email or password you entered is incorrect.";
                        document.getElementById("pwd").value = "";
                    }
                }
            });
        } else {
            document.getElementById("errorMsg").innerHTML = "The email or password you entered is incorrect.";
            document.getElementById("pwd").value = "";
        }
        
    }
    
    
</script>