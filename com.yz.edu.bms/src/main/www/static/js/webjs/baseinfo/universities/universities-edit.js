	var myDataTable;
    var testarrId = [];
    var setting = {};
    $(function() {
        //初始化院校类型下拉框
        _init_select("recruitType", dictJson.recruitType,varRecruitType);

        $('.radio-box input').iCheck({
            checkboxClass: 'icheckbox-blue',
            radioClass: 'iradio-blue',
            increaseArea: '20%'
        });

        $("#form-member-add").validate({
            rules: {
                recruitType: {
                    required: true
                },
                unvsName: {
                    required: true,
                },
                unvsCode: {
                    required: true,
                },
                sort: {
                    required: true,
                },
                isStop: {
                    required: true,
                }
            },
            onkeyup: false,
            focusCleanup: true,
            success: "valid",
            submitHandler: function (form) {
                var taIds = '';
                var keys = Object.keys(taMap);
                if (keys && keys.length > 0) {
                    for (var i = 0; i < keys.length; i++) {
                        var taId = $.trim(taMap[keys[i]].taId);
                        if (taId) {
                            taIds += taId + '#';
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
                    url: $("#exType").val() == "UPDATE" ? "/bdUniversity/editBdUniversity.do" :"/bdUniversity/insertBdUniversity.do", //请求url
                    success : function (data) { //提交成功的回调函数
                        layer.msg('提交成功！', {
                            icon : 1,
                            time : 1000
                        },function () {
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
                    });
                    layer_close();
                }
            })
                ;
            }
        });

        var zipCode = _init_area_select("provinceCode", "cityCode", "districtCode", _province, _city, _district);

        $('.AreaChecked').html(oldSelectArea(taMap));
        /**
         ztree模块开始
         */
        var zTree;
        setting = {
            view: {
                dblClickExpand: false,//双击节点时，是否自动展开父节点的标识
                showLine: true,//是否显示节点之间的连线
                fontCss: {
                    'color': 'black',
                    'font-weight': '400'
                },//字体样式函数
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
                    zTree = $.fn.zTree.getZTreeObj(treeId);
                    if ("C" == treeNode.flag) {
                        onCheck(treeId, treeNode);
                    }
                    zTree.expandNode(treeNode);
                },
                onCheck: function (event, treeId, treeNode) {
                    if (!treeNode.isParent) {
                        var id = treeNode.id;
                        if (treeNode.checked) {
                            testarrId.push(id);
                            preMap[id] = {"taId": id, "taName": treeNode.name};
                        } else {
                            testarrId.removeByValue(id);
                            delete preMap[id];
                        }
                    } else if ("C" == treeNode.flag) {
                        onCheck(treeId, treeNode, true, treeNode.checked);
                    } else {

                        var cNodes = treeNode.children;

                        if (cNodes && cNodes.length > 0) {
                            for (var i = 0; i < cNodes.length; i++) {
                                var cNode = cNodes[i];

                                onCheck(treeId, cNode, true, treeNode.checked);
                            }
                        }
                    }
                },
                beforeExpand: function (treeId, treeNode) {
                    onCheck(treeId, treeNode);
                }
            }
        };

        initTreeInfo();
    });

    //树形节点点击事件
    function onCheck(treeId, treeNode, isCheckEvent, isChecked) {
        if (treeNode.children == null && treeNode.isParent) {
            var zTreee = $.fn.zTree.getZTreeObj("user_tree");
            var testArea = "";
            var arr = [];
            var pNode = treeNode.getParentNode();
            while (pNode != null) {
                arr.push(pNode.id);
                pNode = pNode.getParentNode();
            }
            //反序
            for (var i = arr.length - 1; i >= 0; i--) {
                testArea += arr[i] + "#";
            }
            //根据省市区获取对应考区
            $.ajax({
                type: "POST",
                url: "/testArea/findBdTestArea.do",
                data : {
                "testArea":testArea + treeNode.id
                },
                dataType : 'JSON',
                success:function (data) {
                data = data.body;

                for (var i = 0; i < data.length; i++) {
                    if (isCheckEvent) {
                        zTreee.addNodes(treeNode, [{
                            id: data[i].taId,
                            pId: treeNode.id,
                            name: data[i].taName,
                            open: true,
                            isParent: false,
                            checked: isChecked
                        }]);
                        if (isChecked) {
                            preMap[data[i].taId] = {"taId": data[i].taId, "taName": data[i].taName};
                        } else {
                            delete preMap[data[i].taId];
                        }
                    } else {
                        if (preMap[data[i].taId]) {
                            isChecked = true;
                        } else {
                            isChecked = false;
                        }
                        zTreee.addNodes(treeNode, [{
                            id: data[i].taId,
                            pId: treeNode.id,
                            name: data[i].taName,
                            open: true,
                            isParent: false,
                            checked: isChecked
                        }]);
                    }
                }
            }
        });
        } else {
            var nodes = treeNode.children;

            if (nodes && nodes.length > 0) {
                for (var i = 0; i < nodes.length; i++) {
                    var node = nodes[i];
                    if (isChecked) {
                        preMap[node.id] = {"taId": node.id, "taName": node.name};
                    } else {
                        delete preMap[node.id];
                    }
                }
            }
        }
    }

    //考试地区显示事件
    function showArea(event) {
        initTreeInfo();
        event.stopPropagation();
        $("#pcdztree").fadeIn(200);
    }

    //        考试地区确认选择事件
    function SelectedArea() {
        var keys = Object.keys(preMap);
        if (keys && keys.length > 0) {
            taMap = preMap;
            $('.AreaChecked').html(oldSelectArea(taMap));
            $("#pcdztree").fadeOut(200);
        } else {
            layer.msg('请选择考试区县', {
                icon: 5
            });
        }
    }

    function initTreeInfo() {
        var zNodes = [];
        //对复选框进行响应
        var unvsId = $("#unvsId").val();
        //Ajax调用处理
        $.ajax({
            type: "POST",
            dataType: 'JSON',
            url: '/testArea/getAreaList.do'+"?type=1&mappingId=" + unvsId,
            success:function (data) {
            data = data.body;
            if (data && data.length > 0) {
                for (var i = 0; i < data.length; i++) {
                    zNodes.push({
                        "id": data[i].id,
                        "pId": data[i].pId,
                        "name": data[i].name,
                        "open": false,
                        "isParent": true,
                        "nocheck": false,
                        "checked": data[i].checked,
                        "flag": data[i].flag
                    });
                }

                var zTree = $.fn.zTree.init($("#user_tree"), setting, zNodes);
                $('#pcdztree').siblings('span.btn').text('选取');
            } else {
                $('#pcdztree').siblings('span.btn').text('没有设置考试区县');
            }
        }
    }) ;
    }

    //    考试地区隐藏事件
    function hideArea() {
        $("#pcdztree").fadeOut(200);
    }

    function oldSelectArea(taMap) {
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