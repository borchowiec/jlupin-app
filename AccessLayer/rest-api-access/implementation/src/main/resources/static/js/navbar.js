async function addButtonsToNavbar() {
    const navbar = $(".navbar");

    const userInfo = await getUserInfo();

    if (userInfo) {
        navbar.append(`<span>Hello ${userInfo.username} </span>`);
        navbar.append(`<a href="tasks.html"><button>Tasks</button></a>`);
        navbar.append(`<a href="messages.html"><button>Messages</button></a>`);
        navbar.append(`<a href="logout.html"><button>Logout</button></a>`);
    } else {
        navbar.append(`<a href="register.html"><button>Register</button></a>`);
        navbar.append(`<a href="login.html"><button>Login</button></a>`);
    }
}

$(document).ready(function() {
    addButtonsToNavbar();
});