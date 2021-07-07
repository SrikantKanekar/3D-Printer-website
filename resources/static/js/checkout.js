window.addEventListener('load', function () {
    "use strict";

    calculateTotal();

    function calculateTotal() {
        const objects = $(".object");
        let total = 0;
        objects.each(function () {
            const price = this
                .querySelector(".list_price span")
                .textContent
                .replace(",", "");
            const quantity = this.querySelector(".list_quantity span").textContent;

            total += parseFloat(price) * parseFloat(quantity);
        });
        total = total.toLocaleString();
        document.querySelector(".subtotal span").textContent = total;
        document.querySelector(".total span").textContent = total;
    }

    $(".order_button").on('click', function (e) {
        e.preventDefault();

        // Update Address
        const form = document.querySelector("#checkout_form");
        const url = form.getAttribute("action");
        const inputs = form.querySelectorAll(".input");

        const check = checkValidation(inputs);
        if (check) {
            $.post(url, $(form).serialize(), function (data) {
                if (data === true) {
                    startPayment();
                } else {
                    showAlert("error updating address", "alert-danger");
                }
            });
        }
    });

    function startPayment() {
        paymentSucceed();
    }

    function paymentSucceed() {
        $.post(
            "/checkout/success",
            {
                success: true
            },
            function (data) {
                if (data === true) {
                    window.location.href = "/orders";
                } else if(data === false) {
                    showAlert("Error creating order", "alert-danger");
                } else {
                    showAlert("Payment is not completed", "alert-danger");
                }
            }
        );
    }
});
