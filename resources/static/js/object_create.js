"use strict";
(function (document, window) {

    const hasAdvancedUpload = (function () {
        const div = document.createElement("div");
        return (
            ("draggable" in div || ("ondragstart" in div && "ondrop" in div)) &&
            "FormData" in window &&
            "FileReader" in window
        );
    })();

    const form = document.querySelector(".box");
    const input = form.querySelector('input[type="file"]');
    const filename = form.querySelector(".box_uploading span");
    const errorMsg = form.querySelector(".box_error span");
    const restart = form.querySelector(".box_restart");
    const progressBar = document.querySelector(".progress-bar");
    let droppedFile = false;

    // automatically submit the form on file select
    input.addEventListener("change", function (e) {
        updateFilename(e.target.files[0].name);
        submitForm();
    });

    // drag and drop files if the feature is available
    if (hasAdvancedUpload) {
        form.classList.add("has_advanced_upload");
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
            droppedFile = e.dataTransfer.files[0].name;
            updateFilename(droppedFile);
            submitForm();
        });
    }

    // if the form was submitted
    form.addEventListener("submit", function (e) {

        // preventing the duplicate submissions if the current one is in progress
        if (form.classList.contains("is_uploading")) return false;

        uploadStart();

        // ajax file upload for modern browsers
        if (hasAdvancedUpload) {
            e.preventDefault();
            const formData = new FormData(form);

            if (droppedFile) {
                formData.append(input.getAttribute("name"), droppedFile);
            }

            const request = new XMLHttpRequest();
            request.open(
                form.getAttribute("method"),
                form.getAttribute("action"),
                true
            );
            request.upload.addEventListener(
                "progress",
                function (e) {
                    updateProgress(e);
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

            setTimeout(() => {
                request.send(formData);
            }, 1500);

        } else {
            // fallback Ajax solution upload for older browsers
            let iframeName = "uploadiframe" + new Date().getTime()
            let iframe = document.createElement("iframe");

            iframe.setAttribute("name", iframeName);
            iframe.style.display = "none";

            document.body.appendChild(iframe);
            form.setAttribute("target", iframeName);

            iframe.addEventListener("load", function () {
                let data = JSON.parse(iframe.contentDocument.body.innerHTML);
                form.classList.remove("is_uploading");
                form.classList.add(data === true ? "is_success" : "is_error");
                form.removeAttribute("target");
                if (!data) errorMsg.textContent = data;
                iframe.parentNode.removeChild(iframe);
            });
        }
    });

    function updateFilename(name) {
        filename.textContent = name;
    }

    function submitForm() {
        const event = document.createEvent("HTMLEvents");
        event.initEvent("submit", true, false);
        form.dispatchEvent(event);
    }

    function updateProgress(e) {
        if (e.lengthComputable) {
            const progress = (e.loaded / e.total) * 100;
            progressBar.setAttribute("aria-valuenow", progress.toString());
            progressBar.setAttribute("style", "width:" + progress + "%");
            if (progress === 100) uploadComplete();
        }
    }

    function handleSuccess(data) {
        slicingComplete(data);
        if (data.success === "true") {
            setTimeout(function () {
                window.location.href = "/object/" + data.id;
                clearPage();
            }, 1500);
        }
    }

    function handleError(errorMessage) {
        form.classList.remove("is_uploading");
        form.classList.remove("is_slicing");
        form.classList.add("is_error");
        errorMsg.textContent = errorMessage;
    }

    function uploadStart() {
        form.classList.add("is_uploading");
        form.classList.remove("is_error");
    }

    function slicingComplete(data) {
        form.classList.remove("is_slicing");
        if (data.success === "true") {
            form.classList.add("is_success");
        } else {
            form.classList.add("is_error");
            errorMsg.textContent = "error creating object";
        }
    }

    function uploadComplete() {
        form.classList.remove("is_uploading");
        form.classList.add("is_slicing");
    }

    function clearPage() {
        form.reset();
        droppedFile = false;
    }

    restart.addEventListener("click", function (e) {
        e.preventDefault();
        form.classList.remove("is_error", "is_success");
    });

    // Firefox focus bug fix for file input
    input.addEventListener("focus", function () {
        input.classList.add("has_focus");
    });
    input.addEventListener("blur", function () {
        input.classList.remove("has_focus");
    });

})(document, window);
