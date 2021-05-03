<#macro main css js title="3d printer website">
<!doctype html>
<html lang="en">

	<head>
		<title>${title}</title>
		<meta charset="utf-8">
		<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

		<!--===============================================================================================-->
		<link rel="icon" type="image/png" href="/static/images/icons/favicon.ico" />
		<link rel="apple-touch-icon" type="image/png" href="/static/images/icons/favicon.ico">
		<!--===============================================================================================-->
  		<link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Open+Sans:300,300i,400,400i,600,600i,700,700i|Montserrat:300,400,500,600,700">
		<!--===============================================================================================-->
		<link rel="stylesheet" type="text/css" href="/static/vendor/aos/aos.css">
		<!--===============================================================================================-->
		<link rel="stylesheet" type="text/css" href="/static/vendor/bootstrap-icons/bootstrap-icons.css">
		<!--===============================================================================================-->
		<link rel="stylesheet" type="text/css" href="/static/vendor/glightbox/css/glightbox.min.css">
		<!--===============================================================================================-->
		<link rel="stylesheet" type="text/css" href="/static/vendor/swiper/swiper-bundle.min.css">
		<!--===============================================================================================-->
		<link rel="stylesheet" type="text/css" href="/static/fonts/font-awesome-4.7.0/css/font-awesome.min.css">
		<!--===============================================================================================-->
		<link rel="stylesheet" type="text/css" href="/static/fonts/Linearicons-Free-v1.0.0/icon-font.min.css">
		<!--===============================================================================================-->
		<link rel="stylesheet" type="text/css" href="/static/vendor/animate/animate.css">
		<!--===============================================================================================-->
		<link rel="stylesheet" type="text/css" href="/static/vendor/css-hamburgers/hamburgers.min.css">
		<!--===============================================================================================-->
		<link rel="stylesheet" type="text/css" href="/static/vendor/animsition/css/animsition.min.css">
		<!--===============================================================================================-->
		<link rel="stylesheet" type="text/css" href="/static/vendor/select2/select2.min.css">
		<!--===============================================================================================-->
		<link rel="stylesheet" type="text/css" href="/static/vendor/daterangepicker/daterangepicker.css">
		<!--===============================================================================================-->
		<link rel="stylesheet" type="text/css" href="/static/css/auth/util.css">
		<link rel="stylesheet" type="text/css" href="/static/css/home/style.css">

		<link rel="stylesheet" type="text/css" href=${css}>
		<!--===============================================================================================-->  
		<link rel="stylesheet" type="text/css" href="/static/vendor/bootstrap/css/bootstrap.min.css">
		<!--===============================================================================================-->
	</head>
	<body>
		
		<#nested/>

		<div id="overlay">
			<div class="cv-spinner">
				<span class="spinner"></span>
			</div>
		</div>
		
		<!--===============================================================================================-->
		<script src="/static/vendor/aos/aos.js"></script>
		<!--===============================================================================================-->
  		<script src="/static/vendor/glightbox/js/glightbox.min.js"></script>
		<!--===============================================================================================-->
  		<script src="/static/vendor/isotope-layout/isotope.pkgd.min.js"></script>
		<!--===============================================================================================-->
  		<script src="/static/vendor/php-email-form/validate.js"></script>
		<!--===============================================================================================-->
  		<script src="/static/vendor/purecounter/purecounter.js"></script>
		<!--===============================================================================================-->
  		<script src="/static/vendor/swiper/swiper-bundle.min.js"></script>
		<!--===============================================================================================-->
		<script src="/static/vendor/jquery/jquery-3.2.1.min.js"></script>
		<!--===============================================================================================-->
		<script src="/static/vendor/animsition/js/animsition.min.js"></script>
		<!--===============================================================================================-->
		<script src="/static/vendor/bootstrap/js/popper.js"></script>
		<script src="/static/vendor/bootstrap/js/bootstrap.min.js"></script>
		<!--===============================================================================================-->
		<script src="/static/vendor/select2/select2.min.js"></script>
		<!--===============================================================================================-->
		<script src="/static/vendor/daterangepicker/moment.min.js"></script>
		<script src="/static/vendor/daterangepicker/daterangepicker.js"></script>
		<!--===============================================================================================-->
		<script src="/static/vendor/countdowntime/countdowntime.js"></script>
		<!--===============================================================================================-->
		<script src="/static/js/home/main.js"></script>
		<script src=${js}></script>
	</body>
</html>
</#macro>