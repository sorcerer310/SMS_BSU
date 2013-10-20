document.write("<script type=\"text/javascript\" src=\"js/jDownload/jquery.jdownload.min.js\"></script>");


//要初始化执行的一些东西
//取消ajax的缓存
$.ajaxSetup ({cache:false});

//获得url参数
function getUrlParam (name)
{
	var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
	var r = window.location.search.substr(1).match(reg);
	if (r!=null) 
		return unescape(r[2]);
	return null;
}

//扩展EasyUIDataGrid功能
function extendEasyUIDataGrid(){
	//解决combobox多选更新值问题
	$.extend($.fn.datagrid.defaults.editors.combobox, {
        setValue : function(jq, value) {
            var opts = $(jq).combobox('options');
            if(opts.multiple&&value.indexOf(opts.separator)!=-1){//多选且不只一个值
                var values = value.split(opts.separator);
                $(jq).combobox("setValues", values);
            }
            else{
            	$(jq).combobox("setValue", value);
            }
        }
	});
	//解决combobox多选新建值问题
	$.extend($.fn.datagrid.defaults.editors.combobox, {
		getValue : function(jq) {
			var opts = $(jq).combobox('options');
			if(opts.multiple){
				var values = $(jq).combobox('getValues');
				if(values.length>0){
					if(values[0]==''||values[0]==' '){
						return values.join(',').substring(1);//新增的时候会把空白当成一个值了，去掉
					}
				}
				return values.join(',');
			}
			else
				return $(jq).combobox("getValue");
		},
		setValue : function(jq, value) {
			var opts = $(jq).combobox('options');
			if(opts.multiple&&value.indexOf(opts.separator)!=-1){//多选且不只一个值
				var values = value.split(opts.separator);
				$(jq).combobox("setValues", values);
			}
			else
				$(jq).combobox("setValue", value);
		}
	});
	
	//扩展datagrid密码控件
	$.extend($.fn.datagrid.defaults.editors, {   
	     password: {   
	         init: function(container, options){   
	             var input = $('<input type="password" class="datagrid-editable-input">').appendTo(container);
	             //return input.formatter = function(value){return "******";}
	             return input;
	         },   
	         getValue: function(target){   
	             return $(target).val();
	         },   
	         setValue: function(target, value){   
	             $(target).val(value);   
	         },   
	         resize: function(target, width){   
	             var input = $(target);   
	             if ($.boxModel == true){   
	                 input.width(width - (input.outerWidth() - input.width()));   
	             } else {   
	                 input.width(width);   
	             }   
	         }
	     }   
   	 });
	
	//扩展datagrid日期控件
//	$.extend($.fn.datagrid.defaults.editors, {
//	     datebox: {//datetimebox就是你要自定义editor的名称
//	         init: function(container, options){
//	             var input = $('<input class="easyuidatebox">').appendTo(container);
//	             return input.datetimebox({
//	                 formatter:function(date){
//	                     return new Date(date).format("yyyy-MM-dd");
//	                 }
//	             });
//	         },
//	         getValue: function(target){
//	             return $(target).parent().find('input.combo-value').val();
//	        },
//	         setValue: function(target, value){
//	             $(target).datebox("setValue",value);
//	         },
//	         resize: function(target, width){
//	            var input = $(target);
//	            if ($.boxModel == true){
//	                 input.width(width - (input.outerWidth() - input.width()));
//	            } else {
//	                 input.width(width);
//	             }
//	         }
//	     }
//	});
	//设置日期格式
	 $.fn.datebox.defaults.formatter = function(v){
		   if (v instanceof Date) {
		    var y = v.getFullYear();
		    var m = v.getMonth() + 1;if(m<10){m="0"+m;}
		    var d = v.getDate();if(d<10){d="0"+d;}
		    var h = v.getHours();if(h<10){h="0"+h;}
		    var i = v.getMinutes();if(i<10){i="0"+i;}
		    var s = v.getSeconds();
		    var ms = v.getMilliseconds();
		    if (ms > 0)
		     return y + '-' + m + '-' + d + ' ' + h + ':' + i + ':' + s
		       + '.' + ms;
		    if (h > 0 || i > 0 || s > 0)
		     return y + '-' + m + '-' + d + ' ' + h + ':' + i + ':' + s;
		    return y + '-' + m + '-' + d;
		   }
		   return '';
	  }; 
	  

	  
}

//处理服务端返回的错误信息
function errmsg (data){
	 var failmsg = data._msg;
	 if(data._extramsg!=null){
		 failmsg += ":";
		 failmsg += data._extramsg;
	 }
	 $.messager.alert('登陆失败',failmsg);   
}

//带入url根据查询的数据返回csv
function bsuExportCsv(url){
	//如果页面中没有用于下载iframe，增加iframe到页面中
	if($('#downloadcsv').length<=0)
		$('body').append("<iframe id=\"downloadcsv\" style=\"display:none\"></iframe>");
	$('#downloadcsv').attr('src',url);
}


