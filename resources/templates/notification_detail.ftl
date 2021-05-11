<#import "template.ftl" as layout />
<#import "header.ftl" as header />
<@layout.main title="Notification" css="" js="">

    <@header.header user="${user}" />
    
    <div class="container" style="padding-top: 110px">
        <div class="card">
            <div class="card-header">
                ID : ${notification.id}
            </div>
            <div class="card-body">
                <h3 class="card-title">${notification.title}</h3>
                <h4 class="card-title">${notification.message}</h4>
                <h5 class="card-footer">${notification.posted_at}</h5>
            </div>
        </div>
    </div>
</@layout.main>