document.addEventListener('DOMContentLoaded', function () {
    "use strict";

    const form = document.querySelector(".box");
    const input = form.querySelector('input[type="file"]');
    const filename = form.querySelector(".box_uploading span");
    const errorMsg = form.querySelector(".box_error span");
    const restart = form.querySelector(".box_restart");
    const progressBar = document.querySelector(".progress-bar");

    const canvas = document.querySelector(".canvas");
    const canvasName = document.querySelector(".canvas_name");
    const canvasError = document.querySelector(".canvas_error");
    const createButton = document.querySelector("#create_button");
    const changeButton = document.querySelector("#change_button");

    let file;

    [
        "drag",
        "dragstart",
        "dragend",
        "dragover",
        "dragenter",
        "dragleave",
        "drop",
    ].forEach(function (event) {
        form.addEventListener(event, function (e) {
            e.preventDefault();
            e.stopPropagation();
        });
    });

    ["dragover", "dragenter"].forEach(function (event) {
        form.addEventListener(event, function () {
            form.classList.add("is_dragover");
        });
    });

    ["dragleave", "dragend", "drop"].forEach(function (event) {
        form.addEventListener(event, function () {
            form.classList.remove("is_dragover");
        });
    });

    form.addEventListener("drop", function (e) {
        file = e.dataTransfer.files[0];
        showCanvas();
    });

    // automatically submit the form on file select
    input.addEventListener("change", function (e) {
        file = e.target.files[0];
        showCanvas();
    });

    restart.addEventListener("click", function (e) {
        e.preventDefault();
        form.classList.remove("is_error", "is_success");
    });

    function uploadStart() {
        form.classList.add("is_uploading");
        form.classList.remove("is_error");
    }

    function handleProgress(e) {
        if (e.lengthComputable) {
            const progress = (e.loaded / e.total) * 100;
            updateProgress(progress)
            if (progress === 100) scanningStart();
        }
    }

    function updateProgress(progress) {
        progressBar.setAttribute("aria-valuenow", progress.toString());
        progressBar.setAttribute("style", "width:" + progress + "%");
    }

    function scanningStart() {
        form.classList.remove("is_uploading");
        form.classList.add("is_scanning");
    }

    function handleSuccess(data) {
        creatingObject(data);
        if (data.success === "true") {
            window.location.href = "/object/" + data.id;
            form.reset();
        }
    }

    function creatingObject(data) {
        form.classList.remove("is_scanning");
        if (data.success === "true") {
            form.classList.add("is_success");
        } else {
            form.classList.add("is_error");
            errorMsg.textContent = "Error creating object";
        }
    }

    function handleError(errorMessage) {
        form.classList.remove("is_uploading");
        form.classList.remove("is_scanning");
        form.classList.add("is_error");
        errorMsg.textContent = errorMessage;
    }

    function showCanvas() {
        canvasName.textContent = file.name;
        form.style.display = "none";
        canvas.style.display = "block";

        const url = URL.createObjectURL(file);
        showModel(url,
            function (error) {
                console.log(error);
                createButton.parentElement.style.display = "none";
                canvasError.style.display = "block";
                canvasError.textContent = "Error";
            }, function (sizeError) {
                if (!sizeError) {
                    createButton.parentElement.style.display = "none";
                    canvasError.style.display = "block";
                    canvasError.textContent = "Model size is too large";
                }
            });
        URL.revokeObjectURL(url);
    }

    function hideCanvas() {
        canvas.style.display = "none";
        form.style.display = "block";
        document.body.scrollTop = document.documentElement.scrollTop = 0;
        form.reset();
        removeModel();
        createButton.parentElement.style.display = "block";
        canvasError.style.display = "none";
    }

    changeButton.addEventListener('click', function (e) {
        e.preventDefault();
        hideCanvas();
    });

    createButton.addEventListener('click', function (e) {
        e.preventDefault();
        hideCanvas();

        const id = generateId();

        uploadFirebaseFile(file, file.name, id, function (progress) {
            updateProgress(progress);
        }, function (downloadUrl) {
            const fileUrl = downloadUrl;
            const image = takeSnapshot();
            uploadFirebaseImage(image, id, function (progress) {
                updateProgress(progress);
            }, function (downloadUrl) {
                const imageUrl = downloadUrl;
            });
        });


        //uploadObject();
    });

    function generateId() {
        return Date.now().toString(36) + Math.random().toString(36).substr(2);
    }

    function uploadObject() {
        // preventing the duplicate submissions if the current one is in progress
        if (form.classList.contains("is_uploading")) return false;

        uploadStart();

        const formData = new FormData();
        formData.append("file", file);
        filename.textContent = file.name;

        const request = new XMLHttpRequest();
        request.open(
            form.getAttribute("method"),
            "/object/create",
            true
        );
        request.upload.addEventListener(
            "progress",
            function (e) {
                handleProgress(e);
            },
            false
        );
        request.onreadystatechange = function () {
            if (request.readyState === 4) {
                if (request.status === 200) {
                    let data = JSON.parse(request.responseText);
                    handleSuccess(data);
                } else {
                    handleError(request.responseText);
                }
            }
        };
        request.send(formData);
    }
});
