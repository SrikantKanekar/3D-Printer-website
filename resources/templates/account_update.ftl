<#import "base.ftl" as layout />
<#import "header.ftl" as header />
<@layout.base title="Account Update" css="" js="">
    
    <@header.header user="${user}" />

    <div class="container" style="padding-top: 110px">
        <form action="/account/update" method="post" class="m-5">
            <div class="mb-3">
                <label for="exampleInputUsername" class="form-label">Username</label>
                <input type="text" name="username" value="${user.username}" class="form-control" id="exampleInputUsername" aria-describedby="usernameHelp">
                <div id="usernameHelp" class="form-text">enter username</div>
            </div>
            <button type="submit" class="btn btn-primary">Update</button>
        </form>
    </div>

</@layout.base>