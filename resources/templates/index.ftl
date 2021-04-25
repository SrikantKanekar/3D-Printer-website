<#import "template.ftl" as layout />
<@layout.main title="Home" css="" js="">
    <table class="table">
        <thead class="thead-dark">
            <tr>
                <th scope="col">Email</th>
                <th scope="col">Password</th>
                <th></th>
            </tr>
        </thead>
        <tbody>
            <#list users as user>
            <tr>
                <td>${user.email}</td>
                <td>${user.password}</td>
            </tr>
            </#list>
        </tbody>
    </table>
    <div class="container">
        <div class="row">
            <a href="/auth/login" class="btn btn-secondary float-right" role="button">Login</a>
        </div>
    </div>
</@layout.main>
