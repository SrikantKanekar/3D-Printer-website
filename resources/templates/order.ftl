<#import "base.ftl" as layout />
<#import "header.ftl" as header />
<@layout.base title="Order" css="/static/css/order.css" js="/static/js/order.js">

    <@header.header user="${user}" title="Order" />
    
    <div class="products">
		<div class="container">
			<div class="row">
				<div class="col">

					<!-- Product Sorting -->
					<div class="sorting_bar d-flex flex-md-row flex-column align-items-md-center justify-content-md-start">
						
						<div class="results" data-id="${order.id}" data-email="${order.userEmail}">
							Order ID : ${order.id}
							Status : ${order.status}
							Objects : <span>${objects?size}</span>
							<#if admin?has_content>
								User: ${order.userEmail}
							</#if>
						</div>

						<#if objects?has_content>
							<div class="sorting_container ml-md-auto">
								<div class="sorting">
									<ul class="item_sorting">
										<li>
											<span class="sorting_text">Sort by</span>
											<i class="fa fa-chevron-down" aria-hidden="true"></i>
											<ul>
												<li class="product_sorting_btn" data-isotope-option='{ "sortBy": "original-order" }'><span>Default</span></li>
												<li class="product_sorting_btn" data-isotope-option='{ "sortBy": "status" }'><span>Status</span></li>
												<li class="product_sorting_btn" data-isotope-option='{ "sortBy": "name" }'><span>Name</span></li>
											</ul>
										</li>
									</ul>
								</div>
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
								<div class="product" data-id="${object.id}" data-status="${object.printingStatus}">
									<div class="product_image"><img src="${object.image}" alt=""></div>
									<div class="product_content">
										
										<div class="product_title">
											<a href="/object/${object.id}">${object.filename}</a>
										</div>
										<div class="product_status">${object.printingStatus}</div>
										
										<#if admin?has_content>
											<div class="product_id">ID : ${object.id}</div>
											<div class='btn-group' role='group'>
												<div data-status="0" class="admin_button pending disabled"><a href="">Pending</a></div>
												<div data-status="1" class="admin_button printing"><a href="">Printing</a></div>
												<div data-status="2" class="admin_button printed"><a href="">Printed</a></div>
											</div>
										</#if>
									</div>
								</div>
							</#list>

						</#if>

					</div>
				</div>
			</div>

			<#if admin?has_content>
				<div class="message_section">
					<div class="section_title">Message Notification</div>
						<div class="section_subtitle">Send custom notification to the user</div>
							<div class="message_form_container">
								<form action="/order/send-message" method="POST" class="message_form" id="message_form">
									<div>
										<!-- title -->
										<label for="title">Title*</label>
										<input name="title" type="text" id="title" class="message_input" required="required">
									</div>
									<div>
										<!-- message -->
										<label for="message">message*</label>
										<input name="message" type="text" id="message" class="message_input" required="required">
									</div>
									
									<div class="button message_button"><a href="#">Send</a></div>
								</form>
							</div>
						</div>
					</div>
				</div>
			</#if>
		</div>
	</div>
</@layout.base>