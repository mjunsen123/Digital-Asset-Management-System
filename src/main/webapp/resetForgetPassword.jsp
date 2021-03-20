<%-- 
    Document   : resetForgetPassword
    Created on : Dec 20, 2020, 12:00:28 PM
    Author     : yee_j
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Reset Password</title>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
        <style>
            .center{
                position: absolute;
                top: 50%;
                left: 50%;
                -moz-transform: translateX(-50%) translateY(-50%);
                -webkit-transform: translateX(-50%) translateY(-50%);
                transform: translateX(-50%) translateY(-50%);
                width:450px;
            }         
            button{
                width : 100%;
                margin-bottom: 20px;
            }
            img{
                display: block;
                margin-left: auto;
                margin-right: auto;
                width: 40%;
                margin-bottom: 15px;
            }
            h2{
                font-family: "Lucida Console","Monospace";
                text-align: center;
            }
            .successDialog{
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
                width: 500px;
                background-color: white;
                border-radius: 5px;
                padding :20px;
                padding-bottom: 30px;
                position: relative;
                text-align: center;
            }

            .dialogContent h3{
                margin-left: auto;
                margin-right : auto;
                border-bottom: solid grey;
                padding-bottom: 15px;
                width: 90%;
                color: green;
            }

            .failedDialog{
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

            .dialogContent p{
                text-align: left;
                padding-left: 20px;
            }

            .dialogContent button{
                width: 90%;
                margin-top: 3px;
            }
        </style>
    </head>
    <body>
        <div class="center">
            <img src="img/PAVElogo.png" alt="alt"/>
            <h2>Reset Password</h2>
            <hr>
            <input type="password" id="pwd" placeholder="Password" class="form-control form-control box"/>
            <p style="color:red; padding-left: 10px; padding-top: 5px;" id="passwordError"></p>
            <input type="password" id="rpwd" placeholder="Confirm Password" class="form-control form-control box"/>
            <p style="color:red; padding-left: 10px; padding-top: 5px;" id="confirmError"></p>
            <button class="btn btn-danger" id="register" onclick="reset()">Reset</button>
        </div>
        <div class="successDialog">
            <div class="dialogContent">
                <h3>&#10004; SUCCESS</h3>
                <p style="font-size : 20px;">Your password has been reset, you can now login with your new password.</p>
                <button type="button" class="btn btn-success" onclick="loginPage()">Continue</button>
            </div>
        </div>
        <div class="failedDialog">
            <div class="dialogContent">
                <h3 style="color:red;">&#x2716; FAILED</h3>
                <p style="font-size : 20px;" id="errorMessage">Password reset fail due to invalid password reset request. Please try again later.</p>
                <button type="button" class="btn btn-success" onclick="loginPage()">Continue</button>
            </div>
        </div>
    </body>
</html>

<script>
    let timeout = null;
    document.getElementById('rpwd', 'pwd').addEventListener('keyup', function () {
        var req = /^(?=.*\d)(?=.*[a-zA-Z]).{8,16}$/;
        clearTimeout(timeout);
        timeout = setTimeout(function () {
            if ($('#pwd').val() !== $('#rpwd').val()) {
                document.getElementById("confirmError").innerHTML = "The Confirm Password confirmation does not match";
                document.getElementById("rpwd").classList.add("borderColor");
            } else {
                document.getElementById("confirmError").innerHTML = "";
                document.getElementById("rpwd").classList.remove("borderColor");
            }
            if (document.getElementById("pwd").value.match(req)) {
                document.getElementById("passwordError").innerHTML = "";
                document.getElementById("pwd").classList.remove("borderColor");

            } else {
                document.getElementById("passwordError").innerHTML = "Password length must between 7-17 characters, must contain both numeric digits and letter";
                document.getElementById("pwd").classList.add("borderColor");
            }
        }, 500);
    });

    document.getElementById('pwd').addEventListener('keyup', function () {
        clearTimeout(timeout);
        var req = /^(?=.*\d)(?=.*[a-zA-Z]).{8,16}$/;
        timeout = setTimeout(function () {
            if (document.getElementById("pwd").value.match(req)) {
                document.getElementById("passwordError").innerHTML = "";
                document.getElementById("pwd").classList.remove("borderColor");

            } else {
                document.getElementById("passwordError").innerHTML = "Password length must between 7-17 characters, must contain both numeric digits and letter";
                document.getElementById("pwd").classList.add("borderColor");
            }
            if ($('#pwd').val() !== $('#rpwd').val() && $('#rpwd').val() !== "") {
                document.getElementById("confirmError").innerHTML = "The Confirm Password confirmation does not match";
                document.getElementById("rpwd").classList.add("borderColor");
            } else {
                document.getElementById("confirmError").innerHTML = "";
                document.getElementById("rpwd").classList.remove("borderColor");
            }
        }, 0);
    });

    function loginPage() {
        window.location.href = 'login.jsp';
    }

    function reset() {
        var req = /^(?=.*\d)(?=.*[a-zA-Z]).{8,16}$/;
        var email = '<%= request.getParameter("key1")%>';
        var key = '<%= request.getParameter("key2")%>';
        var password = document.getElementById("pwd").value;
        var confirmPassword = document.getElementById("rpwd").value;
        var validation = false;

        if (password.match(req)) {
            if (password === confirmPassword) {
                validation = true;
            } else {
                document.getElementById("confirmError").innerHTML = "The Confirm Password confirmation does not match";
                document.getElementById("rpwd").classList.add("borderColor");
            }
        } else {
            document.getElementById("passwordError").innerHTML = "Password length must between 7-17 characters, must contain both numeric digits and letter";
            document.getElementById("pwd").classList.add("borderColor");
        }

        if (validation) {
            $.ajax({
                type: 'post',
                url: 'resetPasswordServlet',
                data: {email: email, pwd: password, key: key},
                success: function (result) {
                    if (result === "1") {
                        document.querySelector(".successDialog").style.display = "flex";
                    } else {
                        document.querySelector(".failedDialog").style.display = "flex";
                    }
                }
            });
        }
    }
</script>