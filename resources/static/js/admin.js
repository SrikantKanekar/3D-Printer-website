$(function () {
    "use strict";

    $(".btn-group").on("click", ".btn", function () {

        var orderId = $(this).data("id");
        var status = $(this).data("status");

        $.post(
            "/admin/update/order-status",
            {
                id: orderId,
                order_status: status
            },
            function (data) {
                if (data == "updated") {
                    alert(data);
                }
            }
        );
    });

});