var _GLOBAL_SUCCESS = '00';

function _findDict(groupDictId, dictValue) {
	var groupList = dictJson[groupDictId];
	if(groupList) {
		for (var i = 0; i < groupList.length; i++) {
			var dv = groupList[i].dictValue;
			if (dictValue == dv)
				return groupList[i].dictName;
		}
	}
	return '';
}

function findCityList(provinceCode) {
	for (var i = 0; i < pcdJson.length; i++) {
		var pCode = pcdJson[i].provinceCode;
		if (pCode == provinceCode) {
			return pcdJson[i].city;
		}
	}
	return null;
}

function findDistrict(provinceCode, cityCode) {
	var cityList = findCityList(provinceCode);
	if(cityList) {
  	for (var i = 0; i < cityList.length; i++) {
  		var cCode = cityList[i].cityCode;
  		if (cityCode == cCode) {
  			return cityList[i].district;
  		}
  	}
	}
	return null;
}

function _findProvinceName(provinceCode) {
  for (var i = 0; i < pcdJson.length; i++) {
    var pCode = pcdJson[i].provinceCode;
    if (pCode == provinceCode) {
      return pcdJson[i].provinceName;
    }
  }
  return '';
}

function _findCityName(provinceCode, cityCode) {
  var cityList = findCityList(provinceCode);
  if(cityList) {
    for (var i = 0; i < cityList.length; i++) {
      var cCode = cityList[i].cityCode;
      if (cCode == cityCode) {
        return cityList[i].cityName;
      }
    }
  }
  return '';
}

function _findDistrictName(provinceCode, cityCode, districtCode) {
  var districtList = findDistrict(provinceCode, cityCode);
  if(districtList) {
    for (var i = 0; i < districtList.length; i++) {
      var dCode = districtList[i].districtCode;
      if (dCode == districtCode) {
        return districtList[i].districtName;
      }
    }
  }
  return '';
}

var _my_datatables_language = {
   "processing": "<div class='myDatatablesLodding'><img src='/images/loading02.gif' alt=''></div>",
   // "processing": '<span> </span>',
	"emptyTable" : "<div style='text-align:center;font:bold 13px/22px arial,sans-serif;color:red;'>没有检索到数据！</div>",
	"zeroRecords" : "<div style='text-align:center;font:bold 13px/22px arial,sans-serif;color:red;'>没有检索到数据！</div>",
	"info" : "显示 _START_ 到 _END_ 条记录，总共 _TOTAL_ 条",
	"infoEmpty" : "显示 0 到 0 条记录，总共 0 条",	
	"lengthMenu" : "每页显示<input class='showMenuNb' type='number' value='10'>条",
	"paginate" : {
		"first" : "首页",
		"last" : "尾页",
		"previous" : "上一页",
		"next" : "下一页"
	}
};

$(function() {
	// 设置AJAX的全局默认选项
	$.ajaxSetup({
	    beforeSend : function(){
	        // layer.load(2, {time: 10*1000});
	    },
		error : function(jqXHR, textStatus, error) { // 出错时默认的处理函数
			var text = jqXHR ? jqXHR.responseText : undefined;
			var status = jqXHR.status;
			if('404' == status) {
				layer.msg("访问的资源不存在！",{icon:2,time:5000});
			} else if('0' == status){
				
			} else {
				var errorObj = $(text);
				var errMsg = errorObj.find("#errorMsg");
			    layer.msg(errMsg.text(),{icon:2,time:5000});
			}
		},	
		complete : function(xhr, status) {
		    // layer.closeAll('loading');
			var text = xhr ? xhr.responseText : undefined;
			if(text) {
				if(text.indexOf("{") == 0 || text.indexOf("[") == 0) {
					var data = eval('(' + text + ')');
					if('00' != data.code) {
						if('E000034' == data.code || 'E000035' == data.code) {
							layer.msg(data.msg,{icon:2,time:3000},function(){
								window.top.location.href = '/toLogin.do';
							});
						} else {
							layer.msg(data.msg,{icon:2,time:5000});
						}
						return false;
					}
				}
			}
		}
	});
	
// 声明数组移除方法
	Array.prototype.removeByValue = function(val) {
		  for(var i=0; i<this.length; i++) {
		    if(this[i] == val) {
		      this.splice(i, 1);
		      break;
		    }
		  }
		};
    // 声明数组会否存在当前元素方法
	Array.prototype.contantValue = function(val) {
		  for(var i=0; i<this.length; i++) {
		    if(this[i] == val) {
		      return true;
		    }
		  }
		    return false;
		};
    
	Date.prototype.format =function(format) {
  	var o = {
    	"M+" : this.getMonth()+1, // month
    	"d+" : this.getDate(), // day
    	"h+" : this.getHours(), // hour
    	"m+" : this.getMinutes(), // minute
    	"s+" : this.getSeconds(), // second
    	"q+" : Math.floor((this.getMonth()+3)/3), // quarter
    	"S" : this.getMilliseconds() // millisecond
  	};
  	
  	if(/(y+)/.test(format)) {
  			format=format.replace(RegExp.$1,(this.getFullYear()+"").substr(4- RegExp.$1.length));
  	}
  	
  	for(var k in o) {
  			if(new RegExp("("+ k +")").test(format)) {
  					format = format.replace(RegExp.$1, RegExp.$1.length==1? o[k] : ("00"+ o[k]).substr((""+ o[k]).length));
  			}
  	}
  	return format;
	};

	// 判断环境切换loading图片
    // if(window.location.href.indexOf('bms')!=-1){
     //   _my_datatables_language.processing="<div class='myDatatablesLodding'><img src='/bms/images/loading02.gif' alt=''></div>"
    // }
});
/**
 * 解析第二代身份证
 */
