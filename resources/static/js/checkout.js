(function ($) {
    "use strict";

    $("#checkout-form").submit(function (e) {
        e.preventDefault();
        var url = $(this).attr('action');
        $.post(
            url,
            $(this).serialize(),
            function (data) {

                if (data.startsWith("/")) {
                    window.location.href = data;
                } else {
                    alert(data);
                }
            }
        );
    });

})(jQuery);