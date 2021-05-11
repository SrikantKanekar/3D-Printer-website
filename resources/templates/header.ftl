<#macro header user="" admin="">

  <header id="header" class="fixed-top d-flex align-items-center header-transparent">
    <div class="container d-flex align-items-center">

      <#--  <a href="/" class="logo me-auto"><img src="/static/images/rapid/favicon.png" alt="" class="img-fluid"></a>  -->
      <h1 class="logo me-auto"><a href="/">3Design</a></h1>
      
      <nav id="navbar" class="navbar order-last order-lg-0">
        <ul>
          <li><a class="nav-link active" href="/">Home</a></li>
          <li><a class="nav-link" href="/object/create">Create</a></li>
          <li><a class="nav-link" href="/my-objects">My Designs</a></li>
          <#if (user?has_content)>
          <li class="dropdown"><a href="/account"><span>Account</span> <i class="bi bi-chevron-down"></i></a>
            <ul>
              <li class="dropdown"><a href="/account"><span>My Account</span> <i class="bi bi-chevron-right"></i></a>
                <ul>
                  <li><a href="/account/update">Update</a></li>
                  <li><a href="/account/reset-password">Reset password</a></li>
                  <li><a href="/account/logout">Logout</a></li>
                </ul>
              </li>
              <li><a href="/cart">Cart</a></li>
              <li><a href="/tracking">Track objects</a></li>
              <li><a href="/history">object history</a></li>
              <li><a href="/notification">Notifications</a></li>
            </ul>
          </li>
          <#else>
          <li><a class="nav-link" href="/auth/login">SignIn</a></li>
          </#if>
          <#if (admin?has_content)>
            <li><a class="nav-link" href="/admin">Admin</a></li>
          </#if>
        </ul>
        <i class="bi bi-list mobile-nav-toggle"></i>
      </nav>
      
      <div class="social-links">
        <a href="#" class="instagram"><i class="bi bi-instagram"></i></a>
      </div>

    </div>
  </header>
</#macro>