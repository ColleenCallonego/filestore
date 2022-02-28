<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <meta name="description" content="File Store Drive">
    <meta name="keywords" content="app, responsive, jquery, bootstrap, dashboard, admin">
    <link rel="icon" type="image/x-icon" href="/favicon.ico">
    <title>File Store - Drive</title>

    <link rel="stylesheet" href="/vendor/@fortawesome/fontawesome-free-webfonts/css/fa-brands.css">
    <link rel="stylesheet" href="/vendor/@fortawesome/fontawesome-free-webfonts/css/fa-regular.css">
    <link rel="stylesheet" href="/vendor/@fortawesome/fontawesome-free-webfonts/css/fa-solid.css">
    <link rel="stylesheet" href="/vendor/@fortawesome/fontawesome-free-webfonts/css/fontawesome.css">
    <link rel="stylesheet" href="/vendor/simple-line-icons/css/simple-line-icons.css">
    <link rel="stylesheet" href="/vendor/animate.css/animate.css">
    <link rel="stylesheet" href="/vendor/whirl/dist/whirl.css">
    <link rel="stylesheet" href="/css/bootstrap.css" id="bscss">
    <link rel="stylesheet" href="/css/app.css" id="maincss">
</head>

<body>
<div class="wrapper">
    <header class="topnavbar-wrapper">
        <nav class="navbar topnavbar">
            <div class="navbar-header">
                <a class="navbar-brand" href="#/">
                    <div class="brand-logo">
                        <img class="img-fluid" src="/img/logo.png" alt="FileStore Logo">
                    </div>
                    <div class="brand-logo-collapsed">
                        <img class="img-fluid" src="/img/logo-single.png" alt="FileStore Logo">
                    </div>
                </a>
            </div>
            <ul class="navbar-nav mr-auto flex-row">
                <li class="nav-item">
                    <a class="nav-link d-none d-md-block d-lg-block d-xl-block" href="#" data-trigger-resize="" data-toggle-state="aside-collapsed">
                        <em class="fas fa-bars"></em>
                    </a>
                    <a class="nav-link sidebar-toggle d-md-none" href="#" data-toggle-state="aside-toggled" data-no-persist="true">
                        <em class="fas fa-bars"></em>
                    </a>
                </li>
            </ul>
        </nav>
    </header>
    <#assign section="status">
    <#include "menu.ftl">
    <section class="section-container">
        <div class="content-wrapper">
            <div class="row m-4">
                <div class="col-xl-3 col-md-6">
                    <!-- START card-->
                    <div class="card flex-row align-items-center align-items-stretch border-0">
                        <div class="col-4 d-flex align-items-center bg-primary-dark justify-content-center rounded-left">
                            <em class="icon-cloud-upload fa-3x"></em>
                        </div>
                        <div class="col-8 py-3 bg-primary rounded-right">
                            <div class="h2 mt-0">
                                <#if content.status.store.metrics['upload']??>${content.status.store.metrics['upload']}<#else>0</#if> /
                                <small> (<#if content.status.store.latestMetrics['upload']??>${content.status.store.latestMetrics['upload']}<#else>0</#if>)</small>
                            </div>
                            <div class="text-uppercase">Uploads<small> (max / latests)</small></div>
                        </div>
                    </div>
                </div>
                <div class="col-xl-3 col-md-6">
                    <!-- START card-->
                    <div class="card flex-row align-items-center align-items-stretch border-0">
                        <div class="col-4 d-flex align-items-center bg-purple-dark justify-content-center rounded-left">
                            <em class="icon-cloud-download fa-3x"></em>
                        </div>
                        <div class="col-8 py-3 bg-purple rounded-right">
                            <div class="h2 mt-0">
                                <#if content.status.store.metrics['download']??>${content.status.store.metrics['download']}<#else>0</#if> /
                                <small> (<#if content.status.store.latestMetrics['download']??>${content.status.store.latestMetrics['download']}<#else>0</#if>)</small>
                            </div>
                            <div class="text-uppercase">Downloads<small> (max / latests)</small></div>
                        </div>
                    </div>
                </div>
                <div class="col-xl-3 col-lg-6 col-md-12">
                    <!-- START card-->
                    <div class="card flex-row align-items-center align-items-stretch border-0">
                        <div class="col-4 d-flex align-items-center bg-green-dark justify-content-center rounded-left">
                            <em class="icon-speedometer fa-3x"></em>
                        </div>
                        <div class="col-8 py-3 bg-green rounded-right">
                            <div class="h2 mt-0">
                                ${helper.sizeToBytes(content.status.server.availableMemory, false)} /
                                <small>${helper.sizeToBytes(content.status.server.totalMemory, false)}</small>
                            </div>
                            <div class="text-uppercase">Memory <small>(used/total)</small></div>
                        </div>
                    </div>
                </div>
                <div class="col-xl-3 col-lg-6 col-md-12">
                    <!-- START date widget-->
                    <div class="card flex-row align-items-center align-items-stretch border-0">
                        <div class="col-4 d-flex align-items-center bg-green justify-content-center rounded-left">
                            <div class="text-center">
                                <!-- See formats: https://docs.angularjs.org/api/ng/filter/date-->
                                <div class="text-sm" data-now="" data-format="MMMM"></div>
                                <br>
                                <div class="h2 mt-0" data-now="" data-format="D"></div>
                            </div>
                        </div>
                        <div class="col-8 py-3 rounded-right">
                            <div class="text-uppercase" data-now="" data-format="dddd"></div>
                            <br>
                            <div class="h2 mt-0" data-now="" data-format="h:mm"></div>
                            <div class="text-muted text-sm" data-now="" data-format="a"></div>
                        </div>
                    </div>
                    <!-- END date widget-->
                </div>
            </div>
        </div>
    </section>
</div>
<script src="/vendor/modernizr/modernizr.custom.js"></script>
<script src="/vendor/jquery/dist/jquery.js"></script>
<script src="/vendor/popper.js/dist/umd/popper.js"></script>
<script src="/vendor/bootstrap/dist/js/bootstrap.js"></script>
<script src="/vendor/js-storage/js.storage.js"></script>
<script src="/vendor/jquery.easing/jquery.easing.js"></script>
<script src="/vendor/animo/animo.js"></script>
<script src="/vendor/screenfull/dist/screenfull.js"></script>
<script src="/vendor/moment/min/moment-with-locales.js"></script>
<script src="/js/app.js"></script>
</body>
</html>



