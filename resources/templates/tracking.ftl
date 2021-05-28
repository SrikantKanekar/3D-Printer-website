<#import "base.ftl" as layout />
<#import "header.ftl" as header />
<@layout.base title="Tracking" css="/static/css/tracking.css" js="">

    <@header.header user="${user}" title="Tracking" />

    <div class="container">

        <#if orders?has_content>

            <div class="row">
                <div class="col">
                    <!-- Column Titles -->
                    <div class="tracking_info_columns clearfix">
                        <div class="tracking_info_col tracking_info_col_product">
                            Order ID
                        </div>
                        <div class="tracking_info_col tracking_info_col_price">
                            Status 
                        </div>
                        <div class="tracking_info_col tracking_info_col_quantity">
                            Price
                        </div>
                        <div class="tracking_info_col tracking_info_col_total">
                            Delivery by
                        </div>
                    </div>
                </div>
            </div>

            <#list orders as order>
                <div class="row tracking_items_row">
                    <div class="col">
                        <!-- tracking Item -->
                        <div class="
                                    tracking_item
                                    d-flex
                                    flex-lg-row flex-column
                                    align-items-lg-center align-items-start
                                    justify-content-start
                                ">
                            <!-- Name -->
                            <div class="
                                        tracking_item_product
                                        d-flex
                                        flex-row
                                        align-items-center
                                        justify-content-start
                                    ">
                                <div class="tracking_item_image">
                                    <div>
                                        <img src="${order.image}" alt="" />
                                    </div>
                                </div>
                                <div class="tracking_item_name_container">
                                    <div class="tracking_item_name">
                                        <a href="/order/${order.id}">${order.id}</a>
                                    </div>
                                    <div class="tracking_item_objects">
                                        ${order.objectIds?size} objects
                                    </div>
                                </div>
                            </div>
                            <!-- Status -->
                            <div class="tracking_item_status">${order.status}</div>
                            <!-- Quantity -->
                            <div class="tracking_item_price"><i class="fa fa-inr"></i>${order.price}</div>
                            <!-- Total -->
                            <div class="tracking_item_delivery">${order.deliveryDays}</div>
                        </div>
                    </div>
                </div>
            </#list>

        <#else>
            <div class="no_items_text">No items</div>
        </#if>

    </div>
</@layout.base>