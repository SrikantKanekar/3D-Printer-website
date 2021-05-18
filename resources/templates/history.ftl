<#import "base.ftl" as layout />
<#import "header.ftl" as header />
<@layout.base title="Order history" css="" js="">

    <@header.header user="${user}" />
    
    <div class="container" style="padding-top: 110px">
        <h2>Order history</h2>
        
        <#if orders?has_content>
            <#list orders as order>
                <div class="card">
                    <div class="card-header">
                        ID : ${order.id}
                    </div>
                    <div class="card-body">
                        <h5 class="card-title">${order.userEmail}</h5>
                        <h5 class="card-title">${order.status}</h5>
                        <#list order.objectIds as objectId>
                            <h5 class="card-body">object Id: ${objectId}</h5>
                        </#list>
                        <a href="/order/${order.id}" class="btn btn-primary">view</a>
                    </div>
                </div>
            </#list>
        <#else>
            <div class="m-5">
                <h4>No history items</h4>
            <div>
        </#if>
    </div>
</@layout.base>