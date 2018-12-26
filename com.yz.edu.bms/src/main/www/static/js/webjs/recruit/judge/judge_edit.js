$(function(){
        
         
        if(baseInfo) {
          $('#stdName').text(baseInfo.stdName);
          $('#idType').text(_findDict('idType', baseInfo.idType));
          $('#idCard').text(baseInfo.idCard);
          $('#sex').text(_findDict('sex', baseInfo.sex));
          $('#birthday').text(baseInfo.birthday);
          $('#nation').text(_findDict('nation', baseInfo.nation));
          $('#politicalStatus').text(_findDict('politicalStatus', baseInfo.politicalStatus));
          $('#rprType').text(_findDict('jobType', baseInfo.jobType));
          
          var rprProvinceCode = baseInfo.rprProvinceCode;
          var rprCityCode = baseInfo.rprCityCode;
          var rprDistrictCode = baseInfo.rprDistrictCode;
          var rprAddress = baseInfo.rprAddress;
          
          var ra = _findProvinceName(rprProvinceCode) + _findCityName(rprProvinceCode, rprCityCode) + _findDistrictName(rprProvinceCode, rprCityCode, rprDistrictCode) + (rprAddress ? rprAddress : '');
          
          $('#rprAddress').text(ra);
          $('#jobType').text(_findDict('jobType', baseInfo.jobType));
          $('#wpTime').text(baseInfo.wpTime ? baseInfo.wpTime : '');
          
          var wpProvinceCode = baseInfo.wpProvinceCode;
          var wpCityCode = baseInfo.wpCityCode;
          var wpDistrictCode = baseInfo.wpDistrictCode;
          var wpAddress = baseInfo.wpAddress;
          
          var wa = '';
          wa += _findProvinceName(wpProvinceCode) + _findCityName(wpProvinceCode, wpCityCode) + _findDistrictName(wpProvinceCode, wpCityCode, wpDistrictCode) + (wpAddress ? wpAddress : '');

          $('#wpAddress').text(wa);
          $('#wpTelephone').text(baseInfo.wpTelephone ? baseInfo.wpTelephone : '');
          $('#mobile').text(baseInfo.mobile ? baseInfo.mobile : '');
          $('#emergencyContact').text(baseInfo.emergencyContact ? baseInfo.emergencyContact : '');
        }
        
        if(historyInfo) {
          $('#edcsType').text(_findDict('edcsType', historyInfo.edcsType));
          $('#unvsName').text(historyInfo.unvsName ? historyInfo.unvsName : '');
          $('#graduateTime').text(historyInfo.graduateTime ? historyInfo.graduateTime : '');
          $('#profession').text(historyInfo.profession ? historyInfo.profession : '');
          $('#edcsSystem').text(_findDict('edcsSystem', historyInfo.edcsSystem));
          $('#diploma').text(historyInfo.diploma ? historyInfo.diploma : '');
          
          var oldProvinceCode = historyInfo.oldProvinceCode;
          var oldCityCode = historyInfo.oldCityCode;
          var oldDistrictCode = historyInfo.oldDistrictCode;
          var oldAddress = '';
          oldAddress += _findProvinceName(oldProvinceCode) + _findCityName(oldProvinceCode, oldCityCode) + _findDistrictName(oldProvinceCode, oldCityCode, oldDistrictCode);
          
          $('#oldAddress').text(oldAddress);
        }
        
        if(enrollInfo) {
          
          var pfsnName = enrollInfo.pfsnName;
          var unvsName = enrollInfo.unvsName;
          var recruitType = enrollInfo.recruitType;
          var pfsnCode = enrollInfo.pfsnCode;
          var pfsnLevel = enrollInfo.pfsnLevel;

          var text = '';
          if(recruitType) {
          text += "[" + _findDict("recruitType", recruitType) + "]";
          }
          text += unvsName + ":";
          text += "(" + pfsnCode + ")" + pfsnName + "[" + _findDict("pfsnLevel", pfsnLevel) + "]";
          
          $('#grade').text(_findDict('grade', enrollInfo.grade));
          $('#pfsnLevel').text(_findDict('pfsnLevel', pfsnLevel));
          $('#wish').text(text);
        }
        
        $("#bt_submit").click(function(){
            $.ajax({
                type : 'POST',
                url : '/judge/check.do',
                data : {
                    'learnIds' : learnId,
                    'scholarshipStatus' : '2'
                },
                dataType : 'json',
                success : function(data) {
                    if (_GLOBAL_SUCCESS = data.code) {
                        layer.msg('审核成功', {
                            icon : 1,
                            time : 1000
                        }, function() {
                            layer_close();
                        });
                    }
                }
            });
        });
        
        $("#bt_cancel").click(function(){
            var reason = $('#refundReason').val();
            
            if(!reason) {
                layer.msg('请先填写驳回原因', {
                    icon : 5,
                    time : 1000
                });
                return;
            }
            
            $.ajax({
                type : 'POST',
                url : '/judge/check.do',
                data : {
                    'learnIds' : learnId,
                    'scholarshipStatus' : '3',
                    'reason' : reason
                },
                dataType : 'json',
                success : function(data) {
                    if (_GLOBAL_SUCCESS = data.code) {
                        layer.msg('审核成功', {
                            icon : 1,
                            time : 1000
                        }, function() {
                            layer_close();
                        });
                    }
                }
            });
        });
    });