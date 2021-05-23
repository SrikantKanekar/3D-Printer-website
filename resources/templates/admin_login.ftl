<#import "base.ftl" as layout />
<#import "header.ftl" as header />
<@layout.base title="Admin Login" css="/static/css/auth.css" js="/static/js/auth.js">

    <@header.header user="" title="Admin Login"/>

	<div class="auth">
		<div class="auth_container col-lg-4 col-md-6 col-sm-8">
			
			<div class="auth_title">Admin Login</div>
			<div class="auth_form_container">

				<form class="auth_form" action="/admin/login" method="post" id="auth_form">
					
                    <div data-validate="Please enter name">
						<input name="name" type="text" id="name" class="auth_input" placeholder="Name" required="required">
					</div>

                    <div data-validate="Please enter password">
						<span class="btn-show-pass">
							<i class="fa fa fa-eye"></i>
						</span>
						<input name="password" type="password" id="password" class="auth_input" placeholder="Password" required="required">
					</div>

                    <div class="auth_form_error"></div>
					
					<div class="button auth_submit_button"><a href="#">Login</a></div>
				</form>
			</div>
		</div>
	</div>
</@layout.base>
