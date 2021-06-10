<#import "base.ftl" as layout />
<#import "header.ftl" as header />
<@layout.base title="Object" css="/static/css/object.css" js="/static/js/object.js">

    <@header.header user="${user}" title="Object" />

    <div class="object" data-id="${object.id}" data-status="${object.status}">
        <div class="container">

            <#if object.status == "NONE" || object.status == "CART">
                <div class="row status_none">

                    <!-- Image -->
                    <div class="col-lg-6">
                        <div class="image_selected">
                            <img src="${object.image}" alt="">
                        </div>
                    </div>

                    <!-- Details -->
                    <div class="col-lg-6">
                        <div class="object_content">

                            <div class="object_name">${object.filename}</div>

                            <div class="upto_date" data-value="${object.slicingDetails.uptoDate?c}">
                                Slicing details are not upto date
                            </div>

                            <div class="slicing_details">
                                <div class="time" data-value="${(object.slicingDetails.time)!}">
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

                            <div class="cart_buttons">
                                <div class="button slice">
                                    <a href="/object/slice">Slice</a>
                                </div>
                                <div class="button add_to_cart">
                                    <a href="/object/add-to-cart">Add to cart</a>
                                </div>
                            </div>

                            <div class="remove_cart_button">
                                <div class="button remove_from_cart">
                                    <a href="/object/remove-from-cart">Remove from cart</a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!--  Settings  -->
                <div class="row settings">
                    <div class="col">

                        <!-- Setting tabs -->
                        <ul class="nav nav-tabs" id="myTab" role="tablist">

                            <!-- Basic tab -->
                            <li class="nav-item">
                                <a class="active" id="basic-tab" data-toggle="tab" href="#basic"
                                   role="tab" aria-controls="basic" aria-selected="true">Basic
                                </a>
                            </li>

                            <!-- Intermediate tab -->
                            <li class="nav-item">
                                <a id="intermediate-tab" data-toggle="tab" href="#intermediate" role="tab"
                                   aria-controls="intermediate" aria-selected="true">Intermediate
                                </a>
                            </li>

                            <!-- Advanced tab -->
                            <li class="nav-item">
                                <a id="advanced-tab" data-toggle="tab" href="#advanced" role="tab"
                                   aria-controls="advanced" aria-selected="true">Advanced
                                </a>
                            </li>
                        </ul>

                        <!-- Setting contents -->
                        <div class="tab-content" id="myTabContent">

                            <!-- Basic -->
                            <div class="tab-pane fade show active" id="basic" role="tabpanel"
                                 aria-labelledby="basic-tab">
                                <form class="form" action="/object/update/basic-settings" id="basic_settings_form">

                                    <label for="quality">Quality</label>
                                    <div data-validate="Please select value">
                                        <select name="quality" id="quality" class="dropdown_item_select input"
                                                data-value="${object.basicSetting.quality}">
                                            <option value="SUPER">Super</option>
                                            <option value="DYNAMIC">Dynamic</option>
                                            <option value="STANDARD">Standard</option>
                                            <option value="LOW">Low</option>
                                        </select>
                                    </div>

                                    <label for="infill">Infill (%)</label>
                                    <div data-validate="Value between 0 and 100">
                                        <input type="number" name="infill" id="infill" class="input"
                                               value="${object.basicSetting.infill}"/>
                                    </div>

                                    <label class="checkbox_container">Gradual Infill
                                        <input type="checkbox" id="gradual_infill" name="gradual_infill"
                                                ${object.basicSetting.gradualInfill?then('checked','')}>
                                        <span class="checkmark"></span>
                                    </label>

                                    <label class="checkbox_container">Support
                                        <input type="checkbox" id="support" name="support"
                                                ${object.basicSetting.support?then('checked','')}>
                                        <span class="checkmark"></span>
                                    </label>

                                    <div class="form_message"></div>

                                    <div id="basic_button" class="button form_submit_button">
                                        <a href="#">Update</a>
                                    </div>
                                </form>
                            </div>

                            <!-- Intermediate -->
                            <div class="tab-pane fade" id="intermediate" role="tabpanel"
                                 aria-labelledby="intermediate-tab">
                                <form class="form" action="/object/update/intermediate-settings"
                                      id="intermediate_settings_form">

                                    <label for="layer_height">Layer Height (mm)</label>
                                    <div data-validate="value between 0.1 and 0.3">
                                        <input type="number" name="layer_height" id="layer_height" class="input"
                                               value="${object.intermediateSetting.layerHeight}"/>
                                    </div>

                                    <label for="infill_density">Infill Density (%)</label>
                                    <div data-validate="Value between 0 and 100">
                                        <input type="number" name="infill_density" id="infill_density" class="input"
                                               value="${object.intermediateSetting.infillDensity}"/>
                                    </div>

                                    <label for="infill_pattern">Infill Pattern</label>
                                    <div data-validate="Please select value">
                                        <select name="infill_pattern" id="infill_pattern"
                                                class="dropdown_item_select input"
                                                data-value="${object.intermediateSetting.infillPattern}">
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
                                                ${object.intermediateSetting.generateSupport?then('checked','')}>
                                        <span class="checkmark"></span>
                                    </label>

                                    <label for="support_structure">Support Structure</label>
                                    <div data-validate="Please select value">
                                        <select name="support_structure" id="support_structure"
                                                class="dropdown_item_select input"
                                                data-value="${object.intermediateSetting.supportStructure}">
                                            <option value="NORMAL">Normal</option>
                                            <option value="TREE">Tree</option>
                                        </select>
                                    </div>

                                    <label for="support_placement">Support Placement</label>
                                    <div data-validate="Please select value">
                                        <select name="support_placement" id="support_placement"
                                                class="dropdown_item_select input"
                                                data-value="${object.intermediateSetting.supportPlacement}">
                                            <option value="TOUCHING_BUILD_PLATE">Touching Build Plate</option>
                                            <option value="EVERYWHERE">Everywhere</option>
                                        </select>
                                    </div>

                                    <label for="support_overhang_angle">Support Overhang Angle</label>
                                    <div data-validate="value between 0 and 89">
                                        <input type="number" name="support_overhang_angle"
                                               id="support_overhang_angle"
                                               class="input"
                                               value="${object.intermediateSetting.supportOverhangAngle}"/>
                                    </div>

                                    <label for="support_pattern">Support Pattern</label>
                                    <div data-validate="Please select value">
                                        <select name="support_pattern" id="support_pattern"
                                                class="dropdown_item_select input"
                                                data-value="${object.intermediateSetting.supportPattern}">
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
                                               value="${object.intermediateSetting.supportDensity}"/>
                                    </div>

                                    <div class="form_message"></div>

                                    <div id="intermediate_button" class="button form_submit_button">
                                        <a href="#">Update</a>
                                    </div>
                                </form>
                            </div>

                            <!-- Advanced -->
                            <div class="tab-pane fade" id="advanced" role="tabpanel" aria-labelledby="advanced-tab">
                                <form class="form" action="/object/update/advanced-settings"
                                      id="advanced_settings_form">

                                    <label for="wall_line_width">Wall Line Width (mm)</label>
                                    <div data-validate="Value between 0.4 and 1.2">
                                        <input type="number" name="wall_line_width" id="wall_line_width"
                                               value="${object.advancedSetting.wallLineWidth}" class="input"/>
                                    </div>

                                    <label for="top_bottom_line_width">Top/Bottom Line Width (mm)</label>
                                    <div data-validate="Value between 0.4 and 1.2">
                                        <input type="number" name="top_bottom_line_width" id="top_bottom_line_width"
                                               value="${object.advancedSetting.topBottomLineWidth}" class="input"/>
                                    </div>

                                    <label for="wall_thickness">Wall Thickness (mm)</label>
                                    <div data-validate="Value between 0.4 and 1.2">
                                        <input type="number" name="wall_thickness" id="wall_thickness" class="input"
                                               value="${object.advancedSetting.wallThickness}"/>
                                    </div>

                                    <label for="wall_line_count">Wall Line Count</label>
                                    <div data-validate="Value between 2 and 8">
                                        <input type="number" name="wall_line_count" id="wall_line_count"
                                               class="input"
                                               value="${object.advancedSetting.wallLineCount}"/>
                                    </div>

                                    <label for="top_thickness">Top Thickness (mm)</label>
                                    <div data-validate="Value between 0.8 and 2">
                                        <input type="number" name="top_thickness" id="top_thickness"
                                               value="${object.advancedSetting.topThickness}" class="input"/>
                                    </div>

                                    <label for="bottom_thickness">Bottom Thickness (mm)</label>
                                    <div data-validate="Value between 0.8 and 2">
                                        <input type="number" name="bottom_thickness" id="bottom_thickness"
                                               value="${object.advancedSetting.bottomThickness}" class="input"/>
                                    </div>

                                    <label for="infill_speed">Infill Speed (mm/s)</label>
                                    <div data-validate="Value between 25 and 100">
                                        <input type="number" name="infill_speed" id="infill_speed"
                                               value="${object.advancedSetting.infillSpeed}" class="input"/>
                                    </div>

                                    <label for="outer_wall_speed">Outer Wall Speed (mm/s)</label>
                                    <div data-validate="Value between 25 and 100">
                                        <input type="number" name="outer_wall_speed" id="outer_wall_speed"
                                               value="${object.advancedSetting.outerWallSpeed}" class="input"/>
                                    </div>

                                    <label for="inner_wall_speed">Inner Wall Speed (mm/s)</label>
                                    <div data-validate="Value between 25 and 100">
                                        <input type="number" name="inner_wall_speed" id="inner_wall_speed"
                                               value="${object.advancedSetting.innerWallSpeed}" class="input"/>
                                    </div>

                                    <label for="top_bottom_speed">Top Bottom Speed (mm/s)</label>
                                    <div data-validate="Value between 25 and 100">
                                        <input type="number" name="top_bottom_speed" id="top_bottom_speed"
                                               value="${object.advancedSetting.topBottomSpeed}" class="input"/>
                                    </div>

                                    <label for="support_speed">Support Speed (mm/s)</label>
                                    <div data-validate="Value between 25 and 100">
                                        <input type="number" name="support_speed" id="support_speed"
                                               value="${object.advancedSetting.supportSpeed}" class="input"/>
                                    </div>

                                    <label for="print_sequence">Print Sequence</label>
                                    <div data-validate="Please select value">
                                        <select name="print_sequence" id="print_sequence"
                                                class="dropdown_item_select input"
                                                data-value="${object.advancedSetting.printSequence}">
                                            <option value="ALL_AT_ONCE">All At Once</option>
                                            <option value="ONE_AT_A_TIME">One At A Time</option>
                                        </select>
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

                <div class="delete_object">
                    <div>Delete</div>
                    <div class="button delete_button">
                        <a href="#">Delete</a>
                    </div>
                </div>
            <#elseif object.status == "TRACKING">
                <div class="row status_tracking">

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
                            <div class="object_description">
                                <div class=""><i class="fa fa-inr"></i>${object.slicingDetails.totalPrice!}</div>
                                <div class="">Printing status : ${object.printingStatus}</div>
                                <div class="">Started at : ${object.trackingDetails.started_at!"--"}</div>
                                <div class="">Expected to be completed by <span>5:34 pm</span></div>
                            </div>
                        </div>
                    </div>
                </div>
            <#elseif object.status == "COMPLETED">
                <div class="row status_completed">
                    <!-- Image -->
                    <div class="col-lg-6">
                        <div class="details_image">
                            <div class="image_selected">
                                <img src="${object.image}" alt="">
                            </div>
                        </div>
                    </div>

                    <!-- Details -->
                    <div class="col-lg-6">
                        <div class="object_content">
                            <div class="object_name">${object.filename}</div>
                            <div class="object_description">
                                <div class=""><i class="fa fa-inr"></i>${object.slicingDetails.totalPrice!}</div>
                                <div class="">Printing status : ${object.printingStatus}</div>
                                <div class="">Completed at ${object.trackingDetails.completed_at!"--"}</div>
                                <div class="">Printing duration : 2:32 hr</div>
                            </div>
                        </div>
                    </div>
                </div>
            </#if>
        </div>
    </div>

</@layout.base>