function _resolve_card2(idCardNo) {
	var result = {
			provinceCode : "",
			cityCode : "",
			districtCode : "",
			sex : "",
			birthday:"",
			age:"0"
	};
	
	result.provinceCode = idCardNo.substring(0, 2) + "0000";
	result.cityCode = idCardNo.substring(0, 4) + "00";
	result.districtCode = idCardNo.substring(0, 6);
	
	var bd = idCardNo.substring(6, 14);
	var newBd = bd.substring(0, 4) + "-" + bd.substring(4, 6) + "-" + bd.substring(6, 8);
	result.birthday = newBd;
	
	var sexNum = idCardNo.substring(16, 17);
	result.sex = sexNum % 2 == 0 ? "2" : "1";
	
	 // 获取年龄
    var myDate = new Date();
    var month = myDate.getMonth() + 1;
    var day = myDate.getDate();
    var age = myDate.getFullYear() - idCardNo.substring(6, 10) - 1;
    if (idCardNo.substring(10, 12) < month || idCardNo.substring(10, 12) == month && idCardNo.substring(12, 14) <= day) {
        age++;
    }
    result.age = age;
	return result;
}

/**
 * 初始化下拉选择框
 * 
 * @param selectId
 *            下拉框ID
 * @param dictArray
 *            下拉字典数据
 * @param selectedValue
 *            选中值
 * @returns
 */
function _init_select(config, dictArray, selectedValue,placeholder,mult) {
	var isMult = null == mult?false:mult;
	var selectId;
	var ext1;
	if(typeof(config) == "string") {
		selectId = config;
	} else if(typeof(config) == "object") {
		selectId = config.selectId;
		ext1 = config.ext1;
	}
	
	$("#" + selectId).empty();
	$("#" + selectId).append("<option value=''>请选择</option>");
	if(dictArray) {
		for (var i = 0; i < dictArray.length; i++) {
			var dict = dictArray[i];
			if(ext1) {
	  			if(ext1 != dict.ext1) {
	  				continue;
	  			}
	  			
				if(selectedValue == dict.dictValue) {
					$("#" + selectId).append("<option value='" + dict.dictValue + "' index=" + i + " selected='selected'" + " ext1='" + ext1 + "'>" + dict.dictName + "</option>");
				} else {
					$("#" + selectId).append("<option value='" + dict.dictValue + "' index=" + i + " ext1='" + ext1 + "'>" + dict.dictName + "</option>");
				}
	  		} else {
	  			if(selectedValue == dict.dictValue) {
					$("#" + selectId).append("<option value='" + dict.dictValue + "' index=" + i + " selected='selected' >" + dict.dictName + "</option>");
				} else {
					$("#" + selectId).append("<option value='" + dict.dictValue + "' index=" + i + ">" + dict.dictName + "</option>");
				}
	  		}
		}
	}
	$("#" + selectId).select2({
		placeholder : null==placeholder?"--请选择--":placeholder,
		allowClear : true,
		multiple: isMult
	});
}

/**
 * 初始化复选框
 * 
 * @param cbBoxId
 *            复选框容器ID
 * @param cbName
 *            复选框name(对应字典组名称)
 * @param dictArray
 *            字典数组(对应的字典数据)
 * @param checkedValue
 *            选中值
 * @returns
 */
function _init_radio_box(cbBoxId, cbName, dictArray, checkedValue) {
		if(dictArray) {
    	for(var i=0; i<dictArray.length; i++) {
    		var dict = dictArray[i]; 
    		
    		var dom = "<div class='radio-box'>";
    		dom += "<input type='radio' id='" + cbBoxId + "-" + i 
    		+ "' name='" + cbName 
    		+ "' value='" + dict.dictValue + "'"; 
    	/*
		 * + "' ext_1='" + dict.ext1 + "' ext_2='" + dict.ext2 + "'";
		 */
    		if(checkedValue == dict.dictValue) {
    		 dom += " checked='checked'";
    		}
    		dom += ">";
    		
    		dom += "<label for='" + cbBoxId + "-"  + i + "'>" + dict.dictName + "</label>"
    		dom += "</div>";
    		$("#" + cbBoxId).append(dom);
    	}
		}
	
	$('.radio-box input').iCheck({
		checkboxClass: 'icheckbox-blue',
		radioClass: 'iradio-blue',
		increaseArea: '20%'
	});
}

/**
 * 初始化复选框
 * 
 * @param cbBoxId
 *            复选框容器ID
 * @param cbName
 *            复选框name(对应字典组名称)
 * @param dictArray
 *            字典数组(对应的字典数据)
 * @param checkedValue
 *            选中值
 * @returns
 */
