<#import "template.ftl" as layout />
<@layout.main title="Update" css="" js="">
<div class="container">
    <div class="mt-5">
        <form action="/order/${order.id}/file" method="post" enctype="multipart/form-data">
            <label for="file" class="form-label">Update file : ${order.fileName}</label>
            <input type="file" name="file" class="form-control" id="file" />
            <button type="submit" class="btn btn-primary">Update</button>
        </form>
    </div>

    <div class="mt-5">
        <h3>Basic Properties</h3>
        <form action="/order/${order.id}/basic" method="post">
            <label for="basic" class="form-label">size : ${order.basicSettings.size}</label>
            <input type="number" name="size" class="form-control" id="basic" />
            <button type="submit" class="btn btn-primary">Update</button>
        </form>
    </div>

    <div class="mt-5">
        <h3>Advanced Properties</h3>
        <form action="/order/${order.id}/advanced" method="post">
            <label for="advanced" class="form-label">weight : ${order.advancedSettings.weight}</label>
            <input type="number" name="weight" class="form-control" id="advanced" />
            <button type="submit" class="btn btn-primary">Update</button>
        </form>
    </div>

    <div class="mt-5">
        <h4>status ${order.status}</h4>
        <h4>price ${(order.price)!100}</h4>
        <h4>time ${(order.timeInMin)!8}</h4>
        <h4>delivery ${(order.dueDelivery)!"10 May"}</h4>
    </div>

    <div class="mt-5">
        <a href="/checkout/${order.id}"><button class="btn btn-primary">Checkout</button></a>
    </div>
</div>
</@layout.main>