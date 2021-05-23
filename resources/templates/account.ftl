<#import "base.ftl" as layout />
<#import "header.ftl" as header />
<@layout.base title="Account" css="/static/css/account.css" js="/static/js/account.js">
    
    <@header.header user="${user}" title="Account" />

    <div class="account">
		<div class="container">
			
			<div class="account_detail_container">
				<p>Username : ${user.username}</p>
				<p>Email : ${user.email}</p>
			</div>

			<div class="account_collapsible_container">
				
				<button class="collapsible">Update Account</button>
				<div class="collapsible_content">

					<form action="/account/update" method="POST" id="update_form" class="update_form">
						<div data-validate="Please enter username">
							<input name="username" value="${user.username}" type="text" placeholder="username" class="update_input" required="required">
						</div>
						<div class="update_form_error"></div>

						<div class="button update_button"><a href="#">Update</a></div>
					</form>
				</div>

				<button class="collapsible">Change password</button>
				<div class="collapsible_content">

					<form action="/account/reset-password" method="POST" id="change_password_form" class="change_password_form">
						<div data-validate="Please enter password">
							<input name="old_password" type="password" class="auth_input" placeholder="Old Password" required="required">
						</div>

						<div data-validate="Please enter password">
							<span class="btn-show-pass">
								<i class="fa fa fa-eye"></i>
							</span>
							<input name="new_password" type="password" class="auth_input" placeholder="New Password" required="required">
						</div>

						<div data-validate="Please enter password">
							<input name="confirm_password" type="password" class="auth_input" placeholder="Confirm Password" required="required">
						</div>

						<div class="change_password_form_error"></div>

						<div class="button change_password_button"><a href="#">Change Password</a></div>
					</form>
				</div>

			</div>	
		</div>
	</div>
</@layout.base>