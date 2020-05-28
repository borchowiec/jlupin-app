var url = "http://localhost:8000/rest-api/";

function getAuthCookie() {
    return Cookies.get("Authorization");
}

async function getUserInfo() {
    const authCookie = getAuthCookie();
    if (!authCookie) {
        return null;
    }

    let userInfo = null;

    await fetch(url + "user", {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': authCookie
        }
    })
        .then(response => response.json().then(data => ({status: response.status, body: data})))
        .then(data => {
            if (data.status === 200) {
                userInfo = data.body;
            }
            else {
                alert(data.body.message);
            }
        })
        .catch((error) => {
            console.error('Error:', error);
        });
    return userInfo;
}