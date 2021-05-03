<#import "template.ftl" as layout />
<#import "header.ftl" as header />
<@layout.main title="Update" css="" js="/static/js/order/order.js">

    <@header.header user="${user}" />

    <div class="container" style="padding-top: 110px">
        <div class="mt-5">
            <form action="/order/${order.id}/file" method="post" enctype="multipart/form-data" id="file-form">
                <label for="file" class="form-label">File</label>
                <input type="file" name="file" class="form-control" id="file" />
                <button id="button" type="submit">Upload</button>
            </form>
        </div>

        <div class="mt-5">
            <h3>Basic Settings</h3>
            <form action="/order/${order.id}/basic" method="post" id="basic-form">
                <label for="basic" class="form-label">size</label>
                <input type="number" name="size" value="${order.basicSettings.size}" class="form-control" id="basic" />
                <button id="button" type="submit">Update</button>
            </form>
        </div>

        <div class="mt-5">
            <h3>Advanced Settings</h3>
            <form action="/order/${order.id}/advanced" method="post" id="advanced-form">
                <label for="advanced" class="form-label">weight</label>
                <input type="number" name="weight" value="${order.advancedSettings.weight}" class="form-control" id="advanced" />
                <button id="button" type="submit">Update</button>
            </form>
        </div>

        <div class="mt-5">
            <h4>status ${order.status}</h4>
            <h4>price ${(order.price)!""}</h4>
            <h4>time ${(order.timeInMin)!""}</h4>
            <h4>delivery ${(order.dueDelivery)!""}</h4>
        </div>

        <div class="mt-5">
            <a href="/checkout/${order.id}"><button class="btn btn-primary">Checkout</button></a>
        </div>
    </div>

</@layout.main>