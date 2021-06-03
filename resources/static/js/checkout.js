document.addEventListener('DOMContentLoaded', function () {
    "use strict";

    calculateTotal();

    /**
     * Calculate total
     */
    function calculateTotal() {
        const items = $(".object_list");
        let total = 0;
        items.each(function (index, element) {
            const price = $(this)
                .find(".order_list_value span")
                .text()
                .replace(",", "");
            const quantity = $(this).find(".order_list_quantity span").text();

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

        const url = $(this).attr("action");
        const input = $(this).find(".input");

        const check = checkValidation(input);
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
