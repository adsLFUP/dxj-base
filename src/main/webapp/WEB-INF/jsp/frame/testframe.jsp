<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Add a pagination to TreeGrid - jQuery EasyUI Demo</title>
    <link rel="stylesheet" type="text/css" href="https://www.jeasyui.com/easyui/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="https://www.jeasyui.com/easyui/themes/icon.css">
    <script type="text/javascript" src="https://www.jeasyui.com/easyui/jquery.min.js"></script>
    <script type="text/javascript" src="https://www.jeasyui.com/easyui/jquery.easyui.min.js"></script>
</head>
<body>
    <h2>Add a pagination to TreeGrid</h2>
    <p>This example shows how to add a pagination to TreeGrid.</p>
    
    <table title="Products" class="easyui-treegrid" style="width:700px;height:300px"
            data-options="
                url: 'treegrid4_getdata.php',
                rownumbers: true,
                pagination: true,
                pageSize: 2,
                pageList: [2,10,20],
                idField: 'id',
                treeField: 'name',
                onBeforeLoad: function(row,param){
                    if (!row) {    // load top level rows
                        param.id = 0;    // set id=0, indicate to load new page rows
                    }
                }
            ">
        <thead>
            <tr>
                <th field="name" width="250">Name</th>
                <th field="quantity" width="100" align="right">Quantity</th>
                <th field="price" width="150" align="right" formatter="formatDollar">Price</th>
                <th field="total" width="150" align="right" formatter="formatDollar">Total</th>
            </tr>
        </thead>
    </table>
    <script>
        function formatDollar(value){
            if (value){
                return '$'+value;
            } else {
                return '';
            }
        }
    </script>
</body>
</html>