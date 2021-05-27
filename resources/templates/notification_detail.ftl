<#import "base.ftl" as layout />
<#import "header.ftl" as header />
<@layout.base title="Notification" css="/static/css/notification.css" js="/static/js/notification.js">

    <@header.header user="${user}" title="Notification" />

		<div class="container">
			<div class="row">
				<div class="col">
                                        	
                    <div class="notification_content">
                        <div class="notification_title">
                            <a href="/notification/${notification.id}">${notification.title}</a>
                        </div>
                        <div class="notification_message">${notification.message}</div>
                        <div class="notification_date">${notification.posted_at}</div>
                    </div>
                    
				</div>
			</div>
		</div>
</@layout.base>