<%-- 
    Document   : shareLink
    Created on : Jan 9, 2021, 10:19:08 PM
    Author     : yee_j
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
        <title>File access verification</title>
        <style>
            .center{
                position: absolute;
                top: 50%;
                left: 50%;
                -moz-transform: translateX(-50%) translateY(-50%);
                -webkit-transform: translateX(-50%) translateY(-50%);
                transform: translateX(-50%) translateY(-50%);
                width : 45%;
                height: auto;
                padding: 10px;
            } 
            .center p{
                font-size: 16px;
                font-family: "Verdana", "Sans-serif";
                line-height: 1.5;
                text-align: justify;
                text-justify: inter-word;
            }
            img{
                display: block;
                margin-left: auto;
                margin-right: auto;
                margin-bottom: 15px;
            }

            .center a{
                display: inline;
            }

            #email{
                width :80%;
                display: inline;
            }

            button{
                display: inline;
                margin-left: 10px;
                width: 100px;
            }

            #response{
                margin-top: 10px;
                font-size: 14px;
            }

        </style>
    </head>
    <body>
        <div class="center">
            <img src="img/PAVElogo.png" alt="alt"/>
            <hr>
            <p>Before you are allowed to access the files, you are required to get the file access permission from the owner. Please fill in your pave account E-mail address in the text field below for owner's verification purpose.<p>
            <p>Do not have a PAVE account? <a href="register.jsp">Click here</a></p>
            <hr>
            <div style="text-align : center; margin-bottom: 15px;">
                <input type="text" id="email" placeholder="Email Address" class="form-control form-control"/>
                <button type="button" class="btn btn-primary" onclick="submit()">Submit</button>
            </div>
            <p style="color: red;" id="errorMsg"></p>

        </div>
    </body>

</html>

<script>
    function submit() {
        var key1 = '<%= request.getParameter("key1")%>';
        var key2 = '<%= request.getParameter("key2")%>';
        var email = document.getElementById("email").value;
        var mailformat = /^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/;
        if (email.match(mailformat)) {
            document.location.href = 'shareLinkServlet?key1=' + key1 + "&key2=" + key2 + "&email=" + email;
        }else{
            document.getElementById("errorMsg").innerHTML = "Invalid Email Format!";
        }
    }
</script>