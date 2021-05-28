<#import "base.ftl" as layout />
<#import "header.ftl" as header />
<@layout.base title="Admin" css="/static/css/admin.css" js="/static/js/admin.js">
    
    <@header.header user="${user}" title="Admin" />

	<div class="container">
		
		<#if activeOrders?has_content>
			<div class="row">
				<div class="col">

					<!-- Product Sorting -->
					<div class="sorting_bar d-flex flex-md-row flex-column align-items-md-center justify-content-md-start">
						
						<div class="results">
							<span>${activeOrders?size}</span> active orders
						</div>

						<div class="sorting_container ml-md-auto">
							<div class="sorting">
								<ul class="item_sorting">
									<li>
										<span class="sorting_text">Sort by</span>
										<i class="fa fa-chevron-down" aria-hidden="true"></i>
										<ul>
											<li class="product_sorting_btn" data-isotope-option='{ "sortBy": "original-order" }'><span>Default</span></li>
											<li class="product_sorting_btn" data-isotope-option='{ "sortBy": "status" }'><span>Status</span></li>
											<li class="product_sorting_btn" data-isotope-option='{ "sortBy": "user" }'><span>User</span></li>
											<li class="product_sorting_btn" data-isotope-option='{ "sortBy": "price" }'><span>Price</span></li>
											<li class="product_sorting_btn" data-isotope-option='{ "sortBy": "objects" }'><span>Objects</span></li>
										</ul>
									</li>
								</ul>
							</div>
						</div>
						
					</div>

				</div>
			</div>

			<div class="row">
				<div class="col">
					<div class="product_grid">
					
						<#list activeOrders as order>
						
							<div class="product" data-id="${order.id}">
								<div class="product_image"><img src="${order.image}" alt=""></div>
								<div class="product_content">
								
									<div class="product_id">
										<a href="/order/${order.id}">ID : ${order.id}</a>
									</div>

									<div class="product_details">
										<div class="product_details_content user_email">${order.userEmail}</div>
										<div class="product_details_content price"><i class="fa fa-inr"></i><span>${order.price}<span></div>
									</div>

									<div class="product_details">
										<div class="product_details_content status">${order.status}</div>
										<div class="product_details_content size"><span>${order.objectIds?size}</span> objects</div>
									</div>
									
									<div class='btn-group' role='group'>
										<div data-status="0" class="admin_button placed disabled"><a href="">Placed</a></div>
										<div data-status="1" class="admin_button confirmed"><a href="">Confirmed</a></div>
										<div data-status="2" class="admin_button processing"><a href="">Processing</a></div>
										<div data-status="3" class="admin_button delivering"><a href="">Delivering</a></div>
										<div data-status="4" class="admin_button delivered"><a href="">Delivered</a></div>
									</div>
								</div>
							</div>
							
						</#list>

					</div>
				</div>
			</div>
		<#else>
            <div class="no_items_text">No items</div>
		</#if>

	</div>

</@layout.base>