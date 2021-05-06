<#import "template.ftl" as layout />
<#import "header.ftl" as header />
<@layout.main title="Admin" css="" js="/static/js/admin.js">
    
    <@header.header user="" />

    <div class="container" style="padding-top: 110px">
        <h2>Placed Orders</h2>
        
        <#if processingOrders?has_content>
            <#list processingOrders as order>
                <div class="card">
                    <div class="card-header">
                        ID : ${order.id}
                    </div>
                    <div class="card-body">
                        <h5 class="card-title">${order.fileName}</h5>
                        <a href="/order/${order.id}" class="btn btn-primary">view</a>
                        <div class='btn-group' role='group'>
                            <div type="button" data-href="/admin/printing" data-id="${order.id}" class="btn btn-default printing">Printing</div>
                            <div type="button" data-href="/admin/printed" data-id="${order.id}" class="btn btn-default printed">Printed</div>
                            <div type="button" data-href="/admin/delivering" data-id="${order.id}" class="btn btn-default delivering">Out for delivery</div>
                            <div type="button" data-href="/admin/delivered" data-id="${order.id}" class="btn btn-default delivered">Delivered</div>
                        </div>
                    </div>
                </div>
            </#list>
        <#else>
            <div class="m-5">
                <h4>No orders</h4>
            <div>
        </#if>

        <br><br><br>

        <h2>Completed Orders</h2>
        
        <#if historyOrders?has_content>
            <#list historyOrders as order>
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
                <h4>No orders</h4>
            <div>
        </#if>

    </div>
</@layout.main>