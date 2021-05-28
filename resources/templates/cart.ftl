<#import "base.ftl" as layout />
<#import "header.ftl" as header />
<@layout.base title="Cart" css="/static/css/cart.css" js="/static/js/cart.js">

    <@header.header user="${user}" title="Cart" />
    
    <div class="container">
        
        <#if objects?has_content>
            <div class="cart">
                <div class="row">
                    <div class="col">
                        <div class="cart_info_columns clearfix">
                            <div class="cart_info_col cart_info_col_object">
                                Object
                            </div>
                            <div class="cart_info_col cart_info_col_price">
                                Price
                            </div>
                            <div class="cart_info_col cart_info_col_quantity">
                                Qty
                            </div>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="col">
                        <div class="cart_grid">
                            <#list objects as object>       
                                <div class="cart_item" data-id="${object.id}">
                                    
                                    <!-- Name -->
                                    <div class="cart_item_object">
                                        
                                        <div class="cart_item_image">
                                            <div><img src="${object.image}" alt=""/></div>
                                        </div>
                                        
                                        <div class="cart_item_name_container">
                                            <div class="cart_item_name">
                                                <a href="/object/${object.id}">${object.filename}</a>
                                            </div>
                                            <div class="cart_item_remove">
                                                <a href="/cart/remove">remove</a>
                                            </div>
                                        </div>
                                    </div>
                                    
                                    <!-- Price -->
                                    <div class="cart_item_price"><i class="fa fa-inr"></i> ${object.price}</div>
                                    
                                    <!-- Quantity -->
                                    <div class="cart_item_quantity">
                                        <div class="product_quantity_container">
                                            <div class="product_quantity clearfix">
                                                <span>Qty</span>
                                                
                                                <input class="quantity_input" type="number" name="quantity" value="${object.quantity}" />
                                                
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
                                <input type="text" class="coupon_input" required="required" />
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
                            <div class="cart_total_container">
                                <ul>
                                    <li class="total_grid">
                                        <#list objects as object>  
                                            <div class="cart_total_item grid" data-id="${object.id}">
                                                <div class="cart_total_title">${object.filename}</div>
                                                <div class="cart_total_details">
                                                    <div class="cart_total_quantity">X<span>${object.quantity}</span></div>
                                                    <div class="cart_total_value"><i class="fa fa-inr"></i> <span>${object.price}</span></div>
                                                </div>
                                            </div>
                                        </#list>
                                    </li>
                                    <li class="cart_total_item">
                                        <div class="cart_total_title">Subtotal</div>
                                        <div class="cart_total_value subtotal"><i class="fa fa-inr"></i> <span>0</span></div>
                                    </li>
                                    <li class="cart_total_item">
                                        <div class="cart_total_title">Shipping</div>
                                        <div class="cart_total_value">Free</div>
                                    </li>
                                    <li class="cart_total_item">
                                        <div class="cart_total_title">Total</div>
                                        <div class="cart_total_value total"><i class="fa fa-inr"></i> <span>0</span></div>
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