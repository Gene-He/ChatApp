'use strict';

const webSocket = new WebSocket("ws://" + location.hostname + ":" + location.port + "/chatapp");
var chattingUser = "";
/**
 * Entry point into chat room
 */
window.onload = function() {
    webSocket.onclose = () => alert("WebSocket connection closed");
    webSocket.onmessage = (event) => updateChatRoom(event.data);


}

/**
 * Send a message to the server.
 * @param msg  The message to send to the server.
 */
function sendMessage(msg) {
    console.log("Sending: " + msg);
    webSocket.send(msg);
}

/**
 * Update the chat room with a message.
 * @param message  The message to update the chat room with.
 */
function updateChatRoom(data) {
    var message = JSON.parse(data);
    console.log("Receiving: " + message);
    if(message['type'] === "NewUserResponse"){

    }
    if(message['type'] === "NewRoomResponse"){

    }
    if(message['type'] === "RoomNotificationResponse"){

    }
    if(message['type'] === "RoomUsersResponse"){
        console.log("RoomUsersResponse");

    }
    if(message['type'] === "UserChatHistory"){

    }
    if(message['type'] === "UserRooms"){
        updateHelloUser(message['username']);
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

function updateHelloUser(name)
{
    document.getElementById("hello_user").innerHTML = "Hello, "+name+"!";
}

function toggleLogin() {
    var current = $("#id_login").text()
    if (current === 'Login') {
        $("#id_login").text('Update Profile');
    }
    else {
        $("#id_login").text('Login');
    }
}

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
        // TODO: disable this button
        addRoomListCard(room["name"], room["id"], "owned");
    });

    message["availableRooms"].forEach(function (room) {
        addRoomListCard(room["name"], room["id"], "join");
    });

    message["joinedRooms"].forEach(function (room) {
        // TODO: disable this button
        addRoomListCard(room["name"], room["Id"], "joined");
    });
}

function updateMyRooms(message){
    var roomCard =  document.getElementById("room-card-body");
    while (roomCard.lastChild) {
        roomCard.removeChild(roomCard.lastChild);
    }
    message["joinedRooms"].forEach(function (room) {
        roomCard.appendChild(getRoomTemplate(room));
    });

    message["ownedRooms"].forEach(function (room) {
        roomCard.appendChild(getRoomTemplate(room));
    });
    $(".btn-start-chat").click(function (event) {
       // // query userChatHistory [roomId] [anotherUserId]
       //  var roomInfo = getChatRoomNameFromUser(event.target);
       //  var user_str = "query|userChatHistory|" + roomInfo.id + "|" + $(event.target.parentElement.childNodes[0]).attr("userId");
       //  sendMessage(user_str);
        openChatDialog($(event.target.parentElement.childNodes[0]).attr("name"),roomInfo.name);
    });
}

function updateMyChats(message){

}

/**
 * Send Login Request with User Info to Server
 */
function createUserInfo()
{
//    setTimeout(function() {
//        document.getElementById("login_close").click();
//    }, 4000);
    var uname = document.getElementById("reg_username").value;
    var age = document.getElementById("reg_age").value;
    var loc = document.getElementById("reg_location").value;
    var sch =  document.getElementById("reg_school").value;
    // TODO: user name does not allow space
    // Format: login [userName] [age] [location] [school]
    var user_str = "login|" + uname + "|" + age + "|" + loc+ "|" + sch;
    sendMessage(user_str);
    document.getElementById("login_close").click();
    toggleLogin();
}

/**
 * Send Create Room Request with Room Info to Server
 */
function createRoomInfo()
{
//    setTimeout(function() {
//        document.getElementById("createroom_close").click(); // Click on the checkbox
//    }, 4000);
    var rname = document.getElementById("reg_roomname").value;
    var minage = document.getElementById("reg_minage").value;
    var maxage = document.getElementById("reg_maxage").value;
    var r_loc = document.getElementById("r_location").value;
    var r_sch =  document.getElementById("r_school").value;

    // Grammar: create [roomName] [ageLower] [ageUpper] f[location],g*f[location]g f[school],g*f[school]g
    // TODO: room name does not allow space
    var room_str = "create|"+rname +"|"+ minage +"|"+ maxage+ "|" + r_loc +"|" + r_sch;

    sendMessage(room_str);
    document.getElementById("createroom_close").click(); // Click on the checkbox
}

function getRoomTemplate(room){
    return parseDom(
        '<div class="container"> \
            <div class="card"> \
                <div class="card-header"> \
                    <div class="d-flex justify-content-between"> \
                        <h5 class="card-title" id="' +room["id"]+ '">' + room["name"] + '</h5> \
                        <button type="button" class="btn btn-danger btn-sm" >Leave</button> \
                    </div> \
                </div> \
                <div class="card-body"> \
                    <table class="table">' + createUserTable(room).innerHTML + '</table>' +
                    createNotificationBlock(room) +
                '</div> \
            </div> \
        </div>');
}

function createNotificationBlock(room){
    var block = "";
    var notifications = room["notifications"];
    for (var i = 0; i < notifications.length; i++){
        block += '<div class="alert alert-primary" role="alert">\ '+
                     '<p>' +notifications[i] + '</p>\</div>';
    }
    block += '<div class="input-group mb-3">\
                 <input type="text" class="form-control" placeholder="Broadcast..." aria-label="Recipient\'s username" aria-describedby="button-addon2">\
                 <div class="input-group-append">\
                     <button class="btn btn-outline-secondary" type="button" id="button-addon2 " onclick=sendBroadCast('+room["id"]+',this.parentNode.parentNode)>Send</button>\
                 </div>\
               </div>'
    return block;
}


function parseDom(arg) {
    var objE = document.createElement("div");
    objE.innerHTML = arg;
    return objE.childNodes[0];
}
function createUserTable(room){
    console.log(room);
    var map = room["userNameFromUserId"];
    var tbl  = document.createElement('table');
    appendUser(tbl,room["owner"]["name"],room["owner"]["id"],true);
    for (var key in map){
        appendUser(tbl,map[key],key,false);
    }
    return tbl;
}

function appendUser(tbl,name,id,isOwner){
    var tr = tbl.insertRow();
    var td = tr.insertCell();
    var user = parseDom("<div class=\"d-flex justify-content-between\"></div>")
    var p = document.createElement('p');
    p.setAttribute("name",name);
    p.setAttribute("userId",id);
    p.innerText = name;
    user.appendChild(p);
    if (isOwner) {
        p.appendChild(parseDom('<span class=\"badge badge-primary\">Owner</span>'));
    }
    if (name != chattingUser) {
        user.appendChild(parseDom('<button class="btn btn-success btn-sm btn-start-chat">Chat</button>'));
    }
    td.append(user);
}


function joinRoom(roomId){
    // Grammar: join|[roomId]
    sendMessage("join|" + roomId);
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

function sendBroadCast(roomId,node){
    console.log(roomId);
    console.log(node.getElementsByTagName("input")[0].value);
  // Grammar : broadcast [roomId] [message]
    sendMessage("broadcast|" + roomId +"|"+node.getElementsByTagName("input")[0].value);
}

function getChatRoomNameFromUser(node){
    while (node != null && node.getElementsByClassName("card-title").length == 0){
        node = node.parentNode;
    }
    console.log(node.getElementsByClassName("card-title")[0]);
    console.log(node.getElementsByClassName("card-title")[0].id);
    return {name : node.getElementsByClassName("card-title")[0].innerHTML,id : node.getElementsByClassName("card-title")[0].id};
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