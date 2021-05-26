$(document).ready(function () {
    "use strict";

    initImage();
    initQuantity();
    handleStatus();

    /**
     * handle Status
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

    /* 

	5. Init Image

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

    /* 

	6. Init Quantity

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

    /*
        add to cart
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

    /*
        remove from cart
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
    var basicInput = $("#basic_settings_form .setting_input");

    $(".basic_settings_button").click(function (e) {
        e.preventDefault();
        $("#basic_settings_form").submit();
    });

    $("#basic_settings_form").submit(function (e) {
        e.preventDefault();
        var url = $(this).attr("action");
        var check = checkUpdateValidation();
        if (check) {
            $.post(url, $(this).serialize(), function (data) {
                if (data == true) {
                    $(".basic_setting_form_error").addClass("success");
                    $(".basic_setting_form_error").text("updated");
                } else {
                    $(".basic_setting_form_error").removeClass("success");
                    $(".basic_setting_form_error").text("error");
                }
            });
        }
    });

    basicInput.each(function () {
        $(this).focus(function () {
            hideValidate(this);
        });
    });

    function checkUpdateValidation() {
        var check = true;
        for (var i = 0; i < basicInput.length; i++) {
            if (validate(basicInput[i]) == false) {
                showValidate(basicInput[i]);
                check = false;
            }
        }
        return check;
    }

    /**
     * advanced setting form
     */
    var advancedInput = $("#advanced_settings_form .setting_input");

    $(".advanced_settings_button").click(function (e) {
        e.preventDefault();
        $("#advanced_settings_form").submit();
    });

    $("#advanced_settings_form").submit(function (e) {
        e.preventDefault();
        var url = $(this).attr("action");
        var check = checkValidation();
        if (check) {
            $.post(url, $(this).serialize(), function (data) {
                if (data == true) {
                    $(".advanced_setting_form_error").addClass("success");
                    $(".advanced_setting_form_error").text("updated");
                } else {
                    $(".advanced_setting_form_error").removeClass("success");
                    $(".advanced_setting_form_error").text("error");
                }
            });
        }
    });

    advancedInput.each(function () {
        $(this).focus(function () {
            hideValidate(this);
        });
    });

    function checkValidation() {
        var check = true;
        for (var i = 0; i < advancedInput.length; i++) {
            if (validate(advancedInput[i]) == false) {
                showValidate(advancedInput[i]);
                check = false;
            }
        }
        return check;
    }

    function validate(input) {
        if ($(input).val().trim() == "") {
            return false;
        }
    }

    function showValidate(input) {
        var authField = $(input).parent();
        $(authField).addClass("alert-validate");
    }

    function hideValidate(input) {
        var authField = $(input).parent();
        $(authField).removeClass("alert-validate");
    }

    function showAlert(text, alertClass) {
        var button =
            '<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>';
        $(".alert").text(text);
        $(".alert").append(button);
        $(".alert").addClass(alertClass);
        $(".alert")
            .fadeTo(2000, 500)
            .slideUp(500, function () {
                $(".alert").slideUp(500);
                $(".alert").removeClass(alertClass);
            });
    }
});
