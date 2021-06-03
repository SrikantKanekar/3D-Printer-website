document.addEventListener('DOMContentLoaded', function () {
    "use strict";

    initQuantity();
    calculateTotal();

    /**
     * Cart Grid and Total grid
     */
    const cart_grid = $(".cart_grid").isotope({
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

    const total_grid = $(".total_grid").isotope({
        itemSelector: ".cart_total_item",
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
    $(".cart_item_remove a").click(function (e) {
        e.preventDefault();

        const cart_item = $(this).parents(".cart_item");
        const id = cart_item.data("id");
        const cart_total_item = $(".total_grid").find(`[data-id='${id}']`);
        const url = $(this).attr("href");

        $.post(url, {id: id}, function (data) {
            if (data === true) {
                // remove cart item
                cart_grid.isotope("remove", cart_item).isotope("layout");

                // remove cart total item
                total_grid.isotope("remove", cart_total_item).isotope("layout");
            } else {
                showAlert("error, please try again", "alert-danger");
            }
        });
    });

    cart_grid.on("removeComplete", function () {
        const item_count = $(".cart_item").length;
        if (item_count === 0) {
            $(".cart").hide();
        }
    });

    total_grid.on("removeComplete", function () {
        calculateTotal();
    });

    /**
     * Calculate total
     */
    function calculateTotal() {
        const items = $(".total_grid .cart_total_item");
        let total = 0;
        items.each(function (index, element) {
            const price = $(this)
                .find(".cart_total_value span")
                .text()
                .replace(",", "");
            const id = $(this).data("id");
            const quantity = $(".cart_grid")
                .find(`[data-id='${id}']`)
                .find(".quantity_input")
                .val();
            $(this).find(".cart_total_quantity span").text(quantity);
            total += parseInt(price) * quantity;
        });
        total = total.toLocaleString();
        $(".subtotal span").text(total);
        $(".total span").text(total);
    }

    /**
     * Quantity
     */
    function initQuantity() {
        const url = "cart/quantity";

        $(".quantity_inc").click(function (e) {
            e.preventDefault();

            const id = $(this).parents(".cart_item").data("id");
            const input = $(this)
                .parents(".product_quantity")
                .find(".quantity_input");
            console.log(id);

            const originalVal = input.val();
            const endVal = parseFloat(originalVal) + 1;

            $.post(url, {id: id, quantity: endVal}, function (data) {
                if (data === true) {
                    input.val(endVal);
                    calculateTotal();
                } else {
                    showAlert("unknown error", "alert-danger");
                }
            });
        });

        $(".quantity_dec").click(function (e) {
            e.preventDefault();

            const id = $(this).parents(".cart_item").data("id");
            const input = $(this)
                .parents(".product_quantity")
                .find(".quantity_input");

            const originalVal = input.val();
            if (originalVal > 1) {
                const endVal = parseFloat(originalVal) - 1;
                $.post(url, {id: id, quantity: endVal}, function (data) {
                    if (data === true) {
                        input.val(endVal);
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
    $(".clear_cart_button").click(function (e) {
        e.preventDefault();

        const url = "/cart/clear";

        $.post(url, {}, function (data) {
            if (data === true) {
                $(".cart").hide();
                showAlert("Cleared cart", "alert-success");
            } else {
                showAlert("unknown error", "alert-danger");
            }
        });
    });

    /**
     * Coupon form
     */
    $(".coupon_button").click(function (e) {
        e.preventDefault();
        $("#coupon_form").submit();
    });

    $("#coupon_form").submit(function (e) {
        e.preventDefault();
        showAlert("Invalid coupon ID", "alert-danger");
    });
});
