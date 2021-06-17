<#import "base.ftl" as layout />
<#import "header.ftl" as header />
<@layout.base title="Create" css="/static/css/object_create.css" js="/static/js/object_create.js">

    <@header.header user="${user}" title="Create" />

    <div class="container">
        <form class="box" action="#">

            <div class="box_upload">

                <svg class="box_icon" xmlns="http://www.w3.org/2000/svg" width="50" height="43"
                     viewBox="0 0 50 43">
                    <path d="M48.4 26.5c-.9 0-1.7.7-1.7 1.7v11.6h-43.3v-11.6c0-.9-.7-1.7-1.7-1.7s-1.7.7-1.7 1.7v13.2c0 .9.7 1.7 1.7 1.7h46.7c.9 0 1.7-.7 1.7-1.7v-13.2c0-1-.7-1.7-1.7-1.7zm-24.5 6.1c.3.3.8.5 1.2.5.4 0 .9-.2 1.2-.5l10-11.6c.7-.7.7-1.7 0-2.4s-1.7-.7-2.4 0l-7.1 8.3v-25.3c0-.9-.7-1.7-1.7-1.7s-1.7.7-1.7 1.7v25.3l-7.1-8.3c-.7-.7-1.7-.7-2.4 0s-.7 1.7 0 2.4l10 11.6z"/>
                </svg>

                <input class="box_input" type="file" name="file" id="file" accept=".glb"/>

                <label for="file">
                    <strong>Choose a file</strong>
                    <span class="box_dragndrop"> or drag it here</span>.
                </label>
            </div>

            <div class="box_uploading_file">Uploading <span></span> …</div>
            <div class="box_uploading_img">Uploading Image…</div>
            <div class="box_done">Almost Done!</div>
            <div class="box_error">
                <span></span>. <a href="" class="box_restart" role="button">Try again!</a>
            </div>

            <div class="progress_container">
                <div class="progress-bar" role="progressbar" aria-valuenow="0"
                     aria-valuemin="0" aria-valuemax="100">
                </div>
            </div>
        </form>

        <div class="canvas">
            <div class="canvas_container">
                <canvas id="canvas"></canvas>
            </div>
            <div class="canvas_info">
                <div class="canvas_name"></div>
                <div class="canvas_error"></div>
            </div>
            <div class="canvas_buttons">
                <div class="button">
                    <a id="create_button" href="#">Create</a>
                </div>
                <div class="button">
                    <a id="change_button" href="#">Change</a>
                </div>
            </div>
        </div>
    </div>

    <!-- three js -->
    <script src="https://unpkg.com/three@0.129.0/build/three.js"></script>
    <script src="https://unpkg.com/three@0.129.0/examples/js/loaders/GLTFLoader.js"></script>
    <script src="https://unpkg.com/three@0.129.0/examples/js/controls/OrbitControls.js"></script>

    <!-- dat gui -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/dat-gui/0.7.7/dat.gui.js"
            integrity="sha512-u+vtyZDzyd0zgAEffKfoNx2BnCahAOBBYvzGu6vma1B18zQ6lEGF3F6dAFvqaxDoIU/GZRxcXV5oq23OIrkQwg=="
            crossorigin="anonymous" referrerpolicy="no-referrer"></script>

    <script src="/static/js/object_three.js"></script>

    <script src="https://www.gstatic.com/firebasejs/8.6.7/firebase-app.js"></script>
    <script src="https://www.gstatic.com/firebasejs/8.6.7/firebase-storage.js"></script>
    <script src="/static/js/firebase_storage.js"></script>

</@layout.base>