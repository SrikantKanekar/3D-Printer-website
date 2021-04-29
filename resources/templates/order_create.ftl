<#import "template.ftl" as layout />
<@layout.main title="Create" css="" js="">
    <div class="container mt-5">
        <form action="/order/create" method="post" enctype="multipart/form-data">
            <label for="file" class="form-label">Upload file</label>
            <input type="file" name="file" class="form-control" id="file" />
            <button type="submit" class="btn btn-primary">Upload</button>
        </form>
    </div>
</@layout.main>