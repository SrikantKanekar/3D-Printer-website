window.addEventListener('load', function () {
    "use strict";

    const grid = $(".product_grid").isotope({
        itemSelector: ".product",
        layoutMode: "fitRows",
        fitRows: {
            gutter: 30,
        },
        getSortData: {
            status: function (itemElement) {
                const statusText = itemElement.querySelector(".product_status").textContent;
                return printingStatusOrdinal(statusText);
            },
            name: function (itemElement) {
                return itemElement
                    .querySelector(".product_title")
                    .textContent
                    .toUpperCase();
            },
        },
        animationOptions: {
            duration: 750,
            easing: "linear",
            queue: false,
        },
    });

    const sorting_text = document.querySelector(".sorting_text");
    $(".product_sorting_btn").each(function () {
        $(this).on("click", function () {
            sorting_text.textContent = this.textContent;
            let option = this.getAttribute("data-isotope-option");
            option = JSON.parse(option);
            grid.isotope(option);
        });
    });

    /**
     admin control buttons
     */
    const button_group = $(".btn-group");

    button_group.each(function (index, element) {
        const product = element.closest(".product");
        const status = printingStatusOrdinal(product.getAttribute("data-status"));

        const printing = element.querySelector(".printing");
        const printed = element.querySelector(".printed");

        if (status === 1) {
            printing.classList.add("disabled");
        }
        if (status === 2) {
            printing.classList.add("disabled");
            printed.classList.add("disabled");
        }
    });

    $(".button.admin").on('click', function (e) {
        e.preventDefault();
        // prevent default behavior
    });

    button_group.on("click", ".button.admin", function () {
        const orderId = $(".results").data("id");

        const button = this;
        const product = button.closest(".product");

        const objectId = product.getAttribute("data-id");
        const currentStatus = printingStatusOrdinal(product.getAttribute("data-status"));
        const buttonStatus = button.getAttribute("data-status");

        if (buttonStatus == currentStatus + 1) {
            $.post(
                "/order/update/printing-status",
                {
                    orderId: orderId,
                    objectId: objectId,
                    printing_status: buttonStatus,
                },
                function (data) {
                    if (data === true) {
                        showAlert(
                            getPrintingStatus(buttonStatus) + " Done",
                            "alert-success"
                        );

                        // disable the button
                        button.classList.add("disabled");

                        // update status and isotope sort order
                        product.setAttribute("data-status", getPrintingStatus(buttonStatus));
                        product
                            .querySelector(".product_status")
                            .textContent = getPrintingStatus(buttonStatus);
                        grid.isotope("updateSortData").isotope();
                    } else {
                        showAlert("please start PROCESSING", "alert-danger");
                    }
                }
            );
        } else {
            if (buttonStatus > currentStatus + 1) {
                showAlert(
                    "Please complete " +
                    getPrintingStatus(currentStatus + 1) +
                    " first",
                    "alert-danger"
                );
            } else {
                showAlert(
                    getPrintingStatus(buttonStatus) + " is already completed",
                    "alert-danger"
                );
            }
        }
    });

    function printingStatusOrdinal(status) {
        if (status == "PENDING") return 0;
        if (status == "PRINTING") return 1;
        if (status == "PRINTED") return 2;
    }

    function getPrintingStatus(status) {
        if (status == 0) return "PENDING";
        if (status == 1) return "PRINTING";
        if (status == 2) return "PRINTED";
    }

    /**
     * Message form
     */
    $("#message_button").click(function (e) {
        e.preventDefault();
        $("#message_form").submit();
    });

    $("#message_form").submit(function (e) {
        e.preventDefault();

        const email = $(".results").data("email");
        const url = $(this).attr("action");
        const input = $(this).find(".input");

        const check = checkValidation(input);
        if (check) {
            $.post(
                url,
                {
                    email: email,
                    title: $("[name='title']").val(),
                    message: $("[name='message']").val(),
                },
                function (data) {
                    showAlert(data, "alert-success");
                }
            );
        }
    });
});
