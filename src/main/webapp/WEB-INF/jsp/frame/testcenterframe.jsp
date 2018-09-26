<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="../common/tag.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Full Layout - jQuery EasyUI Demo</title>
<%@include file="../common/head.jsp"%>
<script type="text/javascript">
	var data = $
	{
		frameOrgan
	};
	function toggle() {
		var opts = $('#sm').sidemenu('options');
		$('#sm').sidemenu(opts.collapsed ? 'expand' : 'collapse');
		opts = $('#sm').sidemenu('options');
		$('#sm').sidemenu('resize', {
			width : opts.collapsed ? 60 : 200
		})
	}

	function appendnodes(node) {
		if (node) {
			if (node.children == "" & node.level == 'line') {
				//  					node.children = "";
				$.ajax({
					url : 'getInnerOrgan.do?lastOrganGuid='
							+ node.t_organization_r_guid + '&lineGuid='
							+ node.t_line_r_guid,
					type : 'get',
					dataType : "json",
					async : false,
					success : function(resultData) {
						var nodes = resultData;
						$('#sm').tree('append', {
							parent : node.target,
							data : nodes
						});
					}
				});
			}
		}
	}

	$(function() {
		$('#sm').tree({
			onBeforeExpand : function(node) {
				appendnodes(node);
			}
		});
	});
</script>
</head>
<body class="easyui-layout">
	<!-- 	西边组织机构 -->
	<div id="sm"
		data-options="region:'west',split:true,title:'组织机构',data:data"
		style="width: 220px; padding: 0px;" class="easyui-tree"></div>

	<!-- 	中心展示内容 -->
	<!-- 	<div data-options="region:'center',title:'Center'"> -->
	<!-- 		<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-add'">Add</a> -->
	<!-- 		<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-remove'">Remove</a> -->
	<!-- 		<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-save'">Save</a> -->
	<!-- 		<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-cut',disabled:true">Cut</a> -->
	<!-- 		<a href="#" class="easyui-linkbutton">Text Button</a> -->
	<!-- 	</div> -->

	<div region="center" split="false" title="" iconcls="icon-search"
		style="height: 33.0114px; background: rgb(244, 244, 244); overflow: hidden; width: 1318.01px;"
		collapsible="true" class="panel-body panel-body-noheader layout-body">

		<a href="#" class="easyui-linkbutton"
			data-options="iconCls:'icon-add'">Add</a> <a href="#"
			class="easyui-linkbutton" data-options="iconCls:'icon-remove'">Remove</a>
		<a href="#" class="easyui-linkbutton"
			data-options="iconCls:'icon-save'">Save</a> <a href="#"
			class="easyui-linkbutton"
			data-options="iconCls:'icon-cut',disabled:true">Cut</a> <a href="#"
			class="easyui-linkbutton">Text Button</a>

		<div class="datagrid-header" style="width: 1292px; height: 24.0057px;">
			<div class="datagrid-header-inner" style="display: block;">
				<table class="datagrid-htable" border="0" cellspacing="0"
					cellpadding="0" style="height: 25px;">
					<tbody>
						<tr class="datagrid-header-row">
							<td field="T_Organization_Name"><div class="datagrid-cell"
									style="width: 172px;">
									<span>所属机构</span><span class="datagrid-sort-icon">&nbsp;</span>
								</div></td>
							<td field="Group_Name"><div class="datagrid-cell"
									style="width: 172px;">
									<span>设备名称</span><span class="datagrid-sort-icon">&nbsp;</span>
								</div></td>
							<td field="Sort_No"><div class="datagrid-cell"
									style="width: 172px;">
									<span>排序序号</span><span class="datagrid-sort-icon">&nbsp;</span>
								</div></td>
							<td field="Name" style="display: none;"><div
									class="datagrid-cell" style="width: 172px;">
									<span>设备名称</span><span class="datagrid-sort-icon">&nbsp;</span>
								</div></td>
							<td field="Code" style="display: none;"><div
									class="datagrid-cell" style="width: 172px;">
									<span>设备代码</span><span class="datagrid-sort-icon">&nbsp;</span>
								</div></td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</body>
</html>