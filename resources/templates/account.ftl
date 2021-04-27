<#import "template.ftl" as layout />
<@layout.main title="Account" css="" js="">
    
    <div class="m-5 container">
        <h2>Account</h2>
        <p>Email : ${user.email}</p>
        <p>username : ${user.username}</p>

        <div class="btn-group">
            <a href="/account/update" class="btn btn-primary">Update</a>
            <a href="/account/reset-password" class="btn btn-primary">Reset Password</a>
            <a href="/account/logout" class="btn btn-primary">Logout</a>
        </div>
    </div>
</@layout.main>