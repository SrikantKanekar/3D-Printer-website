$(document).ready(function () {
    "use strict";

    /**
     * Collapsible
     */
    var coll = document.getElementsByClassName("collapsible");
    var i;

    for (i = 0; i < coll.length; i++) {
        coll[i].addEventListener("click", function () {
            this.classList.toggle("active");
            var content = this.nextElementSibling;
            if (content.style.maxHeight) {
                content.style.maxHeight = null;
            } else {
                content.style.maxHeight = content.scrollHeight + "px";
            }
        });
    }

    /**
     * update form
     */
    var updateInput = $(".update_input");

    $(".update_button").click(function (e) {
        e.preventDefault();
        $("#update_form").submit();
    });

    $("#update_form").submit(function (e) {
        e.preventDefault();
        var url = $(this).attr("action");
        var check = checkUpdateValidation();
        if (check) {
            $.post(url, $(this).serialize(), function (data) {
                if (data == "updated") {
                    $(".update_form_error").addClass("success");
                } else {
                    $(".update_form_error").removeClass("success");
                }
                $(".update_form_error").text(data);
            });
        }
    });

    /**
     * Validation
     */
    $(".update_form .update_input").each(function () {
        $(this).focus(function () {
            hideValidate(this);
        });
    });

    function checkUpdateValidation() {
        var check = true;
        for (var i = 0; i < updateInput.length; i++) {
            if (validate(updateInput[i]) == false) {
                showValidate(updateInput[i]);
                check = false;
            }
        }
        return check;
    }

    /**
     * change password form
     */
    var input = $(".auth_input");

    $(".change_password_button").click(function (e) {
        e.preventDefault();
        $("#change_password_form").submit();
    });

    $("#change_password_form").submit(function (e) {
        e.preventDefault();
        var url = $(this).attr("action");
        var check = checkValidation();
        if (check) {
            $.post(url, $(this).serialize(), function (data) {
                if (data == "updated") {
                    $(".change_password_form_error").addClass("success");
                } else {
                    $(".change_password_form_error").removeClass("success");
                }
                $(".change_password_form_error").text(data);
            });
        }
    });

    /**
     * Validation
     */

    $(".change_password_form .auth_input").each(function () {
        $(this).focus(function () {
            hideValidate(this);
        });
    });

    function checkValidation() {
        var check = true;
        for (var i = 0; i < input.length; i++) {
            if (validate(input[i]) == false) {
                showValidate(input[i]);
                check = false;
            }
        }
        return check;
    }

    function validate(input) {
        if ($(input).val().trim() == "") {
            return false;
        }
    }

    function showValidate(input) {
        var authField = $(input).parent();
        $(authField).addClass("alert-validate");
    }

    function hideValidate(input) {
        var authField = $(input).parent();
        $(authField).removeClass("alert-validate");
    }

    /**
     * Password Visibility
     */
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
});
