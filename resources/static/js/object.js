document.addEventListener('DOMContentLoaded', function () {
    "use strict";

    const id = $(".object").data("id");

    initImage();
    initQuantity();
    handleStatus();

    /**
     * 1. handle Status
     */
    function handleStatus() {
        const status = $(".object").data("status");
        if (status === "NONE" || status === "CART") {
            $(".status_none").show();
            if (status === "NONE") {
                $(".product_quantity_container").show();
            } else if (status === "CART") {
                $(".cart_remove_button_container").show();
            }
        } else if (status === "TRACKING") {
            $(".status_tracking").show();
        } else if (status === "COMPLETED") {
            $(".status_completed").show();
        }
    }

    /**
     *  2. Init Image
     */
    function initImage() {
        const images = $(".details_image_thumbnail");
        const selected = $(".details_image_large img");

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
     *  3. Init Quantity
     */
    function initQuantity() {
        const input = $("#quantity_input");
        const incButton = $("#quantity_inc_button");
        const decButton = $("#quantity_dec_button");

        const url = "/object/quantity";

        let originalVal;
        let endVal;

        incButton.on("click", function () {

            originalVal = input.val();
            endVal = parseFloat(originalVal) + 1;

            $.post(url, {id: id, quantity: endVal}, function (data) {
                if (data === true) {
                    input.val(endVal);
                } else {
                    showAlert("unknown error", "alert-danger");
                }
            });
        });

        decButton.on("click", function () {
            originalVal = input.val();
            if (originalVal > 1) {
                endVal = parseFloat(originalVal) - 1;
                $.post(url, {id: id, quantity: endVal}, function (data) {
                    if (data === true) {
                        input.val(endVal);
                    } else {
                        showAlert("unknown error", "alert-danger");
                    }
                });
            }
        });
    }

    /**
     *  4. add to cart
     */
    $(".cart_button a").click(function (e) {
        e.preventDefault();

        const url = $(this).attr("href");

        $.post(url, {id: id}, function (data) {
            if (data.startsWith("/")) {
                window.location.href = data;
            } else if (data === "true") {
                $(".product_quantity_container").hide();
                $(".cart_remove_button_container").show();
                showAlert("Done", "alert-success");
            } else {
                showAlert("error, please try again", "alert-danger");
            }
        });
    });

    /**
     *  5. remove from cart
     */
    $(".cart_remove_button a").click(function (e) {
        e.preventDefault();

        const url = $(this).attr("href");

        $.post(url, {id: id}, function (data) {
            if (data === true) {
                $(".cart_remove_button_container").hide();
                $(".product_quantity_container").show();
                showAlert("Done", "alert-success");
            } else {
                showAlert("error, please try again", "alert-danger");
            }
        });
    });

    /**
     * basic setting form
     */
    $("#basic_button").click(function (e) {
        e.preventDefault();
        $("#basic_settings_form").submit();
    });

    $("#basic_settings_form").submit(function (e) {
        e.preventDefault();

        const url = $(this).attr("action");
        const input = $(this).find(".input");
        const message = $(this).find(".form_message");

        const check = checkValidation(input);
        if (check) {
            $.post(url, $(this).serialize(), function (data) {
                if (data === true) {
                    message.addClass("success");
                    message.text("updated");
                } else {
                    message.removeClass("success");
                    message.text("error");
                }
            });
        }
    });

    /**
     * advanced setting form
     */
    $("#advanced_button").click(function (e) {
        e.preventDefault();
        $("#advanced_settings_form").submit();
    });

    $("#advanced_settings_form").submit(function (e) {
        e.preventDefault();

        const url = $(this).attr("action");
        const input = $(this).find(".input");
        const message = $(this).find(".form_message");

        const check = checkValidation(input);
        if (check) {
            $.post(url, $(this).serialize(), function (data) {
                if (data === true) {
                    message.addClass("success");
                    message.text("updated");
                } else {
                    message.removeClass("success");
                    message.text("error");
                }
            });
        }
    });
});
