var url = "http://localhost:8000/rest-api/";
var input;
var output;
var userId;
var authCookie;

var interlocutors;

function init() {
    var urlParams = new URLSearchParams(window.location.search);
    userId = urlParams.get('userId');
    authCookie = Cookies.get("Authorization");

    input = document.getElementById("input");
    output = document.getElementById("output");

    if (!userId) {
        writeToScreen("Not found userId variable!")
    }
    else if (!authCookie) {
        writeToScreen("Not found authorization token!")
    }
    else {
        fetch(url + "conversation/" + userId, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': authCookie
            }
        })
            .then(response => response.json().then(data => ({status: response.status, body: data})))
            .then(data => {
                if (data.status === 200) {
                    initConversation(data.body);
                }
                else {
                    writeToScreen(data.body.message);
                }
            })
            .catch((error) => {
                console.error('Error:', error);
            });
    }
}

function addMessage(message) {
    writeToScreen(`<div class="message"><b>${interlocutors[message.sender]}</b></br>${message.content}</div>`);
}

function initConversation(data) {
    interlocutors = data.interlocutors;

    data.messages.forEach(message => addMessage(message));
}

function doSend() {
    var content = input.value;
    input.value = "";

    var data = {receiver: userId, content: content};

    fetch(url + "add-message", {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': authCookie
        },
        body: JSON.stringify(data)
    })
    .then(response => response.json().then(data => ({status: response.status, body: data})))
    .then(data => {
        if (data.status === 200) {
            addMessage(data.body);
        }
        else {
            writeToScreen(data.body.message);
        }
    })
    .catch((error) => {
        console.error('Error:', error);
    });
}

function writeToScreen(message) {
    var pre = document.createElement("p");
    pre.style.wordWrap = "break-word";
    pre.innerHTML = message;
    output.appendChild(pre);
}

window.addEventListener("load", init, false);