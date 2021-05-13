<#import "template.ftl" as layout />
<#import "header.ftl" as header />
<@layout.main title="Admin Login" css="/static/css/auth/main.css" js="/static/js/auth.js">

    <@header.header user="" />

	<div class="limiter">
		<div class="container-login100">
			<div class="wrap-login100 p-t-90 p-b-30">
				<form class="login100-form validate-form" action="/admin/login" method="post" id="login-form">
					<div class="text-center p-t-55 p-b-30">
						<span class="txt1">
							Admin Login
						</span>
					</div>

                    <div class="wrap-input100 validate-input m-b-16" data-validate="Please enter password">
                        <input class="input100" type="text" name="name" placeholder="name">
                        <span class="focus-input100"></span>
                    </div>

                    <div class="wrap-input100 validate-input m-b-20" data-validate="Please enter password">
                        <span class="btn-show-pass">
                            <i class="fa fa fa-eye"></i>
                        </span>
                        <input class="input100" type="password" name="Password" placeholder="Password">
                        <span class="focus-input100"></span>
                    </div>

					<div id="login-error" style="color: red"></div>

                    <div class="container-login100-form-btn">
                        <button type="submit" class="login100-form-btn">
                            Login
                        </button>
                    </div>
				</form>
			</div>
		</div>
	</div>
</@layout.main>
