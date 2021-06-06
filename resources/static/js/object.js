document.addEventListener('DOMContentLoaded', function () {
    "use strict";

    const object = $(".object")
    const id = object.data("id");
    const status = object.data("status");

    handleStatus();
    initImage();
    initQuantity();

    /**
     * Handle Status
     */
    function handleStatus() {
        if (status === "NONE" || status === "CART") {
            $(".status_none").show();
            if (status === "NONE") {
                $(".cart_buttons_container").show();
            } else if (status === "CART") {
                $(".remove_cart_button_container").show();
            }
        } else if (status === "TRACKING") {
            $(".status_tracking").show();
        } else if (status === "COMPLETED") {
            $(".status_completed").show();
        }
    }

    /**
     *  Init Image
     */
    function initImage() {
        const selected = $(".image_selected img");
        const images = $(".thumbnail_image");

        images.each(function () {
            const image = $(this);
            image.on("click", function () {
                const imagePath = String(image.data("image"));
                selected.attr("src", imagePath);
                images.removeClass("active");
                image.addClass("active");
            });
        });
    }

    /**
     *  Init Quantity
     */
    function initQuantity() {
        const quantity = document.querySelector(".quantity");
        const input = quantity.querySelector("input");
        const incButton = quantity.querySelector(".quantity_inc");
        const decButton = quantity.querySelector(".quantity_dec");

        const url = "/object/quantity";

        let originalVal;
        let newValue;

        incButton.addEventListener("click", function () {

            originalVal = input.value;
            newValue = parseFloat(originalVal) + 1;

            $.post(url, {id: id, quantity: newValue}, function (data) {
                if (data === true) {
                    input.value = newValue;
                } else {
                    showAlert("unknown error", "alert-danger");
                }
            });
        });

        decButton.addEventListener("click", function () {
            originalVal = input.value;
            if (originalVal > 1) {
                newValue = parseFloat(originalVal) - 1;
                $.post(url, {id: id, quantity: newValue}, function (data) {
                    if (data === true) {
                        input.value = newValue;
                    } else {
                        showAlert("unknown error", "alert-danger");
                    }
                });
            }
        });
    }

    /**
     *  Add to cart
     */
    $(".cart_button a").on('click', function (e) {
        e.preventDefault();

        const url = $(this).attr("href");

        $.post(url, {id: id}, function (data) {
            if (data.startsWith("/")) {
                window.location.href = data;
            } else if (data === "true") {
                $(".cart_buttons_container").hide();
                $(".remove_cart_button_container").show();
                showAlert("Done", "alert-success");
            } else {
                showAlert("error, please try again", "alert-danger");
            }
        });
    });

    /**
     *  Remove from cart
     */
    $(".cart_remove_button a").on('click', function (e) {
        e.preventDefault();

        const url = $(this).attr("href");

        $.post(url, {id: id}, function (data) {
            if (data === true) {
                $(".remove_cart_button_container").hide();
                $(".cart_buttons_container").show();
                showAlert("Done", "alert-success");
            } else {
                showAlert("error, please try again", "alert-danger");
            }
        });
    });

    /**
     * basic setting form
     */
    $("#basic_button").on('click', function (e) {
        e.preventDefault();
        const form = document.querySelector("#basic_settings_form");
        submitSettingForm(form);
    });

    /**
     * advanced setting form
     */
    $("#advanced_button").on('click', function (e) {
        e.preventDefault();
        const form = document.querySelector("#advanced_settings_form");
        submitSettingForm(form);
    });

    function submitSettingForm(form){
        const url = form.getAttribute("action");
        const inputs = form.querySelectorAll(".input");
        const message = form.querySelector(".form_message");

        const check = checkValidation(inputs);
        if (check) {
            $.post(url, $(form).serialize(), function (data) {
                if (data === true) {
                    message.classList.add("success");
                    message.textContent = "updated";
                } else {
                    message.classList.remove("success");
                    message.textContent = "error";
                }
            });
        }
    }
});
