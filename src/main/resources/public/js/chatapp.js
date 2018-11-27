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
//    var room = document.getElementById("room-card-body");
//    room.appendChild(getRoomTemplate("New block"));

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
    if(message['type'] === "NewUserResponse"){

    }
    if(message['type'] === "NewRoomResponse"){

    }
    if(message['type'] === "RoomNotificationResponse"){

    }
    if(message['type'] === "RoomUsersResponse"){

    }
    if(message['type'] === "UserChatHistory"){

    }
    if(message['type'] === "UserRooms"){
        updateRoomList(message);
        updateMyRooms(message);
    }
}

var roomListCardTemplate = "<div class=\"card\">" +
    "<div class=\"card-body\">" +
    "<h5 class=\"card-title\">[RoomName]</h5>" +
    "[Button]" +
    "</div>" +
    "</div>";

var roomListButtonTemplate = "<button type=\"button\" class=\"btn btn-[Type]\" onclick='joinRoom([RoomId])'>[Text]</button>";

function addRoomListCard(roomName, roomId, type){
    var html = roomListCardTemplate.replace("[RoomName]", roomName);
    var buttonHTML = "<p>dummy text</p>";
    if (type === "join"){
        buttonHTML = roomListButtonTemplate.replace("[Type]", "primary");
        buttonHTML = buttonHTML.replace("[Text]", "Join");
    }
    else if (type === "joined"){
        buttonHTML = roomListButtonTemplate.replace("[Type]", "info");
        buttonHTML = buttonHTML.replace("[Text]", "Joined");
    }
    else if (type === "owned"){
        buttonHTML = roomListButtonTemplate.replace("[Type]", "info");
        buttonHTML = buttonHTML.replace("[Text]", "Owned");
    }
    else{
        alert("Bad room type");
        return;
    }
    buttonHTML = buttonHTML.replace("[RoomId]", roomId);
    html = html.replace("[Button]", buttonHTML);
    $("#room-list").append(html);
}

function updateRoomList(message){
    message["ownedRooms"].forEach(function (room) {
        addRoomListCard(room["Name"], room["Id"], "owned");
    });

    message["availableRooms"].forEach(function (room) {
        addRoomListCard(room["Name"], room["Id"], "join");
    });

    message["ownedRooms"].forEach(function (room) {
        addRoomListCard(room["Name"], room["Id"], "owned");
    });
}

function updateMyRooms(message){

}

function updateMyChats(message){

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
    // Format: login [userName] [age] [location] [school]
    var user_str = "login " + uname + " " + age + " " + loc+ " " + sch;
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
        p.innerHTML = users[i];
        user.appendChild(p);
        if (users[i] == owner) {
            p.appendChild(parseDom('<span class=\"badge badge-primary\">Owner</span>'));
        }
        if (users[i] != chatUser) {
            user.appendChild(parseDom('<button class="btn btn-success btn-sm">Chat</button>'));
        }
        td.append(user);
    }
    return tbl;
}

function joinRoom(roomId){
    // Grammar: join [roomId]
    sendMessage("join " + roomId);
}