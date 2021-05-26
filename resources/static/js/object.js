$(document).ready(function () {
    "use strict";

    initImage();
    initQuantity();
    handleStatus();

    /**
     * 1. handle Status
     */
    function handleStatus() {
        var status = $(".object").data("status");
        if (status == "NONE" || status == "CART") {
            $(".status_none").show();
            if (status == "NONE") {
                $(".product_quantity_container").show();
            } else if (status == "CART") {
                $(".cart_remove_button_container").show();
            }
        } else if (status == "TRACKING") {
            $(".status_tracking").show();
        } else if (status == "COMPLETED") {
            $(".status_completed").show();
        }
    }

    /**
     *  2. Init Image
     */
    function initImage() {
        var images = $(".details_image_thumbnail");
        var selected = $(".details_image_large img");

        images.each(function () {
            var image = $(this);
            image.on("click", function () {
                var imagePath = new String(image.data("image"));
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
        // Handle product quantity input
        if ($(".product_quantity").length) {
            var input = $("#quantity_input");
            var incButton = $("#quantity_inc_button");
            var decButton = $("#quantity_dec_button");

            var originalVal;
            var endVal;

            incButton.on("click", function () {
                originalVal = input.val();
                endVal = parseFloat(originalVal) + 1;
                input.val(endVal);
            });

            decButton.on("click", function () {
                originalVal = input.val();
                if (originalVal > 0) {
                    endVal = parseFloat(originalVal) - 1;
                    input.val(endVal);
                }
            });
        }
    }

    /**
     *  4. add to cart
     */
    $(".cart_button a").click(function (e) {
        e.preventDefault();

        var product = $(this).parents(".object");
        var id = product.data("id");
        var url = $(this).attr("href");

        $.post(url, { id: id }, function (data) {
            if (data.startsWith("/")) {
                window.location.href = data;
            } else if (data == "true") {
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

        var product = $(this).parents(".object");
        var id = product.data("id");
        var url = $(this).attr("href");

        $.post(url, { id: id }, function (data) {
            if (data == true) {
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
        
        var url = $(this).attr("action");
        var input = $(this).find(".input");
        var message = $(this).find(".form_message");
        
        var check = checkValidation(input);
        if (check) {
            $.post(url, $(this).serialize(), function (data) {
                if (data == true) {
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
        
        var url = $(this).attr("action");
        var input = $(this).find(".input");
        var message = $(this).find(".form_message");

        var check = checkValidation(input);
        if (check) {
            $.post(url, $(this).serialize(), function (data) {
                if (data == true) {
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
