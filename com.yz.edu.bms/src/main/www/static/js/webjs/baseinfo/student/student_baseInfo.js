var isMine = false;
            $(function() {

                var headPortrait = varOther.headPortrait;

                if (headPortrait) {
                    createPhoto(_FILE_URL + headPortrait + "?" + Date.parse(new Date()));
                }

                $("#stdName").text(varBaseInfo.stdName);
                $("#idType").text(_findDict('idType', varBaseInfo.idType));
                $("#idCard").text(varBaseInfo.idCard);
                $("#sex").text(_findDict('sex', varBaseInfo.sex));
                $("#birthday").text(varBaseInfo.birthday);
                $("#nation").text(_findDict('nation', varBaseInfo.nation));

                $("#address").val(varBaseInfo.address);
                $("#zipCode").val(varBaseInfo.zipCode);
                $("#mobile").val(varBaseInfo.mobile);
                $("#s_mobile").val(varBaseInfo.mobile);
                
                isMine = $("#s_mobile").val() == $("#mobile").val();
                
                if(!isMine) {
                    $("#s_mobile").prop('disabled', 'disabled');
                }
                
                $("#emergencyContact").val(varBaseInfo.emergencyContact);

                $("#telephone").val(varOther.telephone);
                $("#email").val(varOther.email);
                $("#qq").val(varOther.qq);
                $("#wechat").val(varOther.wechat);
                $("#jobTitle").val(varOther.jobTitle);
                $("#workPlace").val(varOther.workPlace);
                $("#remark").val(varOther.remark);
                
                $("#wpAddress").val(varBaseInfo.wpAddress);
                $("#wpTelephone").val(varBaseInfo.wpTelephone);
                $("#wpTime").val(varBaseInfo.wpTime);
                

                _init_select("politicalStatus", dictJson.politicalStatus, varBaseInfo.politicalStatus);
                _init_select("rprType", dictJson.rprType, varBaseInfo.rprType);
                _init_select("jobType", dictJson.jobType, varBaseInfo.jobType);
                _init_select("nation", dictJson.nation, varBaseInfo.nation);

                _init_select("maritalStatus", dictJson.maritalStatus, varOther.maritalStatus);

                var rprZipCode = _init_area_select("rprProvinceCode", "rprCityCode", "rprDistrictCode", varBaseInfo.rprProvinceCode, varBaseInfo.rprCityCode,
                        varBaseInfo.rprDistrictCode);
                var nowZipCode = _init_area_select("nowProvinceCode", "nowCityCode", "nowDistrictCode", varBaseInfo.nowProvinceCode, varBaseInfo.nowCityCode,
                        varBaseInfo.nowDistrictCode);
                var wpZipCode = _init_area_select("wpProvinceCode", "wpCityCode", "wpDistrictCode", varBaseInfo.wpProvinceCode, varBaseInfo.wpCityCode,
                        varBaseInfo.wpDistrictCode);

                $("#zipCode").val(rprZipCode);
                /* $("#rprProvinceCode").change(_zipCodeChange);
                $("#rprCityCode").change(_zipCodeChange);
                $("#rprDistrictCode").change(_zipCodeChange); */

            });
            function createPhoto(imgSrc) {
                var box = $("#member-photo");
                box.empty();
                var img = document.createElement('img');
                $(img).on('load', function() {
                    var height = this.height;
                    var width = this.width;
                    if (height > width) {
                        $(img).css('height', '140px');
                    } else {
                        $(img).css('width', '140px');
                    }
                });
                img.src = imgSrc;
                box.html(img);
            }