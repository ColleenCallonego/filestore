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
    <#assign section="network">
    <#include "menu.ftl">
    <section class="section-container">
        <div class="content-wrapper">
            <div class="content-heading">
                <div>Search Neighbourhood
                    <small>Search and filter results</small>
                </div>
            </div>
            <div class="row mt-4 m-3">
                <div class="col-lg-9">
                    <div class="form-group mb-4">
                        <input class="form-control mb-2" type="text" placeholder="Search neighbours, folders, files, etc.">
                        <div class="d-flex">
                            <button class="btn btn-secondary" type="button">Search</button>
                            <div class="ml-auto">
                                <label class="c-checkbox">
                                    <input id="inlineCheckbox30" type="checkbox" value="option3">
                                    <span class="fa fa-check"></span> Neighbour</label>
                                <label class="c-checkbox">
                                    <input id="inlineCheckbox10" type="checkbox" value="option1">
                                    <span class="fa fa-check"></span> Folder</label>
                                <label class="c-checkbox">
                                    <input id="inlineCheckbox20" type="checkbox" value="option2">
                                    <span class="fa fa-check"></span> File</label>
                            </div>
                        </div>
                    </div>
                    <div class="card card-default">
                        <div class="card-header">
                            <a class="float-right" href="#" data-tool="panel-refresh" data-toggle="tooltip" title="Refresh Card">
                                <em class="fas fa-sync"></em>
                            </a>Search Results</div>
                        <div class="table-responsive">
                            <table class="table table-striped table-bordered table-hover">
                                <thead>
                                <tr>
                                    <th data-check-all="" width="25px">
                                        <div class="checkbox c-checkbox" data-toggle="tooltip" data-title="Check All">
                                            <label class="m-0">
                                                <input type="checkbox">
                                                <span class="fa fa-check"></span>
                                            </label>
                                        </div>
                                    </th>
                                    <th>Description</th>
                                </tr>
                                </thead>
                                <tbody>
                                <#list content.neighbours as neighbour>
                                <tr>
                                    <td>
                                        <div class="checkbox c-checkbox">
                                            <label>
                                                <input type="checkbox">
                                                <span class="fa fa-check"></span>
                                            </label>
                                        </div>
                                    </td>
                                    <td>
                                        <div class="media align-items-center">
                                            <a class="mr-3" href="#">
                                                <img class="img-fluid rounded thumb64" src="/img/dummy.png" alt="">
                                            </a>
                                            <div class="media-body d-flex">
                                                <div>
                                                    <h4 class="m-0">${neighbour.name}</h4>
                                                    <small class="text-muted">http://${neighbour.address}</small>
                                                    <p>Description of neighbour shares.</p>
                                                </div>
                                                <div class="ml-auto">
                                                    <div class="btn btn-info btn-sm">View</div>
                                                </div>
                                            </div>
                                        </div>
                                    </td>
                                </tr>
                                </#list>
                                </tbody>
                            </table>
                        </div>
                        <div class="card-footer">
                            <div class="d-flex">
                                <button class="btn btn-sm btn-secondary">Clear</button>
                                <nav class="ml-auto">
                                    <ul class="pagination pagination-sm">
                                        <li class="page-item active">
                                            <a class="page-link" href="#">1</a>
                                        </li>
                                        <li class="page-item">
                                            <a class="page-link" href="#">2</a>
                                        </li>
                                        <li class="page-item">
                                            <a class="page-link" href="#">3</a>
                                        </li>
                                        <li class="page-item">
                                            <a class="page-link" href="#">»</a>
                                        </li>
                                    </ul>
                                </nav>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-lg-3">
                    <h3 class="m-0 pb-3">Filters</h3>
                    <div class="form-group mb-4">
                        <label class="col-form-label mb-2">by FileType</label>
                        <br>
                        <select class="chosen-select form-control">
                            <optgroup label="IMAGES">
                                <option>image/jpeg</option>
                                <option>image/png</option>
                                <option>image/gif</option>
                                <option>all images</option>
                            </optgroup>
                            <optgroup label="MOVIES">
                                <option>all movies</option>
                            </optgroup>
                            <optgroup label="BOOKS">
                                <option>application/pdf</option>
                                <option>all books</option>
                            </optgroup>
                            <optgroup label="AUDIO">
                                <option>audio/flac</option>
                                <option>audio/mp3</option>
                                <option>audio/ogv</option>
                                <option>all audio</option>
                            </optgroup>
                        </select>
                    </div>
                    <div class="form-group">
                        <label class="col-form-label mb-2">by Date</label>
                        <br>
                        <div class="input-group date" id="datetimepicker">
                            <input class="form-control" type="text">
                            <span class="input-group-append input-group-addon">
                           <span class="input-group-text fas fa-calendar-alt"></span>
                        </span>
                        </div>
                    </div>
                    <button class="btn btn-secondary btn-lg">Apply</button>
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



