/* 第一种数字模式 */
function sub()
{       

var a = buy.ans.value;
	//alert(a);
	a = parseInt(a);            
a--;            
	if(a<0)
	 a = 0;
	buy.ans.value = a;
}
  function add()
{
var a = buy.ans.value;
	a = parseInt(a);
a++;
	buy.ans.value = a;
}

/* 第二种数字模式 */
window.onload = function(){
	var num_jia = document.getElementById("num-jia");
	var num_jian = document.getElementById("num-jian");
	var input_num = document.getElementById("input-num");
	

	
	
	num_jia.onclick = function() {
	
		input_num.value = parseInt(input_num.value) + 1;
		
		
	}
	
	num_jian.onclick = function() {
	
		if(input_num.value <= 1) {
			input_num.value = 1;
		} else {
	
			input_num.value = parseInt(input_num.value) - 1;
			
		}
	
	}
	
	/* 如果在和后台做数据交互时，出现点击加减按钮的值无法传到后台的情况，可以用下面这种方式
	$("body").on("click", ".num-jian", function(m) {
		var obj = $(this).closest("ul").find(".input-num");
		if(obj.val() <= 0) {
		     obj.val(0);
		} else {
		     obj.val(parseInt(obj.val()) - 1);
		}
		obj.change();
	 });
	
	$("body").on("click", ".num-jia", function(m) {
		var obj = $(this).closest("ul").find(".input-num");
		obj.val(parseInt(obj.val()) + 1);
		obj.change();
	});*/
}
