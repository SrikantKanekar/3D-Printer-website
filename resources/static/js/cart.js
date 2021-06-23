window.addEventListener('load', function () {
    "use strict";

    const cart = document.querySelector(".cart");
    const cart_grid = document.querySelector(".cart_grid");
    const total_grid = document.querySelector(".total_grid");

    initQuantity();
    calculateTotal();

    /**
     * Cart Grid and Total grid Isotope
     */
    const cart_isotope = $(cart_grid).isotope({
        itemSelector: ".cart_item",
        layoutMode: "fitRows",
        fitRows: {
            gutter: 30,
        },
        animationOptions: {
            duration: 750,
            easing: "linear",
            queue: false,
        },
    });

    const total_isotope = $(total_grid).isotope({
        itemSelector: ".total_item",
        layoutMode: "fitRows",
        fitRows: {
            gutter: 30,
        },
        animationOptions: {
            duration: 750,
            easing: "linear",
            queue: false,
        },
    });

    /**
     * remove cart item
     */
    $(".item_remove a").on('click', function (e) {
        e.preventDefault();

        const cart_item = this.closest(".cart_item");
        const id = cart_item.getAttribute("data-id");
        const url = this.getAttribute("href");

        const total_item = total_grid.querySelector(`[data-id='${id}']`);

        $.post(url, {id: id}, function (data) {
            if (data === true) {
                // remove cart item
                cart_isotope.isotope("remove", $(cart_item)).isotope("layout");

                // remove cart total item
                total_isotope.isotope("remove", $(total_item)).isotope("layout");
            } else {
                showAlert("error, please try again", "alert-danger");
            }
        });
    });

    cart_isotope.on("removeComplete", function () {
        const cart_count = $(".cart_item").length;
        if (cart_count === 0) {
            cart.style.display = 'none';
        }
    });

    total_isotope.on("removeComplete", function () {
        calculateTotal();
    });

    /**
     * Calculate total
     */
    function calculateTotal() {
        const items = total_grid.querySelectorAll(".total_item");
        let total = 0;
        $(items).each(function () {
            const price = this
                .querySelector(".total_value span")
                .textContent
                .replace(",", "");
            const id = this.getAttribute("data-id");
            const quantity = cart_grid
                .querySelector(`[data-id='${id}']`)
                .querySelector("input")
                .value;
            this.querySelector(".total_quantity span").textContent = quantity;
            total += parseFloat(price) * quantity;
        });
        total = total.toLocaleString();
        document.querySelector(".subtotal span").textContent = total;
        document.querySelector(".total span").textContent = total;
    }

    /**
     * Quantity
     */
    function initQuantity() {

        const url = "cart/quantity";

        $(".quantity_inc").on('click', function (e) {
            e.preventDefault();

            const id = this.closest(".cart_item").getAttribute("data-id");
            const input = this.closest(".quantity").querySelector("input");

            const originalValue = input.value;
            const newValue = parseFloat(originalValue) + 1;

            $.post(url, {id: id, quantity: newValue}, function (data) {
                if (data === true) {
                    input.value = newValue.toString();
                    calculateTotal();
                } else {
                    showAlert("unknown error", "alert-danger");
                }
            });
        });

        $(".quantity_dec").on('click', function (e) {
            e.preventDefault();

            const id = this.closest(".cart_item").getAttribute("data-id");
            const input = this.closest(".quantity").querySelector("input");

            const originalValue = input.value;

            if (originalValue > 1) {
                const newValue = parseFloat(originalValue) - 1;
                $.post(url, {id: id, quantity: newValue}, function (data) {
                    if (data === true) {
                        input.value = newValue.toString();
                        calculateTotal();
                    } else {
                        showAlert("unknown error", "alert-danger");
                    }
                });
            }
        });
    }

    /**
     * clear cart
     */
    $(".clear_cart_button").on('click', function (e) {
        e.preventDefault();

        const url = "/cart/clear";

        $.post(url, {}, function (data) {
            if (data === true) {
                cart.style.display = 'none';
                showAlert("Cleared cart", "alert-success");
            } else {
                showAlert("unknown error", "alert-danger");
            }
        });
    });

    /**
     * Coupon form
     */
    $(".coupon_button").on('click', function (e) {
        e.preventDefault();
        //document.querySelector("#coupon_form"); -> submit
        showAlert("Invalid coupon ID", "alert-danger");
    });
});
