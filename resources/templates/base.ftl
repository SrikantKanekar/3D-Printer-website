<#import "footer.ftl" as footer />
<#macro base css js title="3d printer website">
    <!doctype html>
    <html lang="en">

        <head>
            <title>${title}</title>
            <meta charset="utf-8">
            <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
            <meta name="description" content="A 3D printing website"/>
            <meta name="viewport" content="width=device-width, initial-scale=1">

            <!--favicon-->
            <link rel="icon" type="image/png" href="/static/images/favicon.ico"/>
            <link rel="apple-touch-icon" type="image/png" href="/static/images/favicon.ico">

            <!--font-->
            <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Poppins:300,400,500,600,700,800,900">

            <!--bootstrap-->
            <link rel="stylesheet" type="text/css" href="/static/plugins/bootstrap/css/bootstrap.min.css">

            <!--icons-->
            <link rel="stylesheet" type="text/css" href="/static/plugins/boxicons/css/boxicons.min.css">
            <link rel="stylesheet" type="text/css" href="/static/plugins/bootstrap-icons/bootstrap-icons.css">

            <!--font-awesome-->
            <link rel="stylesheet" type="text/css" href="/static/plugins/font-awesome-4.7.0/css/font-awesome.min.css">

            <!--base-->
            <link rel="stylesheet" type="text/css" href="/static/css/base.css">

            <!--custom-->
            <link rel="stylesheet" type="text/css" href=${css}>

            <!--theme-->
            <script>
                if (localStorage.getItem("theme") === "light_theme") {
                    document.documentElement.classList.add("light_theme");
                }
            </script>
        </head>

        <body>
            <div class="super_container">

                <div id="preloader" class="preloader_overlay">
                    <div class="spinner_container">
                        <span class="spinner"></span>
                    </div>
                </div>

                <div class="main_container">
                    <#nested/>
                </div>

                <@footer.footer/>

                <div id="spinner" class="spinner_overlay">
                    <div class="spinner_container">
                        <span class="spinner"></span>
                    </div>
                </div>

                <div class="alert_container">
                    <div class="alert alert-dismissible fade show" role="alert"></div>
                </div>

                <a href="#" class="back_to_top">
                    <i class="bi bi-arrow-up-short"></i>
                </a>
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