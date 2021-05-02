<#import "template.ftl" as layout />
<@layout.main title="Cart" css="" js="">
    
    <div class="m-5 container">
        <h2>Cart</h2>
        
        <#if orders?has_content>
            <#list orders as order>
                <div class="card">
                    <div class="card-header">
                        ID : ${order.id}
                    </div>
                    <div class="card-body">
                        <h5 class="card-title">${order.fileName}</h5>
                        <a href="/order/${order.id}" class="btn btn-primary stretched-link">view</a>
                        <a href="/cart/${order.id}/remove" class="btn btn-primary ">remove</a>
                    </div>
                </div>
            </#list>
            <div class="m-5">
                <a href="/checkout"><button class="btn btn-primary">Checkout</button></a>
            </div>
        <#else>
            <div class="m-5">
                <h4>No Cart items</h4>
            <div>
        </#if>
    </div>
</@layout.main>