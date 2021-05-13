"use strict";

(function (document, window, index) {
    var isAdvancedUpload = (function () {
        var div = document.createElement("div");
        return (
            ("draggable" in div || ("ondragstart" in div && "ondrop" in div)) &&
            "FormData" in window &&
            "FileReader" in window
        );
    })();

    var form = document.querySelector(".box");
    var input = form.querySelector('input[type="file"]');
    var filename = form.querySelector(".box__uploading span");
    var errorMsg = form.querySelector(".box__error span");
    var restart = form.querySelector(".box__restart");
    var droppedFile = false;
    var progressBar = document.querySelector(".progress-bar");

    var showFile = function (name) {
        filename.textContent = name;
    };

    var triggerFormSubmit = function () {
        var event = document.createEvent("HTMLEvents");
        event.initEvent("submit", true, false);
        form.dispatchEvent(event);
    };

    // automatically submit the form on file select
    input.addEventListener("change", function (e) {
        showFile(e.target.files[0].name);
        triggerFormSubmit();
    });

    // drag and drop files if the feature is available
    if (isAdvancedUpload) {
        // letting the CSS part to know drag&drop is supported by the browser
        form.classList.add("has-advanced-upload");
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
                form.classList.add("is-dragover");
            });
        });

        ["dragleave", "dragend", "drop"].forEach(function (event) {
            form.addEventListener(event, function () {
                form.classList.remove("is-dragover");
            });
        });

        form.addEventListener("drop", function (e) {
            droppedFile = e.dataTransfer.files[0].name; // the file that was dropped
            showFile(droppedFile);
            triggerFormSubmit();
        });
    }

    // if the form was submitted
    form.addEventListener("submit", function (e) {
        // preventing the duplicate submissions if the current one is in progress
        if (form.classList.contains("is-uploading")) return false;
        uploadStart();

        if (isAdvancedUpload) {
            // ajax file upload for modern browsers
            e.preventDefault();
            var formData = new FormData(form);

            if (droppedFile) {
                formData.append(input.getAttribute("name"), droppedFile);
            }

            var request = new XMLHttpRequest();
            request.open(
                form.getAttribute("method"),
                form.getAttribute("action"),
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
                if (request.readyState == 4) {
                    if (request.status == 200) {
                        var data = JSON.parse(request.responseText);
                        handleData(data);
                    } else {
                        handleError(request.responseText);
                    }
                }
            };

            setTimeout(() => {
                request.send(formData);
            }, 3000);
        } else {
            // fallback Ajax solution upload for older browsers
            var iframeName = "uploadiframe" + new Date().getTime(),
                iframe = document.createElement("iframe");

            $iframe = $(
                '<iframe name="' +
                    iframeName +
                    '" style="display: none;"></iframe>'
            );

            iframe.setAttribute("name", iframeName);
            iframe.style.display = "none";

            document.body.appendChild(iframe);
            form.setAttribute("target", iframeName);

            iframe.addEventListener("load", function () {
                var data = JSON.parse(iframe.contentDocument.body.innerHTML);
                form.classList.remove("is-uploading");
                form.classList.add(data == true ? "is-success" : "is-error");
                form.removeAttribute("target");
                if (!data) errorMsg.textContent = data;
                iframe.parentNode.removeChild(iframe);
            });
        }
    });

    var handleProgress = function (e) {
        if (e.lengthComputable) {
            var progress = (e.loaded / e.total) * 100;
            progressBar.setAttribute("aria-valuenow", progress);
            progressBar.setAttribute("style", "width:" + progress + "%");
            if (progress == 100) uploadDone();
            console.log(e.loaded + " / " + e.total);
        }
    };

    var handleData = function (data) {
        slicingDone(data);
        if (data.success == "true") {
            setTimeout(function () {
                window.location.href = "/object/" + data.id;
                clearPage();
            }, 2000);
        }
    };

    var uploadStart = function () {
        form.classList.add("is-uploading");
        form.classList.remove("is-error");
    };

    var uploadDone = function () {
        form.classList.remove("is-uploading");
        form.classList.add("is-slicing");
    };

    var slicingDone = function (data) {
        form.classList.remove("is-slicing");
        if (data.success == "true") {
            form.classList.add("is-success");
        } else {
            form.classList.add("is-error");
            errorMsg.textContent = "error creating object";
        }
    };

    var handleError = function (errorMessage) {
        form.classList.remove("is-uploading");
        form.classList.remove("is-slicing");
        form.classList.add("is-error");
        errorMsg.textContent = errorMessage;
    };

    var clearPage = function () {
        form.reset();
        droppedFile = false;
    };

    // restart the form if has a state of error
    restart.addEventListener("click", function (e) {
        e.preventDefault();
        form.classList.remove("is-error", "is-success");
    });

    // Firefox focus bug fix for file input
    input.addEventListener("focus", function () {
        input.classList.add("has-focus");
    });

    input.addEventListener("blur", function () {
        input.classList.remove("has-focus");
    });
})(document, window, 0);
