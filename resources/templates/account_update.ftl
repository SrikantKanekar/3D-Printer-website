<#import "template.ftl" as layout />
<@layout.main title="Account Update" css="" js="">
    <div class="container">
        <form action="/account/update" method="post" class="m-5">
            <div class="mb-3">
                <label for="exampleInputUsername" class="form-label">Username</label>
                <input type="text" name="username" class="form-control" id="exampleInputUsername" aria-describedby="usernameHelp">
                <div id="usernameHelp" class="form-text">enter username</div>
            </div>
            <button type="submit" class="btn btn-primary">Update</button>
        </form>
    </div>
</@layout.main>