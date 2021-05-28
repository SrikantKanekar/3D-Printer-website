$(document).ready(function () {
    "use strict";

    calculateTotal();

    /**
     * Calculate total
     */
    function calculateTotal() {
        var items = $(".object_list");
        var total = 0;
        items.each(function (index, element) {
            var price = $(this)
                .find(".order_list_value span")
                .text()
                .replace(",", "");
            var quantity = $(this).find(".order_list_quantity span").text();

            total += parseInt(price) * parseInt(quantity);
        });
        total = total.toLocaleString();
        $(".subtotal span").text(total);
        $(".total span").text(total);
    }

    $(".order_button").click(function (e) {
        e.preventDefault();
        $("#checkout_form").submit();
    });

    $("#checkout_form").submit(function (e) {
        e.preventDefault();

        var url = $(this).attr("action");
        var input = $(this).find(".input");

        var check = checkValidation(input);
        if (check) {
            $.post(url, $(this).serialize(), function (data) {
                if (data.startsWith("/")) {
                    window.location.href = data;
                } else {
                    showAlert(data, "alert-danger");
                }
            });
        }
    });
});
