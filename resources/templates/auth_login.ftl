<#import "base.ftl" as layout />
<#import "header.ftl" as header />
<@layout.base title="Login" css="/static/css/auth.css" js="/static/js/auth.js">

    <@header.header user="" title="Login" />

	<div class="auth">
		<div class="auth_container col-lg-4 col-md-6 col-sm-8">
			
			<div class="auth_title">Login</div>
			<div class="auth_form_container">
				<form class="auth_form" action="/auth/login" method="POST" id="auth_form">

					<div data-validate="Please enter email: example@abc.xyz">
						<input name="email" type="email" id="email" class="auth_input" placeholder="Email" required="required">
					</div>

					<div data-validate="Please enter password">
						<span class="btn-show-pass">
							<i class="fa fa fa-eye"></i>
						</span>
						<input name="password" type="password" id="password" class="auth_input" placeholder="Password" required="required">
					</div>

					<div class="auth_form_error"></div>
					
					<div class="button auth_submit_button"><a href="#">Login</a></div>
		
					<div class="flex-col-c p-t-224">
						<span class="txt2 p-b-10">
							Don’t have an account?
						</span>

						<a href="/auth/register?returnUrl=${returnUrl}" class="txt3">
							Sign up now
						</a>
					</div>

					<input name="returnUrl" value="${returnUrl}" style="display: none">
				</form>
			</div>

		</div>
	</div>
</@layout.base>
