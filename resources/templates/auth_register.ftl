<#import "template.ftl" as layout />
<@layout.main title="Register" css="/static/css/auth/main.css" js="/static/js/auth/main.js">
	<div class="limiter">
		<div class="container-login100">
			<div class="wrap-login100 p-t-90 p-b-30">
				<form class="login100-form validate-form" action="/auth/register" method="post">
					<div class="text-center p-t-55 p-b-30">
						<span class="txt1">
							Register with email
						</span>
					</div>

                    <div class="wrap-input100 validate-input m-b-16" data-validate="Please enter email: ex@abc.xyz">
                        <input class="input100" type="text" name="Email" placeholder="Email">
                        <span class="focus-input100"></span>
                    </div>

                    <div class="wrap-input100 validate-input m-b-20" data-validate="Please enter password">
                        <span class="btn-show-pass">
                            <i class="fa fa fa-eye"></i>
                        </span>
                        <input class="input100" type="password" name="Password" placeholder="Password">
                        <span class="focus-input100"></span>
                    </div>

                    <div class="container-login100-form-btn">
                        <button type="submit" class="login100-form-btn">
                            Register
                        </button>
                    </div>
                
					<div class="flex-col-c p-t-224">
						<span class="txt2 p-b-10">
							Already have an account?
						</span>

						<a href="/auth/login" class="txt3 bo1 hov1">
							Sign in now
						</a>
					</div>
				</form>
			</div>
		</div>
	</div>
</@layout.main>