$(document).ready(function () {
    "use strict";

    /**
     * Collapsible
     */
    var coll = document.getElementsByClassName("collapsible");
    var i;

    for (i = 0; i < coll.length; i++) {
        coll[i].addEventListener("click", function () {
            this.classList.toggle("active");
            var content = this.nextElementSibling;
            if (content.style.maxHeight) {
                content.style.maxHeight = null;
            } else {
                content.style.maxHeight = content.scrollHeight + "px";
            }
        });
    }

    /**
     * update form
     */
    $("#update_button").click(function (e) {
        e.preventDefault();
        $("#update_form").submit();
    });

    $("#update_form").submit(function (e) {
        e.preventDefault();

        var url = $(this).attr("action");
        var input = $(this).find(".input");
        var message = $(this).find(".form_message");

        var check = checkValidation(input);
        if (check) {
            $.post(url, $(this).serialize(), function (data) {
                if (data == "updated") {
                    message.addClass("success");
                } else {
                    message.removeClass("success");
                }
                message.text(data);
            });
        }
    });

    /**
     * change password form
     */
    $("#change_password_button").click(function (e) {
        e.preventDefault();
        $("#change_password_form").submit();
    });

    $("#change_password_form").submit(function (e) {
        e.preventDefault();
        var url = $(this).attr("action");
        var input = $(this).find(".input");
        var message = $(this).find(".form_message");

        var check = checkValidation(input);
        if (check) {
            $.post(url, $(this).serialize(), function (data) {
                if (data == "updated") {
                    message.addClass("success");
                } else {
                    message.removeClass("success");
                }
                message.text(data);
            });
        }
    });
});
