$(document).ready(function () {
    "use strict";

    var counter = $(".results span");
    var sorting = $(".sorting_container");
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
                        .find(".product_title")
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
                var parent = $(this).parent().parent().find(".sorting_text");
                parent.text($(this).text());
                var option = $(this).attr("data-isotope-option");
                option = JSON.parse(option);
                grid.isotope(option);
            });
        });

        /*
            add to cart
        */
        $(".product_add_to_cart a").click(function (e) {
            e.preventDefault();

            var product = $(this).parents(".product");
            var id = product.data("id");
            var url = $(this).attr("href");

            $.post(url, { id: id }, function (data) {
                if (data.startsWith("/")) {
                    window.location.href = data;
                } else if (data == "true") {
                    // remove product
                    grid.isotope("remove", product).isotope("layout");

                    // update count
                    var count = parseInt(counter.text(), 10) - 1;
                    counter.text(count);
                    if (count == 0) sorting.hide();
                } else {
                    alert("error, please try again");
                }
            });
        });
    }
});
