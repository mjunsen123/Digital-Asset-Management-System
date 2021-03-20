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
        <title>Share With Me</title>
        <link href="css/sideBar.css" rel="stylesheet" type="text/css">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
        <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>

        <style>
            .shareStatusBtn{
                display: inline-block;
                float : right;
                margin :10px;
            }

            .shareStatusForm{
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
                text-align: left;
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

            .sharePendingTable{
                text-align: left;
                height:30%;
                overflow-y: auto;
                -ms-overflow-style: none;
            }     

            .shareStatusTable{
                text-align: left;
                height:50%;
                overflow-y: auto;
                -ms-overflow-style: none;
            }    

            .shareActionBtn{
                margin-right : 5px;
                cursor: pointer;               
            }

            .shareStatusTable::-webkit-scrollbar {
                display: none;
            }

            .sharePendingTable::-webkit-scrollbar {
                display: none;
            }

            .badge {
                position: absolute;
                right: 10px;
                top: 3px;
                padding: 5px 10px;
                border-radius: 50%;
                background-color: #F35C4A;
                color: white;
            }

        </style>

    </head>
    <body>
        <div class="sidenav" id="mySidenav" style="width:0px;">
            <a href="dashboardServlet">My Files</a>
            <a href="shareFilesServlet">Share With Me</a>
            <a href="setting.jsp">Setting</a>
            <hr style="border-bottom : #000000 1px solid; width: 80%;">            
            <a href="logoutServlet">Logout</a>
        </div>
        <div class="main" id="main"  style="margin-left: 0px;">
            <div class="barIcon" style="margin-top: 5px;">
                <span style="font-size:25px;cursor:pointer;margin-right: 10px;" onclick="navBar()">&#9776;</span>
                <span style="font-size:25px;cursor:pointer;margin-right: 10px;" class="fa disable-select" onclick="back()">&#xf100;</span>
                <span class="currentPath" id="cpath">Share With Me${displayPath}</span>
                <button type="button" class="shareStatusBtn btn btn-default" onclick="showShare()">My Sharing</button>
                <c:if test="${pendingList.size()>0}">
                    <span class="badge" id="pendingNumber">${pendingList.size()}</span>
                </c:if>

            </div>
            <div class="path" style="top:50px; margin: 0px 10px;">
                <table class="table table-hover table-sm tb" id="tableList" >
                    <thead>
                        <tr class="allRow">
                            <th style="width:5%;"></th>
                            <th style="width:55%;">File Name</th>
                            <th style="text-align:right; width:10%;">Owner</th>
                            <th style="text-align:right;width:20%;" >Last Modified Date</th>
                            <th style="width:5%;"></th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach begin="0" items="${shareList}" var="s" varStatus="status">
                            <tr style="height:40px;">
                                <td style="width:5%;">
                                    <!--Check file type-->
                                    <c:if test="${s.type == 'folder'}">
                                        <i class="material-icons">&#xe2c8;</i>
                                    </c:if>
                                </td>
                                <td style="text-align:left;cursor: pointer; text-overflow: ellipsis; max-width:140px; white-space: nowrap; overflow: hidden;" onclick="row('${status.index}', '${s.type}');getTag('${status.index}', '${s.type}')">${s.name}</td>                                
                                <td style="text-align:right;cursor: pointer; width:10%;" onclick="row('${status.index}', '${s.type}');getTag('${status.index}', '${s.type}')">${s.owner}</td>
                                <td style="text-align:right;cursor: pointer; width:20%;" onclick="row('${status.index}', '${s.type}');getTag('${status.index}', '${s.type}')">${s.mdate}</td>
                                <c:choose>
                                    <c:when test="${s.type == 'folder'}">
                                        <td style="text-align: center;"><i class="material-icons disable-select" style="cursor: pointer;" onclick="downloadFolder('${status.index}')">&#xe2c4;</i></td>
                                    </c:when>    
                                    <c:otherwise>
                                        <td style="text-align: center;"><i class="material-icons disable-select" style="cursor: pointer;" onclick="download('${status.index}')">&#xe2c4;</i></td>
                                    </c:otherwise>
                                </c:choose>
                            </tr>
                        </c:forEach>    
                    </tbody>
                </table>
            </div>

            <!--Preview container-->
            <div class="preview" id="preview" style="margin-left: 10px; top: 50px;">
                <img id="previewImage" src="" class="previewImage" style="max-height:75%;"/>
                <iframe id="previewDocument" src="" style="height:75%;" class="previewDocument" frameBorder="0"/></iframe>     
                <div class="previewNameDiv"><i id="previewFileName"></i></div>
                <hr class="previewLine">
                <div class="tagContainer" id="metadataTag">
                    <c:forEach begin="0" items="${metadataArray}" var="m" varStatus="preview">
                        <div class="tag-item" >${m}</div>
                    </c:forEach>
                </div>
            </div>

            <!--Sharing container--> 
            <div class="shareStatusForm">
                <div class="formContent" style="width:60%; height:80%;" id="shareDivContent">
                    <h3>Pending Approval
                        <c:if test="${pendingList.size()>0}">
                            (${pendingList.size()})
                        </c:if>
                    </h3>
                    <hr style="margin:10px 0;">
                    <div class="sharePendingTable">
                        <table class="table table-hover table-sm tb" id="pendingTable">
                            <thead style="height:40px; font-weight: bold;">
                                <tr>
                                    <td style="width:75%;">File Path</td>
                                    <td style="width:15%;">Request By</td>
                                    <td style="width:10%; text-align: center;">Action</td>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach begin="0" items="${pendingList}" var="p" varStatus="pending">
                                    <tr>
                                        <td>${p.path}</td>
                                        <td>${p.recipient}</td>
                                        <td style="text-align: center;"><i class="material-icons disable-select shareActionBtn" style="color:green;" onclick="approve('${pending.index}')">&#xe8e8;</i><i class="material-icons disable-select shareActionBtn" style="color:red;" onclick="reject('${pending.index}')">&#xe5c9;</i></td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                    <h3 style="margin-top:45px;">Current Approved Sharing
                        <c:if test="${approvedList.size()>0}">
                            (${approvedList.size()})
                        </c:if>
                    </h3>
                    <hr style="margin:10px 0;">
                    <div class="shareStatusTable">
                        <table class="table table-hover table-sm tb" id="approveTable">
                            <thead style="height:40px; font-weight: bold;">
                                <tr>
                                    <td style="width:75%;">File Path</td>
                                    <td style="width:15%;">Request By</td>
                                    <td style="width:10%; text-align: center;">Action</td>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach begin="0" items="${approvedList}" var="a" varStatus="approved">
                                    <tr>
                                        <td>${a.path}</td>
                                        <td>${a.recipient}</td>
                                        <td style="text-align: center;"><i class="material-icons disable-select shareActionBtn" style="color:red;" onclick="remove('${approved.index}')">&#xe15c;</i></td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                        <span class="fa disable-select" id="shareCloseBtn" onclick="closeShareContainer()">&#xf00d;</span>
                    </div>

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

    function showAction(fname) {
        document.getElementById(fname + "action").classList.toggle("show");
        document.getElementById(fname + "btn").classList.toggle("show");
    }

    window.addEventListener("click", function (event) {

        if (!event.target.matches('.actionbtn')) {
            var dropdowns = document.getElementsByClassName("rowAction");
            var btn = document.getElementsByClassName("actionbtn");
            var i;
            for (i = 0; i < dropdowns.length; i++) {
                var openDropdown = dropdowns[i];
                if (openDropdown.classList.contains('show')) {
                    openDropdown.classList.remove('show');
                }
            }
            for (i = 0; i < btn.length; i++) {
                var openDropdown = btn[i];
                if (openDropdown.classList.contains('show')) {
                    openDropdown.classList.remove('show');
                }
            }
        }
    });

    function row(i, type) {

        switch (type) {
            case "folder":
                $.ajax({
                    type: 'POST',
                    data: {selectedRow: i},
                    url: 'shareFilesServlet',
                    success: function () {
                        $('#tableList').load(window.location + " #tableList");
                        $('#cpath').load(window.location + " #cpath");
                        document.querySelector(".deleteAction").style.display = "none";
                    }
                });
                break;
            case "png":
            case "jpg":
            case "jpeg":
                document.getElementById("previewDocument").style.display = "none";
                document.getElementById("previewDocument").src = "";
                document.getElementById("previewImage").style.display = "flex";
                document.getElementById("previewImage").src = "fileServlet?row=" + i + "&type=" + type + "&source=share";
                break;
            case "pdf":
            case "docx":
            case "doc":
            case "ppt":
            case "pptx":
                document.getElementById("previewImage").style.display = "none";
                document.getElementById("previewImage").src = "";
                document.getElementById("previewDocument").style.display = "flex";
                document.getElementById("previewDocument").src = "fileServlet?row=" + i + "&type=" + type + "&source=share#toolbar=0&navpanes=0&scrollbar=0";
                break;
            case "txt":
            case "java":
            case "xlsx":
            case "xls":
            case "csv":
                document.getElementById("previewImage").style.display = "none";
                document.getElementById("previewImage").src = "";
                document.getElementById("previewDocument").style.display = "flex";
                document.getElementById("previewDocument").src = "fileServlet?row=" + i + "&type=" + type + "&source=share";
                break;
            default:
                document.getElementById("previewImage").style.display = "none";
                document.getElementById("previewImage").src = "";
                document.getElementById("previewDocument").style.display = "flex";
                document.getElementById("previewDocument").src = "previewNotAvailable.html";
        }
    }

    function getTag(i, type) {
        var action = "get";
        var source = "share";
        if (type !== "folder") {
            $.ajax({
                type: 'GET',
                data: {row: i, action: action, source: source},
                url: 'getMetadataServlet',
                success: function (previewName) {
                    $("#metadataTag").load(" #metadataTag > *");
                    document.getElementById("previewFileName").innerHTML = previewName;
                    setTimeout(function () {
                        document.querySelector(".tagContainer").style.display = "grid";
                    }, 100);
                }
            });
        }
    }

    function back() {
        $.ajax({
            type: 'POST',
            data: {action: "back"},
            url: 'shareFilesServlet',
            success: function () {
                $('#tableList').load(window.location + " #tableList");
                $('#cpath').load(window.location + " #cpath");
            }
        });
    }

    function showShare() {
        document.querySelector(".shareStatusForm").style.display = "flex";
    }

    function closeShareContainer() {
        document.querySelector(".shareStatusForm").style.display = "none";
        location.reload();
    }

    function approve(row) {
        var action = "approve";
        $.ajax({
            type: 'POST',
            data: {row: row, action: action},
            url: 'shareAction',
            success: function () {
                $("#shareDivContent").load(" #shareDivContent > *");
            }
        });
    }

    function reject(row) {
        var action = "reject";
        $.ajax({
            type: 'POST',
            data: {row: row, action: action},
            url: 'shareAction',
            success: function () {
                $("#shareDivContent").load(" #shareDivContent > *");
            }
        });
    }

    function remove(row) {
        var action = "remove";
        $.ajax({
            type: 'POST',
            data: {row: row, action: action},
            url: 'shareAction',
            success: function () {
                $("#shareDivContent").load(" #shareDivContent > *");
            }
        });
    }

    function download(row) {
        window.open("downloadServlet?source=share&row=" + row);
    }

    function downloadFolder(row) {
        window.open("downloadServlet?source=share&type=folder&row=" + row);
    }
</script>