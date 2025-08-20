document.addEventListener("DOMContentLoaded", function () {
    const welcomeMessage = document.getElementById("welcomeMessage");
    const readyButton = document.getElementById("readyButton");
    const dashboard = document.getElementById("dashboard");

    const displayUsername = document.getElementById("displayUsername");
    const displayEmail = document.getElementById("displayEmail");
    const displayShare = document.getElementById("displayShare");
    const displayLearn = document.getElementById("displayLearn");

    const connectionsList = document.getElementById("connectionsList");
    const pendingConnectionsList = document.getElementById("pendingConnectionsList");

    const findMatchesButton = document.getElementById("findMatchesButton");
    const matchResults = document.getElementById("matchResults");
    const matchesList = document.getElementById("matchesList");

    const editBtn = document.getElementById("editProfileBtn");
    const editForm = document.getElementById("editProfileForm");
    const saveBtn = document.getElementById("saveProfileBtn");
    const cancelBtn = document.getElementById("cancelEditBtn");
    const editTeachInput = document.getElementById("editTeach");
    const editLearnInput = document.getElementById("editLearn");

    const user = JSON.parse(localStorage.getItem("devexchangeUser"));
    let userProfile = null;
    let connectedProfileIds = [];

    function loadPendingConnections(userId) {
        fetch(`http://localhost:8080/api/profiles/${userId}/pending`)
            .then(res => res.json())
            .then(pending => {
                pendingConnectionsList.innerHTML = "";
                if (pending.length === 0) {
                    pendingConnectionsList.innerHTML = "<li>No pending requests</li>";
                    return;
                }

                pending.forEach(profile => {
                    const li = document.createElement("li");
                    li.classList.add("pending-item");

                    const nameSpan = document.createElement("span");
                    nameSpan.textContent = `${profile.username}`;

                    const acceptBtn = document.createElement("button");
                    acceptBtn.textContent = "Accept";
                    acceptBtn.classList.add("accept-button");

                    const declineBtn = document.createElement("button");
                    declineBtn.textContent = "Decline";
                    declineBtn.classList.add("decline-button");

                    acceptBtn.addEventListener("click", () => {
                        fetch(`http://localhost:8080/api/profiles/${user.id}/accept/${profile.id}`, {
                            method: "POST"
                        })
                            .then(res => {
                                if (!res.ok) throw new Error("Failed to accept");
                                loadPendingConnections(user.id);
                                loadConnections(user.id);
                            })
                            .catch(err => {
                                console.error("Error accepting request:", err);
                                alert("Failed to accept request.");
                            });
                    });

                    declineBtn.addEventListener("click", () => {
                        fetch(`http://localhost:8080/api/profiles/${user.id}/decline/${profile.id}`, {
                            method: "POST"
                        })
                            .then(res => {
                                if (!res.ok) throw new Error("Failed to decline");
                                loadPendingConnections(user.id);
                            })
                            .catch(err => {
                                console.error("Error declining request:", err);
                                alert("Failed to decline request.");
                            });
                    });

                    const buttonGroup = document.createElement("div");
                    buttonGroup.classList.add("button-group");
                    buttonGroup.appendChild(acceptBtn);
                    buttonGroup.appendChild(declineBtn);

                    li.appendChild(nameSpan);
                    li.appendChild(buttonGroup);
                    pendingConnectionsList.appendChild(li);
                });
            });
    }

    function loadConnections(userId) {
        return fetch(`http://localhost:8080/api/profiles/${userId}/connections`)
            .then(res => res.json())
            .then(connections => {
                connectedProfileIds = connections.map(p => p.id);
                connectionsList.innerHTML = "";
                if (connections.length === 0) {
                    connectionsList.innerHTML = "<li>No connections yet</li>";
                    return;
                }

                connections.forEach(profile => {
                    const li = document.createElement("li");
                    li.textContent = `${profile.username}`;


                    const callBtn = document.createElement("button");
                    callBtn.textContent = "Call";
                    callBtn.classList.add("call-btn");
                    callBtn.addEventListener("click", () => {
                        const meetUrl = `https://meet.google.com/new`;
                        window.open(meetUrl, "_blank");
                    });

                    li.appendChild(callBtn);
                    connectionsList.appendChild(li);
                });
            })
            .catch(err => {
                console.error("Error loading connections:", err);
                connectedProfileIds = [];
            });
    }

    if (user) {
        welcomeMessage.textContent = `Welcome, ${user.username || "User"}!`;
        displayUsername.textContent = user.username || "N/A";
        displayEmail.textContent = user.email || "N/A";

        if (user.id) {
            fetch(`http://localhost:8080/api/profiles/${user.id}`)
                .then(response => {
                    if (!response.ok) throw new Error("Profile fetch failed");
                    return response.json();
                })
                .then(profile => {
                    userProfile = profile;
                    displayShare.textContent = profile.techToShare?.trim() || "Not specified";
                    displayLearn.textContent = profile.techToLearn?.trim() || "Not specified";

                    if (editTeachInput && editLearnInput) {
                        editTeachInput.value = profile.techToShare || "";
                        editLearnInput.value = profile.techToLearn || "";
                    }

                    loadConnections(user.id);
                    loadPendingConnections(user.id);
                })
                .catch(error => {
                    console.error("Error fetching profile:", error);
                });
        }
    }

    readyButton.addEventListener("click", function () {
        dashboard.style.display = "block";
        readyButton.style.display = "none";
    });

    function techMatch(a, b) {
        return a?.trim().toLowerCase() === b?.trim().toLowerCase();
    }

    function loadMatches(userId) {
        fetch(`http://localhost:8080/api/matches/${userId}`)
            .then(response => {
                if (!response.ok) throw new Error("Match fetch failed");
                return response.json();
            })
            .then(matches => {
                matchesList.innerHTML = "";

                const validMatches = matches.filter(m => m.techToShare || m.techToLearn);

                const displayableMatches = validMatches
                    .map(match => {
                        const isOneWay = techMatch(userProfile.techToLearn, match.techToShare);
                        const isMutual = isOneWay && techMatch(userProfile.techToShare, match.techToLearn);
                        return { ...match, isOneWay, isMutual };
                    })
                    .filter(m => m.isOneWay && !connectedProfileIds.includes(m.id));

                if (displayableMatches.length === 0) {
                    matchesList.innerHTML = "<li>No suitable matches found.</li>";
                } else {
                    displayableMatches.forEach(match => {
                        const li = document.createElement("li");
                        li.classList.add("match-item");

                        const details = document.createElement("div");
                        details.classList.add("match-details");
                        details.innerHTML = `
                            <strong>${match.username}</strong><br>
                            Can Teach: ${match.techToShare}<br>
                            Wants to Learn: ${match.techToLearn}
                            ${match.isMutual ? "<br><span style='color: green;'>(Mutual)</span>" : ""}
                        `;

                        const connectButton = document.createElement("button");
                        connectButton.textContent = "Connect";
                        connectButton.classList.add("connect-button");

                        connectButton.addEventListener("click", () => {
                            fetch(`http://localhost:8080/api/profiles/${user.id}/request/${match.id}`, {
                                method: "POST"
                            })
                                .then(response => {
                                    if (!response.ok) throw new Error("Failed to send request");
                                    connectButton.disabled = true;
                                    connectButton.textContent = "Request Sent!";
                                })
                                .catch(err => {
                                    console.error("Connection request failed:", err);
                                    alert("Failed to send connection request.");
                                });
                        });

                        li.appendChild(details);
                        li.appendChild(connectButton);
                        matchesList.appendChild(li);
                    });
                }

                matchResults.style.display = "block";
                matchResults.scrollIntoView({ behavior: "smooth" });
            })
            .catch(error => {
                console.error("Error fetching matches:", error);
                matchesList.innerHTML = "<li>Error fetching matches.</li>";
                matchResults.style.display = "block";
                matchResults.scrollIntoView({ behavior: "smooth" });
            });
    }

    findMatchesButton.addEventListener("click", function () {
        if (!user || !user.id || !userProfile) {
            alert("User profile not loaded yet.");
            return;
        }

        loadConnections(user.id).then(() => {
            loadMatches(user.id);
        });
    });

    if (editBtn && editForm && saveBtn && cancelBtn) {
        editBtn.addEventListener("click", () => {
            editForm.style.display = "block";
            editBtn.style.display = "none";

            editTeachInput.value = userProfile.techToShare || "";
            editLearnInput.value = userProfile.techToLearn || "";
        });

        cancelBtn.addEventListener("click", () => {
            editForm.style.display = "none";
            editBtn.style.display = "inline-block";
        });

        saveBtn.addEventListener("click", () => {
            const updatedProfile = {
                ...userProfile,
                techToShare: editTeachInput.value.trim(),
                techToLearn: editLearnInput.value.trim()
            };

            fetch(`http://localhost:8080/api/profiles/${user.id}`, {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(updatedProfile)
            })
                .then(response => {
                    if (!response.ok) throw new Error("Update failed");
                    return response.json();
                })
                .then(updated => {
                    userProfile = updated;
                    displayShare.textContent = updated.techToShare || "Not specified";
                    displayLearn.textContent = updated.techToLearn || "Not specified";

                    editForm.style.display = "none";
                    editBtn.style.display = "inline-block";
                })
                .catch(err => {
                    console.error("Error saving profile:", err);
                    alert("Failed to save changes.");
                });
        });
    }
});