function _init_checkbox(cbBoxId, cbName, dictArray, checkedValue) {
		if(dictArray) {
    	for(var i=0; i<dictArray.length; i++) {
    		var dict = dictArray[i]; 
    		
    		var dom = "<div class='check-box'>";
    		dom += "<input type='checkbox' id='" + cbBoxId + "-" + i 
    		+ "' name='" + cbName 
    		+ "' value='" + dict.dictValue 
    		+ "' ext_1='" + dict.ext1 
    		+ "' ext_2='" + dict.ext2 + "'";
    		if(null != checkedValue){
    			for (var y = 0; y < checkedValue.length; y++) {
    				if(checkedValue[y] == dict.dictValue) {
    					dom += "checked='checked'";
    					break;
    				}
    			}
    		}
    		dom += ">";
    		
    		dom += "<label for='" + cbBoxId + "-"  + i + "'>" + dict.dictName + "</label>"
    		dom += "</div>";
    		$("#" + cbBoxId).append(dom);
    	}
		}
	//
	// $('.check-box input').iCheck({
	// 	checkboxClass: 'icheckbox-blue',
	// 	radioClass: 'iradio-blue',
	// 	increaseArea: '20%'
	// });
}

/**
 * 初始化地区选择框
 * 
 * @param pSelectId
 *            省select元素ID
 * @param cSelectId
 *            市select元素ID
 * @param dSelectId
 *            县/区select元素ID
 * @param psv
 *            省选中值
 * @param csv
 *            市选中值
 * @param dsv
 *            县/区选中值
 * @returns 邮编
 */
function _init_area_select(pSelectId, cSelectId, dSelectId, psv, csv, dsv,onlyGD) {
	$("#" + pSelectId).empty();
	$("#" + pSelectId).append("<option value=''>请选择</option>");
	var cities = null;
	for (var i = 0; i < pcdJson.length; i++) {
		var pData = pcdJson[i];

	  if (psv == pData.provinceCode) {
			$("#" + pSelectId).find("option:selected").removeAttr("selected");
			$("#" + pSelectId).append("<option value='" + pData.provinceCode + "' index=" + i + " selected='selected'>" + pData.provinceName + "</option>");
			cities = pData.city;
		} else if(onlyGD=='1'){
			$("#" + pSelectId).append("<option value='440000' index=" + i + ">广东省</option>");
			break;
		}else {
          $("#" + pSelectId).append("<option value='" + pData.provinceCode + "' index=" + i + ">" + pData.provinceName + "</option>");
	  }
	}

	var zipCode = _init_city_select(cities, cSelectId, dSelectId, csv, dsv);

	$("#" + pSelectId).change(function() {
		var selected = $(this).val();
		_init_city_select(findCityList(selected), cSelectId, dSelectId);
	});

	$("#" + cSelectId).change(function() {
		var pCode = $("#" + pSelectId).val();
		var selected = $(this).val();
		_init_district_select(findDistrict(pCode, selected), dSelectId);
	});

	$("#" + pSelectId).select2({
		placeholder : "--请选择--",
		allowClear : true
	});
	$("#" + cSelectId).select2({
		placeholder : "--请选择--",
		allowClear : true
	});
	$("#" + dSelectId).select2({
		placeholder : "--请选择--",
		allowClear : true
	});

	return zipCode;
}

/**
 * 初始化地区选择框
 * 
 * @param cityData
 *            市数据数组
 * @param cSelectId
 *            市select元素ID
 * @param dSelectId
 *            县/区select元素ID
 * @param csv
 *            市选中值
 * @param dsv
 *            县/区选中值
 * @returns 邮编
 */
function _init_city_select(cityData, cSelectId, dSelectId, csv, dsv) {
	$("#" + cSelectId).empty();
	$("#" + cSelectId).append("<option value=''>请选择</option>");
	if(!cityData) {
		return '';
	}
	var districts = null;

	for (var i = 0; i < cityData.length; i++) {
		var cData = cityData[i];

		if (csv == cData.cityCode) {
			$("#" + cSelectId).find("option:selected").removeAttr("selected");
			$("#" + cSelectId).append("<option value='" + cData.cityCode + "' index=" + i + " selected='selected'>" + cData.cityName + "</option>");
			districts = cData.district;
		} else {
			$("#" + cSelectId).append("<option value='" + cData.cityCode + "' index=" + i + ">" + cData.cityName + "</option>");
		}
	}

	return _init_district_select(districts, dSelectId, dsv)
}

/**
 * 初始化地区选择框
 * 
 * @param districtData
 *            县/区数据数组
 * @param dSelectId
 *            县/区select元素ID
 * @param dsv
 *            县/区选中值
 * @returns 邮编
 */
function _init_district_select(districtData, dSelectId, dsv) {
	$("#" + dSelectId).empty();
	$("#" + dSelectId).append("<option value=''>请选择</option>");
	
	if(!districtData) {
		return '';
	}
	
	var zipCode = '';
	for (var i = 0; i < districtData.length; i++) {
		var dData = districtData[i];

		if (dsv == dData.districtCode) {
			$("#" + dSelectId).find("option:selected").removeAttr("selected");
			$("#" + dSelectId).append(
					"<option value='" + dData.districtCode + "' index=" + i + " selected='selected' zipCode='" + dData.zipCode + "'>" + dData.districtName + "</option>");
			zipCode = dData.zipCode;
		} else {
			$("#" + dSelectId).append("<option value='" + dData.districtCode + "' index=" + i + " zipCode='" + dData.zipCode + "'>" + dData.districtName + "</option>");
		}
	}

	return zipCode;
}

/**
 * Ajax 下拉刷新
 * 
 * @param config {
 * @param selectId
 *            select标签ID
 * @param searchUrl
 *            数据Url
 * @param sData
 *            查询数据对象
 * @param showText
 *            selectName
 * @param showId
 *            selectValue }
 * @returns
 */
