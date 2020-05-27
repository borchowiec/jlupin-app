const url = "http://localhost:8000/rest-api/";

$(document).ready(function() {
    $("#loginBtn").on("click", () => {
        const username = $("#username").val();
        const password = $("#password").val();

        fetch(url + "authenticate", {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({username: username, password: password})
        })
            .then(response => response.json().then(data => ({status: response.status, body: data})))
            .then(data => {
                if (data.status === 200) {
                    Cookies.set("Authorization", `${data.body.type} ${data.body.token}`);
                    window.open("/rest-api/", "_self");
                }
                else {
                    $(".errorMessage").text(data.body.message);
                }
            })
            .catch((error) => {
                $(".errorMessage").text(error.message);
                console.error('Error:', error);
            });
    });
});