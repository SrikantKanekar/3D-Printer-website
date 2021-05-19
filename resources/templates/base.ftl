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
		
		<!--jquery-->
		<script src="/static/plugins/jquery/jquery-3.2.1.min.js"></script>

		<!--base-->
		<link rel="stylesheet" type="text/css" href="/static/css/base.css">

		<!--responsive-->
		<link rel="stylesheet" type="text/css" href="/static/css/responsive.css">
		
		<!--custom-->
		<link rel="stylesheet" type="text/css" href=${css}>
		
		<script>(function(e,t,n){var r=e.querySelectorAll("html")[0];r.className=r.className.replace(/(^|\s)no-js(\s|$)/,"$1js$2")})(document,window,0);</script>
	</head>
	<body>
		
		<#nested/>

		<@footer.footer/>

		<div id="overlay">
			<div class="cv-spinner">
				<span class="spinner"></span>
			</div>
		</div>

		<!--bootstrap-->
		<script src="/static/plugins/bootstrap/js/bootstrap.bundle.min.js"></script>

		<!--base-->
		<script src="/static/js/base.js"></script>

		<!--custom-->
		<script src=${js}></script>
	</body>
</html>
</#macro>