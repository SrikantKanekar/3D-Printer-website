<#macro base css js title="3d printer website">
<!doctype html>
<html lang="en" class="no-js">

	<head>
		<title>${title}</title>
		<meta charset="utf-8">
		<meta name="viewport" content="width=device-width, initial-scale=1">
		
		<!--favicon-->
		<link rel="icon" type="image/png" href="/static/images/icons/favicon.ico" />
		<link rel="apple-touch-icon" type="image/png" href="/static/images/icons/favicon.ico">
		
		<!--aos-->
  		<link rel="stylesheet" type="text/css" href="/static/plugins/aos/aos.css">

		<!--bootstrap-->
		<link rel="stylesheet" type="text/css" href="/static/plugins/bootstrap/css/bootstrap.min.css">
		<link rel="stylesheet" type="text/css" href="/static/plugins/bootstrap-icons/bootstrap-icons.css">
		
		<!--a-->
		<link rel="stylesheet" type="text/css" href="/static/plugins/glightbox/css/glightbox.min.css">
		
		<!--a-->
		<link rel="stylesheet" type="text/css" href="/static/plugins/swiper/swiper-bundle.min.css">
		
		<!--a-->
		<link rel="stylesheet" type="text/css" href="/static/plugins/animate/animate.css">
		
		<!--a-->
		<link rel="stylesheet" type="text/css" href="/static/plugins/font-awesome/css/font-awesome.css">
		
		<!--a-->
		<link rel="stylesheet" type="text/css" href="/static/plugins/css-hamburgers/hamburgers.min.css">
		
		<!--a-->
		<link rel="stylesheet" type="text/css" href="/static/plugins/animsition/css/animsition.min.css">
		
		<!--a-->
		<link rel="stylesheet" type="text/css" href="/static/plugins/select2/select2.min.css">
		
		<!--a-->
		<link rel="stylesheet" type="text/css" href="/static/plugins/daterangepicker/daterangepicker.css">
		
		<!--base-->
		<link rel="stylesheet" type="text/css" href="/static/css/base.css">
		
		<!--custom-->
		<link rel="stylesheet" type="text/css" href=${css}>
		
		<script>(function(e,t,n){var r=e.querySelectorAll("html")[0];r.className=r.className.replace(/(^|\s)no-js(\s|$)/,"$1js$2")})(document,window,0);</script>
	</head>
	<body>
		
		<#nested/>

		<div id="overlay">
			<div class="cv-spinner">
				<span class="spinner"></span>
			</div>
		</div>
		
		<!--aos-->
		<script src="/static/plugins/aos/aos.js"></script>
		
		<!--a-->
  		<script src="/static/plugins/glightbox/js/glightbox.min.js"></script>
		
		<!--a-->
  		<script src="/static/plugins/isotope-layout/isotope.pkgd.min.js"></script>
		
		<!--a-->
  		<script src="/static/plugins/php-email-form/validate.js"></script>
		
		<!--a-->
  		<script src="/static/plugins/purecounter/purecounter.js"></script>
		
		<!--a-->
  		<script src="/static/plugins/swiper/swiper-bundle.min.js"></script>
		
		<!--jquery-->
		<script src="/static/plugins/jquery/jquery-3.2.1.min.js"></script>
		
		<!--a-->
		<script src="/static/plugins/animsition/js/animsition.min.js"></script>
		
		<!--a-->
		<script src="/static/plugins/select2/select2.min.js"></script>
		
		<!--bootstrap-->
		<script src="/static/plugins/bootstrap/js/bootstrap.bundle.min.js"></script>
		
		<!--a-->
		<script src="/static/plugins/daterangepicker/moment.min.js"></script>
		
		<!--a-->
		<script src="/static/plugins/daterangepicker/daterangepicker.js"></script>
		
		<!--a-->
		<script src="/static/plugins/countdowntime/countdowntime.js"></script>
		
		<!--base-->
		<script src="/static/js/base.js"></script>

		<!--custom-->
		<script src=${js}></script>
	</body>
</html>
</#macro>