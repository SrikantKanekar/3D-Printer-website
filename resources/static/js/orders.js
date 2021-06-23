window.addEventListener('load', function () {
    "use strict";

    const order = $(".order")

    order.each(function () {
        let text;
        const value = $(this).data("date");

        if (value !== ''){
            text = new Date(value).toLocaleDateString()
        } else {
            text = "-"
        }
        this.querySelector(".item_delivered").textContent = text;
    });
});