let authToken;

function addTask(task) {
    console.log(task);
    $(".taskList")
        .append(`<li class="${task.id}"><div class="taskContent">${task.message}</div><input class="taskContentEdit" style="display: none"><button class="remove">REMOVE</button><button class="edit">EDIT</button><button  class="done">DONE</button></li>`);

    // delete
    $(`.taskList .${task.id} .remove`).on("click", () => {
        fetch(url + "task/" + task.id, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': authToken
            }
        })
        .then(response => response.json())
        .then(data => console.log(data))
        .catch((error) => {
            console.error('Error:', error);
        });
        $(`.taskList .${task.id}`).remove();
    });

    // edit
    const editBtn = $(`.taskList .${task.id} .edit`);
    editBtn.on("click", () => {
        const contentDisplay = $(`.taskList .${task.id} .taskContent`);
        const contentEditor = $(`.taskList .${task.id} .taskContentEdit`);

        if (editBtn.text() === "EDIT") {
            let content = contentDisplay.text();
            contentEditor.val(content);
            contentDisplay.hide();
            contentEditor.show();
            editBtn.text("SAVE");
        }
        else {
            let content = contentEditor.val();
            contentDisplay.text(content);
            contentDisplay.show();
            contentEditor.hide();

            task.message = content;
            fetch(url + "task", {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': authToken
                },
                body: JSON.stringify(task)
            })
                .then(response => response.json())
                .then(data => console.log(data))
                .catch((error) => {
                    console.error('Error:', error);
                });

            editBtn.text("EDIT");
        }
    });

    // done
    $(`.taskList .${task.id} .done`).on("click", () => {
        task.status = "DONE";
        fetch(url + "task", {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': authToken
            },
            body: JSON.stringify(task)
        })
        .then(response => response.json())
        .then(data => console.log(data))
        .catch((error) => {
            console.error('Error:', error);
        });
        $(`.taskList .${task.id}`).remove();
    });
}

function loadTasks() {
    fetch(url + "tasks", {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': authToken
        }
    })
        .then(response => response.json())
        .then(data => {
            data
                .filter(task => task.status === "TODO")
                .forEach(task => addTask(task));
        })
        .catch((error) => {
            console.error('Error:', error);
        });
}

$(document).ready(function() {
    authToken = getAuthCookie();
    if (authToken) {
        loadTasks();

        $(".addTaskBtn").on("click", () => {
            const input = $(".newTaskInput");
            const content = input.val();
            input.val("");
            fetch(url + "task", {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': authToken
                },
                body: JSON.stringify({message: content, status: "TODO"})
            })
                .then(response => response.json())
                .then(data => addTask(data))
                .catch((error) => {
                    console.error('Error:', error);
                });
        });
    }
});