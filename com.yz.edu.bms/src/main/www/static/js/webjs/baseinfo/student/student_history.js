 $(function() {
                $("#unvsName").val(varHistory.unvsName);
                $("#adminssionTime").val(varHistory.adminssionTime);
                $("#graduateTime").val(varHistory.graduateTime);
                $("#profession").val(varHistory.profession);
                $("#diploma").val(varHistory.diploma);
                $("#unvsName").val(varHistory.unvsName);
                _init_select("edcsType", dictJson.edcsType, varHistory.edcsType);
        		_init_select("edcsSystem", dictJson.edcsSystem, varHistory.edcsSystem);
        		_init_area_select("oldProvinceCode", "oldCityCode", "oldDistrictCode", varHistory.oldProvinceCode, varHistory.oldCityCode, varHistory.oldDistrictCode);
                
                
                if('2' == recruitType) {
                  	$("#isOpenUnvs-" + isOpenUnvs).attr("checked", "checked");
                  	$(":radio[name='isOpenUnvs']").rules("add", {required:true});
                  	$('.skin-minimal input').iCheck({
                				checkboxClass: 'icheckbox-blue',
                				radioClass: 'iradio-blue',
                				increaseArea: '20%'
                			});
                  }
            });