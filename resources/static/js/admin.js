window.addEventListener('pageshow', function (e) {
    const historyTraversal = e.persisted || (typeof window.performance != 'undefined' && window.performance.navigation.type === 2);
    if (historyTraversal) {
        window.location.reload(false);
    }
});

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
                const statusText = itemElement.querySelector(".status").textContent;
                return statusOrdinal(statusText);
            },
            user: function (itemElement) {
                return itemElement
                    .querySelector(".user_email")
                    .textContent
                    .toUpperCase();
            },
            price: function (itemElement) {
                return itemElement.querySelector(".price span").textContent;
            },
            objects: function (itemElement) {
                return itemElement.querySelector(".size span").textContent;
            },
        },
        animationOptions: {
            duration: 750,
            easing: "linear",
            queue: false,
        },
    });

    $(".sorting_button").each(function () {
        $(this).on("click", function () {
            document.querySelector(".sorting_text").textContent = this.textContent;
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
        const status = statusOrdinal(product.querySelector(".status").textContent);

        const confirmed = element.querySelector(".confirmed");
        const processing = element.querySelector(".processing");
        const delivering = element.querySelector(".delivering");
        const delivered = element.querySelector(".delivered");

        if (status === 1) {
            confirmed.classList.add("disabled");
        }
        if (status === 2) {
            confirmed.classList.add("disabled");
            processing.classList.add("disabled");
        }
        if (status === 3) {
            confirmed.classList.add("disabled");
            processing.classList.add("disabled");
            delivering.classList.add("disabled");
        }
        if (status === 4) {
            confirmed.classList.add("disabled");
            processing.classList.add("disabled");
            delivering.classList.add("disabled");
            delivered.classList.add("disabled");
        }
    });

    $(".button.admin").on('click',function (e) {
        e.preventDefault();
        // prevent default behavior
    });

    const counter = document.querySelector(".results span");
    const sorting = document.querySelector(".sorting_container");

    button_group.on("click", ".button.admin", function () {
        const button = this;
        const product = button.closest(".product");

        const orderId = product.getAttribute("data-id");
        const status = product.querySelector(".status");
        const currentStatus = statusOrdinal(status.textContent);
        const buttonStatus = button.getAttribute("data-status");

        if (buttonStatus == currentStatus + 1) {
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
                        button.classList.add("disabled");

                        // update status and isotope sort order
                        status.textContent = getStatus(buttonStatus);

                        if (buttonStatus == 4) {
                            grid.isotope("remove", $(product)).isotope("layout");

                            // update count
                            const count = parseInt(counter.textContent, 10) - 1;
                            counter.textContent = count.toString();
                            if (count === 0) sorting.style.display = "none";
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