function _simple_ajax_select(config){
	var isMult = null == config.mult?false:config.mult;
	$("#" + config.selectId).empty();
	var _pageSize = 7;
	
	var checkedInfo = config.checkedInfo;

	$("#"+config.selectId).select2({
		ajax : {
			url : config.searchUrl,
			dataType : 'json',
			delay : 250,
			data : function(params) {
				var dd = {
						sName : params.term,
						page : params.page || 1,// 第几页
						rows : _pageSize
				};
        for(var k in config.sData){
           dd[k] = config.sData[k];
        }
				return dd;
			},
			processResults : function(rData, params) {
				if(_GLOBAL_SUCCESS != rData.code) {
					if('E000034' == rData.code || 'E000035' == rData.code) {
						layer.msg(rData.msg,{icon:2,time:5000},function(){
							window.top.location.href = '/toLogin.do';
						});
					} else {
						layer.msg(rData.msg,{icon:2,time:5000});
					}
					return null;
				}
				
				var data = rData.body;
				params.page = params.page || 1;
				return {
					results : $.map(data.data, function(item){
						return {
							text: config.showText(item),
							id: config.showId(item)
						};
					}),// 后台返回的数据集
					pagination : {
						more : (params.page * _pageSize) < data.recordsTotal
					// 总页数为10，那么1-9页的时候都可以下拉刷新
					}
				};
			},
			cache : true
		},
		escapeMarkup : function(markup) {
			return markup;
		},
		allowClear : true,
		placeholder : config.placeholder,
		multiple: isMult,
		minimumInputLength : 0,
		width : config.width
	});

    if(checkedInfo && checkedInfo.value && checkedInfo.name) {
        $("#" + config.selectId).append("<option value='" + checkedInfo.value + "' selected='selected'>" + checkedInfo.name + "</option>");
    }else {
        // $("#" + config.selectId).append(new Option("", "", false, true));
        $("#" + config.selectId).append("<option value=''>请选择</option>");
    }
}


/**
 * 重置当前页面_web_token
 * 
 * @param groupId
 *            token groupId
 * @returns
 */
function _resetToken(groupId,url){
	// Ajax调用处理
	$.ajax({
		type : "POST",
		url : url,
		data : {
			groupId : groupId
		},
		success : function(data) {
			if(data.code == _GLOBAL_SUCCESS){
				data = data.body;
				$("#_web_token").val(data);
			}
		}
	});
}

/**
 * 相同字段表单转换json
 */
$(function() {
	 $.fn.serializeJson = function(){
         var jsonData1 = {};
         var reg = /\W/g;
         var serializeArray = this.serializeArray();
         // 先转换成{"id": ["12","14"], "name": ["aaa","bbb"],
					// "pwd":["pwd1","pwd2"]}这种形式
         $(serializeArray).each(function () {
             if (jsonData1[this.name]) {
                 if ($.isArray(jsonData1[this.name])) {
                     jsonData1[this.name].push(this.value);
                 } else {
                     jsonData1[this.name] = [jsonData1[this.name], this.value];
                 }
             } else {
                 jsonData1[this.name] = this.value;
             }
         });
         // 再转成[{"id": "12", "name": "aaa", "pwd":"pwd1"},{"id": "14",
            // "name":
					// "bb", "pwd":"pwd2"}]的形式
         var vCount = 0;
         // 计算json内部的数组最大长度
         for(var item in jsonData1){
             var tmp = $.isArray(jsonData1[item]) ? jsonData1[item].length : 1;
             vCount = (tmp > vCount) ? tmp : vCount;
         }
         if(vCount > 1) {
             var jsonData2 = new Array();
             for(var i = 0; i < vCount; i++){
                 var jsonObj = {};
                 for(var item in jsonData1) {
                	    if(Array.isArray(jsonData1[item]))
                         jsonObj[item] = jsonData1[item][i];
                      else
                      	 jsonObj[item] = jsonData1[item];
                 }
                 jsonData2.push(jsonObj);
             }
             return JSON.stringify(jsonData2);
         }else{
             return "[" + JSON.stringify(jsonData1) + "]";
         }
     }
 });


/**
 * 搜索栏展开方法
 * 
 * @returns
 */

	
function _showOther(obj){
	var search,showOther,p;
	var height=66;
	var maxHeight=90;
    if($(".page-container .search .cl:first").find(".cl").length>1){
        height=97;
        maxHeight=121;
    }
	var moreHeight={};
			if(obj){
				search=$(obj).parents('.search');
				showOther=$(obj);
				p=showOther.find('p');
			} else {
					search=$('.search');
					showOther=$('.showOther');
					p=$('.showOther p');
			}
			if(moreHeight.height){
			
			} else {
					moreHeight.height=parseInt(search.find('.cl:first-child').css('height'))+35;
			}
		
			if(moreHeight.height<maxHeight){
	        layer.msg('没有更多的选项了哦', {
                icon : 0,
                time : 3000
            });
	        return;
			}
	    if(!p.find('i').hasClass('xiala')){
	        search.css('height',moreHeight.height);
	        p.find('i').css('transform','rotateX(180deg)').addClass("xiala");
	    }else{
	        search.css('height',height);
	        p.find('i').css('transform','rotateX(0deg)').removeClass("xiala");
	    }
};

