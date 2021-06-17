<#import "base.ftl" as layout />
<#import "header.ftl" as header />
<@layout.base title="My objects" css="/static/css/objects.css" js="/static/js/objects.js">

    <@header.header user="${user}" title="My objects" />

    <div class="container">
        <div class="row">
            <div class="col">

                <!--Sorting -->
                <div class="sorting_bar">

                    <div class="results">Showing <span>${objects?size}</span> objects</div>

                    <#if objects?has_content>
                        <div class="sorting_container">
                            <ul class="sorting">
                                <li>
                                    <span class="sorting_text">Sort by</span>
                                    <i class="fa fa-chevron-down" aria-hidden="true"></i>
                                    <ul>
                                        <li class="sorting_button" data-isotope-option='{ "sortBy": "original-order" }'>
                                            <span>Date</span>
                                        </li>
                                        <li class="sorting_button" data-isotope-option='{ "sortBy": "status" }'>
                                            <span>Status</span>
                                        </li>
                                        <li class="sorting_button" data-isotope-option='{ "sortBy": "name" }'>
                                            <span>Name</span>
                                        </li>
                                    </ul>
                                </li>
                            </ul>
                        </div>
                    </#if>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col">
                <div class="product_grid">

                    <#if objects?has_content>

                        <#list objects as object>

                            <!-- Product -->
                            <div class="product" data-id="${object.id}" data-status="${object.status}">
                                <div class="product_image">
                                    <img src="${object.imageUrl}" alt="">
                                </div>
                                <div class="product_content">
                                    <div class="product_title">
                                        <a href="/object/${object.id}">${object.name}</a>
                                    </div>

                                    <div class="slicing_pending" data-uptoDate="${object.slicingDetails.uptoDate?c}">
                                        Slicing pending
                                    </div>
                                    <div class="slicing_done">
                                        <div class="add_to_cart">
                                            <a href="/objects/add-to-cart">
                                                <svg version="1.1" xmlns="http://www.w3.org/2000/svg"
                                                     x="0px" y="0px"
                                                     viewBox="0 0 489 489"
                                                     style="enable-background: new 00 489 489;"
                                                     xml:space="preserve">
												<g>
                                                    <path d="M440.1,422.7l-28-315.3c-0.6-7-6.5-12.3-13.4-12.3h-57.6C340.3,42.5,297.3,0,244.5,0s-95.8,42.5-96.6,95.1H90.3
													c-7,0-12.8,5.3-13.4,12.3l-28,315.3c0,0.4-0.1,0.8-0.1,1.2c0,35.9,32.9,65.1,73.4,65.1h244.6c40.5,0,73.4-29.2,73.4-65.1
													C440.2,423.5,440.2,423.1,440.1,422.7z M244.5,27c37.9,0,68.8,30.4,69.6,68.1H174.9C175.7,57.4,206.6,27,244.5,27z M366.8,462
													H122.2c-25.4,0-46-16.8-46.4-37.5l26.8-302.3h45.2v41c0,7.5,6,13.5,13.5,13.5s13.5-6,13.5-13.5v-41h139.3v41
													c0,7.5,6,13.5,13.5,13.5s13.5-6,13.5-13.5v-41h45.2l26.9,302.3C412.8,445.2,392.1,462,366.8,462z"/>
                                                </g>
											</svg>
                                            </a>
                                        </div>
                                        <div class="price">
                                            <i class="fa fa-inr"></i><span>${object.slicingDetails.totalPrice!}</span>
                                        </div>
                                    </div>
                                    <div class="status_cart">
                                        CART
                                    </div>
                                    <div class="status_tracking">
                                        TRACKING
                                    </div>
                                    <div class="status_completed">
                                        COMPLETED
                                    </div>
                                </div>
                            </div>
                        </#list>
                    </#if>
                </div>
            </div>
        </div>
    </div>
</@layout.base>