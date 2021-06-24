<#import "base.ftl" as layout />
<#import "header.ftl" as header />
<@layout.base title="Checkout" css="/static/css/checkout.css" js="/static/js/checkout.js">

    <@header.header user="${user}" title="Checkout"/>

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
                                            <input name="firstname" value="${address.firstname}" type="text"
                                                   id="firstname" class="input">
                                        </div>
                                    </div>

                                    <!-- lastname -->
                                    <div class="col-xl-6 last_name_col">
                                        <label for="lastname">Last Name*</label>
                                        <div data-validate="Enter lastname">
                                            <input name="lastname" value="${address.lastname}" type="text" id="lastname"
                                                   class="input">
                                        </div>
                                    </div>

                                </div>

                                <!-- phoneNumber -->
                                <label for="phoneNumber">Phone no*</label>
                                <div data-validate="Please enter phoneNumber">
                                    <input name="phoneNumber" value="${(address.phoneNumber?string.computer)!''}"
                                           type="phone" id="phoneNumber" class="input">
                                </div>

                                <!-- Address -->
                                <label for="address">Address*</label>
                                <div data-validate="Please enter address">
                                    <input name="address" value="${address.address}" type="text" id="address"
                                           class="input">
                                </div>

                                <!-- City / Town -->
                                <label for="city">City/Town*</label>
                                <div data-validate="Please select city">
                                    <select name="city" id="city" class="dropdown_item_select input"
                                            data-value="${address.city}">
                                        <option>Panaji</option>
                                        <option>Margao</option>
                                        <option>Ponda</option>
                                        <option>Muapsa</option>
                                    </select>
                                </div>

                                <!-- State -->
                                <label for="state">State*</label>
                                <div data-validate="Please select state">
                                    <select name="state" id="state" class="dropdown_item_select input"
                                            data-value="${address.state}">
                                        <option>Goa</option>
                                    </select>
                                </div>

                                <!-- Country -->
                                <label for="country">Country*</label>
                                <div data-validate="Please select country">
                                    <select name="country" id="country" class="dropdown_item_select input"
                                            data-value="${address.country}">
                                        <option>India</option>
                                    </select>
                                </div>

                                <!-- pinCode -->
                                <label for="pinCode">PinCode*</label>
                                <div data-validate="Please enter pinCode">
                                    <input name="pinCode" value="${(address.pinCode?string.computer)!''}" type="number"
                                           maxlength="6" id="pinCode" class="input">
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
                            <div class="list_bar">
                                <div class="list_title">Object</div>
                                <div class="list_details">
                                    <div class="list_quantity">Qty</div>
                                    <div class="list_price">Price</div>
                                </div>
                            </div>
                            <ul class="order_list">
                                <#list objects as object>
                                    <li class="list_item object">
                                        <div class="list_title">${object.name}</div>
                                        <div class="list_details">
                                            <div class="list_quantity">X<span>${object.quantity}</span></div>
                                            <div class="list_price">
                                                <i class="fa fa-inr"></i><span>${object.slicingDetails.totalPrice}</span>
                                            </div>
                                        </div>
                                    </li>
                                </#list>
                                <li class="list_item">
                                    <div class="list_title">Subtotal</div>
                                    <div class="list_price subtotal">
                                        <i class="fa fa-inr"></i><span>0</span>
                                    </div>
                                </li>
                                <li class="list_item">
                                    <div class="list_title">Shipping</div>
                                    <div class="list_price">Free</div>
                                </li>
                                <li class="list_item">
                                    <div class="list_title">Total</div>
                                    <div class="list_price total">
                                        <i class="fa fa-inr"></i><span>0</span>
                                    </div>
                                </li>
                            </ul>
                        </div>

                        <div class="button order_button"><a href="#">Place Order</a></div>
                    </div>
                </div>

            </div>
        <#else>
            <div class="no_items_text">No items</div>
        </#if>
    </div>
</@layout.base>