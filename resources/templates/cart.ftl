<#import "base.ftl" as layout />
<#import "header.ftl" as header />
<@layout.base title="Cart" css="/static/css/cart.css" js="/static/js/cart.js">

    <@header.header user="${user}" title="Cart" />
    
    <div class="container">

        <#if objects?has_content>
            <div class="cart">

                <!--Column Info-->
                <div class="row">
                    <div class="col">
                        <div class="info_column clearfix">
                            <div class="info_object">Object</div>
                            <div class="info_price">Price</div>
                            <div class="info_quantity">Qty</div>
                        </div>
                    </div>
                </div>

                <!--Cart items-->
                <div class="row">
                    <div class="col">
                        <div class="cart_grid">
                            <#list objects as object>

                                <div class="cart_item" data-id="${object.id}">

                                    <!-- Object -->
                                    <div class="item_object">

                                        <div class="item_image">
                                            <img src="${object.imageUrl}" alt=""/>
                                        </div>

                                        <div class="item_name_container">
                                            <div class="item_name">
                                                <a href="/object/${object.id}">${object.name}</a>
                                            </div>
                                            <div class="item_remove">
                                                <a href="/cart/remove">remove</a>
                                            </div>
                                        </div>
                                    </div>

                                    <!-- Price -->
                                    <div class="item_price"><i class="fa fa-inr"></i> ${object.slicingDetails.totalPrice}</div>

                                    <!-- Quantity -->
                                    <div class="item_quantity">
                                        <div class="quantity">
                                            <span>Qty</span>

                                            <input type="number" value="${object.quantity}"/>

                                            <div class="quantity_buttons">
                                                <div class="quantity_inc quantity_control">
                                                    <i class="fa fa-chevron-up" aria-hidden="true"></i>
                                                </div>

                                                <div class="quantity_dec quantity_control">
                                                    <i class="fa fa-chevron-down" aria-hidden="true"></i>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </#list>
                        </div>
                    </div>
                </div>

                <!--  clear button  -->
                <div class="row">
                    <div class="col button_container">
                        <div class="button clear_cart_button">
                            <a href="#">Clear cart</a>
                        </div>
                    </div>
                </div>

                <div class="row row_extra">

                    <!-- Coupon Code -->
                    <div class="col-lg-4">
                        <div class="section_title">Coupon code</div>
                        <div class="section_subtitle">Enter your coupon code</div>
                        <div class="coupon_form_container">
                            <form action="#" id="coupon_form" class="coupon_form">
                                <input type="text" class="coupon_input"/>
                                <button class="button coupon_button">
                                    <span>Apply</span>
                                </button>
                            </form>
                        </div>
                    </div>

                    <!-- Cart Total -->
                    <div class="col-lg-6 offset-lg-2">
                        <div class="cart_total">
                            <div class="section_title">Cart total</div>
                            <div class="section_subtitle">Final info</div>
                            <div class="total_container">
                                <div class="list_bar">
                                    <div class="list_title">Object</div>
                                    <div class="list_details">
                                        <div class="list_quantity">Qty</div>
                                        <div class="list_price">Price</div>
                                    </div>
                                </div>
                                <ul>
                                    <li class="total_grid">
                                        <#list objects as object>
                                            <div class="total_item grid" data-id="${object.id}">
                                                <div class="total_title">${object.name}</div>
                                                <div class="total_details">
                                                    <div class="total_quantity">
                                                        x<span>${object.quantity}</span>
                                                    </div>
                                                    <div class="total_value">
                                                        <i class="fa fa-inr"></i>
                                                        <span>${object.slicingDetails.totalPrice}</span>
                                                    </div>
                                                </div>
                                            </div>
                                        </#list>
                                    </li>
                                    <li class="total_item">
                                        <div class="total_title">Subtotal</div>
                                        <div class="total_value subtotal">
                                            <i class="fa fa-inr"></i> <span>0</span>
                                        </div>
                                    </li>
                                    <li class="total_item">
                                        <div class="total_title">Shipping</div>
                                        <div class="total_value">Free</div>
                                    </li>
                                    <li class="total_item">
                                        <div class="total_title">Total</div>
                                        <div class="total_value total">
                                            <i class="fa fa-inr"></i> <span>0</span>
                                        </div>
                                    </li>
                                </ul>
                            </div>
                            <div class="button checkout_button">
                                <a href="/checkout">Proceed to checkout</a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        <#else>
            <div class="no_items_text">No items</div>
        </#if>
    </div>
</@layout.base>