$(document).ready(function () {
    "use strict";

    // window.addEventListener('pageshow', function(e){
    //     var historyTraversal = e.persisted || (typeof window.performance != 'undefined' && window.performance.navigation.type === 2);
    //     if (historyTraversal) {
    //         console.log("reloading");
    //         window.location.reload();
    //         console.log("reloaded");
    //     }
    // });


    // $(window).bind("pageshow", function (e) {
    //     if (e.originalEvent.persisted) {
    //         window.location.reload();
    //     }
    // });


    /*
        Isotope
    */
    var counter = $(".results span");
    var sorting = $(".sorting_container");
    var sortingButtons = $(".product_sorting_btn");

    var grid = $(".product_grid").isotope({
        itemSelector: ".product",
        layoutMode: "fitRows",
        fitRows: {
            gutter: 30,
        },
        getSortData: {
            status: function (itemElement) {
                var statusText = $(itemElement).find(".status").text();
                return statusOrdinal(statusText);
            },
            user: function (itemElement) {
                var user_email = $(itemElement)
                    .find(".user_email")
                    .text()
                    .toUpperCase();
                return user_email;
            },
            price: function (itemElement) {
                var price = $(itemElement).find(".price");
                return price.children("span").text();
            },
            objects: function (itemElement) {
                var size = $(itemElement).find(".size");
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
        var status = statusOrdinal(product.find(".status").text());

        var confirmed = $(element).find(".confirmed").first();
        var processing = $(element).find(".processing").first();
        var delivering = $(element).find(".delivering").first();
        var delivered = $(element).find(".delivered").first();

        if (status == 1) {
            confirmed.addClass("disabled");
        }
        if (status == 2) {
            confirmed.addClass("disabled");
            processing.addClass("disabled");
        }
        if (status == 3) {
            confirmed.addClass("disabled");
            processing.addClass("disabled");
            delivering.addClass("disabled");
        }
        if (status == 4) {
            confirmed.addClass("disabled");
            processing.addClass("disabled");
            delivering.addClass("disabled");
            delivered.addClass("disabled");
        }
    });

    $(".admin_button").click(function (e) {
        e.preventDefault();
        // prevent default behavior
    });

    $(".btn-group").on("click", ".admin_button", function () {
        var button = $(this);
        var product = button.parents(".product");

        var orderId = product.data("id");
        var currentStatus = statusOrdinal(product.find(".status").text());
        var buttonStatus = button.data("status");

        if (buttonStatus == currentStatus + 1) {
            $.post(
                "/admin/update/order-status",
                {
                    id: orderId,
                    order_status: buttonStatus,
                },
                function (data) {
                    if (data == true) {
                        // disable the button
                        button.addClass("disabled");

                        // update status and isotope sort order
                        product.find(".status").text(getStatus(buttonStatus));
                        if (buttonStatus == 4) {
                            grid.isotope("remove", product).isotope("layout");
                            
                            // update count
                            var count = parseInt(counter.text(), 10) - 1;
                            counter.text(count);
                            if (count == 0) sorting.hide();
                        }
                        grid.isotope("updateSortData").isotope();
                    }
                }
            );
        }
    });

    function statusOrdinal(status) {
        if (status == "PLACED") return 0;
        if (status == "CONFIRMED") return 1;
        if (status == "PROCESSING") return 2;
        if (status == "DELIVERING") return 3;
        if (status == "DELIVERED") return 4;
    }

    function getStatus(status) {
        if (status == 0) return "PLACED";
        if (status == 1) return "CONFIRMED";
        if (status == 2) return "PROCESSING";
        if (status == 3) return "DELIVERING";
        if (status == 4) return "DELIVERED";
    }
});
