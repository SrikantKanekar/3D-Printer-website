<#import "template.ftl" as layout />
<#import "header.ftl" as header />
<@layout.main title="Create" css="" js="">

    <@header.header user="${user}" />

    <div class="container" style="padding-top: 110px">
        <form action="/object/create" method="post" enctype="multipart/form-data">
            <label for="file" class="form-label">Upload file</label>
            <input type="file" name="file" class="form-control" id="file" />
            <button type="submit" class="btn btn-primary">Upload</button>
        </form>
    </div>
</@layout.main>