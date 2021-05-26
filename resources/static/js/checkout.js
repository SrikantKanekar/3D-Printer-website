$(document).ready(function () {
    "use strict";

    $(".order_button").click(function (e) {
        e.preventDefault();
        $("#checkout_form").submit();
    });

    $("#checkout_form").submit(function (e) {
        e.preventDefault();
        
        var url = $(this).attr("action");
        var input = $(this).find(".input");
        
        var check = checkValidation(input);
        if (check) {
            $.post(url, $(this).serialize(), function (data) {
                if (data.startsWith("/")) {
                    window.location.href = data;
                } else {
                    showAlert(data, "alert-danger");
                }
            });
        }
    });
});
