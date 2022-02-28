<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
	<meta name="description" content="File Store Drive">
	<meta name="keywords" content="app, responsive, jquery, bootstrap, dashboard, admin">
	<link rel="icon" type="image/x-icon" href="/favicon.ico">
	<title>File Store - Drive - Login</title>

	<link rel="stylesheet" href="/vendor/@fortawesome/fontawesome-free-webfonts/css/fa-brands.css">
	<link rel="stylesheet" href="/vendor/@fortawesome/fontawesome-free-webfonts/css/fa-regular.css">
	<link rel="stylesheet" href="/vendor/@fortawesome/fontawesome-free-webfonts/css/fa-solid.css">
	<link rel="stylesheet" href="/vendor/@fortawesome/fontawesome-free-webfonts/css/fontawesome.css">
	<link rel="stylesheet" href="/vendor/simple-line-icons/css/simple-line-icons.css">
	<link rel="stylesheet" href="/css/bootstrap.css" id="bscss">
	<link rel="stylesheet" href="/css/app.css" id="maincss">
</head>

<body>
<div class="wrapper">
	<div class="wrapper">
		<div class="block-center mt-4 wd-xl">
			<div class="card card-flat">
				<div class="card-header text-center bg-dark">
					<a href="#">
						<img class="block-center rounded" src="/img/logo.png" alt="Image">
					</a>
				</div>
				<div class="card-body">
					<p class="text-center py-2">SIGN IN TO CONTINUE.</p>
					<form class="mb-3" id="loginForm" novalidate action='<%= response.encodeURL("j_security_check")%>'>
						<div class="form-group">
							<div class="input-group with-focus">
								<input name="j_username" class="form-control border-right-0" id="exampleInputEmail1" type="email" placeholder="Enter email" autocomplete="off" required>
								<div class="input-group-append">
                           <span class="input-group-text text-muted bg-transparent border-left-0">
                              <em class="fa fa-envelope"></em>
                           </span>
								</div>
							</div>
						</div>
						<div class="form-group">
							<div class="input-group with-focus">
								<input name="j_password" class="form-control border-right-0" id="exampleInputPassword1" type="password" placeholder="Password" required>
								<div class="input-group-append">
                           <span class="input-group-text text-muted bg-transparent border-left-0">
                              <em class="fa fa-lock"></em>
                           </span>
								</div>
							</div>
						</div>
						<button class="btn btn-block btn-primary mt-3" type="submit">Login</button>
					</form>
				</div>
			</div>
			<div class="p-3 text-center">
				<span class="mr-2">&copy;</span>
				<span>2018</span>
				<span class="mr-2">-</span>
				<span>Filestore</span>
			</div>
		</div>
	</div>
	<script src="/vendor/modernizr/modernizr.custom.js"></script>
	<script src="/vendor/jquery/dist/jquery.js"></script>
	<script src="/vendor/bootstrap/dist/js/bootstrap.js"></script>
	<script src="/vendor/js-storage/js.storage.js"></script>
	<script src="/vendor/parsleyjs/dist/parsley.js"></script>
	<script src="/js/app.js"></script>
</div>
</body>
</html>