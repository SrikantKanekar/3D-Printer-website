<#import "base.ftl" as layout />
<#import "header.ftl" as header />
<@layout.base title="Login" css="/static/css/auth.css" js="/static/js/auth.js">

    <@header.header user="" title="Login" />

    <div class="auth_container col-lg-4 col-md-6 col-sm-8">

        <div class="form_title">Login</div>
        <div class="form_container">
            <form class="form" action="/auth/login" method="POST" id="auth_form">

                <div data-validate="Please enter email: example@abc.xyz">
                    <input name="email" type="email" id="email" class="input" placeholder="Email">
                </div>

                <div data-validate="Please enter password">
					<span class="btn-show-pass">
						<i class="fa fa fa-eye"></i>
					</span>
                    <input name="password" type="password" id="password" class="input" placeholder="Password">
                </div>

                <div class="form_message"></div>

                <div id="login_button" class="button form_submit_button"><a href="#">Login</a></div>

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
</@layout.base>
