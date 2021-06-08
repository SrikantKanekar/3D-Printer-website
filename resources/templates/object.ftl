<#import "base.ftl" as layout />
<#import "header.ftl" as header />
<@layout.base title="Object" css="/static/css/object.css" js="/static/js/object.js">

    <@header.header user="${user}" title="Object" />

    <div class="object" data-id="${object.id}" data-status="${object.status}">
        <div class="container">

            <div class="status_none">
                <div class="row">

                    <!-- Image -->
                    <div class="col-lg-6">
                        <div class="image_selected">
                            <img src="${object.image}" alt="">
                        </div>
                        <div class="thumbnail_images">
                            <div class="thumbnail_image active" data-image="${object.image}">
                                <img src="${object.image}" alt="">
                            </div>
                            <div class="thumbnail_image" data-image="/static/images/3d-image2.jpg">
                                <img src="/static/images/3d-image2.jpg" alt="">
                            </div>
                            <div class="thumbnail_image" data-image="/static/images/3d-image3.jpeg">
                                <img src="/static/images/3d-image3.jpeg" alt="">
                            </div>
                            <div class="thumbnail_image" data-image="/static/images/3d-image4.jpeg">
                                <img src="/static/images/3d-image4.jpeg" alt="">
                            </div>
                        </div>
                    </div>

                    <!-- Details -->
                    <div class="col-lg-6">
                        <div class="object_content">

                            <div class="object_name">${object.filename}</div>
                            <div class="object_description"><i class="fa fa-inr"></i>${object.price}</div>
                            <div class="object_description">${object.timeToPrint} minutes for printing</div>

                            <div class="cart_buttons_container">
                                <div class="quantity">
                                    <span>Qty</span>

                                    <input type="number" value="${object.quantity}">

                                    <div class="quantity_buttons">

                                        <div class="quantity_inc quantity_control">
                                            <i class="fa fa-chevron-up" aria-hidden="true"></i>
                                        </div>

                                        <div class="quantity_dec quantity_control">
                                            <i class="fa fa-chevron-down" aria-hidden="true"></i>
                                        </div>
                                    </div>
                                </div>
                                <div class="button cart_button">
                                    <a href="/object/add-to-cart">Add to cart</a>
                                </div>
                            </div>

                            <div class="remove_cart_button_container">
                                <div class="button cart_remove_button">
                                    <a href="/object/remove-from-cart">Remove from cart</a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!--  Settings  -->
                <div class="row">
                    <div class="col">

                        <ul class="nav nav-tabs" id="myTab" role="tablist">
                            <li class="nav-item">
                                <a class="active" id="basic-tab" data-toggle="tab" href="#basic"
                                   role="tab" aria-controls="basic" aria-selected="true">Basic Setting
                                </a>
                            </li>
                            <li class="nav-item">
                                <a id="advanced-tab" data-toggle="tab" href="#advanced" role="tab"
                                   aria-controls="advanced" aria-selected="true">Advanced Setting
                                </a>
                            </li>
                        </ul>

                        <div class="tab-content" id="myTabContent">
                            <div class="tab-pane fade show active" id="basic" role="tabpanel"
                                 aria-labelledby="basic-tab">
                                <form class="form" action="/object/update/basic-settings" id="basic_settings_form">

                                    <label for="layer_height">Layer Height (mm)</label>
                                    <div data-validate="value between 0.1 and 0.3">
                                        <input type="number" name="layer_height" id="layer_height" class="input"
                                               value="${object.basicSettings.layerHeight}"/>
                                    </div>

                                    <label for="wall_thickness">Wall Thickness (mm)</label>
                                    <div data-validate="value should be greater than 0.1">
                                        <input type="number" name="wall_thickness" id="wall_thickness" class="input"
                                               value="${object.basicSettings.wallThickness}"/>
                                    </div>

                                    <label for="infill_density">Infill Density (%)</label>
                                    <div data-validate="Value between 0 and 100">
                                        <input type="number" name="infill_density" id="infill_density" class="input"
                                               value="${object.basicSettings.infillDensity}"/>
                                    </div>

                                    <label for="infill_pattern">Infill Pattern</label>
                                    <div data-validate="Please select value">
                                        <select name="infill_pattern" id="infill_pattern"
                                                class="dropdown_item_select input">
                                            <option value="${object.basicSettings.infillPattern}" selected></option>
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
                                                ${object.basicSettings.generateSupport?then('checked','')}>
                                        <span class="checkmark"></span>
                                    </label>

                                    <label for="support_structure">Support Structure</label>
                                    <div data-validate="Please select value">
                                        <select name="support_structure" id="support_structure"
                                                class="dropdown_item_select input">
                                            <option value="${object.basicSettings.supportStructure}" selected></option>
                                            <option value="NORMAL">Normal</option>
                                            <option value="TREE">Tree</option>
                                        </select>
                                    </div>

                                    <label for="support_placement">Support Placement</label>
                                    <div data-validate="Please select value">
                                        <select name="support_placement" id="support_placement"
                                                class="dropdown_item_select input">
                                            <option value="${object.basicSettings.supportPlacement}" selected></option>
                                            <option value="TOUCHING_BUILD_PLATE">Touching Build Plate</option>
                                            <option value="EVERYWHERE">Everywhere</option>
                                        </select>
                                    </div>

                                    <label for="support_overhang_angle">Support Overhang Angle</label>
                                    <div data-validate="value between 0 and 360">
                                        <input type="number" name="support_overhang_angle" id="support_overhang_angle"
                                               class="input"
                                               value="${object.basicSettings.supportOverhangAngle}"/>
                                    </div>

                                    <label for="support_pattern">Support Pattern</label>
                                    <div data-validate="Please select value">
                                        <select name="support_pattern" id="support_pattern"
                                                class="dropdown_item_select input">
                                            <option value="${object.basicSettings.supportPattern}" selected></option>
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
                                        <input type="number" name="support_density" id="support_density" class="input"
                                               value="${object.basicSettings.supportDensity}"/>
                                    </div>

                                    <div class="form_message"></div>

                                    <div id="basic_button" class="button form_submit_button">
                                        <a href="#">Update</a>
                                    </div>
                                </form>
                            </div>

                            <div class="tab-pane fade" id="advanced" role="tabpanel" aria-labelledby="advanced-tab">
                                <form class="form" action="/object/update/advanced-settings"
                                      id="advanced_settings_form">

                                    <label for="weight">Weight</label>
                                    <div data-validate="Please enter weight">
                                        <input type="number" name="weight" id="weight"
                                               value="${object.advancedSettings.weight?string.computer}" class="input"/>
                                    </div>
                                    <div class="form_message"></div>
                                    <div id="advanced_button" class="button form_submit_button">
                                        <a href="#">Update</a>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="status_tracking">
                <div class="row">

                    <!-- Video streaming -->
                    <div class="col-lg-6">
                        <video class="video_container" controls>
                            <source src="/static/images/3D printed Eiffel tower time lapse.mp4" type="video/mp4">
                            Your browser does not support the video tag.
                        </video>
                    </div>

                    <!-- Details -->
                    <div class="col-lg-6">
                        <div class="object_content">
                            <div class="object_name">${object.filename}</div>
                            <div class="object_description"><i class="fa fa-inr"></i>${object.price}</div>
                            <div class="object_description">Printing status : ${object.printingStatus}</div>

                            <div class="object_description">Started at : ${object.trackingDetails.started_at!"--"}</div>
                            <div class="object_description">Expected to be completed by <span>5:34 pm</span></div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="status_completed">
                <div class="row">
                    <!-- Image -->
                    <div class="col-lg-6">
                        <div class="details_image">
                            <div class="image_selected">
                                <img src="${object.image}" alt="">
                            </div>
                            <div class="thumbnail_images">
                                <div class="thumbnail_image active" data-image="${object.image}">
                                    <img src="${object.image}" alt="">
                                </div>
                                <div class="thumbnail_image" data-image="/static/images/3d-image2.jpg">
                                    <img src="/static/images/3d-image2.jpg" alt="">
                                </div>
                                <div class="thumbnail_image" data-image="/static/images/3d-image3.jpeg">
                                    <img src="/static/images/3d-image3.jpeg" alt="">
                                </div>
                                <div class="thumbnail_image" data-image="/static/images/3d-image4.jpeg">
                                    <img src="/static/images/3d-image4.jpeg" alt="">
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Details -->
                    <div class="col-lg-6">
                        <div class="object_content">
                            <div class="object_name">${object.filename}</div>
                            <div class="object_description"><i class="fa fa-inr"></i>${object.price}</div>
                            <div class="object_description">Printing status : ${object.printingStatus}</div>
                            <div class="object_description">Completed
                                at ${object.trackingDetails.completed_at!"--"}</div>
                            <div class="object_description">Printing duration : 2:32 hr</div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

</@layout.base>