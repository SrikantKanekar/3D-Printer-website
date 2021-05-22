<#import "base.ftl" as layout />
<#import "header.ftl" as header />
<@layout.base title="Admin" css="" js="/static/js/admin.js">
    
    <@header.header admin"${admin}" />

    <div class="container" style="padding-top: 110px">
        <#if activeOrders?has_content>
            <h2>Active Orders</h2>
            <#list activeOrders as order>
                <div class="card">
                    <div class="card-header">
                        ID : ${order.id}
                    </div>
                    <div class="card-body">
                        <h5 class="card-title">${order.userEmail} ${order.status}</h5>
                        <a href="/order/${order.id}" class="btn btn-primary">view</a>
                        <div class='btn-group' role='group'>
                            <div type="button" data-status="0" data-id="${order.id}" class="btn btn-default Placed">Placed</div>
                            <div type="button" data-status="1" data-id="${order.id}" class="btn btn-default Placed">Confirmed</div>
                            <div type="button" data-status="2" data-id="${order.id}" class="btn btn-default Processing">Processing</div>
                            <div type="button" data-status="3" data-id="${order.id}" class="btn btn-default delivering">Out for delivery</div>
                            <div type="button" data-status="4" data-id="${order.id}" class="btn btn-default delivered">Delivered</div>
                        </div>
                    </div>
                </div>
            </#list>
        </#if>

        <br><br>

        
        <#if completedOrders?has_content>
            <h2>Completed Orders</h2>
            <#list completedOrders as order>
                <div class="card">
                    <div class="card-header">
                        ID : ${order.id}
                    </div>
                    <div class="card-body">
                        <h5 class="card-title">${order.userEmail} ${order.status}</h5>
                        <a href="/order/${order.id}" class="btn btn-primary">view</a>
                    </div>
                </div>
            </#list>
        </#if>

    </div>
</@layout.base>