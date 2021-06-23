<#import "base.ftl" as layout />
<#import "header.ftl" as header />
<@layout.base title="Notification" css="/static/css/notification.css" js="/static/js/notification.js">

    <@header.header user="${user}" title="Notification" />

    <div class="container">
        <div class="notification_detail_content">
            <div class="notification_detail_title">
                ${notification.title}
            </div>
            <div class="notification_detail_message">${notification.message}</div>
            <div class="notification_detail_date" data-value="${notification.posted_at}"></div>
        </div>
    </div>
</@layout.base>