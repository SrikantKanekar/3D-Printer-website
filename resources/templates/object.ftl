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
						<div class="details_image">
							<div class="details_image_large"><img src="${object.image}" alt="">
							</div>
							<div class="details_image_thumbnails d-flex flex-row align-items-start justify-content-between">
								<div class="details_image_thumbnail active" data-image="${object.image}"><img src="${object.image}" alt=""></div>
								<div class="details_image_thumbnail" data-image="/static/images/3d-image2.jpeg"><img src="/static/images/3d-image2.jpeg" alt=""></div>
								<div class="details_image_thumbnail" data-image="/static/images/3d-image3.jpeg"><img src="/static/images/3d-image3.jpeg" alt=""></div>
								<div class="details_image_thumbnail" data-image="/static/images/3d-image4.jpeg"><img src="/static/images/3d-image4.jpeg" alt=""></div>
							</div>
						</div>
					</div>

					<!-- Details -->
					<div class="col-lg-6">
						<div class="details_content">
							<div class="details_name">${object.filename}</div>
							<div class="details_price"><i class="fa fa-inr"></i>${object.price}</div>
							<div class="details_price">${object.timeToPrint} minutes for printing</div>
							
							<div class="product_quantity_container">
								<div class="product_quantity clearfix">
									<span>Qty</span>
									<input id="quantity_input" type="text" pattern="[0-9]*" value="1">
									<div class="quantity_buttons">
										<div id="quantity_inc_button" class="quantity_inc quantity_control"><i class="fa fa-chevron-up" aria-hidden="true"></i></div>
										<div id="quantity_dec_button" class="quantity_dec quantity_control"><i class="fa fa-chevron-down" aria-hidden="true"></i></div>
									</div>
								</div>
								<div class="button cart_button"><a href="/object/add-to-cart">Add to cart</a></div>
							</div>
							<div class="cart_remove_button_container">
								<div class="button cart_remove_button"><a href="/object/remove-from-cart">Remove from cart</a></div>
							</div>
						</div>
					</div>
				</div>

				<#--  Settings  -->
				<div class="row">
					<div class="col">
						
						<ul class="nav nav-tabs" id="myTab" role="tablist">
							<li class="nav-item setting_title">
								<a class="active" id="basic-tab" data-toggle="tab" href="#basic" role="tab" aria-controls="basic" aria-selected="true">Basic Settings</a>
							</li>
							<li class="nav-item setting_title">
								<a id="advanced-tab" data-toggle="tab" href="#advanced" role="tab" aria-controls="advanced" aria-selected="true">Advanced Settings</a>
							</li>
						</ul>
						
						<div class="tab-content setting_content" id="myTabContent">
							
							<div class="tab-pane fade show active" id="basic" role="tabpanel" aria-labelledby="basic-tab">
								<div class="setting_form_container">
									<form class="form" action="/object/${object.id}/basic" method="POST" id="basic_settings_form">
										
										<label for="size">Size</label>
										<div data-validate="Please enter size">
											<input type="number" name="size" value="${object.basicSettings.size?string.computer}" id="size" class="input" required="required"/>
										</div>

										<div class="form_message"></div>
										<div id="basic_button" class="button form_submit_button"><a href="#">Update</a></div>
									</form>
								</div>
							</div>

							<div class="tab-pane fade" id="advanced" role="tabpanel" aria-labelledby="advanced-tab">				
								<div class="setting_form_container">
									<form class="form" action="/object/${object.id}/advanced" method="post" id="advanced_settings_form">
										
										<label for="weight">Weight</label>
										<div data-validate="Please enter weight">
											<input type="number" name="weight" value="${object.advancedSettings.weight?string.computer}" id="weight" class="input" required="required"/>
										</div>
										<div class="form_message"></div>
										<div id="advanced_button" class="button form_submit_button"><a href="#">Update</a></div>
									</form>
								</div>
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
						<div class="details_content">
							<div class="details_name">${object.filename}</div>
							<div class="details_price"><i class="fa fa-inr"></i>${object.price}</div>
							<div class="details_status">Printing status : ${object.printingStatus}</div>
							
							<div class="details_started_at">Started at : ${object.trackingDetails.started_at!"--"}</div>
							<div class="details_completed_by">Expected to be completed by <span>5:34 pm</span></div>			
						</div>
					</div>
				</div>				
			</div>

			<div class="status_completed">
				<div class="row">
					<!-- Image -->
					<div class="col-lg-6">
						<div class="details_image">
							<div class="details_image_large"><img src="${object.image}" alt="">
							</div>
							<div class="details_image_thumbnails d-flex flex-row align-items-start justify-content-between">
								<div class="details_image_thumbnail active" data-image="${object.image}"><img src="${object.image}" alt=""></div>
								<div class="details_image_thumbnail" data-image="/static/images/3d-image2.jpeg"><img src="/static/images/3d-image2.jpeg" alt=""></div>
								<div class="details_image_thumbnail" data-image="/static/images/3d-image3.jpeg"><img src="/static/images/3d-image3.jpeg" alt=""></div>
								<div class="details_image_thumbnail" data-image="/static/images/3d-image4.jpeg"><img src="/static/images/3d-image4.jpeg" alt=""></div>
							</div>
						</div>
					</div>

					<!-- Details -->
					<div class="col-lg-6">
						<div class="details_content">
							<div class="details_name">${object.filename}</div>
							<div class="details_price"><i class="fa fa-inr"></i>${object.price}</div>
							<div class="details_status">Printing status : ${object.printingStatus}</div>
							<div class="details_completed_by">Completed at ${object.trackingDetails.completed_at!"--"}</div>
							<div class="details_completed_by">Printing duration : 2:32 hr</div>							
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	
</@layout.base>