function _reset() {
  $(".search input[type='text']").val("");
  $(".search select").val(null).trigger("change");
}
/**
 * 
 * 初始化 校区-部门-部门组联动
 * 
 * @param cSelectId
 *            校区select标签ID
 * @param dSelectId
 *            部门select标签ID
 * @param gSelectId
 *            招生组select标签ID
 * @param cRequestUrl
 *            校区请求URL
 * @param dRequestUrl
 *            部门请求URL
 * @param gRequestUrl
 *            招生组请求URL
 * @param csv
 *            校区选中值
 * @param dsv
 *            部门选中值
 * @param gsv
 *            招生组选中值
 * @returns
 */
function _init_campus_select(cSelectId, dSelectId, gSelectId, cRequestUrl, dRequestUrl, gRequestUrl,csv, dsv, gsv) {
	
	$("#" + cSelectId).empty();
	$("#" + cSelectId).append("<option value=''>请选择</option>");	
    $.ajax({
	     type: "POST",
         url: cRequestUrl,
         dataType : 'json',
         success: function(data){
             if(_GLOBAL_SUCCESS == data.code) {
                 data = data.body;
                 if(data) {
                     for (var i = 0; i < data.length; i++) {
                         var campusData = data[i];
                         if (csv == campusData.campusId) {
                        		 $("#" + cSelectId).find("option:selected").removeAttr("selected");
                        		 $("#" + cSelectId).append("<option value='" + campusData.campusId + "' index=" + i + " selected='selected'>" + campusData.campusName + "</option>");
                        		 _init_dp_select(campusData.campusId,dRequestUrl,gRequestUrl, dSelectId, gSelectId,dsv,gsv);
                         }else{
                        		 $("#" + cSelectId).append("<option value='" + campusData.campusId + "' index=" + i + ">" + campusData.campusName + "</option>");	
                         }
                     }
                 }
             }
         }
    });
   
   
	$("#" + cSelectId).change(function() {
		var selected = $(this).val();
		_init_dp_select(selected,dRequestUrl,gRequestUrl, dSelectId, gSelectId,dsv,gsv);
	});

	$("#" + dSelectId).change(function() {
		var selected = $(this).val();
		_init_group_select(selected,gRequestUrl ,gSelectId);
	});

	$("#" + cSelectId).select2({
		placeholder : "--请选择校区--",
		allowClear : true
	});
	$("#" + dSelectId).select2({
		placeholder : "--请选择部门--",
		allowClear : true
	});
	$("#" + gSelectId).select2({
		placeholder : "--请选择招生组--",
		allowClear : true
	});
}
/**
 * 初始化部门下来信息
 * 
 * @param selected
 *            校区选中的值
 * @param dRequestUrl
 *            部门请求url
 * @param gRequestUrl
 *            招生组请求url
 * @param dSelectId
 *            部门select标签id
 * @param gSelectId
 *            组select标签id
 * @returns
 */
function _init_dp_select(selected,dRequestUrl,gRequestUrl, dSelectId, gSelectId,dsv,gsv) {
	$("#" + dSelectId).empty();
	$("#" + dSelectId).append("<option value=''>请选择</option>");
    $.ajax({
         url: dRequestUrl,
         dataType : 'json',
         data : {
    				"campusId" : function(){
    					return selected;
    				}
    		 },
         success: function(data){
             if(_GLOBAL_SUCCESS == data.code) {
                 data = data.body;
                 if(data) {
                     for (var i = 0; i < data.length; i++) {
                         var dpData = data[i];
                         if (dsv == dpData.dpId) {
                             $("#" + dSelectId).find("option:selected").removeAttr("selected");
                             $("#" + dSelectId).append("<option value='" + dpData.dpId + "' index=" + i + " selected='selected'>" + dpData.dpName + "</option>");
                             _init_group_select(dpData.dpId, gRequestUrl, gSelectId,gsv);
                         }else{
                             $("#" + dSelectId).append("<option value='" + dpData.dpId + "' index=" + i + ">" + dpData.dpName + "</option>");
                         }
                     }
                 }
             }
         }
    });

}
/**
 * 初始化招生组下来信息
 * 
 * @param selected
 *            部门id
 * @param gRequestUrl
 *            招生组请求url
 * @param gSelectId
 *            招生组select标签id
 * @returns
 */
function _init_group_select(selected, gRequestUrl, gSelectId,gsv) {
	$("#" + gSelectId).empty();
	$("#" + gSelectId).append("<option value=''>请选择</option>");
	$.ajax({
         url: gRequestUrl,
         dataType : 'json',
         data : {
    				"dpId" : function(){
    					return selected;
    				}
    		 },
         success: function(data){
             if(_GLOBAL_SUCCESS == data.code) {
                 data = data.body;
                 if(data) {
                     for (var i = 0; i < data.length; i++) {
                         var groupData = data[i];
                         if (gsv == groupData.groupId) {
                             $("#" + gSelectId).find("option:selected").removeAttr("selected");
                             $("#" + gSelectId).append("<option value='" + groupData.groupId + "' index=" + i + " selected='selected'>" + groupData.groupName + "</option>");
                         } else {
                             $("#" + gSelectId).append("<option value='" + groupData.groupId + "' index=" + i + ">" + groupData.groupName + "</option>");	
                         }
                     }
                 }
        		}
         }
    });

}
/**
 * 初始化 校区-部门-员工联动
 * 
 * @param cSelectId
 *            校区select标签ID
 * @param dSelectId
 *            部门select标签ID
 * @param eSelectId
 *            员工select标签ID
 * @param cRequestUrl
 *            校区请求url
 * @param dRequestUrl
 *            部门请求url
 * @param eRequestUrl
 *            员工请求url
 * @param csv
 *            校区选中值
 * @param dsv
 *            部门选中值
 * @param esv
 *            员工选中值
 * @returns
 */
