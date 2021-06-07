window.addEventListener('load', function () {
    "use strict";

    const grid = $(".product_grid").isotope({
        itemSelector: ".product",
        layoutMode: "fitRows",
        fitRows: {
            gutter: 30,
        },
        getSortData: {
            price: function (itemElement) {
                const price = itemElement
                    .querySelector(".product_price span")
                    .textContent;
                return parseInt(price, 10);
            },
            name: function (itemElement) {
                return itemElement
                    .querySelector(".product_title")
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

    /**
     add to cart
     */
    const result = document.querySelector(".results span");

    $(".product_add_to_cart a").on("click",function (e) {
        e.preventDefault();

        const product = this.closest(".product");
        const id = product.getAttribute("data-id");
        const url = this.getAttribute("href");

        $.post(url, {id: id}, function (data) {
            if (data.startsWith("/")) {
                window.location.href = data;
            } else if (data === "true") {

                // remove product
                grid.isotope("remove", product).isotope("layout");

                // update count
                const count = parseInt(result.textContent, 10) - 1;
                result.textContent = count.toString();

                // hide if no objects present
                if (count === 0) {
                    const sorting = document.querySelector(".sorting_container");
                    sorting.style.display = "none";
                }
            } else {
                showAlert(
                    "error, please try again",
                    "alert-danger"
                );
            }
        });
    });
});
