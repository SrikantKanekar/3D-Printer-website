<#import "template.ftl" as layout />
<#import "header.ftl" as header />
<@layout.main title="History" css="" js="">

    <@header.header user="${user}" />
    
    <div class="container" style="padding-top: 110px">
        <h2>History Orders</h2>
        
        <#if orders?has_content>
            <#list orders as order>
                <div class="card">
                    <div class="card-header">
                        ID : ${order.id}
                    </div>
                    <div class="card-body">
                        <h5 class="card-title">${order.fileName}</h5>
                        <a href="/order/${order.id}" class="btn btn-primary stretched-link">view</a>
                    </div>
                </div>
            </#list>
        <#else>
            <div class="m-5">
                <h4>No history items</h4>
            <div>
        </#if>
    </div>
</@layout.main>