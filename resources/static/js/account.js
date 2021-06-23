window.addEventListener('load', function () {
    "use strict";

    /**
     * Collapsible
     */
    const collapsible = document.querySelectorAll(".collapsible");
    let i;

    for (i = 0; i < collapsible.length; i++) {
        collapsible[i].addEventListener("click", function () {

            this.classList.toggle("active");
            const content = this.nextElementSibling;

            if (content.style.maxHeight) {
                content.style.maxHeight = null;
            } else {
                content.style.maxHeight = content.scrollHeight + "px";
            }
        });
    }

    /**
     * Forms
     */
    const update_button = document.querySelector("#update_button");
    const change_password_button = document.querySelector("#change_password_button");

    update_button.addEventListener('click', function (e) {
        e.preventDefault();
        const update_form = document.querySelector("#update_form");
        submitAccountForm(update_form);
    });

    change_password_button.addEventListener('click', function (e) {
        e.preventDefault();
        const change_password_form = document.querySelector("#change_password_form");
        submitAccountForm(change_password_form);
    });

    function submitAccountForm(form) {
        const url = form.getAttribute("action");
        const inputs = form.querySelectorAll(".input");
        const message = form.querySelector(".form_message");

        const check = checkValidation(inputs);
        if (check) {
            $.post(url, $(form).serialize(), function (data) {
                if (data === "updated") {
                    message.classList.add("success");
                } else {
                    message.classList.remove("success");
                }
                message.textContent = data;
            });
        }
    }
});
