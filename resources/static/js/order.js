(function ($) {
    "use strict";

    $(".btn-group").on("click", ".btn", function () {

        var orderId = $(".OrderId").data("id");
        var objectId = $(this).data("id");
        var status = $(this).data("status");

        $.post(
            "/order/update/printing-status",
            {
                orderId: orderId,
                objectId: objectId,
                printing_status: status
            },
            function (data) {
                if (data == "updated") {
                    alert(data);
                }
            }
        );
    });

    $("#notification-form").submit(function (e) {
        e.preventDefault();
        var email = $(".user").data("email");
        var url = $(this).attr('action');
        $.post(
            url,
            {
                email: email,
                title: $("#title").val(),
                message: $("#message").val()
            },
            function (data) {
                alert(data);
            }
        );
    });

})(jQuery);