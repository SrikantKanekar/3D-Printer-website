<#import "base.ftl" as layout />
<#import "header.ftl" as header />
<@layout.base title="Order history" css="/static/css/tracking.css" js="">

    <@header.header user="${user}" title="Order history" />
    
        <div class="container">

            <#if orders?has_content>

                <div class="row">
                    <div class="col">
                        <!-- Column Titles -->
                        <div class="info_columns clearfix">
                            <div class="info_col info_col_order">
                                Order
                            </div>
                            <div class="info_col info_col_status">
                                Status 
                            </div>
                            <div class="info_col info_col_price">
                                Price
                            </div>
                            <div class="info_col info_col_delivery">
                                Delivery
                            </div>
                        </div>
                    </div>
                </div>

                <#list orders as order>
                    <div class="row">
                        <div class="col">
                            <div class="item">
                                
                                <!-- Order -->
                                <div class="item_order">
                                    <div class="item_image">
                                        <img src="${order.image}" alt="" />
                                    </div>
                                    <div class="item_name_container">
                                        <div class="item_name">
                                            <a href="/order/${order.id}">${order.id}</a>
                                        </div>
                                        <div class="item_objects">
                                            ${order.objectIds?size} objects
                                        </div>
                                    </div>
                                </div>
                                
                                <!-- Status -->
                                <div class="item_status">${order.status}</div>
                                
                                <!-- price -->
                                <div class="item_price"><i class="fa fa-inr"></i>${order.price}</div>
                                
                                <!-- delivery -->
                                <div class="item_delivery">${order.deliveryDays} June</div>
                            </div>
                        </div>
                    </div>
                </#list>

            <#else>
                <div class="no_items_text">No items</div>
            </#if>

        </div>
</@layout.base>