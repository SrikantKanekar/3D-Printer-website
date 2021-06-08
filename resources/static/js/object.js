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
     * intermediate setting form
     */
    $("#intermediate_button").on('click', function (e) {
        e.preventDefault();
        const form = document.querySelector("#intermediate_settings_form");
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

    function submitSettingForm(form) {
        const url = form.getAttribute("action");
        const inputs = form.querySelectorAll(".input");
        const message = form.querySelector(".form_message");

        const check = checkSettingValidation(inputs);
        if (check) {
            $.post(url, $(form).serialize() + "&id=" + id, function (data) {
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

    function checkSettingValidation(inputs) {
        let check = true;
        for (let i = 0; i < inputs.length; i++) {
            if (validateSetting(inputs[i]) === false) {
                showValidate(inputs[i]);
                check = false;
            }
        }
        return check;
    }

    function validateSetting(input) {
        let value = input.value.trim();
        if (value === "") {
            return false;
        }
        switch (input.id) {
            //Basic
            case "infill":
                value = parseFloat(input.value);
                if (value < 0 || value > 100) return false;
                break;

            //Intermediate
            case "layer_height":
                value = parseFloat(input.value);
                if (value < 0.1 || value > 0.3) return false;
                break;
            case "infill_density":
                value = parseFloat(input.value);
                if (value < 0 || value > 100) return false;
                break;
            case "support_overhang_angle":
                value = parseFloat(input.value);
                if (value < 0 || value > 89) return false;
                break;
            case "support_density":
                value = parseFloat(input.value);
                if (value < 0 || value > 100) return false;
                break;

            //Advanced
            case "wall_line_width":
            case "top_bottom_line_width":
            case "wall_thickness":
                value = parseFloat(input.value);
                if (value < 0.4 || value > 1.2) return false;
                break;
            case "wall_line_count":
                value = parseFloat(input.value);
                if (value < 2 || value > 8) return false;
                break;
            case "top_thickness":
            case "bottom_thickness":
                value = parseFloat(input.value);
                if (value < 0.8 || value > 2) return false;
                break;
            case "infill_speed":
            case "outer_wall_speed":
            case "inner_wall_speed":
            case "top_bottom_speed":
            case "support_speed":
                value = parseFloat(input.value);
                if (value < 25 || value > 100) return false;
                break;
        }
    }

    // capitalize enum text value of selected
    const quality = document.querySelector("#quality");
    const infillPattern = document.querySelector("#infill_pattern");
    const supportStructure = document.querySelector("#support_structure");
    const supportPlacement = document.querySelector("#support_placement");
    const supportPattern = document.querySelector("#support_pattern");
    const printSequence = document.querySelector("#print_sequence");

    capitalize(quality);
    capitalize(infillPattern);
    capitalize(supportStructure);
    capitalize(supportPlacement);
    capitalize(supportPattern);
    capitalize(printSequence);

    function capitalize(input) {
        const child = input.firstElementChild;
        let text = child.value;
        text = text.replaceAll("_", " ").toLowerCase();
        text = text.replace(/\b\w/g, l => l.toUpperCase());
        child.textContent = text;
    }
});
