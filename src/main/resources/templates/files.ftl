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
    <link rel="stylesheet" href="/css/app.css" id="maincss">ft
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
    <#assign section="files">
    <#include "menu.ftl">
    <section class="section-container">
        <div class="content-wrapper">
            <div class="content-heading">
                <div>
                    <div class="btn-group" role="group">
                        <button class="btn btn-secondary" type="button">
                            <a href="/api/files"><em class="fa fa-home"></em></a>
                        </button>
                        <#list content.path as path>
                            <button class="btn btn-secondary" type="button">
                                <a href="/api/files/${path.id}/content">${path.name}</a>
                            </button>
                        </#list>
                    </div>
                </div>
                <div class="ml-auto">
                    <div class="btn-group" role="group">
                        <button class="btn btn-secondary" type="button" data-toggle="modal" data-target="#createFolderModal">
                            <em class="fa fa-folder"></em>
                        </button>
                        <button class="btn btn-secondary" type="button" data-toggle="modal" data-target="#uploadFileModal">
                            <em class="fa fa-upload"></em>
                        </button>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-12">
                    <#if content.parent.parent?? && content.parent.parent == "42" && content.parent.name = "IMAGES">
                        <#list content.items as item>
                            <div class="container">
                                <img height="200px" src="/api/files/${item.id}/content" alt="${item.id}">
                                <div height="200px" style="position: absolute; bottom: 0; background: rgb(0, 0, 0); background: rgba(0, 0, 0, 0.5); width: 100%; transition: .5s ease; opacity:0; padding: 20px;" class="overlay">
                                    <a href="/api/files/${item.id}/content?download=true" class="mr-3"><i class="fas fa-download"></i></a>
                                    <a href="/api/files/${item.id}/content" class="mr-3"><i class="fas fa-eye"></i></a>
                                    <a href="/api/files/del/${item.parent}/${item.name}"><i class="fa fa-trash"></i></a>
                                </div>
                            </div>
                        </#list>
                    <#else>
                    <table class="table table-striped w-100" id="filestable">
                        <thead>
                        <tr>
                            <th data-priority="1">Nom</th>
                            <th>Taille</th>
                            <th>Type</th>
                            <th class="sort-numeric">Dernière Modification</th>
                            <th>Action</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#if content.parent.parent??>
                        <tr>
                            <td>
                                <i class="fa fa-folder mr-2"></i>
                                <a href="/api/files/${content.parent.parent}/content">..</a>
                            </td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                        </tr>
                        </#if>
                        <#list content.items as item>
                        <tr>
                            <td>
                                <i class="fa ${helper.mimetypeToIcon(item.mimeType)} mr-2"></i>
                                <#if item.isFolder() >
                                    <a href="/api/files/${item.id}/content">${item.name}</a>
                                <#else>
                                    <a href="/api/files/${item.id}/content?download=false">${item.name}</a>
                                </#if>
                            </td>
                            <td>${helper.sizeToBytes(item.size, false)}</td>
                            <td>${item.mimeType}</td>
                            <td>${item.modificationDate}</td>
                            <td>
                                <#if !item.isFolder() >
                                    <a href="/api/files/${item.id}/content?download=true" class="mr-3"><i class="fas fa-download"></i></a>
                                    <a href="/api/files/${item.id}/content" class="mr-3"><i class="fas fa-eye"></i></a>
                                    <a href="/api/files/del/${item.parent}/${item.name}"><i class="fa fa-trash"></i></a>
                                <#else>
                                    <a href="/api/files/del/${item.parent}/${item.name}"><i class="fa fa-trash"></i></a>
                                </#if>
                            </td>
                        </tr>
                        </#list>
                        </tbody>
                    </table>
                    </#if>
                </div>
            </div>
        </div>
    </section>
</div>
<div id="createFolderModal" class="modal fade" role="dialog">
    <div class="modal-dialog">
        <form method="post" action="/api/files/${content.parent.id}" enctype="multipart/form-data">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h4 class="modal-title align-content-center">Create new Folder</h4>
                </div>
                <div class="modal-body">
                    <div class="form-row">
                        <div class="input-group mb-3">
                            <div class="input-group-prepend">
                                <span class="input-group-text" id="basic-addon1">Folder name</span>
                            </div>
                            <input type="text" id="name" name="name" class="form-control" aria-label="Folder Name" aria-describedby="basic-addon1">
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
                    <button type="submit" class="btn btn-primary" id="upload">Create</button>
                </div>
            </div>
        </form>
    </div>
</div>
<div id="uploadFileModal" class="modal fade" role="dialog">
    <div class="modal-dialog">
        <form method="post" action="/api/files/${content.parent.id}" enctype="multipart/form-data">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h4 class="modal-title">Upload file</h4>
                </div>
                <div class="modal-body">
                    <div class="form-row">
                        <div class="input-group col-md-12">
                            <label class="input-group-btn">
                                <span class="btn btn-primary">
                                    Browse… <input type="file" style="display: none;" id="file" name="data">
                                </span>
                            </label>
                            <input type="text" class="form-control" id="filename" name="name" readonly="">
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
                    <button type="submit" class="btn btn-primary" id="upload">Upload</button>
                </div>
            </div>
        </form>
    </div>
</div>
<div id="ViewFileModal" class="modal fade" role="dialog">

</div>
<script src="/vendor/modernizr/modernizr.custom.js"></script>
<script src="/vendor/jquery/dist/jquery.js"></script>
<script src="/vendor/popper.js/dist/umd/popper.js"></script>
<script src="/vendor/bootstrap/dist/js/bootstrap.js"></script>
<script src="/vendor/js-storage/js.storage.js"></script>
<script src="/vendor/jquery.easing/jquery.easing.js"></script>
<script src="/vendor/animo/animo.js"></script>
<script src="/vendor/screenfull/dist/screenfull.js"></script>
<script src="/js/app.js"></script>
<script>
    $('#file').change(function() {
        console.log("called");
        var filename = $('#file').val().replace(/\\/g, '/').replace(/.*\//, '');
        console.log(filename);
        $('#filename').val(filename);
    });

    function image(id){

    }
</script>
<style>
    .container:hover .overlay {
        opacity: 1;
    }
</style>
</body>
</html>