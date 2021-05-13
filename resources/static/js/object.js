(function ($) {
    "use strict";

    $("#basic-form").submit(function (e) {
        e.preventDefault();
        var url = $(this).attr('action');
        $.post(
            url,
            $(this).serialize(),
            function (data, textStatus, jqXHR) {
                $("#basic").text(data.size);
                console.log(data);
            },
            "json"
        );
    });

    $("#advanced-form").submit(function (e) {
        e.preventDefault();
        var url = $(this).attr('action');
        $.post(
            url,
            $(this).serialize(),
            function (data, textStatus, jqXHR) {
                $("#advanced").text(data.size);
                console.log(data);
            },
            "json"
        );
    });

})(jQuery);