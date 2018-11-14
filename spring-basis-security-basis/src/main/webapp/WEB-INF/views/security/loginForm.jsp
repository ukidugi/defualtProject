<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="//code.jquery.com/jquery.min.js"></script>
<!-- 합쳐지고 최소화된 최신 CSS -->
<link rel="stylesheet"
	href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css">
<!-- 합쳐지고 최소화된 옵션 테마 -->
<link rel="stylesheet"
	href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap-theme.min.css">
<!-- 합쳐지고 최소화된 최신 자바스크립트 -->
<script src="//netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js"></script>

<style type="text/css">
body {
	background: #eee !important;
}

.wrapper {
	margin-top: 80px;
	margin-bottom: 80px;
}

.form-signin {
	max-width: 380px;
	padding: 15px 35px 45px;
	margin: 0 auto;
	background-color: #fff;
	border: 1px solid rgba(0, 0, 0, 0.1);
	.
	form-signin-heading
	,
	.checkbox
	{
	margin-bottom
	:
	30px;
}

.checkbox {
	font-weight: normal;
}

.form-control {
	position: relative;
	font-size: 16px;
	height: auto;
	padding: 10px;
	@
	include
	box-sizing(border-box);
	&:
	focus
	{
	z-index
	:
	2;
}

}
input[type="text"] {
	margin-bottom: -1px;
	border-bottom-left-radius: 0;
	border-bottom-right-radius: 0;
}

input[type="password"] {
	margin-bottom: 20px;
	border-top-left-radius: 0;
	border-top-right-radius: 0;
}

}
.textCenter {
	text-align: center
}
</style>

<title>Insert title here</title>
</head>
<body>

	<div class="wrapper">
		<div class="form-signin">
			<form action="/j_spring_security_check" method="post" id="form_login">
				<h2 class="form-signin-heading textCenter">Sign In</h2>
				<input type="text" name="j_username" class="form-control" id="j_username">
				<!-- 폼으로  아디를 j_username,비번을 j_password로 보내주면 자동으로 인메모리 디비이든 일반데이터베이스이든 확인함  -->
				<input type="text" name="j_password" class="form-control" id="j_password"><br />
				<button class="btn btn-lg btn-primary btn-block" id="btn_login">로그인</button>
			</form>
		</div>
	</div>

	<div class="wrapper">
		<div class="form-signin">
			<h2 class="form-signin-heading textCenter">Sign Up</h2>
			<input type="text" class="form-control" id="userId" /> 
			<input type="password" class="form-control" id="userPassword" /> <br>
			<button type="button" class="btn btn-lg btn-primary btn-block" id="create">Sign Up</button>
		</div>
	</div>
	
	<script type="text/javascript">

$("#btn_login").on("click",function(){
	
	$("#form_login").submit();
	
});

$("#create").on("click",function(){
	
	console.log($("#userId").val());
	console.log($("#userPassword").val());
 	 $.ajax({
         type:"POST",
         url:"/create",
         data:{userId:$("#userId").val(),userPassword:$("#userPassword").val()},
         dataType:"JSON", // 옵션이므로 JSON으로 받을게 아니면 안써도 됨
         success : function(data) {
               // 통신이 성공적으로 이루어졌을 때 이 함수를 타게 된다.
               // TODO
               
               
         }
   }); 
});

</script>

</body>
</html>
