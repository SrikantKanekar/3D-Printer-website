$(document).ready(function () {
    "use strict";

    $(".order_button").click(function (e) {
        e.preventDefault();

        if ($("form")[0].checkValidity()) {
            console.log("submitting");
            $("#checkout_form").submit();
        }

        $('form :input[required="required"]').each(function () {
            if (!this.validity.valid) {
                $(this).focus();
                // break
                return false;
            }
        });
    });

    $("#checkout_form").submit(function (e) {
        e.preventDefault();
        console.log("submitted");
        var url = $(this).attr("action");
        $.post(url, $(this).serialize(), function (data) {
            if (data.startsWith("/")) {
                window.location.href = data;
            } else {
                alert(data);
            }
        });
    });
});