function _init_campus_select_emp(cSelectId, dSelectId, eSelectId, cRequestUrl, dRequestUrl, eRequestUrl,csv, dsv, esv) {
	
	$("#" + cSelectId).empty();
	$("#" + cSelectId).append("<option value=''>请选择</option>");	
    $.ajax({
	     type: "POST",
         url: cRequestUrl,
         dataType : 'json',
         success: function(data) {
             if(_GLOBAL_SUCCESS == data.code) {
                 data = data.body;
                 if(data) {
                		for (var i = 0; i < data.length; i++) {
                			    var campusData = data[i];
                			    if (csv == campusData.campusId) {
                					    $("#" + cSelectId).find("option:selected").removeAttr("selected");
                					    $("#" + cSelectId).append("<option value='" + campusData.campusId + "' index=" + i + " selected='selected'>" + campusData.campusName + "</option>");
                					    _init_dp_select_emp(campusData.campusId,dRequestUrl,eRequestUrl, dSelectId, eSelectId,dsv,esv);
                			    }else{
                			        $("#" + cSelectId).append("<option value='" + campusData.campusId + "' index=" + i + ">" + campusData.campusName + "</option>");	
                			    }
                		 }
                 }
              }
         }
    });
   
   
	$("#" + cSelectId).change(function() {
		var selected = $(this).val();
		_init_dp_select_emp(selected,dRequestUrl,eRequestUrl, dSelectId, eSelectId,dsv,esv);
	});

	$("#" + dSelectId).change(function() {
		var selected = $(this).val();
		_init_emp_select(selected,eRequestUrl ,eSelectId);
	});

	$("#" + cSelectId).select2({
		placeholder : "--请选择校区--",
		allowClear : true
	});
	$("#" + dSelectId).select2({
		placeholder : "--请选择部门--",
		allowClear : true
	});
	$("#" + eSelectId).select2({
		placeholder : "--请选择员工--",
		allowClear : true
	});
}

function _init_dp_select_emp(selected,dRequestUrl,eRequestUrl, dSelectId, eSelectId,dsv,esv) {
	$("#" + dSelectId).empty();
	$("#" + dSelectId).append("<option value=''>请选择</option>");
    $.ajax({
         url: dRequestUrl,
         dataType : 'json',
         data : {
				"campusId" : function(){
					return selected;
				}
		 },
         success: function(data){
             if(_GLOBAL_SUCCESS == data.code) {
                 data = data.body;
                 if(data) {
                     for (var i = 0; i < data.length; i++) {
                         var dpData = data[i];
                         if (dsv == dpData.dpId) {
                             $("#" + dSelectId).find("option:selected").removeAttr("selected");
                             $("#" + dSelectId).append("<option value='" + dpData.dpId + "' index=" + i + " selected='selected'>" + dpData.dpName + "</option>");
                             _init_emp_select(dpData.dpId, eRequestUrl, eSelectId,esv)
                         }else{
                             $("#" + dSelectId).append("<option value='" + dpData.dpId + "' index=" + i + ">" + dpData.dpName + "</option>");
                         }
                     }
                 }
             }
         }
    });

}

function _init_emp_select(selected, eRequestUrl, eSelectId,esv) {
  	$("#" + eSelectId).empty();
  	$("#" + eSelectId).append("<option value=''>请选择</option>");
  	$.ajax({
           url: eRequestUrl,
           dataType : 'json',
           data : {
      				"dpId" : function(){
      					return selected;
    				  }
  		     },
           success: function(data){
             if(_GLOBAL_SUCCESS == data.code) {
                 data = data.body;
                 if(data) {
                     for (var i = 0; i < data.length; i++) {
                         var empData = data[i];
                         if (esv == empData.empId) {
                             $("#" + eSelectId).find("option:selected").removeAttr("selected");
                             $("#" + eSelectId).append("<option value='" + empData.empId + "' index=" + i + " selected='selected'>" + empData.empName + "</option>");
                         }else{
                             $("#" + eSelectId).append("<option value='" + empData.empId + "' index=" + i + ">" + empData.empName + "</option>");	
                         }
                     }
                 }
        		}
         }
    });
}

var loadXML = function(xmlString){
    var xmlDoc=null;
    // 判断浏览器的类型
    // 支持IE浏览器
    if(!window.DOMParser && window.ActiveXObject){   // window.DOMParser
                                                        // 判断是否是非ie浏览器
        var xmlDomVersions = ['MSXML.2.DOMDocument.6.0','MSXML.2.DOMDocument.3.0','Microsoft.XMLDOM'];
        for(var i=0;i<xmlDomVersions.length;i++){
            try{
                xmlDoc = new ActiveXObject(xmlDomVersions[i]);
                xmlDoc.async = false;
                xmlDoc.loadXML(xmlString); // loadXML方法载入xml字符串
                break;
            }catch(e){
            }
        }
    }
    // 支持Mozilla浏览器
    else if(window.DOMParser && document.implementation && document.implementation.createDocument){
        try{
            /*
			 * DOMParser 对象解析 XML 文本并返回一个 XML Document 对象。 要使用
			 * DOMParser，使用不带参数的构造函数来实例化它，然后调用其 parseFromString() 方法
			 * parseFromString(text, contentType) 参数text:要解析的 XML 标记
			 * 参数contentType文本的内容类型 可能是 "text/xml" 、"application/xml" 或
			 * "application/xhtml+xml" 中的一个。注意，不支持 "text/html"。
			 */
            domParser = new  DOMParser();
            xmlDoc = domParser.parseFromString(xmlString, 'text/xml');
        }catch(e){
        }
    }
    else{
        return null;
    }

    return xmlDoc;
};

