<#import "footer.ftl" as footer />
<#macro base css js title="3d printer website">
<!doctype html>
<html lang="en" class="no-js">

	<head>
		<title>${title}</title>
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <meta name="description" content="A 3D printing website" />
		<meta name="viewport" content="width=device-width, initial-scale=1">
		
		<!--favicon-->
		<link rel="icon" type="image/png" href="/static/images/icons/favicon.ico" />
		<link rel="apple-touch-icon" type="image/png" href="/static/images/icons/favicon.ico">

		<!--bootstrap-->
		<link rel="stylesheet" type="text/css" href="/static/plugins/bootstrap/css/bootstrap.min.css">

		<!--font-awesome-->
		<link rel="stylesheet" type="text/css" href="/static/plugins/font-awesome-4.7.0/css/font-awesome.min.css">

		<!--base-->
		<link rel="stylesheet" type="text/css" href="/static/css/base.css">

		<!--custom-->
		<link rel="stylesheet" type="text/css" href=${css}>
		
		<!--responsive-->
		<link rel="stylesheet" type="text/css" href="/static/css/responsive.css">

		<script>(function(e,t,n){var r=e.querySelectorAll("html")[0];r.className=r.className.replace(/(^|\s)no-js(\s|$)/,"$1js$2")})(document,window,0);</script>
	</head>
	
	<body>
		<div class="super_container">
			<#nested/>

			<@footer.footer/>
		
			<div id="overlay">
				<div class="cv-spinner">
					<span class="spinner"></span>
				</div>
			</div>

			<div class="alert_container">
				<div class="alert alert-dismissible" role="alert"></div>
			</div>
		</div>

		<!--jquery-->
		<script src="/static/plugins/jquery/jquery-3.2.1.min.js"></script>

		<!--bootstrap-->
		<script src="/static/plugins/bootstrap/js/bootstrap.bundle.min.js"></script>

		<script src="/static/plugins/greensock/TweenMax.min.js"></script>
		<script src="/static/plugins/greensock/TimelineMax.min.js"></script>
		<script src="/static/plugins/scrollmagic/ScrollMagic.min.js"></script>
		<script src="/static/plugins/greensock/animation.gsap.min.js"></script>
		<script src="/static/plugins/greensock/ScrollToPlugin.min.js"></script>
		<script src="/static/plugins/OwlCarousel2-2.2.1/owl.carousel.js"></script>
		<script src="/static/plugins/Isotope/isotope.pkgd.min.js"></script>
		<script src="/static/plugins/easing/easing.js"></script>
		<script src="/static/plugins/parallax-js-master/parallax.min.js"></script>

		<!--base-->
		<script src="/static/js/base.js"></script>

		<!--custom-->
		<script src=${js}></script>
	</body>
</html>
</#macro>