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
            .then(response => response.json())
            .then(data => {
                if (data.status === 500) {
                    $(".errorMessage").text(data.message);
                }
                else {
                    Cookies.set("Authorization", `${data.type} ${data.token}`);
                    window.open("/rest-api/", "_self");
                }
                console.log(data);
            })
            .catch((error) => {
                $(".errorMessage").text(error.message);
                console.error('Error:', error);
            });
    });
});