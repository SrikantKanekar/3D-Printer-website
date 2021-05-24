<#import "base.ftl" as layout />
<#import "header.ftl" as header />
<@layout.base title="Notifications" css="" js="">

    <@header.header user="${user}" title="Notifications"/>
    
    <div class="container" style="padding-top: 110px">
        <#if notifications?has_content>
            <h2>Notifications</h2>
            <#list notifications as notification>
                <div class="card">
                    <div class="card-header">
                        ID : ${notification.id}
                    </div>
                    <div class="card-body">
                        <h4 class="card-title">${notification.title}</h4>
                        <h5 class="card-footer">${notification.posted_at}</h5>
                        <a href="/notification/${notification.id}" class="btn btn-primary">view</a>
                    </div>
                </div>
            </#list>
        <#else>
            <div class="m-5">
                <h4>No Notifications</h4>
            <div>
        </#if>
    </div>
</@layout.base>