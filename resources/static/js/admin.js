(function ($) {
    "use strict";

    $(".btn-group").on("click", ".btn", function () {

        var objectId = $(this).data("id");
        var url = $(this).data("href");

        $.post(
            url,
            { id: objectId },
            function (data) {
                if (data == "true") {
                    $(this).addClass("active").siblings().removeClass("active");
                }
            }
        );
    });

})(jQuery);