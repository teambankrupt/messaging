var stompClient = null;
var subscription = null;
connect();
updateScroll();


function setConnected(connected) {
    document.getElementById('connect').disabled = connected;
    document.getElementById('disconnect').disabled = !connected;
    document.getElementById('conversationDiv').style.visibility
        = connected ? 'visible' : 'hidden';
    document.getElementById('response').innerHTML = '';
}

function connect() {
    if (stompClient != null) return;
    var baseUrl = document.getElementById('base_url').value;
    var socket = new SockJS(baseUrl + "/connect");
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        // setConnected(true);
        console.log('Connected: ' + frame);
        // alert('connected: \n' + frame)
    });
}

function subscribe(elementId, chatroomId) {
    console.log("Subscribing chatroom 1..");
    if (subscription != null)
        subscription.unsubscribe();
    const url = '/topic/chatrooms/' + chatroomId + '/messages';
    subscription = stompClient.subscribe(url, function (messageOutput) {
        // console.log(messageOutput)
        showMessageOutput(JSON.parse(messageOutput.body));
    });

    $('#chat_room_id').val(chatroomId);
    $("[id^=chatRoomItem]").attr('class', '');
    $('#' + elementId).attr('class', 'active');
}

function subscribeServerTime() {
    if (subscription != null)
        subscription.unsubscribe();
    const url = '/topic/time';
    subscription = stompClient.subscribe(url, function (messageOutput) {
        // console.log(messageOutput)
        showMessageOutput(messageOutput.body);
    });
}

function disconnect() {
    if (stompClient != null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendMessage() {
    var from = document.getElementById('chat_room_id').value;
    var text = document.getElementById('content').value;
    var requObject = JSON.stringify({'chat_room_id': from, 'content': text, 'from':'01316343767'});
    // console.log(requObject);
    stompClient.send("/messages/chat", {}, requObject);
}

function showMessageOutput(message) {
    // var response = document.getElementById('msg_body');
    // var p = document.createElement('p');
    // p.style.wordWrap = 'break-word';
    // console.log(messageOutput);
    // p.appendChild(document.createTextNode(messageOutput.from + ": "
    //     + messageOutput.content + " (" + messageOutput.time + ")"));
    // response.appendChild(p);
    // console.log($('#username').val());
    // console.log(messageOutput.from);

        $('#msg_body').append("<div class='d-flex justify-content-start mb-4'> <div class='img_cont_msg'> <img src='https://static.turbosquid.com/Preview/001292/481/WV/_D.jpg' class='rounded-circle user_img_msg'> </div> <div class='msg_cotainer'> " +
            message);
    updateScroll();
    $('#content').val('');
}

function updateScroll() {
    var element = document.getElementById("msg_body");
    element.scrollTop = element.scrollHeight;
}

function onKeyChange() {
    var key = window.event.keyCode;

    // If the user has pressed enter
    if (key === 13) {
        sendMessage();
        return false;
    } else {
        return true;
    }
}
