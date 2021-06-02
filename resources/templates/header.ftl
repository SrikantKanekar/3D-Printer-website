<#macro header user="" title="3Design">
    <!-- Header -->
    <header class="header">

        <div class="header_container">
            <div class="container">
                <div class="row">
                    <div class="col">
                        <div class="
									header_content
									d-flex
									flex-row
									align-items-center
									justify-content-start
								">
                            <div class="logo">
                                ${title}
                            </div>
                            <nav class="main_nav">
                                <ul>
                                    <li class="active"><a href="/">Home</a></li>
                                    <li><a href="/object/create">Create</a></li>
                                    <li><a href="/my-objects">My Objects</a></li>

                                    <#if (user?has_content)>
                                        <li class="hassubs">
                                            <a href="/account">Account</a>
                                            <ul>
                                                <li>
                                                    <a href="/account">Account</a>
                                                </li>
                                                <li>
                                                    <a href="/tracking">Tracking</a>
                                                </li>
                                                <li>
                                                    <a href="/history">History</a>
                                                </li>
                                                <li>
                                                    <a href="/notification">Notifications</a>
                                                </li>
                                                <li>
                                                    <a href="/account/logout">Logout</a>
                                                </li>
                                            </ul>
                                        </li>
                                    </#if>

                                </ul>
                            </nav>
                            <div class="header_extra ml-auto">

                                <div class="shopping_cart">

                                    <#if (user?has_content)>
                                        <a href="/cart">
                                            <svg version="1.1" xmlns="http://www.w3.org/2000/svg"
                                                 xmlns:xlink="http://www.w3.org/1999/xlink" x="0px" y="0px"
                                                 viewBox="0 0 489 489" style="enable-background: new 00 489 489;"
                                                 xml:space="preserve">
                                                <g>
                                                    <path d="M440.1,422.7l-28-315.3c-0.6-7-6.5-12.3-13.4-12.3h-57.6C340.3,42.5,297.3,0,244.5,0s-95.8,42.5-96.6,95.1H90.3
                                                    c-7,0-12.8,5.3-13.4,12.3l-28,315.3c0,0.4-0.1,0.8-0.1,1.2c0,35.9,32.9,65.1,73.4,65.1h244.6c40.5,0,73.4-29.2,73.4-65.1
                                                    C440.2,423.5,440.2,423.1,440.1,422.7z M244.5,27c37.9,0,68.8,30.4,69.6,68.1H174.9C175.7,57.4,206.6,27,244.5,27z M366.8,462
                                                    H122.2c-25.4,0-46-16.8-46.4-37.5l26.8-302.3h45.2v41c0,7.5,6,13.5,13.5,13.5s13.5-6,13.5-13.5v-41h139.3v41
                                                    c0,7.5,6,13.5,13.5,13.5s13.5-6,13.5-13.5v-41h45.2l26.9,302.3C412.8,445.2,392.1,462,366.8,462z"/>
                                                </g>
										    </svg>
                                            <div>Cart</div>
                                        </a>
                                    <#else>
                                        <a href="/auth/login">
                                            <div>SignIn</div>
                                        </a>
                                    </#if>
                                </div>

                                <div class="theme_icon">
                                    <i class="fa" aria-hidden="true"></i>
                                </div>

                                <div class="hamburger">
                                    <i class="fa fa-bars" aria-hidden="true"></i>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Social -->
        <div class="header_social">
            <ul>
                <li>
                    <a href="#"><i class="fa fa-pinterest" aria-hidden="true"></i></a>
                </li>
                <li>
                    <a href="#"><i class="fa fa-instagram" aria-hidden="true"></i></a>
                </li>
                <li>
                    <a href="#"><i class="fa fa-facebook" aria-hidden="true"></i></a>
                </li>
                <li>
                    <a href="#"><i class="fa fa-twitter" aria-hidden="true"></i></a>
                </li>
            </ul>
        </div>

    </header>

    <!-- Menu -->
    <div class="menu menu_mm trans_300">

        <div class="menu_container menu_mm">
            <div class="page_menu_content">
                <ul class="page_menu_nav menu_mm">

                    <li class="page_menu_item menu_mm">
                        <a href="/">Home<i class="fa fa-angle-down"></i></a>
                    </li>

                    <li class="page_menu_item menu_mm">
                        <a href="/object/create">Create<i class="fa fa-angle-down"></i></a>
                    </li>

                    <li class="page_menu_item menu_mm">
                        <a href="/my-objects">My Objects<i class="fa fa-angle-down"></i></a>
                    </li>

                    <#if (user?has_content)>
                        <li class="page_menu_item has-children menu_mm">
                            <a href="/account">Account<i class="fa fa-angle-down"></i></a>
                            <ul class="page_menu_selection menu_mm">
                                <li class="page_menu_item menu_mm">
                                    <a href="/account">Account<i class="fa fa-angle-down"></i></a>
                                </li>
                                <li class="page_menu_item menu_mm">
                                    <a href="/tracking">Tracking<i class="fa fa-angle-down"></i></a>
                                </li>
                                <li class="page_menu_item menu_mm">
                                    <a href="/history">History<i class="fa fa-angle-down"></i></a>
                                </li>
                                <li class="page_menu_item menu_mm">
                                    <a href="/notification">Notifications<i class="fa fa-angle-down"></i></a>
                                </li>
                                <li class="page_menu_item menu_mm">
                                    <a href="/account/logout">Logout<i class="fa fa-angle-down"></i></a>
                                </li>
                            </ul>
                        </li>
                    </#if>

                </ul>
            </div>
        </div>

        <div class="menu_close">
            <i class="fa fa-times" aria-hidden="true"></i>
        </div>

        <div class="menu_social">
            <ul>
                <li>
                    <a href="#"><i class="fa fa-pinterest" aria-hidden="true"></i></a>
                </li>
                <li>
                    <a href="#"><i class="fa fa-instagram" aria-hidden="true"></i></a>
                </li>
                <li>
                    <a href="#"><i class="fa fa-facebook" aria-hidden="true"></i></a>
                </li>
                <li>
                    <a href="#"><i class="fa fa-twitter" aria-hidden="true"></i></a>
                </li>
            </ul>
        </div>
    </div>

    <script>
        const icon = document.querySelector(".theme_icon i");
        if (localStorage.getItem("theme") === "dark_theme") {
            icon.classList.add("fa-sun-o");
        } else {
            icon.classList.add("fa-moon-o");
        }
    </script>
</#macro>