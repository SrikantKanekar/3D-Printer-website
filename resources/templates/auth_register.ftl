<#import "base.ftl" as layout />
<#import "header.ftl" as header />
<@layout.base title="Register" css="/static/css/auth.css" js="/static/js/auth.js">

    <@header.header user="" title="Register" />

	<div class="auth">
		<div class="auth_container col-lg-4 col-md-6 col-sm-8">

			<div class="auth_title">Register</div>
			<div class="auth_form_container">
				<form class="auth_form" action="/auth/register" method="POST" id="auth_form">

					<div data-validate="Please enter Username">
						<input name="username" type="text" id="username" class="auth_input" placeholder="Username" required="required">
					</div>

					<div data-validate="Please enter email: example@abc.xyz">
						<input name="email" type="email" id="email" class="auth_input" placeholder="Email" required="required">
					</div>

					<div data-validate="Please enter password">
						<span class="btn-show-pass">
							<i class="fa fa fa-eye"></i>
						</span>
						<input name="password1" type="password" id="password1" class="auth_input" placeholder="Password" required="required">
					</div>

					<div data-validate="Please enter password">
						<input name="password2" type="password" id="password2" class="auth_input" placeholder="Confirm password" required="required">
					</div>

					<div class="auth_form_error"></div>

					<div class="button auth_submit_button"><a href="#">Register</a></div>
                
					<div class="flex-col-c p-t-224">
						<span class="txt2 p-b-10">
							Already have an account?
						</span>

						<a href="/auth/login?returnUrl=${returnUrl}" class="txt3">
							Sign in now
						</a>
					</div>

					<input name="returnUrl" value="${returnUrl}" style="display: none">
				</form>
			</div>

		</div>
	</div>
</@layout.base>