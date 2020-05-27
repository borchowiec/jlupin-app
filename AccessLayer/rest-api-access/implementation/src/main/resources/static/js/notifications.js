var wsUri = "ws://localhost:8000/rest-api/notifications";

function initNotifications() {
    connect();
}

function connect() {
    websocket = new WebSocket(wsUri);
    websocket.onopen = function(evt) { onOpen(evt) };
    websocket.onclose = function(evt) { onClose(evt) };
    websocket.onmessage = function(evt) { onMessage(evt) };
    websocket.onerror = function(evt) { onError(evt) };
}

function onOpen(evt) {
    console.log("[NOTIFICATIONS] connected")
}

function onClose(evt) {
    console.log("[NOTIFICATIONS] disconnected")
}

function onMessage(evt) {
    data = JSON.parse(evt.data);
    const box = $(".notification-box");

    box.empty();
    if (data.type === "MESSAGE") {
        box.append(`<a href="conversation.html?userId=${data.sender}"><p style="font-size: 20px"><b>New message!</b></p><p style="font-size: 16px">${data.content}</p></a>`);
    }
    else {
        box.append(`<p>${data.content}</p>`);
    }
    box.show();

    setTimeout(() => box.hide(), 3000)
}

function onError(evt) {
    console.log("[NOTIFICATIONS] error: " + evt)
}

window.addEventListener("load", initNotifications, false);