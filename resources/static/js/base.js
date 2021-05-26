/******************************


1. Vars and Inits
2. Set Header
3. Init Menu
4. Ajax Spinner
5. Alert


******************************/
$(document).ready(function () {
    "use strict";

    /**
     * 1. Vars and Inits
     */
    var header = $(".header");
    var hambActive = false;
    var menuActive = false;

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
            header.addClass("scrolled");
        } else {
            header.removeClass("scrolled");
        }
    }

    /**
     * 3. Init Menu
     */
    function initMenu() {
        if ($(".hamburger").length) {
            var hamb = $(".hamburger");

            hamb.on("click", function (event) {
                event.stopPropagation();

                if (!menuActive) {
                    openMenu();

                    $(document).one("click", function cls(e) {
                        if ($(e.target).hasClass("menu_mm")) {
                            $(document).one("click", cls);
                        } else {
                            closeMenu();
                        }
                    });
                } else {
                    $(".menu").removeClass("active");
                    menuActive = false;
                }
            });

            //Handle page menu
            if ($(".page_menu_item").length) {
                var items = $(".page_menu_item");
                items.each(function () {
                    var item = $(this);

                    item.on("click", function (evt) {
                        if (item.hasClass("has-children")) {
                            evt.preventDefault();
                            evt.stopPropagation();
                            var subItem = item.find("> ul");
                            if (subItem.hasClass("active")) {
                                subItem.toggleClass("active");
                                TweenMax.to(subItem, 0.3, { height: 0 });
                            } else {
                                subItem.toggleClass("active");
                                TweenMax.set(subItem, { height: "auto" });
                                TweenMax.from(subItem, 0.3, { height: 0 });
                            }
                        } else {
                            evt.stopPropagation();
                        }
                    });
                });
            }
        }
    }

    function openMenu() {
        var fs = $(".menu");
        fs.addClass("active");
        hambActive = true;
        menuActive = true;
    }

    function closeMenu() {
        var fs = $(".menu");
        fs.removeClass("active");
        hambActive = false;
        menuActive = false;
    }

    /**
     * 4. Ajax spinner
     */
    $(document).ajaxSend(function () {
        $("#overlay").fadeIn(300);
    });

    $(document).ajaxComplete(function () {
        $("#overlay").fadeOut(300);
    });

    $(document).ajaxError(function (event, xhr, setting, error) {
        showAlert("an error occoured", "alert-danger");
        console.log(error);
    });
});

/**
 * 5. Alert
 */
function showAlert(text, alertClass) {
    var button =
        '<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>';
    $(".alert").text(text);
    $(".alert").append(button);
    $(".alert").addClass(alertClass);
    $(".alert_container")
        .fadeTo(2000, 500)
        .slideUp(500, function () {
            $(".alert_container").slideUp(500);
            $(".alert").removeClass(alertClass);
        });
}

/**
 * 6. Form
 */
function showValidate(input) {
    var field = $(input).parent();
    $(field).addClass("alert-validate");
}

function hideValidate(input) {
    var field = $(input).parent();
    $(field).removeClass("alert-validate");
}

function validate(input) {
    if ($(input).attr("type") == "email") {
        if (
            $(input)
                .val()
                .trim()
                .match(
                    /^([a-zA-Z0-9_\-\.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([a-zA-Z0-9\-]+\.)+))([a-zA-Z]{1,5}|[0-9]{1,3})(\]?)$/
                ) == null
        ) {
            return false;
        }
    } else {
        if ($(input).val().trim() == "") {
            return false;
        }
    }
}

function checkValidation(input) {
    var check = true;
    for (var i = 0; i < input.length; i++) {
        if (validate(input[i]) == false) {
            showValidate(input[i]);
            check = false;
        }
    }
    return check;
}

$(".input").each(function () {
    $(this).focus(function () {
        hideValidate(this);
    });
});

var showPass = 0;
$(".btn-show-pass").on("click", function () {
    if (showPass == 0) {
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