//检测div中所有条件内容
function makeUrl(url,csv){
	var jsonstr = "{";
	//此处专门处理在table中的条件
	$("#tb_table").find("tr").each(function(){
 		$(this).find("td").children().each(function(){
			if($(this)[0].tagName=='INPUT'){
				
				//如果为combobox,并且值不为空
				if(new RegExp(/easyui-combobox/).test($(this).attr('class')) && $(this).combobox('getValue')!='') {
					jsonstr+="\"" + $(this).attr('id') +"\":\"";
					jsonstr += $(this).combobox('getValue');
					jsonstr += "\",";
				}
				//如果为validatebox并且值不为空
				else if(new RegExp(/easyui-validatebox/).test($(this).attr('class')) && $(this).val()!=''){
					jsonstr+="\"" + $(this).attr('id') +"\":\"";
					jsonstr += $(this).val();
					jsonstr += "\",";
				}
				//如果为datebox并且值不为空
				else if(new RegExp(/easyui-datebox/).test($(this).attr('class')) && $(this).datebox('getValue')!=''){
					jsonstr+="\"" + $(this).attr('id') +"\":\"";
					jsonstr += $(this).datebox('getValue');
					jsonstr += "\",";
				}
			}
 		});
 	}); 
 	//此处用于处理div中的条件
//	$('#tb').children().each(function(){
		//检索所有标签名为INPUT的标签
//		if($(this)[0].tagName=='INPUT'){
//			//如果为combobox,并且值不为空
//			if(new RegExp(/easyui-combobox/).test($(this).attr('class')) && $(this).combobox('getValue')!='') {
//				jsonstr+="\"" + $(this).attr('id') +"\":\"";
//				jsonstr += $(this).combobox('getValue');
//				jsonstr += "\",";
//			}
//			//如果为validatebox并且值不为空
//			else if(new RegExp(/easyui-validatebox/).test($(this).attr('class')) && $(this).val()!=''){
//				jsonstr+="\"" + $(this).attr('id') +"\":\"";
//				jsonstr += $(this).val();
//				jsonstr += "\",";
//			}
//			//如果为datebox并且值不为空
//			else if(new RegExp(/easyui-datebox/).test($(this).attr('class')) && $(this).datebox('getValue')!=''){
//				jsonstr+="\"" + $(this).attr('id') +"\":\"";
//				jsonstr += $(this).datebox('getValue');
//				jsonstr += "\",";
//			}
//		}
//	});
	jsonstr = jsonstr.substring(0,jsonstr.length-1);
	jsonstr+="}";
	var returl;
	if(jsonstr=="}"){
		var urlp = {};
		returl = url+"?"+$.param(urlp);
	}
	else	{
		returl = url+"?"+$.param(eval("("+jsonstr+")"));
	}
	if(csv==true)
		returl += "&csv=true";
	return returl;
}

/*function getUserUnitid(){
	 $.get("general_update?" + $.param(urlp),
         function (data, status) {
             var json = eval("(" + data + ")");
             if (json._no != "3021")
                 $.message.alert("更新失败", json._msg);
             getData();
         });
}
*/
/**
 * 新建一条数据
 * @param {} urlp 带入要新建数据的参数,其中包括unitid
 */
function insertData(urlp,func){
	$.get("tool_get_unitid",function(data,status){
		var tjson = eval("("+data+")");
		if(tjson._no=="3021"){
			var unitid = tjson._extramsg;
			urlp.unitid = unitid;
		     $.get("general_update?" + $.param(urlp),
		           function (data, status) {
		               var json = eval("(" + data + ")");
		               if (json.id == undefined)
		                   $.messager.alert("保存失败", json._msg);
		               func.call();
		               //getData();
		      });
		 }
    });
}

/**
 * 向服务器更新数据
 * @param {} urlp	向servlet传送的url参数
 * @param {} func	执行完成后要执行的操作，一般用于刷新数据
 */
function updateData(urlp,func){
	 //数据检查
	 $.get("general_update_check?"+$.param(urlp),
		function(data,status){
		 var cjson = eval("(" + data + ")");
		 if(cjson._no =="3020"){
        	 //更新数据
             $.get("general_update?" + $.param(urlp),
                     function (data, status) {
                         var json = eval("(" + data + ")");
                         if (json._no != "3003")
                             $.message.alert("更新失败", json._msg);
                         func.call();
                     });
		 }
		 else if(cjson._no=="1020"){
			 $.messager.alert("操作失败","你没有操作该条数据的权限");
			 func.call();
		 }
	 });
}
/**
 * 删除数据
 * @param {} urlp	要删除带入的参数
 * @param {} func	删除数据后执行的操作,一般用来刷新数据
 */
function deleteData(urlp,func){
	//数据检查
	$.get("general_update_check?"+$.param(urlp),
		function(data,status){
			var cjson = eval("(" + data + ")");
		 	if(cjson._no =="3020"){
		 		//删除数据
				$.get("general_update?" + $.param(urlp),
			       function (data, status) {
			           var json = eval("(" + data + ")");
			           if (json._no != "3004")
			               $.message.alert("删除失败", json._msg);
			           func.call();
			    });
		 	}else if(cjson._no=="1020"){
		 		$.messager.alert("操作失败","你没有操作该条数据的权限");
			 	func.call();
		 	}
	});
}

