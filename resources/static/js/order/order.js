(function ($) {
    "use strict";

    $("#file-form").submit(function (e) {
        e.preventDefault();
        var url = $(this).attr('action');

        var data = new FormData($(this)[0]);

        $.ajax({
            url: url,
            data: data,
            cache: false,
            contentType: false,
            processData: false,
            type: "POST",
            method: "POST",
            success: function (data) {
                alert(data);
            }
        });
    });

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