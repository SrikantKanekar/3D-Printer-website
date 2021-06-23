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

    $(".sorting_button").each(function () {
        $(this).on("click", function () {
            document.querySelector(".sorting_text").textContent = this.textContent;
            let option = this.getAttribute("data-isotope-option");
            option = JSON.parse(option);
            grid.isotope(option);
        });
    });

    const notification = document.querySelector(".notification_grid")

    if (document.body.contains(notification)){
        const dates = $(".notification_date")
        dates.each(function () {
             setDate(this)
        });
    } else {
        // detail screen
        const detailDate = document.querySelector(".notification_detail_date")
        setDate(detailDate)
    }

    function setDate(element) {
        const value = element.getAttribute("data-value");
        let text = value !== '' ? new Date(value).toLocaleString() : "-";
        element.textContent = text;
    }
});
