window.addEventListener('load', function () {
    "use strict";

    const object = $(".object")
    const id = object.data("id");
    const status = object.data("status");
    const fileUrl = object.data("file");
    const fileExtension = object.data("extension").toLowerCase();
    const inputs = $("input");

    // status
    const statusNone = document.querySelector(".status_none");
    const statusTracking = document.querySelector(".status_tracking");
    const statusCompleted = document.querySelector(".status_completed");

    showModel(fileUrl, fileExtension,
        function () {
            //Nothing
        }, function () {
            //Nothing
        }
    );

    /**
     * Handle Status
     */
    if (document.body.contains(statusNone)) {

        const sliceButton = document.querySelector("#slice_button");
        const cartButton = document.querySelector("#cart_button");
        const removeCartButton = document.querySelector("#remove_cart_button");
        const deleteButton = document.querySelector(".delete_object");

        const slicingPending = statusNone.querySelector(".slicing_pending_text");
        const slicingDetails = statusNone.querySelector(".slicing_details");

        const settingMessage = document.querySelector(".form_message");

        if (status === "NONE") {
            let slicingDone = slicingPending.getAttribute("data-value");
            if (slicingDone === "true") {
                setSlicingDetails();
                showCartButton();
            } else {
                showSliceButton()
            }
        } else if (status === "CART") {
            setSlicingDetails();
            showRemoveCartButton();
        }

        function showSliceButton() {
            hideAllButtons();
            sliceButton.style.display = "block";
            deleteButton.style.display = "block";
        }

        function showCartButton() {
            hideAllButtons();
            cartButton.style.display = "block";
            deleteButton.style.display = "block";
            enableSettings();
        }

        function showRemoveCartButton() {
            hideAllButtons();
            removeCartButton.style.display = "block";
            disableSettings();
        }

        function hideAllButtons() {
            sliceButton.style.display = "none"
            cartButton.style.display = "none";
            removeCartButton.style.display = "none";
            deleteButton.style.display = "none";
        }

        /**
         *  Slicing
         */
        function setSlicingDetails() {
            const children = slicingDetails.children;
            $(children).each(function () {
                let value = this.getAttribute("data-value");
                if (this.className === "time") {
                    value = millisToTime(parseFloat(value))
                }
                if (value) $(this).find("span").text(value);
            });
            showSlicingDetails();
        }

        function updateSlicingDetails(data) {
            slicingDetails.querySelector(".time span").textContent = millisToTime(parseFloat(data.time));
            slicingDetails.querySelector(".material_weight span").textContent = data.materialWeight;
            slicingDetails.querySelector(".material_cost span").textContent = data.materialCost;
            slicingDetails.querySelector(".electricity_cost span").textContent = data.electricityCost;
            slicingDetails.querySelector(".total_price span").textContent = data.totalPrice;

            showCartButton();
            showSlicingDetails();
        }

        function removeSlicingDetails() {
            hideSlicingDetails()
            showSliceButton();
        }

        function showSlicingDetails() {
            slicingDetails.style.display = "block";
            slicingPending.style.display = "none";
        }

        function hideSlicingDetails() {
            slicingDetails.style.display = "none";
            slicingPending.style.display = "block";
        }

        $("#slice_button a").on('click', function (e) {
            e.preventDefault();

            hideSettingMessage();
            const url = $(this).attr("href");

            $.post(url, {id: id}, function (data) {
                if (data !== "null") {
                    showAlert("Slicing Done", "alert-success");
                    updateSlicingDetails(data);
                } else {
                    showAlert("Error", "alert-danger");
                }
            });
        });

        /**
         *  Add to cart
         */
        $("#cart_button a").on('click', function (e) {
            e.preventDefault();

            const url = $(this).attr("href");
            $.post(url, {id: id}, function (data) {
                if (data.startsWith("/")) {
                    window.location.href = data;
                } else if (data === "true") {
                    showRemoveCartButton();
                    showAlert("Added to Cart", "alert-success");
                } else {
                    showAlert("Error", "alert-danger");
                }
            });
        });

        /**
         *  Remove from cart
         */
        $("#remove_cart_button a").on('click', function (e) {
            e.preventDefault();

            const url = $(this).attr("href");
            $.post(url, {id: id}, function (data) {
                if (data === true) {
                    showCartButton();
                    showAlert("Removed from Cart", "alert-success");
                } else {
                    showAlert("Error", "alert-danger");
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
                    showAlert("Successfully Deleted", "alert-success");
                    deleteFirebaseFolder(id, function () {
                        window.location.href = "/objects";
                    });
                } else {
                    showAlert("Error", "alert-danger");
                }
            });
        });

        /**
         * Settings
         */
        $("#setting_button").on('click', function (e) {
            e.preventDefault();
            const form = document.querySelector("#setting_form");
            const url = form.getAttribute("action");
            const inputs = form.querySelectorAll(".input");

            const check = checkValidation(inputs);
            if (check) {
                $.post(url, $(form).serialize() + "&id=" + id, function (data) {
                    if (data === true) {
                        settingMessage.classList.add("success");
                        settingMessage.textContent = "successfully updated";
                        removeSlicingDetails();
                    } else {
                        settingMessage.classList.remove("success");
                        settingMessage.textContent = "Error";
                    }
                });
            } else {
                showAlert("Please Correct the Errors", "alert-danger");
            }
        });

        function hideSettingMessage() {
            settingMessage.style.display = "none";
        }

    } else if (document.body.contains(statusTracking)) {
        disableSettings();

        const started_at = document.querySelector(".started_at")
        setTime(started_at)

    } else if (document.body.contains(statusCompleted)) {
        disableSettings();

        const completed_at = document.querySelector(".completed_at")
        setTime(completed_at)

        setDuration()

        function setDuration() {
            const duration = document.querySelector(".duration")
            const started = duration.getAttribute("data-value")
            const completed = completed_at.getAttribute("data-value")
            let difference = Math.abs(new Date(completed) - new Date(started))
            duration.querySelector("span").textContent = millisToTime(difference)
        }
    }

    /**
     * Settings
     */
    const basicContainer = document.querySelector(".basic_container");
    const advancedContainer = document.querySelector(".advanced_container");
    const checkbox = document.querySelector("#advanced");
    if (checkbox.checked) {
        enableAdvancedSetting();
    } else {
        enableBasicSetting();
    }

    checkbox.addEventListener('change', function (e) {
        if (e.currentTarget.checked) {
            enableAdvancedSetting();
        } else {
            enableBasicSetting();
        }
    });

    function enableBasicSetting() {
        advancedContainer.style.display = "none";
        basicContainer.style.display = "block";
    }

    function enableAdvancedSetting() {
        basicContainer.style.display = "none";
        advancedContainer.style.display = "block";
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

    function setTime(element) {
        const value = element.getAttribute("data-value");
        let text = value !== '' ? new Date(value).toLocaleString() : "-";
        element.querySelector("span").textContent = text;
    }

    function millisToTime(duration) {
        let minutes = Math.floor((duration / (1000 * 60)) % 60);
        let hours = Math.floor((duration / (1000 * 60 * 60)) % 24);
        minutes = (minutes < 10) ? "0" + minutes : minutes;
        return hours + ":" + minutes + " hrs";
    }
});