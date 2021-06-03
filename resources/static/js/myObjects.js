window.addEventListener('load', function () {
    "use strict";

    const counter = $(".results span");
    const sorting = $(".sorting_container");
    const sortingButtons = $(".product_sorting_btn");

    if ($(".product_grid").length) {
        const grid = $(".product_grid").isotope({
            itemSelector: ".product",
            layoutMode: "fitRows",
            fitRows: {
                gutter: 30,
            },
            getSortData: {
                price: function (itemElement) {
                    const priceEle = $(itemElement)
                        .find(".product_price span")
                        .text();
                    return parseInt(priceEle);
                },
                name: function (itemElement) {
                    return $(itemElement)
                        .find(".product_title")
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

        /*
            add to cart
        */
        $(".product_add_to_cart a").click(function (e) {
            e.preventDefault();

            const product = $(this).parents(".product");
            const id = product.data("id");
            const url = $(this).attr("href");

            $.post(url, {id: id}, function (data) {
                if (data.startsWith("/")) {
                    window.location.href = data;
                } else if (data === "true") {
                    // remove product
                    grid.isotope("remove", product).isotope("layout");

                    // update count
                    const count = parseInt(counter.text(), 10) - 1;
                    counter.text(count);
                    if (count === 0) sorting.hide();
                } else {
                    showAlert(
                        "error, please try again",
                        "alert-danger"
                    );
                }
            });
        });
    }
});
