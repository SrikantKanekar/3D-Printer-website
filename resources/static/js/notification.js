window.addEventListener('load', function () {
    "use strict";

    const grid = $(".notification_grid").isotope({
        itemSelector: ".notification",
        layoutMode: "fitRows",
        fitRows: {
            gutter: 30,
        },
        getSortData: {
            date: function (itemElement) {
                return itemElement
                    .querySelector(".notification_date")
                    .textContent
                    .toUpperCase();
            },
            name: function (itemElement) {
                return itemElement
                    .querySelector(".notification_title")
                    .textContent
                    .toUpperCase();
            },
        },
        animationOptions: {
            duration: 750,
            easing: "linear",
            queue: false,
        },
    });

    const sorting_text = document.querySelector(".sorting_text");
    $(".product_sorting_btn").each(function () {
        $(this).on("click", function () {
            sorting_text.textContent = this.textContent;
            let option = this.getAttribute("data-isotope-option");
            option = JSON.parse(option);
            grid.isotope(option);
        });
    });
});
