$(document).ready(function(){
    var json3 = [
            {"cgId":4,"cgName":"酒水","cgParentId":0,},
            {"cgId":6,"cgName":"食品","cgParentId":0,},
            {"cgId":7,"cgName":"日用","cgParentId":0,},
            {"cgId":8,"cgName":"肉类","cgParentId":0,}
    ]
    var json4 = [
        {"cgId":4,"cgName":"酒水","cgParentId":0,
            "category":
                [{"cgId":9,"cgName":"洋酒","cgParentId":4,"category":null},
                {"cgId":10,"cgName":"红酒","cgParentId":4,"category":null},
                {"cgId":11,"cgName":"啤酒","cgParentId":4,"category":null},
                {"cgId":12,"cgName":"白酒","cgParentId":4,"category":null}
                ]
            },
            {"cgId":6,"cgName":"食品","cgParentId":0,
            "category":
                [
                    {"cgId":13,"cgName":"速食","cgParentId":6,"category":null},
                    {"cgId":14,"cgName":"罐头","cgParentId":6,"category":null},
                    {"cgId":15,"cgName":"零食","cgParentId":6,"category":null},
                    {"cgId":16,"cgName":"干货","cgParentId":6,"category":null}
                ]},
            {"cgId":7,"cgName":"日用","cgParentId":0,
            "category":
                [{"cgId":17,"cgName":"洗漱","cgParentId":7,"category":null},
                {"cgId":18,"cgName":"厨具","cgParentId":7,"category":null}
            ]},
            {"cgId":8,"cgName":"肉类","cgParentId":0,
            "category":
                [
                    {"cgId":19,"cgName":"蛋禽","cgParentId":8,"category":null},
                    {"cgId":20,"cgName":"猪肉","cgParentId":8,"category":null},
                    {"cgId":21,"cgName":"牛羊肉","cgParentId":8,"category":null},
                    {"cgId":22,"cgName":"水产","cgParentId":8,"category":null}
                ]
            }
    ]
    //检查是否重复
    function checkValid(table,id){
        var isValid = true;
        var $htr=table.find("tr");
        $.each($htr,function(i,n){
            if($(n).attr("id") == id){
                isValid = false;
                return false;
            }
        });
        return isValid;
    }
   
    //一级类别管理渲染
    function addOneType(json){
        var self=$("#onetypeTable");
        var $str='';
        $.each(json, function (i, n) {
            if(!checkValid(self,n.cgId)) return false;
                $str += '<tr id="'+n.cgId+'">';
                $str += '<td>' + n.cgId + '</td>';
                $str += '<td>'+ n.cgName +'</td><td>';
                $str += '<a class="deleteType">删除</a><a class="corretType" data-toggle="modal" data-target="#typeModal">修改</a>';
                $str +='</td>';
                $str += '</tr>';
    });
    self.append($str);
    }
    addOneType(json3);

    //二级类别管理渲染
    function addTwoType(json){
        var self=$("#twotypeTable");
        var $str='';
        $.each(json, function (i, n) {
            if(!checkValid(self,n.cgId)) return false;
            $.each(n.category, function (j,s) { 
                $str += '<tr id="'+n.cgId+'">';
                $str += '<td>' + s.cgId + '</td>';
                $str += '<td>' + s.cgName + '</td>';
                $str += '<td>'+ n.cgName +'</td><td>';
                $str += '<a class="deleteType">删除</a><a class="corretType" data-toggle="modal" data-target="#typeModal">修改</a>';
                $str +='</td>';
                $str += '</tr>';
            })
    });
    self.append($str);
    }
    addTwoType(json4);
    //点击添加一级类别触发事件
    $(".addFirstType").click(function () {
        $('.typeTtile').text('添加类别')
        $('.typeBtn').text('添加')
        $('.typeBox').html('<div><span>一级类别：</span><input type="text"  class="firstType"></div>')
    })
    //点击添加二级类别触发事件
    $(".addSecondType").click(function () {
        var schools = [
            { 'id': 1, 'name': '南京大学' },
            { 'id': 2, 'name': '北京大学' },
            { 'id': 3, 'name': '浙江大学' },
            { 'id': 4, 'name': '清华大学' },
            { 'id': 5, 'name': '湖南大学' },
        ];
        $('.typeTtile').text('添加类别')
        $('.typeBtn').text('添加')
        var html='';
        html+='<div><span>一级类别：</span><select class="typeSelect"> '
        for (var i = 0; i < schools.length; i++){
            console.log()
                html+='<option value="' + schools[i].id + '" >'+ schools[i].name+'</option>';
        }
        html+='</select></div>'
        html+='<div><span>二级类别：</span><input   class="secondType"/></div>'
        $('.typeBox').html(html);
    })
    //点击删除类别按钮触发事
    $(".deleteType").click(function () {
        layer.confirm('您确定要删除吗', function(index){
            layer.close(index);
        });
    })
    //点击修改类别按钮触发事件
    $(".corretType").click(function () {
        $('.typeTtile').text('修改类别')
        $('.typeBtn').text('修改')
    })
})