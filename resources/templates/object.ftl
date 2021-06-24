<#import "base.ftl" as layout />
<#import "header.ftl" as header />
<@layout.base title="Object" css="/static/css/object.css" js="/static/js/object.js">

    <@header.header user="${user}" title="Object" />

    <div class="object"
         data-id="${object.id}"
         data-status="${object.status}"
         data-file="${object.fileUrl}"
         data-extension="${object.fileExtension}">

        <div class="container">

            <#if object.status == "NONE" || object.status == "CART">
                <div class="row status_none">

                    <!-- Canvas -->
                    <div class="col-lg-6">
                        <div class="canvas_container">
                            <canvas id="canvas"></canvas>
                            <div id="canvasLoader" class="canvas_overlay">
                                <div class="spinner_container">
                                    <span class="spinner"></span>
                                </div>
                            </div>
                            <div class="canvas_error_container">
                                <div></div>
                            </div>
                        </div>
                    </div>

                    <!-- Details -->
                    <div class="col-lg-6">
                        <div class="object_content">

                            <div class="object_name">${object.name}</div>

                            <div class="slicing_pending_text" data-value="${object.slicingDetails.uptoDate?c}">
                                Slicing details are not upto date
                            </div>

                            <div class="slicing_details">
                                <div class="time" data-value="${(object.slicingDetails.time?string.computer)!}">
                                    Time <span></span>
                                </div>
                                <div class="material_weight"
                                     data-value="${(object.slicingDetails.materialWeight)!}">
                                    Material Weight <span></span>g
                                </div>
                                <div class="material_cost" data-value="${(object.slicingDetails.materialCost)!}">
                                    Material Cost <i class="fa fa-inr"></i><span></span>
                                </div>
                                <div class="electricity_cost"
                                     data-value="${(object.slicingDetails.electricityCost)!}">
                                    Electricity Cost <i class="fa fa-inr"></i><span></span>
                                </div>
                                <div class="total_price" data-value="${(object.slicingDetails.totalPrice)!}">
                                    Total Price <i class="fa fa-inr"></i><span></span>
                                </div>
                            </div>

                            <div class="button_container">
                                <div id="slice_button" class="button">
                                    <a href="/object/slice">Slice</a>
                                </div>
                                <div id="cart_button" class="button">
                                    <a href="/object/add-to-cart">Add to cart</a>
                                </div>
                                <div id="remove_cart_button" class="button">
                                    <a href="/object/remove-from-cart">Remove from cart</a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            <#elseif object.status == "TRACKING">
                <div class="row status_tracking">

                    <!-- Video streaming -->
                    <div class="col-12">
                        <video class="video_container" controls>
                            <source src="/static/images/3D printed Eiffel tower time lapse.mp4" type="video/mp4">
                            Your browser does not support the video tag.
                        </video>
                    </div>

                    <div class="col-lg-6">
                        <div class="canvas_container">
                            <canvas id="canvas"></canvas>
                            <div id="canvasLoader" class="canvas_overlay">
                                <div class="spinner_container">
                                    <span class="spinner"></span>
                                </div>
                            </div>
                            <div class="canvas_error_container">
                                <div></div>
                            </div>
                        </div>
                    </div>

                    <!-- Details -->
                    <div class="col-lg-6">
                        <div class="object_content">
                            <div class="object_name">${object.name}</div>
                            <div class="object_description">
                                <div class=""><i class="fa fa-inr"></i>${object.slicingDetails.totalPrice!}</div>
                                <div class="">Printing status : ${object.printingStatus}</div>
                                <div class="started_at" data-value="${object.trackingDetails.started_at!''}">Started at : <span></span></div>
                            </div>
                        </div>
                    </div>
                </div>
            <#elseif object.status == "COMPLETED">
                <div class="row status_completed">
                    <!-- Canvas -->
                    <div class="col-lg-6">
                        <div class="canvas_container">
                            <canvas id="canvas"></canvas>
                            <div id="canvasLoader" class="canvas_overlay">
                                <div class="spinner_container">
                                    <span class="spinner"></span>
                                </div>
                            </div>
                            <div class="canvas_error_container">
                                <div></div>
                            </div>
                        </div>
                    </div>

                    <!-- Details -->
                    <div class="col-lg-6">
                        <div class="object_content">
                            <div class="object_name">${object.name}</div>
                            <div class="object_description">
                                <div class=""><i class="fa fa-inr"></i>${object.slicingDetails.totalPrice!}</div>
                                <div class="">Printing status : ${object.printingStatus}</div>
                                <div class="completed_at" data-value="${object.trackingDetails.completed_at}">Completed on : <span></span></div>
                                <div class="duration" data-value="${object.trackingDetails.started_at}">Printing duration : <span></span></div>
                            </div>
                        </div>
                    </div>
                </div>
            </#if>

            <!--  Settings  -->
            <div class="row setting">
                <div class="col">

                    <div class="setting_title">Settings</div>

                    <form class="form" action="/object/update-setting" id="setting_form">

                        <label class="checkbox_container">Advanced User
                            <input type="checkbox" id="advanced" name="advanced"
                                    ${object.setting.advanced?then('checked','')}>
                            <span class="checkmark"></span>
                        </label>

                        <div class="basic_container">
                            <!-- Basic -->
                            <label for="quality">Quality</label>
                            <div data-validate="Please select value">
                                <select name="quality" id="quality" class="dropdown_item_select input"
                                        data-value="${object.setting.quality}">
                                    <option value="SUPER">Super</option>
                                    <option value="DYNAMIC">Dynamic</option>
                                    <option value="STANDARD">Standard</option>
                                    <option value="LOW">Low</option>
                                </select>
                            </div>

                            <label for="infill">Infill (%)</label>
                            <div data-validate="Value between 0 and 100">
                                <input type="number" name="infill" id="infill" class="input"
                                       value="${object.setting.infill}"/>
                            </div>

                            <label class="checkbox_container">Gradual Infill
                                <input type="checkbox" id="gradual_infill" name="gradual_infill"
                                        ${object.setting.gradualInfill?then('checked','')}>
                                <span class="checkmark"></span>
                            </label>

                            <label class="checkbox_container">Support
                                <input type="checkbox" id="support" name="support"
                                        ${object.setting.support?then('checked','')}>
                                <span class="checkmark"></span>
                            </label>
                        </div>

                        <div class="advanced_container">
                            <!-- Intermediate -->
                            <label for="layer_height">Layer Height (mm)</label>
                            <div data-validate="value between 0.1 and 0.3">
                                <input type="number" name="layer_height" id="layer_height" class="input"
                                       value="${object.setting.layerHeight}"/>
                            </div>

                            <label for="infill_density">Infill Density (%)</label>
                            <div data-validate="Value between 0 and 100">
                                <input type="number" name="infill_density" id="infill_density" class="input"
                                       value="${object.setting.infillDensity}"/>
                            </div>

                            <label for="infill_pattern">Infill Pattern</label>
                            <div data-validate="Please select value">
                                <select name="infill_pattern" id="infill_pattern"
                                        class="dropdown_item_select input"
                                        data-value="${object.setting.infillPattern}">
                                    <option value="LINES">Lines</option>
                                    <option value="GRID">Grid</option>
                                    <option value="TRIANGLES">Triangles</option>
                                    <option value="TRI_HEXAGON">Tri Hexagon</option>
                                    <option value="CUBIC">Cubic</option>
                                    <option value="CUBIC_SUBDIVISION">Cubic Subdivision</option>
                                    <option value="OCTET">Octet</option>
                                    <option value="QUARTER_CUBIC">Quarter Cubic</option>
                                    <option value="CONCENTRIC">Concentric</option>
                                    <option value="ZIG_ZAG">Zig Zag</option>
                                    <option value="CROSS">Cross</option>
                                    <option value="CROSS_3D">Cross 3D</option>
                                    <option value="GYROID">Gyroid</option>
                                </select>
                            </div>

                            <label class="checkbox_container">Generate Support
                                <input type="checkbox" id="generate_support" name="generate_support"
                                        ${object.setting.generateSupport?then('checked','')}>
                                <span class="checkmark"></span>
                            </label>

                            <label for="support_structure">Support Structure</label>
                            <div data-validate="Please select value">
                                <select name="support_structure" id="support_structure"
                                        class="dropdown_item_select input"
                                        data-value="${object.setting.supportStructure}">
                                    <option value="NORMAL">Normal</option>
                                    <option value="TREE">Tree</option>
                                </select>
                            </div>

                            <label for="support_placement">Support Placement</label>
                            <div data-validate="Please select value">
                                <select name="support_placement" id="support_placement"
                                        class="dropdown_item_select input"
                                        data-value="${object.setting.supportPlacement}">
                                    <option value="TOUCHING_BUILD_PLATE">Touching Build Plate</option>
                                    <option value="EVERYWHERE">Everywhere</option>
                                </select>
                            </div>

                            <label for="support_overhang_angle">Support Overhang Angle</label>
                            <div data-validate="value between 0 and 89">
                                <input type="number" name="support_overhang_angle"
                                       id="support_overhang_angle"
                                       class="input"
                                       value="${object.setting.supportOverhangAngle}"/>
                            </div>

                            <label for="support_pattern">Support Pattern</label>
                            <div data-validate="Please select value">
                                <select name="support_pattern" id="support_pattern"
                                        class="dropdown_item_select input"
                                        data-value="${object.setting.supportPattern}">
                                    <option value="LINES">Lines</option>
                                    <option value="GRID">Grid</option>
                                    <option value="TRIANGLES">Triangles</option>
                                    <option value="CONCENTRIC">Concentric</option>
                                    <option value="ZIG_ZAG">Zig Zag</option>
                                    <option value="CROSS">Cross</option>
                                    <option value="GYROID">Gyroid</option>
                                </select>
                            </div>

                            <label for="support_density">Support Density (%)</label>
                            <div data-validate="Value between 0 and 100">
                                <input type="number" name="support_density" id="support_density"
                                       class="input"
                                       value="${object.setting.supportDensity}"/>
                            </div>


                            <!-- Advanced -->
                            <label for="wall_line_width">Wall Line Width (mm)</label>
                            <div data-validate="Value between 0.4 and 1.2">
                                <input type="number" name="wall_line_width" id="wall_line_width"
                                       value="${object.setting.wallLineWidth}" class="input"/>
                            </div>

                            <label for="top_bottom_line_width">Top/Bottom Line Width (mm)</label>
                            <div data-validate="Value between 0.4 and 1.2">
                                <input type="number" name="top_bottom_line_width" id="top_bottom_line_width"
                                       value="${object.setting.topBottomLineWidth}" class="input"/>
                            </div>

                            <label for="wall_thickness">Wall Thickness (mm)</label>
                            <div data-validate="Value between 0.4 and 1.2">
                                <input type="number" name="wall_thickness" id="wall_thickness" class="input"
                                       value="${object.setting.wallThickness}"/>
                            </div>

                            <label for="wall_line_count">Wall Line Count</label>
                            <div data-validate="Value between 2 and 8">
                                <input type="number" name="wall_line_count" id="wall_line_count"
                                       class="input"
                                       value="${object.setting.wallLineCount}"/>
                            </div>

                            <label for="top_thickness">Top Thickness (mm)</label>
                            <div data-validate="Value between 0.8 and 2">
                                <input type="number" name="top_thickness" id="top_thickness"
                                       value="${object.setting.topThickness}" class="input"/>
                            </div>

                            <label for="bottom_thickness">Bottom Thickness (mm)</label>
                            <div data-validate="Value between 0.8 and 2">
                                <input type="number" name="bottom_thickness" id="bottom_thickness"
                                       value="${object.setting.bottomThickness}" class="input"/>
                            </div>

                            <label for="infill_speed">Infill Speed (mm/s)</label>
                            <div data-validate="Value between 25 and 100">
                                <input type="number" name="infill_speed" id="infill_speed"
                                       value="${object.setting.infillSpeed}" class="input"/>
                            </div>

                            <label for="outer_wall_speed">Outer Wall Speed (mm/s)</label>
                            <div data-validate="Value between 25 and 100">
                                <input type="number" name="outer_wall_speed" id="outer_wall_speed"
                                       value="${object.setting.outerWallSpeed}" class="input"/>
                            </div>

                            <label for="inner_wall_speed">Inner Wall Speed (mm/s)</label>
                            <div data-validate="Value between 25 and 100">
                                <input type="number" name="inner_wall_speed" id="inner_wall_speed"
                                       value="${object.setting.innerWallSpeed}" class="input"/>
                            </div>

                            <label for="top_bottom_speed">Top Bottom Speed (mm/s)</label>
                            <div data-validate="Value between 25 and 100">
                                <input type="number" name="top_bottom_speed" id="top_bottom_speed"
                                       value="${object.setting.topBottomSpeed}" class="input"/>
                            </div>

                            <label for="support_speed">Support Speed (mm/s)</label>
                            <div data-validate="Value between 25 and 100">
                                <input type="number" name="support_speed" id="support_speed"
                                       value="${object.setting.supportSpeed}" class="input"/>
                            </div>

                            <label for="print_sequence">Print Sequence</label>
                            <div data-validate="Please select value">
                                <select name="print_sequence" id="print_sequence"
                                        class="dropdown_item_select input"
                                        data-value="${object.setting.printSequence}">
                                    <option value="ALL_AT_ONCE">All At Once</option>
                                    <option value="ONE_AT_A_TIME">One At A Time</option>
                                </select>
                            </div>
                        </div>

                        <div class="form_message"></div>

                        <div id="setting_button" class="button form_submit_button">
                            <a href="#">Update</a>
                        </div>
                    </form>
                </div>
            </div>

            <!--delete button-->
            <div class="delete_object">
                <div>Delete</div>
                <div class="button delete_button">
                    <a href="#">Delete</a>
                </div>
            </div>
        </div>
    </div>


    <!-- three js -->
    <script src="https://unpkg.com/three@0.129.0/build/three.js"></script>
    <script src="https://unpkg.com/three@0.129.0/examples/js/loaders/GLTFLoader.js"></script>
    <script src="https://unpkg.com/three@0.129.0/examples/js/loaders/OBJLoader.js"></script>
    <script src="https://unpkg.com/three@0.129.0/examples/js/loaders/STLLoader.js"></script>
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