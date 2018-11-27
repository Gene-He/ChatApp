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

function createUserInfo()
{
    setTimeout(function() { $('#myModal').modal('hide'); }, 4000);
    var uname = document.getElementById("reg_username").value;
    var age = document.getElementById("reg_age").value;
    var loc = document.getElementById("reg_location").value;
    var sch =  document.getElementById("reg_school").value;
    // Grammar: login [userName] [age] [location] [school]
    var user_str = "login "+uname +" "+ age +" "+ loc+" " + sch;
   // alert(user_str);
    sendMessage(user_str);
   // document.getElementById("loginUserResult").innerHTML = "Login Success!";

}

function createRoomInfo()
{
    setTimeout(function() { $('#roomModal').modal('hide'); }, 4000);
    var rname = document.getElementById("reg_roomname").value;
    var minage = document.getElementById("reg_minage").value;
    var maxage = document.getElementById("reg_maxage").value;
    var r_loc = document.getElementById("r_location").value;
    var r_sch =  document.getElementById("r_school").value;
    // Grammar: create [roomName] [ageLower] [ageUpper] f[location],g*f[location]g f[school],g*f[school]g
    var room_str = "create "+rname +" "+ minage +" "+ maxage+ " " + r_loc +" " + r_sch;

    //alert(room_str);
    sendMessage(room_str);
    //document.getElementById("createRoomResult").innerHTML = "Create Success!";
}