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
        <title>PAVE</title>
        <link href="css/sideBar.css" rel="stylesheet" type="text/css">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
        <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
    </head>
    <body>
        <div class="sidenav" id="mySidenav" style="width:0px;">
            <a href="dashboardServlet">My Files</a>
            <a href="shareFilesServlet">Share With Me</a>
            <a href="setting.jsp">Setting</a>
            <hr style="border-bottom : #000000 1px solid; width: 80%;">
            <a href="logoutServlet">Logout</a>
        </div>
        <div class="main" id="main" style="margin-left: 0px;">
            <div class="barIcon" style="margin-top: 5px;">
                <span style="font-size:25px;cursor:pointer;margin-right: 10px;" onclick="navBar()">&#9776;</span>
                <span style="font-size:25px;cursor:pointer;margin-right: 10px;" class="fa disable-select" onclick="back()">&#xf100;</span>
                <span class="currentPath" id="cpath">${cpath}</span>
            </div>

            <!--Action Bar-->
            <div class="action">
                <div class="createAction"><button class="btn" id="formButton"><i class="fa fa-folder"></i> New Folder</button></div>
                <div class="uploadAction">
                    <button class="btn dropdownbtn" style="background-color: transparent; color: black; border: 1px solid #CACFD2;" onclick="showUploadOption()"><i class="fa fa-upload"></i> Upload</button>
                    <div class="uploadOption" id="uploadDropdown">
                        <form id="fileForm" enctype="multipart/form-data" method="POST">
                            <input type="file" id="chooseFile" multiple/>
                            <label for="chooseFile">Upload File</label>
                        </form>
                        <form id="folderForm" enctype="multipart/form-data" method="POST">
                            <input type="file"  id="chooseFolder" webkitdirectory multiple/>
                            <label for="chooseFolder">Upload Folder</label>
                        </form>
                    </div>
                </div>
                <div class="deleteAction" id="deleteActionId"><button class="btn " style="background-color: transparent; color: black; border: 1px solid #CACFD2;" onclick="deleteFile()"><i class="fa fa-trash"></i> Delete</button></div>
            </div>         

            <!--Search File-->
            <div class="search">
                <input type="text" placeholder="Search file or folder name..." onkeyup="searchFiles()" id="searchInput">
                <i class="fa fa-search" style="cursor:pointer;" onclick="searchFiles()" id="searchBtn"></i>
                <div class="searchTable">
                    <table id="searchTable">
                        <tbody>
                            <c:forEach begin="0" items="${searchList}" var="s" varStatus="search">
                                <tr onclick="searchPreview('${search.index}', '${s.type}');getSearchTag('${search.index}', '${s.type}')"><td>${s.name}<br/><i>Type : ${s.type}</i><br/><i>Tag : ${s.metadata}</i></td></tr>
                                    </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>

            <!--Get File List-->
            <div class="path">
                <table class="table table-hover table-sm tb" id="tableList">
                    <thead>
                        <tr class="allRow">
                            <th style="width:5%;"><input type="checkbox" class="masterchkbox" id='masterCheck' onclick="checkAll(this);showDelete()"></th>
                            <th style="width:60%;">File Name</th>
                            <th style="width:5%;"></th>
                            <th style="text-align:right; width:10%;">File Size</th>
                            <th style="text-align:right;width:20%;" >Last Modified Date</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach begin="0" items="${fileList}" var="f" varStatus="status">
                            <tr style="height:40px;">
                                <td style="width:5%;"><input type="checkbox" class="chkbox" value="${status.index}" onclick="showDelete()"></td>
                                <td style="text-align:left;cursor: pointer; text-overflow: ellipsis; max-width:140px; white-space: nowrap; overflow: hidden;" onclick="row('${status.index}', '${f.type}');getTag('${status.index}', '${f.type}')">${f.name}</td>
                                <td style="width:5%;">
                                    <div class="actionDiv" id="${f.name}actionDiv">
                                        <span class="actionbtn disable-select fa" id="${f.name}btn" onclick="showAction('${f.name}')">&#xf142;</span>
                                        <div class="rowAction" id="${f.name}action">
                                            <c:choose>
                                                <c:when test="${f.type == 'folder'}">
                                                    <button onclick="deleteButton(${status.index})">Delete</button>
                                                    <button onclick="showRenameForm('${status.index}', '${f.name}')">Rename</button>
                                                    <button onclick="downloadFolder('${status.index}')">Download</button>
                                                    <button onclick="showShareForm('${status.index}', '${f.name}')">Share</button>                                                </c:when>    
                                                <c:otherwise>
                                                    <button onclick="deleteButton(${status.index})">Delete</button>
                                                    <button onclick="showRenameForm('${status.index}', '${f.name}')">Rename</button>
                                                    <button onclick="downloadRow('${status.index}')">Download</button>
                                                    <button onclick="showShareForm('${status.index}', '${f.name}')">Share</button>
                                                </c:otherwise>
                                            </c:choose>

                                        </div>
                                    </div>
                                </td>
                                <td style="text-align:right;cursor: pointer; width:10%;" onclick="row('${status.index}', '${f.type}');getTag('${status.index}', '${f.type}')">${f.size}
                                    <c:if test="${f.type == 'folder'}">
                                        <i class="material-icons">&#xe2c8;</i>
                                    </c:if>
                                </td>
                                <td style="text-align:right;cursor: pointer; width:20%;" onclick="row('${status.index}', '${f.type}');getTag('${status.index}', '${f.type}')">${f.mdate}</td>
                            </tr>
                        </c:forEach>    
                    </tbody>
                </table>                  
            </div>  

            <!--Previewer-->
            <div class="preview" id="preview" style="margin-left: 10px;">
                <img id="previewImage" src="" class="previewImage"/>
                <iframe id="previewDocument" src="" class="previewDocument" frameBorder="0"/></iframe>     
                <div class="previewNameDiv"><i id="previewFileName"></i></div>
                <div class="previewActionBtn" style="visibility:hidden;" id="previewActionBtn">
                    <button type="button" class="btn btn-danger" onclick="deletePreview()">Delete</button>
                    <button type="button" class="btn btn-secondary" onclick="renamePreview()">Rename</button>
                    <button type="button" class="btn btn-primary" onclick="downloadPreview()">Download</button>
                    <button type="button" class="btn btn-success" onclick="sharePreview()">Share</button>
                </div> 
                <hr class="previewLine">
                <div class="tagContainer" id="metadataTag">
                    <c:forEach begin="0" items="${metadataArray}" var="m" varStatus="preview">
                        <div class="tag-item" >${m}<span class="fa disable-select" onclick="deleteMetadata('${preview.index}')">&#xf00d;</span></div>
                    </c:forEach>

                    <div class="tag-item" id="addItem" style="display:none;"><input type="text" maxlength="10" placeholder="New tag" id="addTag" onblur="addMetadata()"/></div>
                </div>
            </div>

            <!--Create Folder Container-->    
            <div class="createForm">
                <div class="formContent">
                    <h3>Create a folder</h3>
                    <input type="text" placeholder="Enter new folder name" onfocus="this.placeholder = ''" onblur="this.placeholder = 'Enter new folder name'" id="folderName"/>
                    <p id="errorMsg" style="color: red; margin-top: 5px;"></p>
                    <button type="button" class="btn btn-light" onclick="createFolder()">Create</button>
                    <span class="fa disable-select" id="closeBtn">&#xf00d;</span>
                </div>
            </div>

            <!--Rename Folder Container-->
            <div class="renameForm">
                <div class="formContent">
                    <h3>Rename</h3>
                    <input type="hidden" id="rowNumber"/>
                    <input type="text" id="renameFolderName"/>
                    <p id="RenameErrorMsg" style="color: red; margin-top: 5px;"></p>
                    <button type="button" class="btn btn-light" onclick="renameFolder()">OK</button>
                    <span class="fa disable-select" id="renameCloseBtn">&#xf00d;</span>
                </div>
            </div>
            <div class="renamePreviewForm">
                <div class="formContent">
                    <h3>Rename</h3>
                    <input type="text" id="renameFolderNamePreview"/>
                    <input type="hidden" id="previewName" value=""/>
                    <p id="RenameErrorMsgPreview" style="color: red; margin-top: 5px;"></p>
                    <button type="button" class="btn btn-light" onclick="submitRenamePreview()">OK</button>
                    <span class="fa disable-select" id="renameCloseBtnPreview">&#xf00d;</span>
                </div>
            </div>

            <!--Sharing Container-->
            <div class="shareForm">
                <div class="formContent" style="min-width:500px;">
                    <input type="hidden" id="shareRow" value=""/>
                    <h3 id="shareName"></h3><hr>
                    <input type="radio" id="shareEmail" name="methods" value="email" checked="checked">
                    <label class="disable-select" for="shareEmail">Share by Email</label>
                    <input type="radio" id="shareLink" name="methods" value="link">
                    <label class="disable-select" for="shareLink">Share by Link</label>

                    <div class="linkDiv">
                        <input type="text" id="generatedLink" readonly value="">
                        <button onclick="copy()"><i class="fa fa-copy"></i></button>
                        <p id="shareLinkResponseMsg" style="color:red;"></p>
                    </div>

                    <div class="emailDiv">
                        <input type="text" id="recipientEmail" placeholder="Recipient Email Address">
                        <p id="shareResponseMsg"></p>
                        <button type="button" class="btn btn-light" onclick="share()">Share</button>
                    </div>

                    <span class="fa disable-select" id="shareCloseBtn">&#xf00d;</span>
                </div>
            </div>
            <div class="loading" >
                <div class="loader"></div>
            </div>
    </body>
