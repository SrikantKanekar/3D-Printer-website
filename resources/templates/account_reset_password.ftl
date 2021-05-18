<#import "base.ftl" as layout />
<#import "header.ftl" as header />
<@layout.base title="Reset Password" css="" js="">

    <@header.header user="${user}" />

    <div class="container" style="padding-top: 110px">
        <form action="/account/reset-password" method="post" class="m-5">
            <div class="mb-3">
                <label for="old_password" class="form-label">Old Password</label>
                <input type="password" name="old_password" class="form-control" id="old_password">
            </div>
            <div class="mb-3">
                <label for="new_password" class="form-label">New Password</label>
                <input type="password" name="new_password" class="form-control" id="new_password">
            </div>
            <div class="mb-3">
                <label for="confirm_password" class="form-label">Confirm Password</label>
                <input type="password" name="confirm_password" class="form-control" id="confirm_password">
            </div>
            <button type="submit" class="btn btn-primary">Reset password</button>
        </form>
    </div>
</@layout.base>