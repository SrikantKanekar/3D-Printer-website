<#import "template.ftl" as layout />
<#import "header.ftl" as header />
<@layout.main title="Order" css="" js="/static/js/order.js">

    <@header.header user="${user}" admin="${admin}"/>
    
    <div class="container" style="padding-top: 110px">
        <h2 class="OrderId" data-id="${order.id}">Order ID: ${order.id}</h2>
        <h3>Status: ${order.status}</h3>
        <h3>User: ${order.userEmail}</h3>
        
        <#list order.objectIds as objectId>
            <div class="card">
                <div class="card-header">
                    ID : ${objectId} 
                </div>
                <div class="card-body">
                    <a href="/object/${objectId}" class="btn btn-primary stretched-link">view</a>
                    <#if admin?has_content>
                        <div class='btn-group' role='group'>
                            <div type="button" data-status="0" data-id="${objectId}" class="btn btn-default None">None</div>
                            <div type="button" data-status="1" data-id="${objectId}" class="btn btn-default Printing">Printing</div>
                            <div type="button" data-status="2" data-id="${objectId}" class="btn btn-default Printed">Printed</div>
                        </div>
                    </#if>
                </div>
            </div>
        </#list>
    </div>
</@layout.main>