</html>

<script>

    document.getElementById("recipientEmail").addEventListener('keydown', function (e) {
        if (e.keyCode === 9) {
            var input = document.getElementById("recipientEmail");
            var i = input.value.lastIndexOf(";");
            var l = input.value.length;
            if (l > i + 2 && input.value.sub(i, l) !== " ") {
                e.preventDefault();
                input.value = input.value + "; ";
            }
        }
    });

    document.getElementById("shareLink").addEventListener("click", function () {
        var row = document.getElementById("shareRow").value;
        if (row !== "") {
            $.ajax({
                type: 'POST',
                data: {row: row},
                url: 'shareServlet',
                success: function (link) {
                    if (link.length > 0) {
                        document.querySelector(".emailDiv").style.display = "none";
                        document.querySelector(".linkDiv").style.display = "block";
                        document.getElementById("generatedLink").value = link;
                    } else {
                        document.getElementById("shareLinkResponseMsg").innerHTML = "Failed to generate link";
                    }
                }
            });
        } else {
            $.post("shareServlet", function (link) {
                if (link.length > 0) {
                    document.querySelector(".emailDiv").style.display = "none";
                    document.querySelector(".linkDiv").style.display = "block";
                    document.getElementById("generatedLink").value = link;
                } else {
                    document.getElementById("shareLinkResponseMsg").innerHTML = "Failed to generate link";
                }
            });
        }

    });

    document.getElementById("shareEmail").addEventListener("click", function () {
        document.querySelector(".linkDiv").style.display = "none";
        document.querySelector(".emailDiv").style.display = "block";
        document.getElementById("generatedLink").value = "";
        document.getElementById("shareLinkResponseMsg").innerHTML = "";
    });

    document.getElementById("shareCloseBtn").addEventListener("click", function () {
        document.querySelector(".linkDiv").style.display = "none";
        document.querySelector(".emailDiv").style.display = "block";
        document.getElementById("shareEmail").checked = true;
        document.getElementById("shareLink").checked = false;
        document.getElementById("shareLinkResponseMsg").innerHTML = "";
        document.getElementById('shareResponseMsg').innerHTML = '';
        document.getElementById('recipientEmail').value = '';
        document.getElementById('shareRow').value = '';
        document.getElementById('generatedLink').value = '';
        document.querySelector(".shareForm").style.display = "none";
    });

    document.getElementById("formButton").addEventListener("click", function () {
        document.querySelector(".createForm").style.display = "flex";
    });

    document.getElementById("closeBtn").addEventListener("click", function () {
        document.getElementById('folderName').value = '';
        document.querySelector(".createForm").style.display = "none";
        document.getElementById("errorMsg").innerHTML = "";
    });

    document.getElementById("renameCloseBtn").addEventListener("click", function () {
        document.querySelector(".renameForm").style.display = "none";
        document.getElementById("renameFolderName").value = '';
        document.getElementById("rowNumber").value = '';
        document.getElementById("RenameErrorMsgPreview").innerHTML = "";
    });

    document.getElementById("renameCloseBtnPreview").addEventListener("click", function () {
        document.querySelector(".renamePreviewForm").style.display = "none";
        document.getElementById("renameFolderNamePreview").value = '';
        document.getElementById("rowNumber").value = '';
        document.getElementById("RenameErrorMsg").innerHTML = "";
    });

    document.getElementById("chooseFile").onchange = function () {
        document.querySelector(".loading").style.display = "flex";
        var path = $('#cpath').text();
        var data = new FormData();
        data.append("currentPath", path);
        var totalfiles = document.getElementById('chooseFile').files.length;
        for (var i = 0; i < totalfiles; i++) {
            data.append("files[]", document.getElementById('chooseFile').files[i]);
        }
        $.ajax({
            type: 'POST',
            data: data,
            processData: false,
            contentType: false,
            cache: false,
            url: 'uploadServlet',
            enctype: 'multipart/form-data',
            success: function (result) {
                if (result === "1") {
                    location.reload();
                } else if (result === "0") {
                    alert("Upload failed");
                }
                document.querySelector(".loading").style.display = "none";
            }
        });
    };

    document.getElementById("chooseFolder").onchange = function () {
        document.querySelector(".loading").style.display = "flex";
        var path = $('#cpath').text();
        var data = new FormData();
        data.append("currentPath", path);
        var totalfiles = document.getElementById('chooseFolder').files.length;
        for (var i = 0; i < totalfiles; i++) {
            data.append("files[]", document.getElementById('chooseFolder').files[i]);
        }
        $.ajax({
            type: 'POST',
            data: data,
            processData: false,
            contentType: false,
            cache: false,
            url: 'uploadServlet',
            enctype: 'multipart/form-data',
            success: function (result) {
                if (result === "1") {
                    location.reload();
                } else if (result === "0") {
                    alert("Upload failed");
                }
                document.querySelector(".loading").style.display = "none";
            }
        });
    };


    window.addEventListener("click", function (event) {
        if (!event.target.matches('.dropdownbtn')) {
            var dropdowns = document.getElementsByClassName("uploadOption");
            var i;
            for (i = 0; i < dropdowns.length; i++) {
                var openDropdown = dropdowns[i];
                if (openDropdown.classList.contains('show')) {
                    openDropdown.classList.remove('show');
                }
            }
        }

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

        if (!event.target.matches('#searchInput') && !event.target.matches('#searchBtn')) {
    <c:remove var="searchList" scope="session" />
            $('#searchTable').load(window.location + " #searchTable");
        }

    });

    function renameFolder() {
        var name = document.getElementById("renameFolderName").value;
        var row = document.getElementById("rowNumber").value;
        var path = $('#cpath').text();

        $.ajax({
            type: 'POST',
            data: {name: name, row: row, currentPath: path},
            url: 'renameServlet',
            success: function (result) {
                if (result === "1") {
                    $('#tableList').load(window.location + " #tableList");
                    $('#cpath').load(window.location + " #cpath");
                    document.querySelector(".renameForm").style.display = "none";
                    document.getElementById("renameFolderName").value = '';
                    document.getElementById("rowNumber").value = '';
                    document.getElementById("RenameErrorMsg").innerHTML = "";
                } else if (result === "0") {
                    document.getElementById("RenameErrorMsg").innerHTML = "Invalid file name";
                } else {
                    document.getElementById("RenameErrorMsg").innerHTML = "File name exists";
                }

            }
        });
    }

    function showRenameForm(row, name) {
        document.querySelector(".renameForm").style.display = "flex";
        var n = name.indexOf(".");
        var input = document.getElementById("renameFolderName");
        document.getElementById("rowNumber").value = row;
        input.value = name;
        input.focus();
        input.setSelectionRange(0, n);
    }

    function showShareForm(row, name) {
        document.querySelector(".shareForm").style.display = "flex";
        document.getElementById("shareName").innerHTML = "Sharing " + name;
        document.getElementById("shareRow").value = row;
    }

    function checkAll(x) {
        var checkboxes = document.querySelectorAll('input[type="checkbox"]');
        for (var checkbox of checkboxes) {
            checkbox.checked = x.checked;
        }
    }

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

    function row(i, type) {

        switch (type) {
            case "folder":
                $.ajax({
                    type: 'POST',
                    data: {selectedRow: i},
                    url: 'folderServlet',
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
                document.getElementById("previewImage").src = "fileServlet?row=" + i + "&type=" + type;
                document.getElementById("previewActionBtn").style.visibility = "visible";
                break;
            case "pdf":
            case "docx":
            case "doc":
            case "ppt":
            case "pptx":
                document.getElementById("previewImage").style.display = "none";
                document.getElementById("previewImage").src = "";
                document.getElementById("previewDocument").style.display = "flex";
                document.getElementById("previewDocument").src = "fileServlet?row=" + i + "&type=" + type + "#toolbar=0&navpanes=0&scrollbar=0";
                document.getElementById("previewActionBtn").style.visibility = "visible";
                break;
            case "txt":
            case "java":
            case "xlsx":
            case "xls":
            case "csv":
                document.getElementById("previewImage").style.display = "none";
                document.getElementById("previewImage").src = "";
                document.getElementById("previewDocument").style.display = "flex";
                document.getElementById("previewDocument").src = "fileServlet?row=" + i + "&type=" + type;
                document.getElementById("previewActionBtn").style.visibility = "visible";
                break;
            default:
                document.getElementById("previewImage").style.display = "none";
                document.getElementById("previewImage").src = "";
                document.getElementById("previewDocument").style.display = "flex";
                document.getElementById("previewDocument").src = "previewNotAvailable.html";
                document.getElementById("previewActionBtn").style.visibility = "visible";
        }
    }

    function getTag(i, type) {
        var action = "get";
        if (type !== "folder") {
            $.ajax({
                type: 'GET',
                data: {row: i, action: action},
                url: 'getMetadataServlet',
                success: function (previewName) {
                    $("#metadataTag").load(" #metadataTag > *");
                    document.getElementById("previewName").value = previewName;
                    document.getElementById("previewFileName").innerHTML = previewName;
                    setTimeout(function () {
                        document.getElementById("addItem").style.display = "flex";
                        document.querySelector(".tagContainer").style.display = "grid";
                    }, 100);
                }
            });
        }
    }

    function deleteMetadata(i) {
        var action = "delete";
        $.ajax({
            type: 'GET',
            data: {index: i, action: action},
            url: 'getMetadataServlet',
            success: function () {
                $("#metadataTag").load(" #metadataTag > *");
                setTimeout(function () {
                    document.querySelector(".tagContainer").style.display = "grid";
                    document.getElementById("addItem").style.display = "flex";
                }, 100);
            }
        });
    }

    function addMetadata() {
        var tag = document.getElementById("addTag").value;
        var regex = /^[a-zA-Z0-9 ]*$/;
        var validation = false;
        var action = "add";
        if (tag !== "") {
            if (tag.match(regex)) {
                validation = true;
            }
        }
        if (validation) {
            $.ajax({
                type: 'GET',
                data: {tag: tag, action: action},
                url: 'getMetadataServlet',
                success: function () {
                    $("#metadataTag").load(" #metadataTag > *");
                    setTimeout(function () {
                        document.querySelector(".tagContainer").style.display = "grid";
                        document.getElementById("addItem").style.display = "flex";
                    }, 100);
                }
            });
        }
    }

    function back() {
        var path = $('#cpath').text();
        $.ajax({
            type: 'POST',
            data: {backPath: path},
            url: 'folderServlet',
            success: function () {
                $('#tableList').load(window.location + " #tableList");
                $('#cpath').load(window.location + " #cpath");
                document.querySelector(".deleteAction").style.display = "none";
            }
        });
    }

    function createFolder() {
        var path = $('#cpath').text();
        var name = document.getElementById("folderName").value;
        var validation = /^[^\\/?%*:|"<>\.]+$/;
        if (name.match(validation)) {
            $.ajax({
                type: 'POST',
                data: {currentPath: path, folderName: name},
                url: 'createFolderServlet',
                success: function (result) {
                    if (result === "0") {
                        document.querySelector(".createForm").style.display = "none";
                        document.getElementById('folderName').value = '';
                        document.getElementById("errorMsg").innerHTML = "";
                        $('#tableList').load(window.location + " #tableList");
                        $('#cpath').load(window.location + " #cpath");
                    } else if (result === "1") {
                        document.getElementById('folderName').value = '';
                        document.getElementById("errorMsg").innerHTML = "Invalid folder name";

                    } else if (result === "2") {
                        document.getElementById('folderName').value = '';
                        document.getElementById("errorMsg").innerHTML = "Folder name exist";
                    } else {
                        document.getElementById('folderName').value = '';
                        document.getElementById("errorMsg").innerHTML = "Unknown error occur, please try again later";
                    }
                }
            });
        } else {
            document.getElementById('folderName').value = '';
            document.getElementById("errorMsg").innerHTML = "Invalid folder name";
        }
    }

    function deleteFile() {
        document.querySelector(".loading").style.display = "flex";
        var currentPath = $('#cpath').text();
        var checkedValue = document.getElementsByClassName("chkbox");
        var chk = [];
        for (var i = 0; i < checkedValue.length; i++) {
            if (checkedValue[i].checked) {
                chk.push(checkedValue[i].value);
            }
        }
        $.ajax({
            type: 'POST',
            data: {target: chk, currentPath: currentPath},
            url: 'deleteServlet',
            success: function () {
                document.querySelector(".loading").style.display = "none";
                location.reload();
            }

        });
    }

    function deleteButton(row) {
        var currentPath = $('#cpath').text();
        var chk = [];
        chk.push(row);
        $.ajax({
            type: 'POST',
            data: {target: chk, currentPath: currentPath},
            url: 'deleteServlet',
            success: function () {
                $('#tableList').load(window.location + " #tableList");
                $('#cpath').load(window.location + " #cpath");
            }

        });
    }

    function showUploadOption() {
        document.getElementById("uploadDropdown").classList.toggle("show");
    }

    function showAction(fname) {
        document.getElementById(fname + "action").classList.toggle("show");
        document.getElementById(fname + "btn").classList.toggle("show");
    }

    function searchFiles() {
        var input = document.getElementById("searchInput").value;
        if (input === "") {
    <c:remove var="searchList" scope="session" />
            $('#searchTable').load(window.location + " #searchTable");
        } else {
            $.ajax({
                type: 'GET',
                data: {keyword: input},
                url: 'searchServlet',
                success: function () {
                    $('#searchTable').load(window.location + " #searchTable");
                }
            });
        }
    }

    function searchPreview(i, type) {
        switch (type) {
            case "folder":
                $.ajax({
                    type: 'GET',
                    data: {row: i},
                    url: 'searchPreviewServlet',
                    success: function () {
                        $('#tableList').load(window.location + " #tableList");
                        $('#cpath').load(window.location + " #cpath");
                    }
                });
                break;
            case "png":
            case "jpg":
            case "jpeg":
                document.getElementById("previewDocument").style.display = "none";
                document.getElementById("previewDocument").src = "";
                document.getElementById("previewImage").style.display = "flex";
                document.getElementById("previewImage").src = "searchPreviewServlet?row=" + i + "&type=" + type;
                document.getElementById("previewActionBtn").style.visibility = "visible";
                break;
            case "pdf":
            case "docx":
            case "doc":
            case "ppt":
            case "pptx":
                document.getElementById("previewImage").style.display = "none";
                document.getElementById("previewImage").src = "";
                document.getElementById("previewDocument").style.display = "flex";
                document.getElementById("previewDocument").src = "searchPreviewServlet?row=" + i + "&type=" + type + "#toolbar=0&navpanes=0&scrollbar=0";
                document.getElementById("previewActionBtn").style.visibility = "visible";
                break;
            case "txt":
            case "java":
            case "xlsx":
            case "xls":
            case "csv":
                document.getElementById("previewImage").style.display = "none";
                document.getElementById("previewImage").src = "";
                document.getElementById("previewDocument").style.display = "flex";
                document.getElementById("previewDocument").src = "searchPreviewServlet?row=" + i + "&type=" + type;
                document.getElementById("previewActionBtn").style.visibility = "visible";
                break;
            default:
                document.getElementById("previewImage").style.display = "none";
                document.getElementById("previewImage").src = "";
                document.getElementById("previewDocument").style.display = "flex";
                document.getElementById("previewDocument").src = "previewNotAvailable.html";
                document.getElementById("previewActionBtn").style.visibility = "visible";
        }
    }

    function getSearchTag(i, type) {
        var action = "getSearch";
        if (type !== "folder") {
            $.ajax({
                type: 'GET',
                data: {row: i, action: action},
                url: 'getMetadataServlet',
                success: function (previewName) {
                    $("#metadataTag").load(" #metadataTag > *");
                    document.getElementById("previewName").value = previewName;
                    document.getElementById("previewFileName").innerHTML = previewName;
                    setTimeout(function () {
                        document.getElementById("addItem").style.display = "flex";
                        document.querySelector(".tagContainer").style.display = "grid";
                    }, 100);
                }
            });
        }
    }

    function deletePreview() {
        var currentPath = $('#cpath').text();
        var source = "preview";
        $.ajax({
            type: 'POST',
            data: {source: source, currentPath: currentPath},
            url: 'deleteServlet',
            success: function () {
                location.reload();
            }
        });
    }

    function renamePreview() {
        document.querySelector(".renamePreviewForm").style.display = "flex";
        var name = document.getElementById("previewName").value;
        var input = document.getElementById("renameFolderNamePreview");
        var n = name.indexOf(".");
        input.value = name;
        input.focus();
        input.setSelectionRange(0, n);

    }

    function submitRenamePreview() {
        var name = document.getElementById("renameFolderNamePreview").value;
        var path = $('#cpath').text();
        var source = "preview";
        $.ajax({
            type: 'POST',
            data: {name: name, currentPath: path, source: source},
            url: 'renameServlet',
            success: function (result) {
                if (result === "1") {
                    location.reload();
                } else {
                    document.getElementById("RenameErrorMsg").innerHTML = "Invalid file name";
                }

            }
        });
    }

    function share() {
        var row = document.getElementById("shareRow").value;
        var input = document.getElementById("recipientEmail").value;
        var inputArr = input.replace(/ /g, '').split(";");
        var emailreq = /^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/;
        var validation = true;
        if (input.replace(/ /g, '') === "") {
            document.getElementById("shareResponseMsg").innerHTML = "Please fill in recipient Email address!";
        } else {
            for (var i = 0; i < inputArr.length; i++) {
                if (inputArr[i] === "") {
                    delete inputArr[i];
                } else {
                    if (!inputArr[i].match(emailreq) || inputArr[i] === "") {
                        validation = false;
                    }
                }
            }
            if (!validation) {
                document.getElementById("shareResponseMsg").style.color = "red";
                document.getElementById("shareResponseMsg").innerHTML = "Invalid email address, please check again!";
            } else {
                //if row is null which mean that user share from previewer button
                if (row !== "") {
                    $.ajax({
                        type: 'POST',
                        data: {row: row, arr: inputArr},
                        url: 'shareServlet',
                        success: function (success) {
                            if (success === "1") {
                                document.getElementById("shareResponseMsg").style.color = "green";
                                document.getElementById("shareResponseMsg").innerHTML = "Sharing success, recipient now can access the files from their PAVE account!";
                            } else {
                                document.getElementById("shareResponseMsg").style.color = "red";
                                document.getElementById("shareResponseMsg").innerHTML = "Sharing failed, please try again later.";
                            }
                        }
                    });
                } else {
                    $.ajax({
                        type: 'POST',
                        data: {arr: inputArr},
                        url: 'shareServlet',
                        success: function (success) {
                            if (success === "1") {
                                document.getElementById("shareResponseMsg").style.color = "green";
                                document.getElementById("shareResponseMsg").innerHTML = "Sharing success, recipient now can access the files from their PAVE account!";
                            } else {
                                document.getElementById("shareResponseMsg").style.color = "red";
                                document.getElementById("shareResponseMsg").innerHTML = "Sharing failed, please try again later.";
                            }
                        }
                    });
                }

            }
        }
    }

    function copy() {
        var copyText = document.getElementById("generatedLink");
        copyText.select();
        copyText.setSelectionRange(0, 99999);
        document.execCommand("copy");
    }

    function downloadRow(row) {
        window.open("downloadServlet?row=" + row);
    }

    function downloadPreview() {
        window.open("downloadServlet");
    }

    function downloadFolder(row) {
        window.open("downloadServlet?type=folder&row=" + row);
    }

    function showDelete() {
        if ($(".chkbox:checked").length > 0) {
            document.querySelector(".deleteAction").style.display = "inline-block";
        } else {
            document.querySelector(".deleteAction").style.display = "none";
        }
    }

    function sharePreview() {
        var fileName = document.getElementById("previewFileName").innerHTML;
        document.querySelector(".shareForm").style.display = "flex";
        document.getElementById("shareName").innerHTML = "Sharing " + fileName;
    }

</script>