/** 将form标点序列化 **/
$.fn.serializeObject = function(){
    var o = {};
    var a = this.serializeArray();
    $.each(a, function() {
        if (o[this.name]) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });
    return o;
};


/**
 * 判断是否学期
 * @param itemName		科目名称
 * @param gradeArray	年级数组
 * @returns 返回替换后的科目名称
 */
function getItemName(itemName,grade){
	var grades= new Array("201809","201803","201703")
	for (var i = 0; i < grades.length; i++) {
		if(grades[i] == grade){
			itemName = itemName.replace("年","学期");
			return itemName;
		}
	}
	return itemName;
}
/*添加自定义表格分页控件*/
if($.fn.dataTable){
    $.fn.dataTableExt.oApi.fnExtStylePagingInfo = function ( oSettings )
    {
        return {
            "iStart":         oSettings._iDisplayStart,
            "iEnd":           oSettings.fnDisplayEnd(),
            "iLength":        oSettings._iDisplayLength,
            "iTotal":         oSettings.fnRecordsTotal(),
            "iFilteredTotal": oSettings.fnRecordsDisplay(),
            "iPage":          oSettings._iDisplayLength === -1 ?
                0 : Math.ceil( oSettings._iDisplayStart / oSettings._iDisplayLength ),
            "iTotalPages":    oSettings._iDisplayLength === -1 ?
                0 : Math.ceil( oSettings.fnRecordsDisplay() / oSettings._iDisplayLength )
        };
    };
    $.fn.dataTableExt.oPagination.full_numbers= {
        'oDefaults': {
            'iShowPages': 5
        },
        'fnClickHandler': function(e) {
            var fnCallbackDraw = e.data.fnCallbackDraw,
                oSettings = e.data.oSettings,
                sPage = e.data.sPage;

            // 设置输入显示数据条数
			var showMenuNb=$(".showMenuNb");
            if($(".HuiTab")){
                $(".tabCon").each(function (i,e) {
                    if($(this).css('display')=='block'){
                        showMenuNb=$(this).find('.showMenuNb')
                    }
                })
            }
            if(!showMenuNb.val()||parseInt(showMenuNb.val())<=0||parseInt(showMenuNb.val())>500){
                layer.msg('请输入1-500间的正确数字！', {
                    icon : 0,
                    time : 1000
                });
                showMenuNb.val(10);
                return;
            }
            oSettings._iDisplayLength=parseInt(showMenuNb.val());

            if ($(this).is('[disabled]')) {
                return false;
            }

            oSettings.oApi._fnPageChange(oSettings, sPage);
            fnCallbackDraw(oSettings);

            return true;
        },
        // fnInit is called once for each instance of pager
        'fnInit': function(oSettings, nPager, fnCallbackDraw) {
            var oPaging = oSettings.oInstance.fnExtStylePagingInfo();
            var oClasses = oSettings.oClasses,
                oLang = oSettings.oLanguage.oPaginate,
                that = this;

            var iShowPages = oSettings.oInit.iShowPages || this.oDefaults.iShowPages,
                iShowPagesHalf = Math.floor(iShowPages / 2);

            $.extend(oSettings, {
                _iShowPages: iShowPages,
                _iShowPagesHalf: iShowPagesHalf,
            });

            var oFirst = $('<a class="' + oClasses.sPageButton + ' sPageFirst"><i class="iconfont  icon-first"></i></a>'),
                oPrevious = $('<a class="' + oClasses.sPageButton + ' sPagePrevious"><i class="iconfont  icon-previous"></i></a>'),
                oNumbers = $('<span class="' + oClasses.sPageNumbers + '"></span>'),
                oNext = $('<a class="' + oClasses.sPageButton + ' sPageNext"><i class="iconfont  icon-next"></i></a>'),
                oLast = $('<a style="border-right:1px solid #e4e4e4;" class="' + oClasses.sPageButton + ' sPageLast"><i class="iconfont  icon-last"></i></a>'),
                input=$('<a class="input"></a>'),
                inputSpanO = $('<span class="ml-30 inputSpanO">跳转到</span>'),
                inputIn = $('<input class="paginate_button paginate_number ml-10 inputIn" type="text">'),
                inputSpanT=$('<span class="go paginate_button paginate_number inputSpanT">GO</span>');

            $(input).append(inputSpanO,inputIn,inputSpanT)
            oFirst.click({ 'fnCallbackDraw': fnCallbackDraw, 'oSettings': oSettings, 'sPage': 'first' }, that.fnClickHandler);
            oPrevious.click({ 'fnCallbackDraw': fnCallbackDraw, 'oSettings': oSettings, 'sPage': 'previous' }, that.fnClickHandler);
            oNext.click({ 'fnCallbackDraw': fnCallbackDraw, 'oSettings': oSettings, 'sPage': 'next' }, that.fnClickHandler);
            oLast.click({ 'fnCallbackDraw': fnCallbackDraw, 'oSettings': oSettings, 'sPage': 'last' }, that.fnClickHandler);

            inputSpanT.on('click',function () {
                var pageValue;
                if(!$(inputIn).val()){
                    pageValue=oSettings._iCurrentPage-1;
                }else {
                    pageValue = parseInt($(inputIn).val(), 10) - 1 ; // -1 because pages are 0 indexed, but the UI is 1
				};

                var oPaging = oSettings.oInstance.fnExtStylePagingInfo();

                if(pageValue === NaN || pageValue<0 ){
                    pageValue = 0;
                }else if(pageValue >= oPaging.iTotalPages ){
                    pageValue = oPaging.iTotalPages -1;
                }
                oSettings.oApi._fnPageChange(oSettings, pageValue);
                // 设置输入显示数据条数
                var showMenuNb=$(".showMenuNb");
                if($(".HuiTab")){
                    $(".tabCon").each(function (i,e) {
                        if($(this).css('display')=='block'){
                            showMenuNb=$(this).find('.showMenuNb')
                        }
                    })
                }
                if(!showMenuNb.val()||parseInt(showMenuNb.val())<=0||parseInt(showMenuNb.val())>500){
                    layer.msg('请输入1-500间的正确数字！', {
                        icon : 0,
                        time : 1000
                    });
                    showMenuNb.val(10);
                    return;
                }
                oSettings._iDisplayLength=parseInt(showMenuNb.val());
                fnCallbackDraw(oSettings);
            });

            // Draw
            $(nPager).append(oFirst, oPrevious, oNumbers, oNext, oLast,input);
        },
        // fnUpdate is only called once while table is rendered
        'fnUpdate': function(oSettings, fnCallbackDraw) {
            var oClasses = oSettings.oClasses,
                that = this;

            var tableWrapper = oSettings.nTableWrapper;

            // Update stateful properties
            this.fnUpdateState(oSettings);

            if (oSettings._iCurrentPage === 1) {
                $('.sPageFirst').attr('disabled', true);
                $('.sPagePrevious').attr('disabled', true);
            } else {
                $('.sPageFirst').removeAttr('disabled');
                $('.sPagePrevious').removeAttr('disabled');
            }

            if (oSettings._iTotalPages === 0 || oSettings._iCurrentPage === oSettings._iTotalPages) {
                $('.sPageNext').attr('disabled', true);
                $('.sPageLast').attr('disabled', true);
            } else {
                $('.sPageNext').removeAttr('disabled');
                $('.sPageLast').removeAttr('disabled');
            }

            var i, oNumber, oNumbers = $('.' + oClasses.sPageNumbers, tableWrapper);

            // Erase
            oNumbers.html('');

            for (i = oSettings._iFirstPage; i <= oSettings._iLastPage; i++) {
                oNumber = $('<a class="' + oClasses.sPageButton + ' ' + oClasses.sPageNumber + '">' + oSettings.fnFormatNumber(i) + '</a>');

                if (oSettings._iCurrentPage === i) {
                    oNumber.addClass('current').attr('active', true).attr('disabled', true);
                } else {
                    oNumber.click({ 'fnCallbackDraw': fnCallbackDraw, 'oSettings': oSettings, 'sPage': i - 1 }, that.fnClickHandler);
                }

                // Draw
                oNumbers.append(oNumber);
            }

            // Add ellipses
            if (1 < oSettings._iFirstPage) {
                oNumbers.prepend('<span class="' + oClasses.sPageEllipsis + '"></span>');
            }

            if (oSettings._iLastPage < oSettings._iTotalPages) {
                oNumbers.append('<span class="' + oClasses.sPageEllipsis + '"></span>');
            }
        },
        // fnUpdateState used to be part of fnUpdate
        // The reason for moving is so we can access current state info before fnUpdate is called
        'fnUpdateState': function(oSettings) {
            var iCurrentPage = Math.ceil((oSettings._iDisplayStart + 1) / oSettings._iDisplayLength),
                iTotalPages = Math.ceil(oSettings.fnRecordsDisplay() / oSettings._iDisplayLength),
                iFirstPage = iCurrentPage - oSettings._iShowPagesHalf,
                iLastPage = iCurrentPage + oSettings._iShowPagesHalf;

            if (iTotalPages < oSettings._iShowPages) {
                iFirstPage = 1;
                iLastPage = iTotalPages;
            } else if (iFirstPage < 1) {
                iFirstPage = 1;
                iLastPage = oSettings._iShowPages;
            } else if (iLastPage > iTotalPages) {
                iFirstPage = (iTotalPages - oSettings._iShowPages) + 1;
                iLastPage = iTotalPages;
            }

            $.extend(oSettings, {
                _iCurrentPage: iCurrentPage,
                _iTotalPages: iTotalPages,
                _iFirstPage: iFirstPage,
                _iLastPage: iLastPage
            });
        }
    };
}

$(function () {
    // 回车搜索
    $(document).on("keyup", function (e) {
        if (e.keyCode == 13) {
            $('.search .f-r.mr-5 a:first-child,.search .f-r.mr-15 a:first-child').trigger('click')
        }
    });

    if($(".page-container .search").length){
        $(".page-container .search").each(function (i,e) {
            var searchHeight=66;
            var clLen=$(e).find(".cl:first").find(".cl").length;
            if(clLen>1){
                if(clLen==2){
                    $(e).find(".showOther").hide();
                    searchHeight=70;
                }else{
                    searchHeight=97;
                }
            }else{
                searchHeight=50;
                $(e).find(".showOther").hide()
            }
            $(e).css("height",searchHeight)
		})
	}
    //移除刷新页面按钮
    // $("nav.breadcrumb a.btn.btn-success.radius").remove();
})