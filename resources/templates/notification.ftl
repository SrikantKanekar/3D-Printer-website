<#import "base.ftl" as layout />
<#import "header.ftl" as header />
<@layout.base title="Notifications" css="/static/css/notification.css" js="/static/js/notification.js">

    <@header.header user="${user}" title="Notifications"/>

	<div class="container">
		<div class="row">
			<div class="col">

				<!-- Notification Sorting -->
				<div class="sorting_bar d-flex flex-md-row flex-column align-items-md-center justify-content-md-start">
					
					<div class="results">Showing <span>${notifications?size}</span> notifications</div>
					
					<#if notifications?has_content>
						<div class="sorting_container ml-md-auto">
							<div class="sorting">
								<ul class="item_sorting">
									<li>
										<span class="sorting_text">Sort by</span>
										<i class="fa fa-chevron-down" aria-hidden="true"></i>
										<ul>
											<li class="product_sorting_btn" data-isotope-option='{ "sortBy": "original-order" }'><span>Date</span></li>
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
				
				<#if notifications?has_content>
					<div class="notification_grid">
					
						<#list notifications as notification>
							<div class="notification">
								<div class="notification_content">
									<div class="notification_title">
										<a href="/notification/${notification.id}">${notification.title}</a>
									</div>
									<div class="notification_message">${notification.message}</div>
									<div class="notification_date">${notification.posted_at}</div>
								</div>
								<hr class="divider">
							</div>
						</#list>

					</div>
				</#if>
			</div>
		</div>
	</div>
	
</@layout.base>