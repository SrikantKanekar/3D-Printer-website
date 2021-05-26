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
						<div class="billing checkout_section">
							<div class="section_title">Billing Address</div>
							<div class="section_subtitle">Enter your address info</div>
							<div class="checkout_form_container">
								<form action="/checkout/pay" method="POST" id="checkout_form" class="checkout_form">
									
									<div class="row">
										<div class="col-xl-6">
											<!-- firstname -->
											<label for="firstname">First Name*</label>
											<input name="firstname" value="${address.firstname}" type="text" id="firstname" class="checkout_input" required="required">
										</div>
										<div class="col-xl-6 last_name_col">
											<!-- lastname -->
											<label for="lastname">Last Name*</label>
											<input name="lastname" value="${address.lastname}" type="text" id="lastname" class="checkout_input" required="required">
										</div>
									</div>
									
									<div>
										<!-- phoneNumber -->
										<label for="phoneNumber">Phone no*</label>
										<input name="phoneNumber" value="${(address.phoneNumber?string.computer)!''}" type="phone" id="phoneNumber" class="checkout_input" required="required">
									</div>
									
									<div>
										<!-- Address -->
										<label for="address">Address*</label>
										<input name="address" value="${address.address}" type="text" id="address" class="checkout_input" required="required">
									</div>
									
									<div>
										<!-- City / Town -->
										<label for="city">City/Town*</label>
										<select name="city" id="city" class="dropdown_item_select checkout_input" require="required">
											<option selected>${address.city}</option>
											<option>Panaji</option>
											<option>Margao</option>
											<option>Ponda</option>
											<option>Muapsa</option>
										</select>
									</div>
									
									<div>
										<!-- State -->
										<label for="state">State*</label>
										<select name="state" id="state" class="dropdown_item_select checkout_input" require="required">
											<option selected>Goa</option>
										</select>
									</div>
									
									<div>
										<!-- Country -->
										<label for="country">Country*</label>
										<select name="country" id="country" class="dropdown_item_select checkout_input" require="required">
											<option selected>India</option>
										</select>
									</div>
									
									<div>
										<!-- pinCode -->
										<label for="pinCode">Pincode*</label>
										<input name="pinCode" value="${(address.pinCode?string.computer)!''}" type="number" maxlength="6" id="pinCode" class="checkout_input" required="required">
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
			<#else>
				<div class="m-5">
					<h4>No checkout items</h4>
				<div>
        	</#if>
		</div>
	</div>
</@layout.base>