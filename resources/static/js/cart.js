$(document).ready(function () {
    "use strict";

    initQuantity();
    calculateTotal();

    /**
     * Cart Grid and Total grid
     */
    var cart_grid = $(".cart_grid").isotope({
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

    var total_grid = $(".total_grid").isotope({
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

        var cart_item = $(this).parents(".cart_item");
        var id = cart_item.data("id");
        var cart_total_item = $(".total_grid").find(`[data-id='${id}']`);
        var url = $(this).attr("href");

        $.post(url, { id: id }, function (data) {
            if (data == true) {
                // remove cart item
                cart_grid.isotope("remove", cart_item).isotope("layout");

                // remove cart total item
                total_grid.isotope("remove", cart_total_item).isotope("layout");
            } else {
                showAlert("error, please try again", "alert-danger");
            }
        });
    });

    cart_grid.on("removeComplete", function (evevt, removedItems) {
        var item_count = $(".cart_item").length;
        if (item_count == 0) {
            $(".cart").hide();
        }
    });

    total_grid.on("removeComplete", function (evevt, removedItems) {
        calculateTotal();
    });

    /**
     * Calculate total
     */
    function calculateTotal() {
        var items = $(".total_grid .cart_total_item");
        var total = 0;
        items.each(function (index, element) {
            var price = $(this)
                .find(".cart_total_value span")
                .text()
                .replace(",", "");
            var id = $(this).data("id");
            var quantity = $(".cart_grid")
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
        var url = "cart/quantity";

        $(".quantity_inc").click(function (e) {
            e.preventDefault();

            var id = $(this).parents(".cart_item").data("id");
            var input = $(this)
                .parents(".product_quantity")
                .find(".quantity_input");
            console.log(id);

            var originalVal = input.val();
            var endVal = parseFloat(originalVal) + 1;

            $.post(url, { id: id, quantity: endVal }, function (data) {
                if (data == true) {
                    input.val(endVal);
                    calculateTotal();
                } else {
                    showAlert("unknown error", "alert-danger");
                }
            });
        });

        $(".quantity_dec").click(function (e) {
            e.preventDefault();

            var id = $(this).parents(".cart_item").data("id");
            var input = $(this)
                .parents(".product_quantity")
                .find(".quantity_input");

            var originalVal = input.val();
            if (originalVal > 1) {
                var endVal = parseFloat(originalVal) - 1;
                $.post(url, { id: id, quantity: endVal }, function (data) {
                    if (data == true) {
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

        var url = "/cart/clear";

        $.post(url, {}, function (data) {
            if (data == true) {
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
