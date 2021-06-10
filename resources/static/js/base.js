/******************************

 1. Vars and Inits
 2. Set Header
 3. Init Menu
 4. Theme icon
 5. Ajax Spinner
 6. Alert
 7. Form

 ******************************/
document.addEventListener('DOMContentLoaded', function () {
    "use strict";

    /**
     * 1. Vars and Inits
     */
    const header = document.querySelector(".header");
    const menu = document.querySelector(".menu");
    let menuActive = false;

    setHeader();
    $(window).on("resize", function () {
        setHeader();
    });
    $(document).on("scroll", function () {
        setHeader();
    });

    initMenu();

    /**
     * 2. Set Header
     */
    function setHeader() {
        if ($(window).scrollTop() > 100) {
            header.classList.add("scrolled");
        } else {
            header.classList.remove("scrolled");
        }
    }

    /**
     * 3. Init Menu
     */
    function initMenu() {
        $(".hamburger").on("click", function (e) {
            e.stopPropagation();

            if (!menuActive) {
                openMenu();
                $(document).one("click", function () {
                    closeMenu();
                });
            } else {
                closeMenu();
            }
        });

        //Handle page menu
        const items = $(".menu_item");
        items.each(function () {
            const item = $(this);

            item.on("click", function (e) {
                if (item.hasClass("has-children")) {
                    e.preventDefault();
                    e.stopPropagation();
                    const subItem = item.find("> ul");
                    if (subItem.hasClass("active")) {
                        subItem.toggleClass("active");
                        TweenMax.to(subItem, 0.3, {height: 0});
                    } else {
                        subItem.toggleClass("active");
                        TweenMax.set(subItem, {height: "auto"});
                        TweenMax.from(subItem, 0.3, {height: 0});
                    }
                } else {
                    e.stopPropagation();
                }
            });
        });
    }

    function openMenu() {
        menu.classList.add("active");
        menuActive = true;
    }

    function closeMenu() {
        menu.classList.remove("active");
        menuActive = false;
    }

    /**
     * 4. Theme icon
     */
    const icon = $(".theme_icon").find("i");
    icon.on('click', function () {
        $("html").toggleClass("light_theme");
        updateTheme();

    });

    function updateTheme() {

        if ($("html").hasClass("light_theme")) {
            icon.removeClass("fa-sun-o");
            icon.addClass("fa-moon-o");
            localStorage.setItem("theme", "light_theme");
        } else {
            icon.removeClass("fa-moon-o");
            icon.addClass("fa-sun-o");
            localStorage.setItem("theme", "dark_theme");
        }
    }

    /**
     * 4. Ajax call spinner
     */
    $(document).ajaxSend(function () {
        $(".spinner_overlay").fadeIn(300);
    });

    $(document).ajaxComplete(function () {
        $(".spinner_overlay").fadeOut(300);
    });

    $(document).ajaxError(function (event, xhr, setting, error) {
        showAlert("an error occurred", "alert-danger");
        console.log(error);
    });
});

/**
 * 5. Alert
 */
const alert_container = document.querySelector(".alert_container");
const alert = alert_container.querySelector(".alert");

function showAlert(text, alertClass) {
    const button = '<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>';
    alert.textContent = text;
    alert.innerHTML += button;
    alert.classList.add(alertClass);

    $(".alert_container")
        .fadeTo(2000, 500)
        .slideUp(500, function () {
            $(".alert_container").slideUp(500);
            alert.classList.remove(alertClass);
        });
}

$(".alert").on("close.bs.alert", function (e) {
    e.preventDefault();
    alert_container.style.display = 'none';
});

/**
 * 6. Form
 */
function showValidate(input) {
    const field = $(input).parent();
    $(field).addClass("alert-validate");
}

function hideValidate(input) {
    const field = $(input).parent();
    $(field).removeClass("alert-validate");
}

function validate(input) {
    let value = input.value.trim();
    if (value === "") {
        return false;
    }
    switch (input.id) {
        case "email":
            if (value.match(/^([a-zA-Z0-9_\-.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([a-zA-Z0-9\-]+\.)+))([a-zA-Z]{1,5}|[0-9]{1,3})(]?)$/) == null) {
                return false;
            }
            break;

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

function checkValidation(input) {
    let check = true;
    for (let i = 0; i < input.length; i++) {
        if (validate(input[i]) === false) {
            showValidate(input[i]);
            check = false;
        }
    }
    return check;
}

const input = $("input");
input.on('focus', function () {
    hideValidate(this);
});
input.on('focusout', function () {
    if (validate(this) === false) {
        showValidate(this);
    }
});

let showPass = 0;
$(".btn-show-pass").on("click", function () {
    if (showPass === 0) {
        $(this).next("input").attr("type", "text");
        $(this).find("i").removeClass("fa-eye");
        $(this).find("i").addClass("fa-eye-slash");
        showPass = 1;
    } else {
        $(this).next("input").attr("type", "password");
        $(this).find("i").removeClass("fa-eye-slash");
        $(this).find("i").addClass("fa-eye");
        showPass = 0;
    }
});

// set dropdown values
$(".dropdown_item_select").each(function () {
    this.value = this.getAttribute("data-value");
});

//prevent scrolling value on input
$(document).on('wheel', 'input[type=number]', function () {
    $(this).blur();
})