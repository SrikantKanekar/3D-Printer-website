window.addEventListener('load', function () {
    "use strict";

    $(".product").each(function () {
        handleStatus(this);
    });

    const grid = $(".product_grid").isotope({
        itemSelector: ".product",
        layoutMode: "fitRows",
        fitRows: {
            gutter: 30,
        },
        getSortData: {
            status: function (itemElement) {
                const status = itemElement.getAttribute("data-status");
                if (status === "NONE") {
                    const uptoDate = itemElement.querySelector(".slicing_pending").getAttribute("data-uptoDate");
                    if (uptoDate === "false") return 0; else return 1;
                }
                else if (status === "CART") return 2;
                else if (status === "TRACKING") return 3;
                else if (status === "COMPLETED") return 4;
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
    $(".add_to_cart a").on("click", function (e) {
        e.preventDefault();

        const product = this.closest(".product");
        const id = product.getAttribute("data-id");
        const url = this.getAttribute("href");

        $.post(url, {id: id}, function (data) {
            if (data.startsWith("/")) {
                window.location.href = data;
            } else if (data === "true") {
                product.setAttribute("data-status", "CART");
                product.querySelector(".slicing_done").style.display = "none";
                product.querySelector(".status_cart").style.display = "block";
                grid.isotope("updateSortData").isotope();
            } else {
                showAlert(
                    "error, please try again",
                    "alert-danger"
                );
            }
        });
    });

    function handleStatus(object) {
        const status = $(object).data("status");
        if (status === "NONE") {
            const slicingPending = object.querySelector(".slicing_pending");
            const uptoDate = slicingPending.getAttribute("data-uptoDate");
            if (uptoDate === "false") {
                slicingPending.style.display = "block";
            } else {
                object.querySelector(".slicing_done").style.display = "block";
            }
        } else if (status === "CART") {
            object.querySelector(".status_cart").style.display = "block";
        } else if (status === "TRACKING") {
            object.querySelector(".status_tracking").style.display = "block";
        } else if (status === "COMPLETED") {
            object.querySelector(".status_completed").style.display = "block";
        }
    }
});
