<#import "template.ftl" as layout />
<#import "header.ftl" as header />
<@layout.main title="Update" css="" js="/static/js/object.js">

    <@header.header user="${user}" />

    <div class="container" style="padding-top: 110px">
        <div class="mt-5">
            <h2>${object.filename}</h2>
        </div>

        <div class="mt-5">
            <h3>Basic Settings</h3>
            <form action="/object/${object.id}/basic" method="post" id="basic-form">
                <label for="basic" class="form-label">size</label>
                <input type="number" name="size" value="${object.basicSettings.size}" class="form-control" id="basic" />
                <button id="button" type="submit">Update</button>
            </form>
        </div>

        <div class="mt-5">
            <h3>Advanced Settings</h3>
            <form action="/object/${object.id}/advanced" method="post" id="advanced-form">
                <label for="advanced" class="form-label">weight</label>
                <input type="number" name="weight" value="${object.advancedSettings.weight}" class="form-control" id="advanced" />
                <button id="button" type="submit">Update</button>
            </form>
        </div>

        <div class="mt-5">
            <h4>status ${object.status!""}</h4>
            <h4>price ${(object.price)!""}</h4>
            <h4>time ${(object.timeToPrint)!""}</h4>
            <h4>delivery ${(object.dueDelivery)!""}</h4>
        </div>
    </div>

</@layout.main>