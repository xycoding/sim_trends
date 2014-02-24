<%@ page language="java" contentType="text/html" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>微博主题提示，示例</title> 
<meta http-equiv="content" content="text/html;charset=utf-8">

<script type="text/javascript" src="http://code.jquery.com/jquery-2.0.3.js"></script>
<script type="text/javascript">
function sub()
{
   $.ajax({
   type:"post",
   url:"./t",
   data:{
   status:$("#status").val()
   },
   success:function(data,textStatus){
    $("#resText").html(data);
$("#status").val($("#status").val().trim()+data);   }
   });
}
</script>

</head>

<body>
<form action="./test" id="form" method="post">
<p style="font-style:italic;font-size:24px;">请输入微博内容：&nbsp;&nbsp; <br/></p>
<p><textarea name="status" id="status" rows="3" cols="50" style="font-size:24px;"></textarea></p>
<p><input type="button" id="send" value="试试话题提示？" onclick="sub();" style="font-size:18px;"/></p>
</form>
<div class="recommend">
</div>
<div id="resText" style="color:#FF0000;font-size:36px;">

</div>
<p><br/><br/><br/></p>
<p>eg1:我们刚刚在3w参加了黑客松拉力赛，很赞！！！！很刺激、很兴奋。</p>
<p>eg2:人生可张狂、可失意、可乐极、可痛彻、可笑、可哭，就是不可平凡、不可寡淡、不可无聊。</p>
<p>eg3:月薪一万在北京能过什么样的生活？打算来北京的童鞋可以参考下。（凤凰财经）</p>
</body>
</html>