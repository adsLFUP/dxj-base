<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="../../common/tag.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Full Layout - jQuery EasyUI Demo</title>
<%@include file="../../common/head.jsp"%>
<script type="text/javascript">
	(function($){
		function pagerFilter(data){
			if ($.isArray(data)){	// is array
				data = {
					total: data.length,
					rows: data
				}
			}
			var target = this;
			var dg = $(target);
			var state = dg.data('datagrid');
			var opts = dg.datagrid('options');
			if (!state.allRows){
				state.allRows = (data.rows);
			}
			if (!opts.remoteSort && opts.sortName){
				var names = opts.sortName.split(',');
				var orders = opts.sortOrder.split(',');
				state.allRows.sort(function(r1,r2){
					var r = 0;
					for(var i=0; i<names.length; i++){
						var sn = names[i];
						var so = orders[i];
						var col = $(target).datagrid('getColumnOption', sn);
						var sortFunc = col.sorter || function(a,b){
							return a==b ? 0 : (a>b?1:-1);
						};
						r = sortFunc(r1[sn], r2[sn]) * (so=='asc'?1:-1);
						if (r != 0){
							return r;
						}
					}
					return r;
				});
			}
			var start = (opts.pageNumber-1)*parseInt(opts.pageSize);
			var end = start + parseInt(opts.pageSize);
			data.rows = state.allRows.slice(start, end);
			return data;
		}

		var loadDataMethod = $.fn.datagrid.methods.loadData;
		var deleteRowMethod = $.fn.datagrid.methods.deleteRow;
		$.extend($.fn.datagrid.methods, {
			clientPaging: function(jq){
				return jq.each(function(){
					var dg = $(this);
                    var state = dg.data('datagrid');
                    var opts = state.options;
                    opts.loadFilter = pagerFilter;
                    var onBeforeLoad = opts.onBeforeLoad;
                    opts.onBeforeLoad = function(param){
                        state.allRows = null;
                        return onBeforeLoad.call(this, param);
                    }
                    var pager = dg.datagrid('getPager');
					pager.pagination({
						onSelectPage:function(pageNum, pageSize){
							opts.pageNumber = pageNum;
							opts.pageSize = pageSize;
							pager.pagination('refresh',{
								pageNumber:pageNum,
								pageSize:pageSize
							});
							dg.datagrid('loadData',state.allRows);
						}
					});
                    $(this).datagrid('loadData', state.data);
                    if (opts.url){
                    	$(this).datagrid('reload');
                    }
				});
			},
            loadData: function(jq, data){
                jq.each(function(){
                    $(this).data('datagrid').allRows = null;
                });
                return loadDataMethod.call($.fn.datagrid.methods, jq, data);
            },
            deleteRow: function(jq, index){
            	return jq.each(function(){
            		var row = $(this).datagrid('getRows')[index];
            		deleteRowMethod.call($.fn.datagrid.methods, $(this), index);
            		var state = $(this).data('datagrid');
            		if (state.options.loadFilter == pagerFilter){
            			for(var i=0; i<state.allRows.length; i++){
            				if (state.allRows[i] == row){
            					state.allRows.splice(i,1);
            					break;
            				}
            			}
            			$(this).datagrid('loadData', state.allRows);
            		}
            	});
            },
            getAllRows: function(jq){
            	return jq.data('datagrid').allRows;
            }
		})
	})(jQuery);

	function getData(){
		var rows = [];
		for(var i=1; i<=800; i++){
			var amount = Math.floor(Math.random()*1000);
			var price = Math.floor(Math.random()*1000);
			rows.push({
				inv: 'Inv No '+i,
				date: $.fn.datebox.defaults.formatter(new Date()),
				name: 'Name '+i,
				amount: amount,
				price: price,
				cost: amount*price,
				note: 'Note '+i
			});
		}
		return rows;
	}
	
	$(function(){
		$('#dg').datagrid({
			data:getData(),
		}).datagrid('clientPaging');
	});
</script>
</head>
<body class="easyui-layout">
	<!-- 	中心展示内容 -->
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
	<div class="easyui-layout"  style="width:100%;height:96%;">
		<table id="dg" style="width:700px;height:300px" data-options="
					rownumbers:true,
					singleSelect:true,
					autoRowHeight:false,
					pagination:true,
					fit:true,
					pageSize:10">
			<thead>
				<tr>
					<th field="inv" width="80">Inv No</th>
					<th field="date" width="100">Date</th>
					<th field="name" width="80">Name</th>
					<th field="amount" width="80" align="right">Amount</th>
					<th field="price" width="80" align="right">Price</th>
					<th field="cost" width="100" align="right">Cost</th>
					<th field="note" width="110">Note</th>
				</tr>
			</thead>
		</table>
	</div>
		
		
		
		
	</div>
</body>
</html>