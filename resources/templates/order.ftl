<#import "base.ftl" as layout />
<#import "header.ftl" as header />
<@layout.base title="Order" css="/static/css/order.css" js="/static/js/order.js">

    <@header.header user="${user}" title="Order" />
    
	<div class="container">

		<div class="row">
			<div class="col">

				<!-- Product Sorting -->
				<div class="sorting_bar">
					
					<div class="results" data-id="${order.id}" data-email="${order.userEmail}">
						Order ID : ${order.id}
						Status : ${order.status}
						Objects : <span>${objects?size}</span>
						<#if admin?has_content>
							User: ${order.userEmail}
						</#if>
					</div>

					<#if objects?has_content>
						<div class="sorting_container">
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
											<div data-status="0" class="button admin pending disabled"><a href="">Pending</a></div>
											<div data-status="1" class="button admin printing"><a href="">Printing</a></div>
											<div data-status="2" class="button admin printed"><a href="">Printed</a></div>
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
			<div class="row">
				<div class="message_section col-lg-6">
					<div class="form_title">Message Notification</div>
					<div class="form_subtitle">Send custom notification to the user</div>
					<div class="form_container">
						<form class="form" action="/order/send-message" method="POST" id="message_form">
							
							<!--  title  -->
							<div data-validate="Please enter title">
								<input name="title" type="text" class="input">
							</div>
							
							<!-- message -->
							<div data-validate="Please enter message">
								<input name="message" type="text" class="input">
							</div>
							
							<div id="message_button" class="button form_submit_button"><a href="#">Send</a></div>
						</form>
					</div>
				</div>
			</div>
		</#if>

	</div>

</@layout.base>