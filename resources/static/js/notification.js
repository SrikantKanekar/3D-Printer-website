$(window).on("load", function (e) {
    "use strict";

    var sortingButtons = $(".product_sorting_btn");

    var grid = $(".notification_grid").isotope({
        itemSelector: ".notification",
        layoutMode: "fitRows",
        fitRows: {
            gutter: 30,
        },
        getSortData: {
            date: function (itemElement) {
                var date = $(itemElement)
                    .find(".notification_date")
                    .text()
                    .toUpperCase();
                return date;
            },
            name: function (itemElement) {
                var name = $(itemElement)
                    .find(".notification_title")
                    .text()
                    .toUpperCase();
                return name;
            },
        },
        animationOptions: {
            duration: 750,
            easing: "linear",
            queue: false,
        },
    });

    // Sort based on the value from the sorting_type dropdown
    sortingButtons.each(function () {
        $(this).on("click", function () {
            var parent = $(this).parent().parent().find(".sorting_text");
            parent.text($(this).text());
            var option = $(this).attr("data-isotope-option");
            option = JSON.parse(option);
            grid.isotope(option);
        });
    });
});
