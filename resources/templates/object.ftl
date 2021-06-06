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
                                <form class="form" action="/object/${object.id}/basic" id="basic_settings_form">

                                    <label for="size">Size</label>
                                    <div data-validate="Please enter size">
                                        <input type="number" name="size" id="size"
                                               value="${object.basicSettings.size?string.computer}"
                                               class="input"/>
                                    </div>

                                    <div class="form_message"></div>
                                    <div id="basic_button" class="button form_submit_button">
                                        <a href="#">Update</a>
                                    </div>
                                </form>
                            </div>
                            <div class="tab-pane fade" id="advanced" role="tabpanel" aria-labelledby="advanced-tab">
                                <form class="form" action="/object/${object.id}/advanced" id="advanced_settings_form">

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