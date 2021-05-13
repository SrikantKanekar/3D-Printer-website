<#import "template.ftl" as layout />
<#import "header.ftl" as header />
<@layout.main title="Cart" css="" js="">

    <@header.header user="${user}" />
    
    <div class="container" style="padding-top: 110px">
        <h2>Cart</h2>
        
        <#if objects?has_content>
            <#list objects as object>
                <div class="card">
                    <div class="card-header">
                        ID : ${object.id}
                    </div>
                    <div class="card-body">
                        <h5 class="card-title">${object.filename}</h5>
                        <a href="/object/${object.id}" class="btn btn-primary">view</a>
                        <a href="/cart/${object.id}/remove" class="btn btn-primary ">remove</a>
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