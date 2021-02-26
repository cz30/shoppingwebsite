$(function(){
	$.ajax({
		url:"/carts/get/list",
		type:"GET",
		data:{
			uId:$.cookie('userId')
		},
		datatype:"json",
		async: false,
		success:function(data){
			
			console.log(data);
			
			if (data.length == 0){
				$('#zyCart-Empty').show();
				$('#zyCart').hide();
			} else {
				var tbody = document.getElementsByTagName ('tbody')[0];
				for ( var i = 0; i < data.length; i++){
					var tr = tbody.insertRow (tbody.rows.length);
					var obj = data[i];

					tr.innerHTML = '<td class="zy-check"><input class="check-one check" type="checkbox" spfId="'+ obj.spfId +'" cartId="'+ obj.cartId +'"></td>'+
						'<td class="zy-item"><div class="picturebox fl"><img src="' +obj.cartImage +'"></div>'+
						'<span class="commodity-name">'+ obj.cartTitle +'</span>'+
						'<div class="commodity-specifications"><span>'+ obj.cartContent +'</span></div></td>'+
						'<td class="zy-price pricenum">'+ obj.cartOprice.toFixed(2) +'</td>'+
						'<td class="zy-amount"><span class="reduce">-</span><input class="count-input" type="text" value="'+obj.cartNum+'"><span class="add">+</span><span class="stock">库存：<span class="stocknumber">' + ((obj.spfCount==null)? 0 : obj.spfCount) + '</span></span></td>'+
						'<td class="zy-sum pricenum">'+ obj.cartNprice.toFixed(2) +'</td>'+
						'<td class="zy-op"><a class="delete">删除</a></td>';
				}
			}
			
		}
	})

	$('#backtoindex').on('click',function () {
		window.open('index.html','_self');
	})
})


/**
 * [模拟队列的方式添加多个事件到onload事件中]
 * @param {[type]} func [description]
 */
function addLoadEvent(func) {
    var oldonload = window.onload;
    if (typeof window.onload != 'function') {
        window.onload = func;
    } else {
        window.onload = function() {
            oldonload();
            func();
        }
    }
}

/**
 * [通过类名获取元素集，并解决了低版本IE对于此方法的不兼容问题]
 * @param  {[string]} className [描述类名的字符串]
 * @return {[元素数组]}           [含有相同类型名的元素数组]
 */
function getElementsByClassName(className) {
    if (!document.getElementsByClassName) {
        document.getElementsByClassName = function(className) {
            var ret = [];
            var els = document.getElementsByTagName('*');
            for (var i = 0, len = els.length; i < len; i++) {
                if (els[i].className == className || els[i].className.indexOf(className + ' ') != -1 ||
                    els[i].className.indexOf(' ' + className) != -1 ||
                    els[i].className.indexOf(' ' + className + ' ') != -1) {
                    ret.push(els[i]);
                }
            }
        }
    }
    return ret;
}

/**
 * [获取carttable中的所有包含商品的行]
 * @return {[type]} [返回所有包含商品的行数组]
 */
function getAllGoodsItems() {
    var cartTable = document.getElementById('cartTable');
    var trs = cartTable.children[1].rows;
    return trs;
}

function getAllCheckInputs() {
    var checkInputs = document.getElementsByClassName('check');
    return checkInputs;
}

/**
 * [修改input值,并自动修改所在行的css，突显选中效果]
 * @param  {[type]} tr [description]
 * @return {[type]}    [description]
 */
function changeInputChecked(trs) {
    for (var i = 0; i < trs.length; i++) {
		var spfid = trs[i].children[0].children[0].attributes.spfid.value;
        if (trs[i].getElementsByTagName('input')[0].checked) {
            trs[i].className = 'on';
        } else {
            trs[i].className = '';
        }
		
    }
}

/**
 * [计算已选择的总件数]
 * @return {[type]} [description]
 */
function getSelectedTotalCount(trs) {
    var selected = 0;
    for (var i = 0; i < trs.length; i++) {
        if (trs[i].getElementsByTagName('input')[0].checked) {
            selected += parseInt(trs[i].getElementsByTagName('input')[1].value);
        }
    }
    return selected;
}

/**
 * [计算已选择的总价]
 * @param  {[type]} trs [description]
 * @return {[type]}     [description]
 */
