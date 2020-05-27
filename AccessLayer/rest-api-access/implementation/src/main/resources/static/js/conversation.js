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
            .then(response => response.json())
            .then(data => {
                initConversation(data);
            })
            .catch((error) => {
                console.error('Error:', error);
            });
    }
}

function initConversation(data) {
    interlocutors = data.interlocutors;

    data.messages.forEach(message => {
        writeToScreen(`<div class="message"><b>${interlocutors[message.sender]}</b></br>${message.content}</div>`);
    });
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
        .then(response => {
            if (response.ok) {
                writeToScreen("Message was sent");
            }
            else {
                writeToScreen("Error. Cannot sent message");
            }
        });
}

function writeToScreen(message) {
    var pre = document.createElement("p");
    pre.style.wordWrap = "break-word";
    pre.innerHTML = message;
    output.appendChild(pre);
}

window.addEventListener("load", init, false);