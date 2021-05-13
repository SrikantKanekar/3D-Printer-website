
(function ($) {
    "use strict";

    /*==================================================================
    [ Validate ]*/
    var input = $('.validate-input .input100');

    $("#login-form").submit(function (e) {
        e.preventDefault();
        var url = $(this).attr('action');
        var check = checkValidation();
        if (check) {
            $.post(
                url,
                $(this).serialize(),
                function (data) {
                    if (data.startsWith("/")) {
                        window.location.href = data;
                    } else {
                        $("#login-error").text(data);
                    }
                }
            );
        }
    });

    $("#register-form").submit(function (e) {
        e.preventDefault();
        var url = $(this).attr('action');
        var check = checkValidation();
        if (check) {
            $.post(
                url,
                $(this).serialize(),
                function (data) {
                    if (data.startsWith("/")) {
                        window.location.href = data;
                    } else {
                        $("#register-error").text(data);
                    }
                }
            );
        }
    });

    $('.validate-form .input100').each(function () {
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
        if ($(input).attr('type') == 'email' || $(input).attr('name') == 'Email') {
            if ($(input).val().trim().match(/^([a-zA-Z0-9_\-\.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([a-zA-Z0-9\-]+\.)+))([a-zA-Z]{1,5}|[0-9]{1,3})(\]?)$/) == null) {
                return false;
            }
        }
        else {
            if ($(input).val().trim() == '') {
                return false;
            }
        }
    }

    function showValidate(input) {
        var thisAlert = $(input).parent();

        $(thisAlert).addClass('alert-validate');
    }

    function hideValidate(input) {
        var thisAlert = $(input).parent();

        $(thisAlert).removeClass('alert-validate');
    }

    /*==================================================================
    [ Show pass ]*/
    var showPass = 0;
    $('.btn-show-pass').on('click', function () {
        if (showPass == 0) {
            $(this).next('input').attr('type', 'text');
            $(this).find('i').removeClass('fa-eye');
            $(this).find('i').addClass('fa-eye-slash');
            showPass = 1;
        }
        else {
            $(this).next('input').attr('type', 'password');
            $(this).find('i').removeClass('fa-eye-slash');
            $(this).find('i').addClass('fa-eye');
            showPass = 0;
        }

    });

})(jQuery);