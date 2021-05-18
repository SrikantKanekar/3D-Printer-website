<#import "base.ftl" as layout />
<#import "header.ftl" as header />
<@layout.base title="Account" css="" js="">
    
    <@header.header user="${user}" />

    <div class="container" style="padding-top: 110px">
        <h2>Account</h2>
        <p>Email : ${user.email}</p>
        <p>username : ${user.username}</p>
    </div>
</@layout.base>