$(document).ready(function () {
    "use strict";

    /**
     * Form
     */ 
    $("#login_button").click(function (e) {
        e.preventDefault();
        $("#auth_form").submit();
    });

    $("#auth_form").submit(function (e) {
        e.preventDefault();

        var url = $(this).attr("action");
        var input = $(this).find(".input");
        var message = $(this).find(".form_message");

        var check = checkValidation(input);
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
