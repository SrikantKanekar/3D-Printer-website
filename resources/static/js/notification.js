window.addEventListener('load', function () {
    "use strict";

    const sortingButtons = $(".product_sorting_btn");

    const grid = $(".notification_grid").isotope({
        itemSelector: ".notification",
        layoutMode: "fitRows",
        fitRows: {
            gutter: 30,
        },
        getSortData: {
            date: function (itemElement) {
                return $(itemElement)
                    .find(".notification_date")
                    .text()
                    .toUpperCase();
            },
            name: function (itemElement) {
                return $(itemElement)
                    .find(".notification_title")
                    .text()
                    .toUpperCase();
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
            const parent = $(this).parent().parent().find(".sorting_text");
            parent.text($(this).text());
            let option = $(this).attr("data-isotope-option");
            option = JSON.parse(option);
            grid.isotope(option);
        });
    });
});
