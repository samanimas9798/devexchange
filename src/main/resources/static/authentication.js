

function getLoggedInUser() {
    const userData = localStorage.getItem("devexchangeUser");
    return userData ? JSON.parse(userData) : null;
}


function setLoggedInUser(user) {
    localStorage.setItem("devexchangeUser", JSON.stringify(user));
}


function logoutUser() {
    localStorage.removeItem("devexchangeUser");
    window.location.href = "index.html";
}


function requireLogin(redirectIfNot = "index.html") {
    const user = getLoggedInUser();
    if (!user) {
        alert("You must be logged in to view this page.");
        window.location.href = redirectIfNot;
    }
    return user;
}


function redirectIfLoggedIn(redirectIfYes = "view-profile.html") {
    const user = getLoggedInUser();
    if (user) {
        window.location.href = redirectIfYes;
    }
}

document.addEventListener("DOMContentLoaded", () => {
    const logoutBtn = document.getElementById("logoutBtn");
    if (logoutBtn) {
        logoutBtn.addEventListener("click", () => {
            localStorage.removeItem("devexchangeUser");
            window.location.href = "index.html";
        });
    }
});


window.auth = {
    getLoggedInUser,
    setLoggedInUser,
    logoutUser,
    requireLogin,
    redirectIfLoggedIn
};
