<#import "base.ftl" as layout />
<#import "header.ftl" as header />
<@layout.base title="Create" css="/static/css/object_create.css" js="/static/js/object_create.js">

    <@header.header user="${user}" title="Create" />

    <div class="upload_box">
        <div class="container">

            <form class="box" method="post" action="/object/create" enctype="multipart/form-data">
                <div class="box__input">
                    <svg class="box__icon" xmlns="http://www.w3.org/2000/svg" width="50" height="43" viewBox="0 0 50 43"><path d="M48.4 26.5c-.9 0-1.7.7-1.7 1.7v11.6h-43.3v-11.6c0-.9-.7-1.7-1.7-1.7s-1.7.7-1.7 1.7v13.2c0 .9.7 1.7 1.7 1.7h46.7c.9 0 1.7-.7 1.7-1.7v-13.2c0-1-.7-1.7-1.7-1.7zm-24.5 6.1c.3.3.8.5 1.2.5.4 0 .9-.2 1.2-.5l10-11.6c.7-.7.7-1.7 0-2.4s-1.7-.7-2.4 0l-7.1 8.3v-25.3c0-.9-.7-1.7-1.7-1.7s-1.7.7-1.7 1.7v25.3l-7.1-8.3c-.7-.7-1.7-.7-2.4 0s-.7 1.7 0 2.4l10 11.6z" /></svg>
                    <input class="box__file" type="file" name="file" id="file" />
                    <label for="file"><strong>Choose a file</strong><span class="box__dragndrop"> or drag it here</span>.</label>
                    <button class="box__button" type="submit">Upload</button>
                </div>
                <div class="box__uploading">Uploading <span></span> …</div>
                <div class="box__slicing">Slicing…</div>
                <div class="box__success">Creating New Object…</div>
                <div class="box__error"><span></span>. <a href="" class="box__restart" role="button">Try again!</a></div>
                <div class="progress_container">
                    <div class="progress-bar" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100"></div>
                </div>
            </form>
        </div>
    </div>
</@layout.base>