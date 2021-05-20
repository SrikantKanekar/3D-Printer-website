/******************************

[Table of Contents]

1. Vars and Inits
2. Set Header
3. Init Menu
4. Ajax Spinner


******************************/
$(document).ready(function () {
    "use strict";

    /* 

	1. Vars and Inits

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

    /* 

	2. Set Header

	*/

    function setHeader() {
        if ($(window).scrollTop() > 100) {
            header.addClass("scrolled");
        } else {
            header.removeClass("scrolled");
        }
    }

    /* 

	3. Init Menu

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

    /* 

	4. Ajax spinner

	*/

    $(document).ajaxSend(function () {
        $("#overlay").fadeIn(300);
    });

    $(document).ajaxComplete(function () {
        $("#overlay").fadeOut(300);
    });

    $(document).ajaxError(function (event, xhr, setting, error) {
        alert("an error occoured");
        console.log(error);
    });


});
