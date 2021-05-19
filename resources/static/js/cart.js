/******************************

[Table of Contents]

1. Vars and Inits
2. Init Quantity


******************************/

$(document).ready(function () {
    "use strict";

    /* 

	1. Vars and Inits

	*/
    initQuantity();


    /* 

	2. Init Quantity

	*/

    function initQuantity() {
        // Handle product quantity input
        if ($(".product_quantity").length) {
            var input = $("#quantity_input");
            var incButton = $("#quantity_inc_button");
            var decButton = $("#quantity_dec_button");

            var originalVal;
            var endVal;

            incButton.on("click", function () {
                originalVal = input.val();
                endVal = parseFloat(originalVal) + 1;
                input.val(endVal);
            });

            decButton.on("click", function () {
                originalVal = input.val();
                if (originalVal > 0) {
                    endVal = parseFloat(originalVal) - 1;
                    input.val(endVal);
                }
            });
        }
    }
});
