$(document).ready(function () {
    "use strict";

    initIsotope();

    function initIsotope() {
        var sortingButtons = $(".product_sorting_btn");

        if ($(".product_grid").length) {
            var grid = $(".product_grid").isotope({
                itemSelector: ".product",
                layoutMode: "fitRows",
                fitRows: {
                    gutter: 30,
                },
                getSortData: {
                    price: function (itemElement) {
                        var priceEle = $(itemElement)
                            .find(".product_price")
                            .text()
                            .replace("$", "");
                        return parseFloat(priceEle);
                    },
                    name: function (itemElement) {
                        var nameEle = $(itemElement)
                            .find(".product_name")
                            .text()
                            .toUpperCase();
                        return nameEle;
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
                    var parent = $(this)
                        .parent()
                        .parent()
                        .find(".sorting_text");
                    parent.text($(this).text());
                    var option = $(this).attr("data-isotope-option");
                    option = JSON.parse(option);
                    grid.isotope(option);
                });
            });
        }
    }

    /*
        add to cart
    */
    $(".product_add_to_cart").click(function (e) {
        e.preventDefault();
        var product = $(this).parent().parent().parent().find(".product");
        console.log(product);
        var url = $(this).attr("href");
        console.log(url);
    });
});
