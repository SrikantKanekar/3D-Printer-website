(function ($) {
    "use strict";

    $(".btn-group").on("click", ".btn", function () {

        var orderId = $(this).data("id");
        var url = $(this).data("href");

        $.post(
            url,
            { id: orderId },
            function (data) {
                if (data == "true") {
                    $(this).addClass("active").siblings().removeClass("active");
                }
            }
        );
    });

})(jQuery);