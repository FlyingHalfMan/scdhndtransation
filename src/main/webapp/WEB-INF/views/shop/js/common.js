	function menu_style() {
		var id = document.getElementById("product_class");
		var menu_id = document.getElementById("menu_ul");
		if (menu_id.className == 'menu_ul') {
			menu_id.className = 'hide';
		} else {
			menu_id.className = 'menu_ul';
		}
	}
	
/*	function menu_style2() {
		var id = document.getElementById("product_class");
		var menu_id = document.getElementById("menu_ul");
		document.getElementById("menu").className = 'menu';
		
		if (menu_id.className == 'menu_ul') {
			menu_id.className = 'hide';
			document.getElementById("menu").className = 'menu2';	
			document.getElementById("icon1").style.backgroundPosition = '0px 2px';	
		} else {
			menu_id.className = 'menu_ul';
			document.getElementById("menu").className = 'menu';
			document.getElementById("icon1").style.backgroundPosition = '0px -10px';
		}
	}*/
	function menu_style2(i) {		//产品菜单鼠标经过效果
		var id = document.getElementById("product_class");
		var menu_id = document.getElementById("menu_ul");
		document.getElementById("menu").className = 'menu';	
		if (i == 1) {
			menu_id.className = 'hide';
			document.getElementById("menu").className = 'menu2';	
			document.getElementById("icon1").style.backgroundPosition = '0px 2px';	
		} else if (i == 2) {
			menu_id.className = 'menu_ul';
			document.getElementById("menu").className = 'menu';
			document.getElementById("icon1").style.backgroundPosition = '0px -10px';
		}
	}
	
	function g(o){return document.getElementById(o);}
	function HoverLi(n,m,q,p){
		for(var i=1;i<=m;i++)
		{
			g(q+i).className='normaltab';
			g(p+i).className='hide';
		}
		g(p+n).className='nohide';
		g(q+n).className='hot';
	}	
	
	function margin_top_height(i) {
		if (i == 1){//判断是为主页
			document.getElementById("con_left").style.height = document.getElementById("menu_ul").offsetHeight-8+"px";
		}		
		var wrapwidth = document.body.clientWidth;						//获取浏览器宽度
		document.getElementById("wrap").style.width = wrapwidth+'px';	//设置浏览器宽度
		if (wrapwidth >= 940) {											//根据浏览器宽度设置我的账户、产品菜单定位
			document.getElementById("menu_ul").style.position = 'absolute';		
			document.getElementById("my_account").style.position = 'absolute';
			document.getElementById("menu_ul").style.left = (wrapwidth-940)/2+"px";		
			document.getElementById("my_account").style.left = (wrapwidth-940)/2+667+"px";
		} else {
			document.getElementById("menu_ul").style.position = 'absolute';
			document.getElementById("my_account").style.position = 'absolute';
			document.getElementById("my_account").style.left = 0+"px";
			document.getElementById("my_account").style.left = 667+"px";
		} 
		//document.getElementById("my_account").style.left = '869'+'px';
	}
	function view_account() {
		document.getElementById("my_account").style.display = 'block' ;	
		document.getElementById("my_icon").style.display = 'none' ;		
	}
	function hide_account() {
		document.getElementById("my_account").style.display = 'none' ;	
		document.getElementById("my_icon").style.display = 'block' ;		
	}	
	function view_menu(i) {
		document.getElementById("foucs"+i).className = 'foucs';
	}
	function hide_menu(i) {
		document.getElementById("foucs"+i).className = '';
	}
	
	function product_style(i){
		if (i == 1) {
			document.getElementById("pro_box").className = 'box_3';
			document.getElementById("pic1").src = 'images/product_btn1.jpg';
			document.getElementById("pic2").src = 'images/product_btn2.jpg';	
		} else {
			document.getElementById("pro_box").className = 'box_3_list';
			document.getElementById("pic2").src = 'images/product_btn3.jpg';
			document.getElementById("pic1").src = 'images/product_btn4.jpg';	
		}	
	}
	
	var rollText_k=4; //菜单总数
	var rollText_i=1; //菜单默认值
	//rollText_tt=setInterval("rollText(1)",8000);	//按指定的时间来运行函数
	function rollText(a){
		clearInterval(rollText_tt);
		rollText_tt=setInterval("rollText(1)",8000);
		rollText_i+=a;
		if (rollText_i>rollText_k){
			rollText_i=1;
		}
		if (rollText_i==0){
			rollText_i=rollText_k;
		}
		//alert(i)
		for (var j=1; j<=rollText_k; j++){
			document.getElementById("rollTextMenu"+j).style.display="none";
		}
		document.getElementById("rollTextMenu"+rollText_i).style.display="block";
		//document.getElementById("pageShow").innerHTML = rollText_i+"/"+rollText_k;
	} 
	