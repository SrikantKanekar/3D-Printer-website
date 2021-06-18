document.addEventListener('DOMContentLoaded', function () {
    "use strict";

    const box = document.querySelector(".box");
    const input = box.querySelector('input[type="file"]');
    const filename = box.querySelector(".box_uploading_file span");
    const errorMsg = box.querySelector(".box_error span");
    const restart = box.querySelector(".box_restart");
    const progressBar = document.querySelector(".progress-bar");

    const canvas = document.querySelector(".canvas");
    const canvasName = document.querySelector(".canvas_name");
    const canvasSizeError = document.querySelector(".canvas_size_error");
    const createButton = document.querySelector("#create_button");
    const changeButton = document.querySelector("#change_button");

    let file;
    let uploading;

    [
        "drag",
        "dragstart",
        "dragend",
        "dragover",
        "dragenter",
        "dragleave",
        "drop",
    ].forEach(function (event) {
        box.addEventListener(event, function (e) {
            e.preventDefault();
            e.stopPropagation();
        });
    });

    ["dragover", "dragenter"].forEach(function (event) {
        box.addEventListener(event, function () {
            if (!uploading) {
                box.classList.add("is_dragover");
            }
        });
    });

    ["dragleave", "dragend", "drop"].forEach(function (event) {
        box.addEventListener(event, function () {
            box.classList.remove("is_dragover");
        });
    });

    box.addEventListener("drop", function (e) {
        if (!uploading) {
            file = e.dataTransfer.files[0];
            showCanvas();
        }
    });

    // automatically submit the form on file select
    input.addEventListener("change", function (e) {
        file = e.target.files[0];
        showCanvas();
    });

    restart.addEventListener("click", function (e) {
        e.preventDefault();
        box.classList.remove("is_error", "is_done");
    });

    function uploadingFile() {
        uploading = true;
        filename.textContent = file.name;
        box.classList.add("is_uploading_file");
        box.classList.remove("is_error");
    }

    function uploadingImage() {
        box.classList.remove("is_uploading_file");
        box.classList.add("is_uploading_img");
    }

    function uploadingDone() {
        box.classList.remove("is_uploading_img");
        box.classList.add("is_done");
    }

    function updateProgress(progress) {
        progressBar.setAttribute("aria-valuenow", progress.toString());
        progressBar.setAttribute("style", "width:" + progress + "%");
    }

    function showError(errorMessage) {
        uploading = false;
        box.classList.remove("is_uploading_file");
        box.classList.remove("is_uploading_img");
        box.classList.add("is_error");
        errorMsg.textContent = errorMessage;
    }

    function showCanvas() {
        canvasName.textContent = file.name.replace(/\.[^/.]+$/, "");
        box.style.display = "none";
        canvas.style.display = "block";

        const url = URL.createObjectURL(file);
        showModel(url, getFileExtension(),
            function (error) {
                createButton.parentElement.style.display = "none";
            }, function (sizeError) {
                if (!sizeError) {
                    createButton.parentElement.style.display = "none";
                    canvasSizeError.style.display = "block";
                    canvasSizeError.textContent = "Model size is too large";
                }
            });
        URL.revokeObjectURL(url);
    }

    function hideCanvas() {
        canvas.style.display = "none";
        box.style.display = "block";
        document.body.scrollTop = document.documentElement.scrollTop = 0;
        box.reset();
        removeModel();
        createButton.parentElement.style.display = "block";
        canvasSizeError.style.display = "none";
        document.querySelector('.canvas_error_container').style.display = "none";
    }

    changeButton.addEventListener('click', function (e) {
        e.preventDefault();
        hideCanvas();
    });

    createButton.addEventListener('click', function (e) {
        e.preventDefault();

        if (canvasName.textContent !== "") {
            uploadingFile();
            const image = takeSnapshot();
            hideCanvas();
            const id = generateId();

            uploadFirebaseFile(file, file.name, id, function (progress) {
                updateProgress(progress);
            }, function (fileUrl) {
                uploadingImage();
                uploadFirebaseImage(image, id, function (progress) {
                    updateProgress(progress);
                }, function (imageUrl) {
                    uploadingDone();
                    uploadObject(id, canvasName.textContent, fileUrl, imageUrl, getFileExtension());
                });
            });
        } else {
            showAlert("Name cannot be enpty", "alert-danger");
        }
    });

    function generateId() {
        return Date.now().toString(36) + Math.random().toString(36).substr(2);
    }

    function getFileExtension() {
        let lastDot = file.name.lastIndexOf('.');
        return file.name.substring(lastDot + 1);
    }

    function uploadObject(id, name, fileUrl, imageUrl, fileExtension) {
        $.post(
            "/object/create",
            {
                id: id,
                name: name,
                file_url: fileUrl,
                image_url: imageUrl,
                file_extension: fileExtension
            },
            function (data) {
                uploading = false;
                if (data === "error") {
                    showError("Error creating object");
                } else {
                    window.location.href = "/object/" + data;
                    box.reset();
                }
            }
        );
    }
});
