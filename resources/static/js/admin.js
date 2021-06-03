window.addEventListener('pageshow', function (e) {
    const historyTraversal = e.persisted || (typeof window.performance != 'undefined' && window.performance.navigation.type === 2);
    if (historyTraversal) {
        window.location.reload(false);
    }
});

window.addEventListener('load', function () {
    "use strict";

    /**
     Isotope
     */
    const counter = $(".results span");
    const sorting = $(".sorting_container");
    const sortingButtons = $(".product_sorting_btn");

    const grid = $(".product_grid").isotope({
        itemSelector: ".product",
        layoutMode: "fitRows",
        fitRows: {
            gutter: 30,
        },
        getSortData: {
            status: function (itemElement) {
                const statusText = $(itemElement).find(".status").text();
                return statusOrdinal(statusText);
            },
            user: function (itemElement) {
                return $(itemElement)
                    .find(".user_email")
                    .text()
                    .toUpperCase();
            },
            price: function (itemElement) {
                const price = $(itemElement).find(".price");
                return price.children("span").text();
            },
            objects: function (itemElement) {
                const size = $(itemElement).find(".size");
                return size.children("span").text();
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
            const parent = $(this).parent().parent().find(".sorting_text");
            parent.text($(this).text());
            let option = $(this).attr("data-isotope-option");
            option = JSON.parse(option);
            grid.isotope(option);
        });
    });

    /*
        admin control buttons
    */
    $(".btn-group").each(function (index, element) {
        const product = $(element).parents(".product");
        const status = statusOrdinal(product.find(".status").text());

        const confirmed = $(element).find(".confirmed").first();
        const processing = $(element).find(".processing").first();
        const delivering = $(element).find(".delivering").first();
        const delivered = $(element).find(".delivered").first();

        if (status === 1) {
            confirmed.addClass("disabled");
        }
        if (status === 2) {
            confirmed.addClass("disabled");
            processing.addClass("disabled");
        }
        if (status === 3) {
            confirmed.addClass("disabled");
            processing.addClass("disabled");
            delivering.addClass("disabled");
        }
        if (status === 4) {
            confirmed.addClass("disabled");
            processing.addClass("disabled");
            delivering.addClass("disabled");
            delivered.addClass("disabled");
        }
    });

    $(".button.admin").click(function (e) {
        e.preventDefault();
        // prevent default behavior
    });

    $(".btn-group").on("click", ".button.admin", function () {
        const button = $(this);
        const product = button.parents(".product");

        const orderId = product.data("id");
        const currentStatus = statusOrdinal(product.find(".status").text());
        const buttonStatus = button.data("status");

        if (buttonStatus === currentStatus + 1) {
            $.post(
                "/admin/update/order-status",
                {
                    id: orderId,
                    order_status: buttonStatus,
                },
                function (data) {
                    if (data === true) {
                        showAlert(
                            getStatus(buttonStatus) + " Done",
                            "alert-success"
                        );

                        // disable the button
                        button.addClass("disabled");

                        // update status and isotope sort order
                        product.find(".status").text(getStatus(buttonStatus));
                        if (buttonStatus === 4) {
                            grid.isotope("remove", product).isotope("layout");

                            // update count
                            const count = parseInt(counter.text(), 10) - 1;
                            counter.text(count);
                            if (count === 0) sorting.hide();
                        }
                        grid.isotope("updateSortData").isotope();
                    } else {
                        showAlert(
                            "Please print all objects first",
                            "alert-danger"
                        );
                    }
                }
            );
        } else {
            if (buttonStatus > currentStatus + 1) {
                showAlert(
                    "Please complete " +
                    getStatus(currentStatus + 1) +
                    " first",
                    "alert-danger"
                );
            } else {
                showAlert(
                    getStatus(buttonStatus) + " is already completed",
                    "alert-danger"
                );
            }
        }
    });

    function statusOrdinal(status) {
        if (status === "PLACED") return 0;
        if (status === "CONFIRMED") return 1;
        if (status === "PROCESSING") return 2;
        if (status === "DELIVERING") return 3;
        if (status === "DELIVERED") return 4;
    }

    function getStatus(status) {
        if (status === 0) return "PLACED";
        if (status === 1) return "CONFIRMED";
        if (status === 2) return "PROCESSING";
        if (status === 3) return "DELIVERING";
        if (status === 4) return "DELIVERED";
    }
});
