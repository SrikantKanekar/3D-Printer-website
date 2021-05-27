$(window).on("load", function (e) {
    "use strict";

    /*
        Isotope
    */
    var sortingButtons = $(".product_sorting_btn");

    var grid = $(".product_grid").isotope({
        itemSelector: ".product",
        layoutMode: "fitRows",
        fitRows: {
            gutter: 30,
        },
        getSortData: {
            status: function (itemElement) {
                var statusText = $(itemElement).find(".product_status").text();
                return printingStatusOrdinal(statusText);
            },
            name: function (itemElement) {
                var nameEle = $(itemElement)
                    .find(".product_title")
                    .text()
                    .toUpperCase();
                return nameEle;
            },
        },
        animationOptions: {
            duration: 750,
            easing: "linear",
            queue: false,
        },
    });

    // Sort based on the value from the sorting_type dropdown
    sortingButtons.each(function () {
        $(this).on("click", function () {
            var parent = $(this).parent().parent().find(".sorting_text");
            parent.text($(this).text());
            var option = $(this).attr("data-isotope-option");
            option = JSON.parse(option);
            grid.isotope(option);
        });
    });

    /*
        admin control buttons
    */
    $(".btn-group").each(function (index, element) {
        var product = $(element).parents(".product");
        var status = printingStatusOrdinal(product.data("status"));

        var printing = $(element).find(".printing").first();
        var printed = $(element).find(".printed").first();

        if (status == 1) {
            printing.addClass("disabled");
        }
        if (status == 2) {
            printing.addClass("disabled");
            printed.addClass("disabled");
        }
    });

    $(".admin_button").click(function (e) {
        e.preventDefault();
        // prevent default behavior
    });

    $(".btn-group").on("click", ".admin_button", function () {
        var orderId = $(".results").data("id");

        var button = $(this);
        var product = button.parents(".product");
        var objectId = product.data("id");
        var currentStatus = printingStatusOrdinal(product.data("status"));
        var buttonStatus = button.data("status");

        if (buttonStatus == currentStatus + 1) {
            $.post(
                "/order/update/printing-status",
                {
                    orderId: orderId,
                    objectId: objectId,
                    printing_status: buttonStatus,
                },
                function (data) {
                    if (data == true) {
                        showAlert(
                            getPrintingStatus(buttonStatus) + " Done",
                            "alert-success"
                        );

                        // disable the button
                        button.addClass("disabled");

                        // update status and isotope sort order
                        product.data("status", getPrintingStatus(buttonStatus));
                        product
                            .find(".product_status")
                            .text(getPrintingStatus(buttonStatus));
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

        var email = $(".results").data("email");
        var url = $(this).attr("action");
        var input = $(this).find(".input");

        var check = checkValidation(input);
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
