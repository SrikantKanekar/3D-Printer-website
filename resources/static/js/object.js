document.addEventListener('DOMContentLoaded', function () {
    "use strict";

    const object = $(".object")
    const id = object.data("id");
    const status = object.data("status");
    const inputs = $("input");

    // status
    const statusNone = document.querySelector(".status_none");
    const statusTracking = document.querySelector(".status_tracking");
    const statusCompleted = document.querySelector(".status_completed");

    /**
     * Handle Status
     */
    if (document.body.contains(statusNone)) {

        const cartButtons = document.querySelector(".cart_buttons");
        const removeButton = document.querySelector(".remove_cart_button");
        const deleteButton = document.querySelector(".delete_object");

        if (status === "NONE") {
            addedToCart(false);
        } else if (status === "CART") {
            addedToCart(true);
        }

        function addedToCart(boolean) {
            if (boolean) {
                cartButtons.style.display = "none";
                removeButton.style.display = "block";
                deleteButton.style.display = "none";
                disableSettings();
            } else {
                cartButtons.style.display = "block";
                removeButton.style.display = "none";
                deleteButton.style.display = "block";
                enableSettings();
            }
        }

        /**
         *  Slicing
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
                        addedToCart(true);
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
                    addedToCart(false);
                    showAlert("Done", "alert-success");
                } else {
                    showAlert("error, please try again", "alert-danger");
                }
            });
        });

        /**
         *  Delete Object
         */
        $(".delete_button a").on('click', function (e) {
            e.preventDefault();
            const url = "/object/delete";
            $.post(url, {id: id}, function (data) {
                if (data === true) {
                    window.location.href = "/objects";
                    showAlert("Deleted Object", "alert-success");
                } else {
                    showAlert("error in deleting object", "alert-danger");
                }
            });
        });

        /**
         * Settings
         */
        // set dropdown values
        $(".dropdown_item_select").each(function () {
            this.value = this.getAttribute("data-value");
        });

        $("#basic_button").on('click', function (e) {
            e.preventDefault();
            const form = document.querySelector("#basic_settings_form");
            submitSettingForm(form);
        });

        $("#intermediate_button").on('click', function (e) {
            e.preventDefault();
            const form = document.querySelector("#intermediate_settings_form");
            submitSettingForm(form);
        });

        $("#advanced_button").on('click', function (e) {
            e.preventDefault();
            const form = document.querySelector("#advanced_settings_form");
            submitSettingForm(form);
        });

        function submitSettingForm(form) {
            const url = form.getAttribute("action");
            const inputs = form.querySelectorAll(".input");
            const message = form.querySelector(".form_message");

            const check = checkValidation(inputs);
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
            } else {
                showAlert("Form has errors", "alert-danger");
            }
        }

        function enableSettings() {

            inputs.each(function () {
                this.readOnly = false;
            });
            inputs.off('click');
            $("select").each(function () {
                $(this).find("option").each(function () {
                    this.disabled = false;
                });
            });
            $("input[type=checkbox]").off('click');
            $(".form_submit_button").show();
        }

        function disableSettings() {
            inputs.each(function () {
                this.readOnly = true;
            });
            inputs.on('click', function () {
                showAlert("Settings can't be changed", "alert-danger");
            });
            $("select").each(function () {
                const value = this.value;
                $(this).find("option").each(function () {
                    if (this.value !== value) {
                        this.disabled = true;
                    }
                });
            });
            $("input[type=checkbox]").on('click', function () {
                return false;
            });
            $(".form_submit_button").hide();
        }

    } else if (document.body.contains(statusTracking)) {
        // Todo
    } else if (document.body.contains(statusCompleted)) {
        // Todo
    }
});