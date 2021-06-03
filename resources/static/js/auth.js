document.addEventListener('DOMContentLoaded', function () {
    "use strict";

    $("#login_button").click(function (e) {
        e.preventDefault();
        $("#auth_form").submit();
    });

    $("#auth_form").submit(function (e) {
        e.preventDefault();

        const url = $(this).attr("action");
        const input = $(this).find(".input");
        const message = $(this).find(".form_message");

        const check = checkValidation(input);
        if (check) {
            $.post(url, $(this).serialize(), function (data) {
                if (data.startsWith("/")) {
                    window.location.href = data;
                } else {
                    message.text(data);
                }
            });
        }
    });
});
