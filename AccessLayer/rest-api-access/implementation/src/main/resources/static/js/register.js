const url = "http://localhost:8000/rest-api/";

$(document).ready(function() {
    $("#registerBtn").on("click", () => {
        const username = $("#username").val();
        const password = $("#password").val();
        const passwordRepeated = $("#passwordRepeated").val();

        if (password !== passwordRepeated) {
            $(".errorMessage").text("Passwords are not the same.");
        }
        else {
            fetch(url + "add-user", {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({username: username, password: password})
            })
                .then(response => response.json())
                .then(data => {
                    if (data.status === 400) {
                        console.log(data);
                        if (data.errors) {
                            $(".errorMessage").text(`[${data.errors[0].field}] ${data.errors[0].defaultMessage}`);
                        }
                        else {
                            $(".errorMessage").text(data.message);
                        }
                    }
                    else {
                        window.open("/rest-api/login.html", "_self");
                    }
                })
                .catch((error) => {
                    $(".errorMessage").text(error.message);
                    console.error('Error:', error);
                });
        }
    });
});