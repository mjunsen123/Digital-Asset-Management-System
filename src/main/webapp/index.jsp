<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/animejs/2.0.2/anime.min.js"></script>
        <title>PAVE</title>

        <style>
            @import url(https://fonts.googleapis.com/css?family=Khula:700);

            body, html {
                height: 100%;
                margin: 0;
            }

            .bg {
                /* The image used */
                background-image: url("img/indexbg.jpg");

                /* Full height */
                height: 100%; 

                /* Center and scale the image nicely */
                background-position: center;
                background-repeat: no-repeat;
                background-size: cover;
            }

            .center{
                position: absolute;
                top: 50%;
                left: 50%;
                -moz-transform: translateX(-50%) translateY(-50%);
                -webkit-transform: translateX(-50%) translateY(-50%);
                transform: translateX(-50%) translateY(-50%);
                box-shadow: 6px 6px 29px -4px rgba(0,0,0,0.10);
                background: rgba(0, 0, 0, 0.7);
                width : 60%;
                height: 40%;
            }

            .hidden {
                opacity:0;
            }
            .console-container {
                font-family:Khula;
                font-size:4em;
                text-align:center;
                height:150px;
                width:100%;
                display:block;
                color:white;
                margin-top: 50px;
            }
            .console-underscore {
                display:inline-block;
                position:relative;
                top:-0.14em;
                left:10px;
            }

            .action{
                display:block;
                text-align: center;
            }

            .registerBtn{
                width: 40%;
                font-size: 20px;
            }


        </style>
    </head>
    <body>
        <div class="bg">
            <div class="center">
                <div class='console-container'>
                    <span id='text'></span><div class='console-underscore' id='console'>&#95;</div>
                </div>
                <div class="action">
                    <a href="login.jsp" class="btn btn-default registerBtn">Get Started!</a>
                </div>
            </div>
        </div>
    </body>
</html>

<script>
    // function([string1, string2],target id,[color1,color2])    
    consoleText(['Welcome to PAVE', "Manage your digital assets", 'anywhere, anytime.'], 'text', ['white', 'white', 'white']);

    function consoleText(words, id, colors) {
        if (colors === undefined)
            colors = ['#fff'];
        var visible = true;
        var con = document.getElementById('console');
        var letterCount = 1;
        var x = 1;
        var waiting = false;
        var target = document.getElementById(id);
        target.setAttribute('style', 'color:' + colors[0]);
        window.setInterval(function () {

            if (letterCount === 0 && waiting === false) {
                waiting = true;
                target.innerHTML = words[0].substring(0, letterCount);
                window.setTimeout(function () {
                    var usedColor = colors.shift();
                    colors.push(usedColor);
                    var usedWord = words.shift();
                    words.push(usedWord);
                    x = 1;
                    target.setAttribute('style', 'color:' + colors[0]);
                    letterCount += x;
                    waiting = false;
                }, 800);
            } else if (letterCount === words[0].length + 1 && waiting === false) {
                waiting = true;
                window.setTimeout(function () {
                    x = -1;
                    letterCount += x;
                    waiting = false;
                }, 1000);
            } else if (waiting === false) {
                target.innerHTML = words[0].substring(0, letterCount);
                letterCount += x;
            }
        }, 120);
        window.setInterval(function () {
            if (visible === true) {
                con.className = 'console-underscore hidden';
                visible = false;

            } else {
                con.className = 'console-underscore';

                visible = true;
            }
        }, 450);
    }
</script>
