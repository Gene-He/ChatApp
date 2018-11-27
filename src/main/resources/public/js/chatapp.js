'use strict';

const webSocket = new WebSocket("ws://" + location.hostname + ":" + location.port + "/chatapp");

/**
 * Entry point into chat room
 */
window.onload = function() {

    webSocket.onclose = () => alert("WebSocket connection closed");

    // TODO button event handler to the Send Message  to Server.
    $("#btn-msg").click(function () {

        sendMessage(msg_str);
    });

    // TODO call updateChatRoom every time a message is received from the server
    webSocket.onmessage = (event) => updateChatRoom(event.data);

    //test render chatroom
    var room = document.getElementById("room-card-body");
    room.appendChild(getRoomTemplate("New block"));

    $(".btn-start-chat").click(function (event) {
        //TODO Send request
        openChatDialog($(event.target.parentElement.childNodes[0]).attr("name"),getChatRoomNameFromUser(event.target));
    });

}

/**
 * Send a message to the server.
 * @param msg  The message to send to the server.
 */
function sendMessage(msg) {
    webSocket.send(msg);
}

/**
 * Update the chat room with a message.
 * @param message  The message to update the chat room with.
 */
function updateChatRoom(message) {

}

/**
 * Send Login Request with User Info to Server
 */
function createUserInfo()
{
    setTimeout(function() {
        document.getElementById("login_close").click();
    }, 4000);
    var uname = document.getElementById("reg_username").value;
    var age = document.getElementById("reg_age").value;
    var loc = document.getElementById("reg_location").value;
    var sch =  document.getElementById("reg_school").value;
    // Grammar: login [userName] [age] [location] [school]
    var user_str = "login "+uname +" "+ age +" "+ loc+" " + sch;
     sendMessage(user_str);

}

/**
 * Send Create Room Request with Room Info to Server
 */
function createRoomInfo()
{
    setTimeout(function() {
        document.getElementById("createroom_close").click(); // Click on the checkbox
    }, 4000);
    var rname = document.getElementById("reg_roomname").value;
    var minage = document.getElementById("reg_minage").value;
    var maxage = document.getElementById("reg_maxage").value;
    var r_loc = document.getElementById("r_location").value;
    var r_sch =  document.getElementById("r_school").value;
    // Grammar: create [roomName] [ageLower] [ageUpper] f[location],g*f[location]g f[school],g*f[school]g
    var room_str = "create "+rname +" "+ minage +" "+ maxage+ " " + r_loc +" " + r_sch;

    sendMessage(room_str);
}

function getRoomTemplate(title){
    return parseDom(
        '<div class="container"> \
            <div class="card"> \
                <div class="card-header"> \
                    <div class="d-flex justify-content-between"> \
                        <h5 class="card-title">' + title + '</h5> \
                        <button type="button" class="btn btn-danger btn-sm" >Leave</button> \
                    </div> \
                </div> \
                <div class="card-body"> \
                    <table class="table">' + createUserTable().innerHTML + '</table>' +
                    createNotificationBlock() +
                '</div> \
            </div> \
        </div>');
}

function createNotificationBlock(){
    var block = "";
    var notifications  = ["A broadcasts : This is a broadcast.","B broadcasts : This is also a broadcast.","C left room"]
    for (var i = 0; i < notifications.length; i++){
        block += '<div class="alert alert-primary" role="alert">\ '+
                     '<p>' +notifications[i] + '</p>\</div>';
    }
    block += '<div class="input-group mb-3">\
                 <input type="text" class="form-control" placeholder="Broadcast..." aria-label="Recipient\'s username" aria-describedby="button-addon2">\
                 <div class="input-group-append">\
                     <button class="btn btn-outline-secondary" type="button" id="button-addon2">Send</button>\
                 </div>\
               </div>'
    return block;
}


function parseDom(arg) {
    var objE = document.createElement("div");
    objE.innerHTML = arg;
    return objE.childNodes[0];
}
function createUserTable(){
    //test example
    var users = ["Allen","Bob","Cindy","Emma"];
    var owner = "Allen";
    var chatUser = "Bob";
    var login = "Cindy";

    var tbl  = document.createElement('table');
    for(var i = 0; i < users.length; i++) {
        if (users[i] == login) continue;
        var tr = tbl.insertRow();
        var td = tr.insertCell();
        var user = parseDom("<div class=\"d-flex justify-content-between\"></div>")
        var p = document.createElement('p');
        p.setAttribute("name",users[i]);
        p.innerText = users[i];
        user.appendChild(p);
        if (users[i] == owner) {
            p.appendChild(parseDom('<span class=\"badge badge-primary\">Owner</span>'));
        }
        if (users[i] != chatUser) {
            user.appendChild(parseDom('<button class="btn btn-success btn-sm btn-start-chat">Chat</button>'));
        }
        td.append(user);
    }
    return tbl;
}
function openChatDialog(userName,roomName){
    var room = document.getElementById("chat-box");
    console.log(room);
    var fc = room.firstChild;
    room.replaceChild(getChatTemplate(userName,roomName,null),fc);
}

function getChatTemplate(userName,roomName,chatHistory){
    return parseDom(
        '<div class="container"> \
            <div class="card"> \
                <div class="card-header"> \
                    <div class="d-flex justify-content-between"> \
                        <h5 class="card-title">' + userName  + ' via ' + roomName + '</h5> \
                        <button type="button" class="btn btn-danger btn-sm" >End</button> \
                    </div> \
                </div> \
                <div class="card-body"> ' + getChatHistory(chatHistory) +
               '</div> \
             </div> \
             <div class="input-group mb-3">\
                <input type="text" class="form-control" placeholder="Message..." aria-label="Recipient\'s username" aria-describedby="button-addon2">\
                <div class="input-group-append">\
                    <button class="btn btn-outline-secondary" type="button">Send</button>\
                </div>\
             </div>\
        </div>');

}

function getChatRoomNameFromUser(node){
    while (node != null && node.getElementsByClassName("card-title").length == 0){
        node = node.parentNode;
    }
    return node.getElementsByClassName("card-title")[0].innerHTML;
}

function getChatHistory(chatHistory){
    chatHistory = [{"sender" : "Allen","message" : "Hi I am Allen"},{"sender" : "Bob","message" : "Hi I am Bob"}]
    var login = "Allen";
    if (chatHistory == null){
        return "";
    }
    var history = "";
    for (var i = 0; i < chatHistory.length; i++){
        if (chatHistory[i].sender == login ){
            history += '<div class="alert alert-primary" role="alert">' +
                           '<p>' + chatHistory[i].message +  '</p>\n' +
                       '</div>'
        }else {
            history += '<div class="alert alert-success" role="alert">' +
                '<p>' + chatHistory[i].message +  '</p>\n' +
                '</div>'
        }
    }
    return history;
}