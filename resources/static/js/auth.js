window.addEventListener('load', function () {
    "use strict";

    $("#login_button").on('click', function (e) {
        e.preventDefault();
        const form = document.querySelector("#auth_form");
        const url = form.getAttribute("action");
        const inputs = form.querySelectorAll(".input");
        const message = form.querySelector(".form_message");

        const check = checkValidation(inputs);
        if (check) {
            $.post(url, $(form).serialize(), function (data) {
                if (data.startsWith("/")) {
                    window.location.href = data;
                } else {
                    message.textContent = data;
                }
            });
        }
    });
});
