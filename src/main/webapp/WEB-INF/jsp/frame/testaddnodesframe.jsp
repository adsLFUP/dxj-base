<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>jQuery EasyUI</title>
	<link rel="stylesheet" type="text/css" href="http://www.jeasyui.net/Public/js/easyui/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="http://www.jeasyui.net/Public/js/easyui/themes/icon.css">
	<script type="text/javascript" src="http://code.jquery.com/jquery-1.4.2.min.js"></script>
	<script type="text/javascript" src="http://www.jeasyui.net/Public/js/easyui/jquery.easyui.min.js"></script>
	<script type="text/javascript">
		function appendnodes(){
			var node = $('#tt').tree('getSelected');
			if (node){
				var nodes = [{
					"id":13,
					"text":"Raspberry"
				},{
					"id":14,
					"text":"Cantaloupe"
				}];
				$('#tt').tree('append', {
					parent:node.target,
					data:nodes
				});
			}
		}
	</script>
</head>
<body>
	<h1>Tree</h1>
	<div style="margin-bottom:10px;">
		<a href="#" onclick="appendnodes()">append nodes</a>
	</div>
	<div style="width:200px;height:auto;border:1px solid #ccc;">
<!-- 		<ul id="tt" class="easyui-tree" url="tree_data.json"></ul> -->
		<ul id="tt" class="easyui-tree" url="../json/test/tree_data.json"></ul>
	</div>
</body>
</html>