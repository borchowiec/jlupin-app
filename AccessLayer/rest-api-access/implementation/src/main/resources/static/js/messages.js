$(document).ready(function() {

    let authCookie = getAuthCookie();

    // load conversations
    if (authCookie) {
        const conversations = $(".conversations");
        fetch(url + "interlocutors", {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': authCookie
            },
        })
        .then(response => response.json().then(data => ({status: response.status, body: data})))
        .then(data => {
            if (data.status === 200) {
                data.body.forEach(user => {
                    conversations.append(`<li><div class="username">${user.username}</div><a href="conversation.html?userId=${user.id}"><button>SEND MESSAGE</button></li></a>`);
                })
            }
            else {
                alert(data.body.message);
            }
        })
        .catch((error) => {
            console.error('Error:', error);
        });
    }

    // find users
    $(".findUserBtn").on("click", () => {
        const input = $(".usernameInput");
        let phrase = input.val();
        input.val("");

        const resultContainer = $(".result");
        resultContainer.empty();

        fetch(url + "users/by-phrase/" + phrase, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            },
        })
        .then(response => response.json())
        .then(data => {
            data.forEach(user => {
                resultContainer.append(`<li><div class="username">${user.username}</div><a href="conversation.html?userId=${user.id}"><button>SEND MESSAGE</button></li></a>`);
            })
        })
        .catch((error) => {
            console.error('Error:', error);
        });
    })
});