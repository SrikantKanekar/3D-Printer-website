<#import "base.ftl" as layout />
<#import "header.ftl" as header />
<@layout.base title="Account" css="/static/css/account.css" js="/static/js/account.js">

    <@header.header user="${user}" title="Account" />

    <div class="container">

        <div class="account_detail_container">
            <p>Username : ${user.username}</p>
            <p>Email : ${user.email}</p>
        </div>

        <#--  Update Account  -->
        <button class="collapsible">Update Account</button>
        <div class="collapsible_content col-lg-10">
            <div class="update_form_container">
                <form class="form" action="/account/update" method="POST" id="update_form">
                    <label for="username">Username</label>
                    <div data-validate="Please enter username">
                        <input name="username" id="username" value="${user.username}" type="text" class="input"
                               required="required">
                    </div>
                    <div class="form_message"></div>

                    <div id="update_button" class="button form_submit_button"><a href="#">Update</a></div>
                </form>
            </div>
        </div>

        <#--  Change password  -->
        <button class="collapsible">Change password</button>
        <div class="collapsible_content col-lg-10">
            <div class="change_password_form_container">
                <form class="form" action="/account/reset-password" method="POST" id="change_password_form">
                    <div data-validate="Please enter password">
                        <input name="old_password" type="password" class="input" placeholder="Old Password"
                               required="required">
                    </div>

                    <div data-validate="Please enter password">
						<span class="btn-show-pass">
							<i class="fa fa fa-eye"></i>
						</span>
                        <input name="new_password" type="password" class="input" placeholder="New Password"
                               required="required">
                    </div>

                    <div data-validate="Please enter password">
                        <input name="confirm_password" type="password" class="input" placeholder="Confirm Password"
                               required="required">
                    </div>

                    <div class="form_message"></div>

                    <div id="change_password_button" class="button form_submit_button">
                        <a href="#">Change Password</a>
                    </div>
                </form>
            </div>
        </div>

    </div>
</@layout.base>