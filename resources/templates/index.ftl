<#import "base.ftl" as layout />
<#import "header.ftl" as header />
<@layout.base title="Home" css="/static/css/index.css" js="/static/js/index.js">
    
	<@header.header user="${user}" />

</@layout.base>
