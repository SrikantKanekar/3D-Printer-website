document.addEventListener('DOMContentLoaded', function () {
    "use strict";

    const object = $(".object")
    const id = object.data("id");
    const status = object.data("status");

    // status
    const statusNone = document.querySelector(".status_none");
    const statusTracking = document.querySelector(".status_tracking");
    const statusCompleted = document.querySelector(".status_completed");

    /**
     * Handle Status
     */
    if (status === "NONE" || status === "CART") {
        statusNone.style.display = "block";
        if (status === "NONE") {
            $(".cart_buttons").show();
        } else if (status === "CART") {
            $(".remove_cart_button").show();
        }
    } else if (status === "TRACKING") {
        statusTracking.style.display = "block";
    } else if (status === "COMPLETED") {
        statusCompleted.style.display = "block";
    }


    /**
     *  Slice
     */
    const uptoDate = statusNone.querySelector(".upto_date");
    const slicingDetails = statusNone.querySelector(".slicing_details");
    setSlicingDetails();

    function setSlicingDetails() {
        let uptoDateValue = uptoDate.getAttribute("data-value");
        if (uptoDateValue === "true") {
            slicingDetails.style.display = "block";
            const children = slicingDetails.children;
            $(children).each(function () {
                let value = $(this).data("value");
                if (value) $(this).find("span").text(value);
            });
        } else {
            uptoDate.style.display = "block";
        }
    }

    $(".slice a").on('click', function (e) {
        e.preventDefault();

        const url = $(this).attr("href");

        $.post(url, {id: id}, function (data) {
            if (data !== "null") {
                showAlert("Slicing Done", "alert-success");
                updateSlicingDetails(data);
            } else {
                showAlert("error, please try again", "alert-danger");
            }
        });
    });

    function updateSlicingDetails(data) {
        slicingDetails.querySelector(".time").setAttribute("data-value", data.time);
        slicingDetails.querySelector(".material_weight").setAttribute("data-value", data.materialWeight);
        slicingDetails.querySelector(".material_cost").setAttribute("data-value", data.materialCost);
        slicingDetails.querySelector(".electricity_cost").setAttribute("data-value", data.electricityCost);
        slicingDetails.querySelector(".total_price").setAttribute("data-value", data.totalPrice);

        uptoDate.setAttribute("data-value", "true");
        uptoDate.style.display = "none";
        slicingDetails.style.display = "none";
        setSlicingDetails();
    }

    function removeSlicingDetails() {
        uptoDate.setAttribute("data-value", "false");
        uptoDate.style.display = "none";
        slicingDetails.style.display = "none";
        setSlicingDetails();
    }

    /**
     *  Add to cart
     */
    $(".add_to_cart a").on('click', function (e) {
        e.preventDefault();

        let uptoDateValue = uptoDate.getAttribute("data-value");
        if (uptoDateValue === "true") {
            const url = $(this).attr("href");
            $.post(url, {id: id}, function (data) {
                if (data.startsWith("/")) {
                    window.location.href = data;
                } else if (data === "true") {
                    $(".cart_buttons").hide();
                    $(".remove_cart_button").show();
                    showAlert("Done", "alert-success");
                } else {
                    showAlert("error, please try again", "alert-danger");
                }
            });
        } else {
            showAlert("Please complete slicing", "alert-danger");
        }
    });

    /**
     *  Remove from cart
     */
    $(".remove_from_cart a").on('click', function (e) {
        e.preventDefault();

        const url = $(this).attr("href");

        $.post(url, {id: id}, function (data) {
            if (data === true) {
                $(".remove_cart_button").hide();
                $(".cart_buttons").show();
                showAlert("Done", "alert-success");
            } else {
                showAlert("error, please try again", "alert-danger");
            }
        });
    });

    /**
     * Settings
     */
    // set dropdown values
    $(".dropdown_item_select").each(function (){
        this.value = this.getAttribute("data-value");
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
                    removeSlicingDetails();
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

    // const infillPattern = document.querySelector("#infill_pattern");
    // const supportStructure = document.querySelector("#support_structure");
    // const supportPlacement = document.querySelector("#support_placement");
    // const supportPattern = document.querySelector("#support_pattern");
    // const printSequence = document.querySelector("#print_sequence");
    //
    // capitalize(infillPattern);
    // capitalize(supportStructure);
    // capitalize(supportPlacement);
    // capitalize(supportPattern);
    // capitalize(printSequence);
    //
    // function capitalize(input) {
    //     const child = input.firstElementChild;
    //     let text = child.value;
    //     text = text.replaceAll("_", " ").toLowerCase();
    //     text = text.replace(/\b\w/g, l => l.toUpperCase());
    //     child.textContent = text;
    // }
});
