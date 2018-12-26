 var myDataTable;
    var ut = {};

    var setting = {};

    $(function () {
        $('.skin-minimal input').iCheck({
            checkboxClass: 'icheckbox-blue',
            radioClass: 'iradio-blue',
            increaseArea: '20%'
        });
        //定义院校变量，以免重复获取
        var unvsIdJson;
        var unvsName;
        //初始化专业大类下拉框
        _init_select("pfsnCata", dictJson.pfsnCata, $("#pfsnCata").val());

        //初始化年度下拉框
        _init_select("year", dictJson.year, $("#year").val());

        //初始化年级下拉框
        _init_select("grade", dictJson.grade, $("#grade").val());

        //初始化专业层次下拉框
        _init_select("pfsnLevel", dictJson.pfsnLevel, $("#pfsnLevel").val());

        //初始化授课方式下拉框
        _init_select("teachMethod", dictJson.teachMethod, $("#teachMethod").val());

        $('.AreaChecked').html(oldSelectArea());

        //初始化院校名称下拉框
        _simple_ajax_select({
            selectId: "unvsId",
            searchUrl: "/bdUniversity/findAllKeyValue.do",
            sData : {},
            showText : function (item) {
            return item.unvs_name;
            },
            showId : function (item) {
            ut[item.unvs_id] = item.recruit_type;
            return item.unvs_id;
            }
            ,
            checkedInfo : {
            value : varunvsId, name:varunvsName},
            placeholder : '--请选择院校--'
    		});

        $("#unvsId").change(function () {
            var uId = $(this).val();

            var rt = ut[uId];

            initRecruitDiv(rt);
        });

        //标签块
        $.Huitab("#tab_demo .tabBar span", "#tab_demo .tabCon", "current", "click", "0");

        $("#form-member-add").validate({
            rules: {
                year: {
                    required: true
                },
                grade: {
                    required: true,
                },
                unvsId: {
                    required: true,
                },
                pfsnName: {
                    required: true,
                },
                pfsnCode: {
                    remote: { //验证编码名是否存在
                        type: "POST",
                        url: "/unvsProfession/validate.do", //servlet
                        data : {
                            exType : function () {
                                    return $("#exType").val();
                                    }
                        ,
                            paramName : function () {
                                return $("#pfsnCode").val();
                                    }
                            }
                        },
                 required : true
            },
        pfsnCata : {
            required : true,
        }
        ,
        pfsnLevel : {
            required : true,
        }
        ,
        teachMethod : {
            required : true,
        }
        ,
        testSubject : {
            required : true,
        }
        ,
        isAllow : {
            required : true,
        }
        ,
        pcdztree : {
            required : true,
        }
    },
        messages : {
            pfsnCode : {
                remote : "专业编码已存在"
            }
        }
        ,
        onkeyup : false,
        focusCleanup:true,
        success:"valid",
        submitHandler:function (form) {
            var taIds = '';
            var keys = Object.keys(taMap);
            if (keys && keys.length > 0) {
                for (var i = 0; i < keys.length; i++) {
                    var taId = $.trim(taMap[keys[i]].taId);
                    if (taId) {
                        taIds += taId + ',';
                    }
                }
            }

            if (taIds.length > 0) {
                taIds = taIds.substr(0, taIds.length - 1);
            } else {
                layer.msg('请选择考区', {
                    icon: 5,
                    time: 1000
                });
                return;
            }

            $("#testAreaId").val(taIds);

            $(form).ajaxSubmit({
                type: "post", //提交方式
                dataType: "json", //数据类型
                url: $("#exType").val() == "UPDATE" ? "/unvsProfession/editBdUnvsProfession.do" :"/unvsProfession/insertBdUnvsProfession.do", //请求url
                success : function (data) { //提交成功的回调函数
                layer.msg('操作成功！', {icon: 1, time: 1000}, function () {
                    if($("#exType").val() == "UPDATE"){
                        window.parent.myDataTable.fnDraw(false);
                    }else{
                        window.parent.myDataTable.fnDraw(true);
                    }
                    layer_close();
                });
            },
            error : function (data) {
                layer.msg('操作失败', {
                    icon: 1,
                    time: 1000
                },function(){
                    layer_close();
                });

            }
        })
            ;
        }
    })
        ;

        initRecruitDiv(varrecruitType);

        /**
         ztree模块开始
         */

        setting = {
            view: {
                dblClickExpand: false,//双击节点时，是否自动展开父节点的标识
                showLine: true,//是否显示节点之间的连线
                fontCss: {'color': 'black', 'font-weight': '400'},//字体样式函数
                selectedMulti: false
                //设置是否允许同时选中多个节点
            },
            check: {
                //chkboxType: { "Y": "ps", "N": "ps" },
                chkStyle: "checkbox",//复选框类型
                enable: true
                //每个节点上是否显示 CheckBox
            },
            data: {
                simpleData: {//简单数据模式
                    enable: true,
                    idKey: "id",
                    pIdKey: "pId",
                    rootPId: ""
                }
            },
            callback: {
                beforeClick: function (treeId, treeNode) {
                    var zTree = $.fn.zTree.getZTreeObj("user_tree");
                    zTree.expandNode(treeNode);//触发beforeExpand
                }
            }
        };

        /**
         ztree模块结束
         */
        /**
         数据表格开始
         */
        myDataTable = $('.table-sort').dataTable({
            "serverSide": true,
            "dom": 'rtilp',
            "ajax": {
                url: "/sysLogInfo/findAllSysLogInfo.do",type : "post",
                data:{
                    "method":"editBdUniversity"
                    }
                },
        "pageLength":10,
        "pagingType":"full_numbers",
         "ordering":false,
         "searching":false,
         "createdRow":function (row, data, dataIndex) {
            $(row).addClass('text-c');
        },
        "language":{
                "emptyTable":"没有检索到数据！",
                "zeroRecords":"没有检索到数据！",
                "lengthMenu":"每页显示 _MENU_ 条"
        },
        columns : [{"mData": "userName"},
            {"mData": "method"},
            {"mData": null}],
            "columnDefs":[{
            "render": function (data, type, row, meta) {
                return new Date(row.updateTime).format('yyyy-MM-dd hh:mm:ss');
            },
            "targets": 2
        }]
    });
        /**
         数据表格结束
         */
    });


    //考试地区显示事件
    function showArea(event) {
        var unvsId = $("#unvsId").val();

        if (unvsId) {
            initTreeInfo(unvsId)
        } else {
            layer.msg('请选择院校', {
                icon: 1,
                time: 1000
            });
            return;
        }

        event.stopPropagation();
        $("#pcdztree").fadeIn(200);
    }

    //        考试地区确认选择事件
    function SelectedArea() {
        var zTree = $.fn.zTree.getZTreeObj("user_tree");
        var nodes = zTree.getCheckedNodes(true);
        var html = "";
        taMap = {};
        for (var i = 0; i < nodes.length; i++) {
            if (!nodes[i].isParent) {
                html += nodes[i].name + "，";
                taMap[nodes[i].id] = {"taId": nodes[i].id, "taName": nodes[i].name};
            }
        }

        if (html.length > 0)
            html = html.substr(0, html.length - 1);

        $('.AreaChecked').html(html);
        $("#pcdztree").fadeOut(200);
    }

    function oldSelectArea() {
        var html = '';
        var keys = Object.keys(taMap);
        if (keys && keys.length > 0) {
            for (var i = 0; i < keys.length; i++) {
                html += taMap[keys[i]].taName + '，';
            }
        }

        if (html.length > 0)
            html = html.substr(0, html.length - 1);

        return html;
    }

    //    考试地区隐藏事件
    function hideArea() {
        $("#pcdztree").fadeOut(200);
    }

    function initTestGroup(pfsnLevel) {
        _simple_ajax_select({
            selectId: "groupId",
            searchUrl: "/unvsProfession/findTestGroup.do"+"?pfsnLevel=" + pfsnLevel , sData:{},
            showText : function (item) {
            return item.groupName;
            },
        showId : function (item) {
            return item.groupId;
        },
        checkedInfo : {
            value : vargroupId, name:vargroupName},
            placeholder : '--请选择考试科目组--'
    });
    }

    function initRecruitDiv(rt) {
        if ("1" == rt) {
            $("#groupId").rules('add', {required: true});

            $("#pfsnLevel").change(function () {
                initTestGroup($(this).val());
            });
            var pl = $("#pfsnLevel").val();
            if (pl) {
                initTestGroup(pl);
            }

            $("#showTestSubject").text($("#showTestSubjects").val());
            $("#groupId").change(function () {
                var selected = $(this).val();
                //根据组id加载科目
                $.ajax({
                    type: "POST",
                    url: "/unvsProfession/findTestSubject.do",
                data:{
                    groupId:selected
                },
                dataType : 'json',
                    success:function (data) {
                    data = data.body;
                    $("#showTestSubject").text(data);
                }
            })
                ;
            });
            $("#div_passScore").show();
            $("#div_groupId").show();
            $("#div_ts").show();
        } else {
            $("#div_passScore").hide();
            $("#div_groupId").hide();
            $("#div_ts").hide();
        }
    }

    function initTreeInfo(unvsId) {
        if (unvsId) {
            var zNodes = [];
            //Ajax调用处理
            $.ajax({
                type: "POST",
                dataType: "JSON", //数据类型
                url: "/unvsProfession/getAreaList.do"+"?unvsId=" + unvsId + "&pfsnId=" + $("#pfsnId").val(),
                success:function (data) {
                data = data.body;
                if (data && data.length > 0) {

                    for (var i = 0; i < data.length; i++) {
                        var isChecked = false;
                        var isParent = true;
                        if ("T" == data[i].flag) {
                            if (taMap[data[i].id]) {
                                isChecked = true;
                            }
                            isParent = false;
                        }

                        zNodes.push({
                            "id": data[i].id,
                            "pId": data[i].pId,
                            "name": data[i].name,
                            "open": false,
                            "isParent": isParent,
                            "nocheck": false,
                            "flag": data[i].flag,
                            "checked": isChecked
                        });
                    }

                    zTree = $.fn.zTree.init($("#user_tree"), setting, zNodes);
                    $('#pcdztree').siblings('span.btn').text('选取');
                } else {
                    $('#pcdztree').siblings('span.btn').text('该院校并没有设置考区');
                }
            }
        })
            ;
        }
    }