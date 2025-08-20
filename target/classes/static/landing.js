document.addEventListener("DOMContentLoaded", function () {

    if (window.auth) {
        auth.redirectIfLoggedIn();
    } else {

        const existingUser = localStorage.getItem("devexchangeUser");
        if (existingUser) {
            window.location.href = "view-profile.html";
            return;
        }
    }

    const loginForm = document.getElementById("loginForm");

    loginForm.addEventListener("submit", async function (e) {
        e.preventDefault();

        const email = document.getElementById("loginEmail").value.trim();
        const password = document.getElementById("loginPassword").value.trim();

        if (!email || !password) {
            alert("Please enter both email and password.");
            return;
        }

        try {
            const response = await fetch("http://localhost:8080/api/users/login", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({ email, password })
            });

            if (!response.ok) {
                throw new Error("Invalid email or password");
            }

            const userData = await response.json();


            if (window.auth) {
                auth.setLoggedInUser(userData);
            } else {
                localStorage.setItem("devexchangeUser", JSON.stringify(userData));
            }

            alert("Login successful! Redirecting...");
            window.location.href = "view-profile.html";
        } catch (error) {
            alert("Login failed: " + error.message);
            console.error("Login error:", error);
        }
    });
});