function getPriceTotalCount(trs) {
    var price = 0;
    for (var i = 0; i < trs.length; i++) {
        if (trs[i].getElementsByTagName('input')[0].checked) {
            price += parseFloat(trs[i].cells[4].innerHTML);
        }
    }
    return price;
}

/**
 * [处理单选和全选条目的checkbox，修改classname以高亮显示]
 * @return {[type]} [description]
 */
function selectItem() {
    var trs = getAllGoodsItems();
    var checkInputs = getAllCheckInputs();
    var checkAllInputs = document.getElementsByClassName('check-all');
    

    //为每行的第一个input元素绑定onclick事件
    for (var i = 0; i < checkInputs.length; i++) {
        checkInputs[i].onclick = function() {
            if (this.className == 'check-all check') { //如果是全选input
                for (var n = 0; n < checkInputs.length; n++) {
                    checkInputs[n].checked = this.checked;
                }
            }
            if (this.checked == false) { //取消全选input
                for (var k = 0; k < checkAllInputs.length; k++) {
                    checkAllInputs[k].checked = false;
                }
            }

			/* 计算选中商品件数 */
			var selectednumber = 0;
			for (var i = 0; i < trs.length; i++) {
				if (trs[i].getElementsByTagName('input')[0].checked) {
					selectednumber ++;
				}
			}
			if (selectednumber == checkInputs.length - 1) {
				checkAllInputs[0].checked = true;
			}
			
            changeInputChecked(trs); //检查inpout的check如果改变，则动态改变所在行的css
            updateInnerHTMLForPriceTotalSelectedTotal(trs); //计算并修改修改页面中的数字
            
        }
    }
}

/**
 * [计算总价并修改页面中的数字]
 */
var selectedCount = 0;
function updateInnerHTMLForPriceTotalSelectedTotal(trs) {
    selectedCount = getSelectedTotalCount(trs);
    var priceCount = getPriceTotalCount(trs);
    var slectedTotal = document.getElementById('slectedTotal');
    var priceTotal = document.getElementById('priceTotal');
    priceTotal.innerHTML = priceCount.toFixed(2);
}

/**
 * [计算小计并显示到网页中]
 */
function getsubTotal(tr) {
    var tds = tr.cells;
    var price = parseFloat(tds[2].innerHTML);
    var count = parseInt(tr.getElementsByTagName('input')[1].value);
    var subTatal = parseFloat(price * count);
    tds[4].innerHTML = subTatal.toFixed(2);
}

/**
 * [处理每个商品行里的按钮事件]
 */
function setItemButtonEvent() {
    var trs = getAllGoodsItems();
    for (var i = 0; i < trs.length; i++) {
		let stock = trs[i].getElementsByClassName('stocknumber');
        trs[i].onclick = function(e) {
            e = e || window.event;
            var el = e.srcElement;
            var cls = el.className;
            var input = this.getElementsByTagName('input')[1];
            var val = parseInt(input.value);
            var reduceButton = this.getElementsByClassName('reduce')[0];
            var addButton = this.getElementsByClassName('add')[0];
			// var spfid = this.children[0].children[0].attributes.spfid.value;
			var cartId = this.children[0].children[0].attributes.cartId.value;
            switch (cls) {
                case 'reduce':
				
					if (val > 1) {
						val -= 1;
					} else {
						
						$('#Floating-frame').text('不能再少了！');
						$('#Floating-frame').css('opacity','0.8');
						$('#Floating-frame').show();
						$('#Floating-frame').stop(true,false).fadeOut(1000);
					}
					input.value = val;
					getsubTotal(this);
					
                    break;
                case 'add':
					if (val < stock[0].innerHTML) {
						val += 1;
						
					} else {
						
						val = stock[0].innerHTML;
						
						$('#Floating-frame').text('不能再多了！');
						$('#Floating-frame').css('opacity','0.8');
						$('#Floating-frame').show();
						$('#Floating-frame').stop(true,false).fadeOut(1000);
					}
					
					input.value = val;
					getsubTotal(this);
					
                    break;
                case 'delete':
                    var conf = confirm("确定要删除吗？");
					
                    if (conf) {
						$.ajax({
							url:"/carts/move/to/cart",
							type: "POST",
							data:{
								uId:$.cookie('userId'),
								cartId:cartId
							},
							success:function(){
								console.log("success");
							}
						})
						
						this.parentNode.removeChild(this);
                    }
                    break;
                default:
                    // statements_def
                    break;
            }
            updateInnerHTMLForPriceTotalSelectedTotal(trs);
        }
    }
}

