<%-- 
    Document   : register.jsp
    Created on : Nov 18, 2020, 11:34:10 AM
    Author     : yee_j
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
        <title>Register</title>

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
            .box{
                margin-right: 10px;
                margin-left: auto;
                margin-right : auto;
                width: 98%;
            }
            button{
                width : 100%;
                margin-bottom: 20px;
                margin-top:20px;
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
            .fname{
                float : left;
                width : 48%;
            }
            .lname{
                float : right;
                width : 48%;
            }

            .borderColor{
                border-color: red;                
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
                width: 450px;
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
        <div class="center">
            <img src="img/PAVElogo.png" alt="alt"/>
            <input type="email" id="email" placeholder="Email Address" class="form-control form-control box"/>
            <p style="color:red; padding-left: 10px; padding-top: 5px;" id="emailError"></p>
            <input type="password" id="pwd" placeholder="Password" class="form-control form-control box"/>
            <p style="color:red; padding-left: 10px; padding-top: 5px;" id="passwordError"></p>
            <input type="password" id="rpwd" placeholder="Confirm Password" class="form-control form-control box"/>
            <p style="color:red; padding-left: 10px; padding-top: 5px;" id="confirmError"></p>
            <div class="name">
                <input type="text" id="fname" placeholder="First Name" class="form-control form-control fname"/>
                <input type="text" id="lname" placeholder="Last Name" class="form-control form-control lname"/>                    
            </div>
            <button class="btn btn-primary" id="register" onclick="register()">Register</button>
            <button type="button" class="btn btn-danger" onclick="loginPage()">Login Page</button>
        </div>
        <div class="successDialog">
            <div class="dialogContent">
                <h3>&#10004; SUCCESS</h3>
                <p style="font-size : 20px;">A verification link has been sent to your email account. Please click on the link to verify your email.</p>
                <button type="button" class="btn btn-success" onclick="loginPage()">Continue</button>
            </div>
        </div>
        <div class="failedDialog">
            <div class="dialogContent">
                <h3 style="color:red;">&#x2716; FAILED</h3>
                <p style="font-size : 20px;" id="errorMessage">An unexpected error has occurred. Please try again later.</p>
                <button type="button" class="btn btn-success" onclick="closeDialog()">Continue</button>
            </div>
        </div>
        <div class="loading" >
            <div class="loader"></div>
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

    document.getElementById('fname').addEventListener('keyup', function () {
        clearTimeout(timeout);
        var req = /^[a-zA-Z\s]*$/;
        timeout = setTimeout(function () {
            if (document.getElementById("fname").value.match(req)) {
                document.getElementById("fname").classList.remove("borderColor");
            } else {
                document.getElementById("fname").classList.add("borderColor");
            }
        }, 0);
    });

    document.getElementById('lname').addEventListener('keyup', function () {
        clearTimeout(timeout);
        var req = /^[a-zA-Z\s]*$/;
        timeout = setTimeout(function () {
            if (document.getElementById("lname").value.match(req)) {
                document.getElementById("lname").classList.remove("borderColor");
            } else {
                document.getElementById("lname").classList.add("borderColor");
            }
        }, 0);
    });

    document.getElementById('email').addEventListener('keyup', function () {
        clearTimeout(timeout);
        var req = /^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/;
        timeout = setTimeout(function () {
            if (document.getElementById("email").value.match(req)) {
                document.getElementById("email").classList.remove("borderColor");
                document.getElementById("emailError").innerHTML = "";
            } else {
                document.getElementById("email").classList.add("borderColor");
                document.getElementById("emailError").innerHTML = "Invalid Email format.";
            }
        }, 300);
    });

    function register() {
        var nameReq = /^[a-zA-Z\s]*$/;
        var pswReq = /^(?=.*\d)(?=.*[a-zA-Z]).{7,15}$/;
        var emailreq = /^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/;
        var email = document.getElementById("email").value;
        var pwd = document.getElementById("pwd").value;
        var rpwd = document.getElementById("rpwd").value;
        var fname = document.getElementById("fname").value;
        var lname = document.getElementById("lname").value;
        var validation = false;

        if (email.match(emailreq) && email !== "") {
            if (pwd.match(pswReq) && pwd !== "") {
                if (pwd === rpwd) {
                    if (fname.match(nameReq) && fname !== "") {
                        if (lname.match(nameReq) && lname !== "") {
                            validation = true;
                        } else {
                            document.getElementById("lname").classList.add("borderColor");
                            validation = false;
                        }
                    } else {
                        document.getElementById("fname").classList.add("borderColor");
                        validation = false;
                    }
                } else {
                    document.getElementById("confirmError").innerHTML = "The Confirm Password confirmation does not match.";
                    document.getElementById("rpwd").classList.add("borderColor");
                    validation = false;
                }
            } else {
                document.getElementById("passwordError").innerHTML = "Password length must between 7-17 characters, must contain both numeric digits and letter.";
                document.getElementById("pwd").classList.add("borderColor");
                validation = false;
            }
        } else {
            document.getElementById("email").classList.add("borderColor");
            document.getElementById("emailError").innerHTML = "Invalid Email format.";
            validation = false;
        }


        if (validation) {
            document.querySelector(".loading").style.display = "flex";
            $.ajax({
                type: 'post',
                url: 'registerServlet',
                data: {email: email, pwd: pwd, fname: fname, lname: lname},
                success: function (result) {
                    document.querySelector(".loading").style.display = "none";
                    if (result === "0") {
                        document.querySelector(".successDialog").style.display = "flex";
                    } else if (result === "2") {
                        document.getElementById("errorMessage").innerHTML = "The email address already exists.";
                        document.querySelector(".failedDialog").style.display = "flex";
                    } else {
                        document.getElementById("errorMessage").innerHTML = "An unexpected error has occurred. Please try again later.";
                        document.querySelector(".failedDialog").style.display = "flex";
                    }
                }
            });
        }
    }

    function loginPage() {
        window.location.href = 'login.jsp';
    }

    function closeDialog() {
        document.querySelector(".failedDialog").style.display = "none";
    }


</script>