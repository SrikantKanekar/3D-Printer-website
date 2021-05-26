<#import "base.ftl" as layout />
<#import "header.ftl" as header />
<@layout.base title="Admin Login" css="/static/css/auth.css" js="/static/js/auth.js">

    <@header.header user="${user}" title="Admin Login"/>

	<div class="auth">
		<div class="auth_container col-lg-4 col-md-6 col-sm-8">
			
			<div class="form_title">Admin Login</div>
			<div class="form_container">

				<form class="form" action="/admin/login" method="POST" id="auth_form">
					
                    <div data-validate="Please enter name">
						<input name="name" type="text" id="name" class="input" placeholder="Name" required="required">
					</div>

                    <div data-validate="Please enter password">
						<span class="btn-show-pass">
							<i class="fa fa fa-eye"></i>
						</span>
						<input name="password" type="password" id="password" class="input" placeholder="Password" required="required">
					</div>

                    <div class="form_message"></div>
					
					<div id="login_button" class="button form_submit_button"><a href="#">Login</a></div>
				</form>
			</div>
		</div>
	</div>
</@layout.base>
