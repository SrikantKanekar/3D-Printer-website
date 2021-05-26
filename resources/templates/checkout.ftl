<#import "base.ftl" as layout />
<#import "header.ftl" as header />
<@layout.base title="Checkout" css="/static/css/checkout.css" js="/static/js/checkout.js">
    
    <@header.header user="${user}" title="Checkout"/>
    
	<div class="checkout">
		<div class="container">
			<#if objects?has_content>
				<div class="row">

					<!-- Billing Info -->
					<div class="col-lg-6">
						<div class="checkout_section">
							<div class="form_title">Billing Address</div>
							<div class="form_subtitle">Enter your address info</div>
							<div class="form_container">
								<form class="form" action="/checkout/pay" method="POST" id="checkout_form">
									
									<div class="row">
										
										<!-- firstname -->
										<div class="col-xl-6">
											<label for="firstname">First Name*</label>
											<div data-validate="Enter firstname">
												<input name="firstname" value="${address.firstname}" type="text" id="firstname" class="input" required="required">
											</div>
										</div>
										
										<!-- lastname -->
										<div class="col-xl-6 last_name_col">
											<label for="lastname">Last Name*</label>
											<div data-validate="Enter lastname">											
												<input name="lastname" value="${address.lastname}" type="text" id="lastname" class="input" required="required">
											</div>
										</div>

									</div>
									
									<!-- phoneNumber -->
									<label for="phoneNumber">Phone no*</label>
									<div data-validate="Please enter phoneNumber">										
										<input name="phoneNumber" value="${(address.phoneNumber?string.computer)!''}" type="phone" id="phoneNumber" class="input" required="required">
									</div>
									
									<!-- Address -->
									<label for="address">Address*</label>
									<div data-validate="Please enter address">										
										<input name="address" value="${address.address}" type="text" id="address" class="input" required="required">
									</div>
									
									<!-- City / Town -->
									<label for="city">City/Town*</label>
									<div data-validate="Please select city">										
										<select name="city" id="city" class="dropdown_item_select input" require="required">
											<option selected>${address.city}</option>
											<option>Panaji</option>
											<option>Margao</option>
											<option>Ponda</option>
											<option>Muapsa</option>
										</select>
									</div>
									
									<!-- State -->
									<label for="state">State*</label>
									<div data-validate="Please select state">										
										<select name="state" id="state" class="dropdown_item_select input" require="required">
											<option selected>Goa</option>
										</select>
									</div>
									
									<!-- Country -->
									<label for="country">Country*</label>
									<div data-validate="Please select country">										
										<select name="country" id="country" class="dropdown_item_select input" require="required">
											<option selected>India</option>
										</select>
									</div>
									
									<!-- pinCode -->
									<label for="pinCode">Pincode*</label>
									<div data-validate="Please enter pincode">									
										<input name="pinCode" value="${(address.pinCode?string.computer)!''}" type="number" maxlength="6" id="pinCode" class="input" required="required">
									</div>
								</form>
							</div>
						</div>
					</div>

					<!-- Order Info -->
					<div class="col-lg-6">
						<div class="order checkout_section">
							<div class="section_title">Your order</div>
							<div class="section_subtitle">Order details</div>

							<!-- Order details -->
							<div class="order_list_container">
								<div class="order_list_bar d-flex flex-row align-items-center justify-content-start">
									<div class="order_list_title">Product</div>
									<div class="order_list_value ml-auto">Total</div>
								</div>
								<ul class="order_list">
									<#list objects as object>
										<li class="d-flex flex-row align-items-center justify-content-start">
											<div class="order_list_title">${object.filename}</div>
											<div class="order_list_value ml-auto">${object.price}</div>
										</li>
									</#list>
									<li class="d-flex flex-row align-items-center justify-content-start">
										<div class="order_list_title">Subtotal</div>
										<div class="order_list_value ml-auto">$59.90</div>
									</li>
									<li class="d-flex flex-row align-items-center justify-content-start">
										<div class="order_list_title">Shipping</div>
										<div class="order_list_value ml-auto">Free</div>
									</li>
									<li class="d-flex flex-row align-items-center justify-content-start">
										<div class="order_list_title">Total</div>
										<div class="order_list_value ml-auto">$59.90</div>
									</li>
								</ul>
							</div>

							<!-- Order Text -->
							<div class="order_text">some text....</div>
							<div class="button order_button"><a href="#">Place Order</a></div>
						</div>
					</div>

				</div>
        	</#if>
		</div>
	</div>
</@layout.base>