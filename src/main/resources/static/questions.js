document.addEventListener("DOMContentLoaded", () => {

    const user = auth.requireLogin();

    const postBtn = document.getElementById("postQuestionBtn");
    const titleInput = document.getElementById("questionTitle");
    const bodyInput = document.getElementById("questionBody");
    const questionsList = document.getElementById("questionsList");
    const tagsSelect = document.getElementById("tagsSelect");

    let allQuestions = [];
    let activeTag = null;

    function loadQuestions() {
        fetch("http://localhost:8080/api/questions")
            .then(res => res.json())
            .then(data => {
                allQuestions = data; // store all
                renderQuestions(allQuestions);
            })
            .catch(err => {
                console.error("Failed to load questions:", err);
                questionsList.innerHTML = "<p>Error loading questions.</p>";
            });
    }

    function fetchReplies(questionId, repliesDiv) {
        fetch(`http://localhost:8080/api/replies/${questionId}`)
            .then(res => res.json())
            .then(replies => {
                repliesDiv.innerHTML = "";
                replies.forEach(reply => {
                    const replyEl = document.createElement("div");
                    replyEl.classList.add("reply");
                    replyEl.innerHTML = `<strong>${reply.username}:</strong> ${reply.content}`;
                    repliesDiv.appendChild(replyEl);
                });
            })
            .catch(err => {
                console.error("Failed to fetch replies:", err);
                repliesDiv.innerHTML = "<p>Error loading replies.</p>";
            });
    }

    function formatDate(dateString) {
        if (!dateString) return "Unknown date";
        const date = new Date(dateString);
        return date.toLocaleDateString("en-US", {
            year: "numeric",
            month: "short",
            day: "numeric"
        });
    }

    function renderQuestions(questions) {
        questionsList.innerHTML = "";

        questions.forEach(q => {
            const qDiv = document.createElement("div");
            qDiv.className = "question";

            const repliesDiv = document.createElement("div");
            repliesDiv.className = "replies";

            const replyInput = document.createElement("textarea");
            replyInput.placeholder = "Write a reply...";

            const replyBtn = document.createElement("button");
            replyBtn.textContent = "Reply";

            replyBtn.addEventListener("click", () => {
                const reply = replyInput.value.trim();
                if (!reply) return;

                if (!user || !user.id) {
                    alert("You must be logged in to post a reply.");
                    return;
                }

                fetch(`http://localhost:8080/api/replies/${q.id}/${user.id}`, {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify({ content: reply })
                })
                    .then(res => {
                        if (!res.ok) throw new Error("Failed to post reply.");
                        replyInput.value = "";
                        fetchReplies(q.id, repliesDiv);
                    })
                    .catch(err => {
                        console.error("Error posting reply:", err);
                        alert("Failed to post reply.");
                    });
            });


            let tagsHTML = "";
            if (q.tags && q.tags.length > 0) {
                tagsHTML = `<div class="question-tags">
                    ${q.tags.map(tag => `<span class="tag" data-tag="${tag}">${tag}</span>`).join(" ")}
                </div>`;
            }

            // Use postedAt
            const postedDate = formatDate(q.postedAt);

            qDiv.innerHTML = `
                <h3>${q.title}</h3>
                <p>${q.description}</p>
                <small>Posted by: ${q.username} | ${postedDate}</small>
                ${tagsHTML}
            `;

            const replySection = document.createElement("div");
            replySection.classList.add("reply-section");
            replySection.appendChild(replyInput);
            replySection.appendChild(replyBtn);
            replySection.appendChild(repliesDiv);

            qDiv.appendChild(replySection);
            questionsList.appendChild(qDiv);

            fetchReplies(q.id, repliesDiv);
        });


        document.querySelectorAll(".tag").forEach(tagEl => {
            tagEl.addEventListener("click", () => {
                const selectedTag = tagEl.dataset.tag;

                if (activeTag === selectedTag) {

                    activeTag = null;
                    renderQuestions(allQuestions);
                } else {

                    activeTag = selectedTag;
                    const filtered = allQuestions.filter(q => q.tags.includes(selectedTag));
                    renderQuestions(filtered);
                }
            });
        });
    }

    postBtn.addEventListener("click", () => {
        const title = titleInput.value.trim();
        const body = bodyInput.value.trim();
        const tags = Array.from(document.querySelectorAll("#tagsSelect option:checked")).map(opt => opt.value);

        if (!title || !body) {
            alert("Please enter both a title and a description.");
            return;
        }

        if (!user || !user.id) {
            alert("You must be logged in to post a question.");
            return;
        }

        fetch(`http://localhost:8080/api/questions/${user.id}`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ title, description: body, tags }) // send tags
        })
            .then(res => res.json())
            .then(() => {
                titleInput.value = "";
                bodyInput.value = "";
                loadQuestions();
            });
    });

    loadQuestions();
});