/**
 * [绑定键盘触发事件，屏蔽非数字输入和无效输入]
 */
function setOnkeyupEvent() {
    var trs = getAllGoodsItems();
    for (var i = 0; i < trs.length; i++) {
		let stock = trs[i].getElementsByClassName('stocknumber');
        trs[i].getElementsByTagName('input')[1].onkeyup = function() {
            var val = parseInt(this.value);
            var tr = this.parentNode.parentNode;
			
			console.log(stock[0].innerHTML);
            var reduce = tr.getElementsByTagName('span')[1];
            if (isNaN(val) || val < 1) {
                val = 1;
            } else if (val > stock[0].innerHTML) {
				val = stock[0].innerHTML;
			}
            this.value = val;
            getsubTotal(tr);
        }
    }
}


/**
 * [处理多选的删除]
 */
function deleteallButtonEvent() {
	var j=0;
	var trs = getAllGoodsItems();
    var deleteAll = document.getElementById('deleteAll');
    deleteAll.onclick = function() {
		
		if (selectedCount != 0) {
			
            var conf = confirm("确定要删除吗？");
            if (conf) {
				
				var checkid=new Array();
				
                for (var i = 0; i < trs.length; i++) {
                    var input = trs[i].getElementsByTagName('input')[0];
					var cartId = parseInt(input.attributes.cartId.value);
					
                    if (input.checked) {
						
						checkid[j++] = cartId;
						
                        trs[i].parentNode.removeChild(trs[i]);
						
                        i--;
                    }
                }
				
				console.log(checkid);
								
								
				$.ajax({
					
					url:`/carts/move/to/cart?uId=${$.cookie('userId')}&cartId=`+ checkid,
					type: "POST",
					datatype:"json",
					success:function(){
						console.log("success");
					}
				})
            }
			updateInnerHTMLForPriceTotalSelectedTotal(trs);
		}
    }
}

 //依次添加到onload事件中
addLoadEvent(selectItem);
addLoadEvent(setItemButtonEvent);
addLoadEvent(setOnkeyupEvent);
addLoadEvent(deleteallButtonEvent);


/* FloatBar浮动条浮动效果 */
window.onscroll = function(){
	var top = document.getElementById('FloatBar');
	var instead = document.getElementById('FBinstead');
	if(document.documentElement.scrollTop >= document.body.clientHeight - document.documentElement.clientHeight - 480){
		
		top.className = "container";
		instead.style.display = "none";
	}else{
		
		top.className = "container navbar navbar-default navbar-fixed-bottom";
		instead.style.display = "block";
	}
}

/* 结算跳转 */
$(function(){
	
	$('#settlementbtn').on('click',function(){
		var reloadCartAmount = [];
		if (selectedCount != 0) {

			var trs = getAllGoodsItems();

			for (var i = 0; i < trs.length; i++) {
				var input = trs[i].getElementsByTagName('input')[0];
				var inputAmount = trs[i].getElementsByTagName('input')[1];
				if (input.checked) {
					reloadCartAmount.push({cartId:input.attributes.cartid.value ,amount:inputAmount.value });
				}
			}
			$.ajax({
				url:"/carts/reload/cart",
				type: "GET",
				datatype:"json",
				data:{
					uId:$.cookie('userId'),
					reloadCartAmount:JSON.stringify(reloadCartAmount)
				},
				beforeSend:function(){
					$("#loading").show();
				},
				success:function(){
					console.log("success");
					window.open('trade','_self');
				},
				error: function(data){
					console.log(data);
				},
				complete:function(){
					$("#loading").hide();
				}
			})
		}else{
			$('#prompt-box').show();
		}
		
	}).on('mouseenter',function(){
		
		if (selectedCount == 0) {
			this.style.cursor = 'not-allowed';
		}else{
			this.style.cursor = 'pointer';
		}
		
	}).on('mouseleave',function(){
		$('#prompt-box').hide();
	});

})
