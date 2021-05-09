<#import "template.ftl" as layout />
<#import "header.ftl" as header />
<@layout.main title="Tracking" css="" js="">

    <@header.header user="${user}" />
    
    <div class="container" style="padding-top: 110px">
        <h2>Track objects</h2>
        
        <#if objects?has_content>
            <#list objects as object>
                <div class="card">
                    <div class="card-header">
                        ID : ${object.id}
                    </div>
                    <div class="card-body">
                        <h5 class="card-title">${object.fileName}</h5>
                        <a href="/object/${object.id}" class="btn btn-primary stretched-link">view</a>
                    </div>
                </div>
            </#list>
        <#else>
            <div class="m-5">
                <h4>No tracking items</h4>
            <div>
        </#if>
    </div>
</@layout.main>