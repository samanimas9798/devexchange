document.addEventListener("DOMContentLoaded", function () {
    const skillOptions = {
        programming: ["Java", "JavaScript", "Python", "C#", "PHP", "Kotlin", "TypeScript"],
        frameworks: ["React", "Angular", "Spring Boot", "Django", "Vue"],
        tools: ["Git", "Docker", "Figma", "VS Code", "Jira"],
        web: ["HTML", "CSS", "Responsive Design", "Accessibility", "SEO"]
    };

    const skillCategory = document.getElementById("skillCategory");
    const specificSkill = document.getElementById("specificSkill");
    const learnCategory = document.getElementById("learnCategory");
    const learnSkill = document.getElementById("learnSkill");

    function updateDropdown(dropdown, items) {
        dropdown.innerHTML = '<option value="">-- Select a Skill --</option>';
        if (items) {
            items.forEach(skill => {
                const option = document.createElement("option");
                option.value = skill;
                option.textContent = skill;
                dropdown.appendChild(option);
            });
        }
    }

    skillCategory.addEventListener("change", () => updateDropdown(specificSkill, skillOptions[skillCategory.value]));
    learnCategory.addEventListener("change", () => updateDropdown(learnSkill, skillOptions[learnCategory.value]));

    document.querySelector(".signup-form").addEventListener("submit", async function (e) {
        e.preventDefault();

        const userData = {
            username: document.getElementById("username").value,
            email: document.getElementById("email").value,
            password: document.getElementById("password").value,
            techToShare: document.getElementById("specificSkill").value,
            techToLearn: document.getElementById("learnSkill").value
        };

        try {
            const response = await fetch("http://localhost:8080/api/users", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(userData)
            });

            if (!response.ok) throw new Error(`Signup failed: ${response.status}`);

            const data = await response.json();

            // Save returned profile info to localStorage
            localStorage.setItem("devexchangeUser", JSON.stringify({
                id: data.id,
                username: data.username,
                email: data.email,
                techToShare: data.techToShare || "Not specified",
                techToLearn: data.techToLearn || "Not specified"
            }));

            alert("Signup successful! Redirecting to your profile...");
            window.location.href = "/view-profile.html";

        } catch (error) {
            console.error("Signup error:", error);
            alert("Signup failed: " + error.message);
        }
    });
});
