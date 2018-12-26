var myDataTable;

            $(function() {
                
                _init_select("inviteType", dictJson.inviteType);
                _init_select("sScholarship", dictJson.scholarship);
                
                myDataTable = $('.table-sort').dataTable({
                    "serverSide" : true,
                    "dom" : 'rtilp',
                    "ajax" : {
                        url : "/fans/getUndistributedList.do",
                        type : "post",
                        data : function(pageData) {
                            return search_data(pageData);
                        }
                    },
                    "pageLength" : 10,
                    "pagingType" : "full_numbers",
                    "ordering" : false,
                    "searching" : false,
                    "lengthMenu" : [ 10, 20 ],
                    "createdRow" : function(row, data, dataIndex) {
                        $(row).addClass('text-c');
                    },
                    "language" : _my_datatables_language,
                    columns : [ {
                        "mData" : null
                    }, {
                        "mData" : null
                    }, {
                        "mData" : null
                    }, {
                        "mData" : null
                    }, {
                        "mData" : null
                    }, {
                        "mData" : null
                    }],
                    "columnDefs" : [ {
                        "render" : function(data, type, row, meta) {
                            return '<input type="checkbox" value="'+ row.userId + '" name="userIds"/>';
                        },
                        "targets" : 0
                    }, {
                        "render" : function(data, type, row, meta) {
                            return user(row);
                        },
                        "targets" : 1,
                        "className" : "text-l"
                    }, {
                        "render" : function(data, type, row, meta) {
                            return account(row.accList);
                        },
                        "targets" : 2,
                        "className" : "text-l"
                    }, {
                        "render" : function(data, type, row, meta) {
                            return pUser(row);
                        },
                        "targets" : 3,
                        "className" : "text-l"
                    }, {
                        "render" : function(data, type, row, meta) {
                            return source(row);
                        },
                        "targets" : 4
                    }, {
                        "render" : function(data, type, row, meta) {
                        	var dom = '';
                            dom = '<a title="分配" href="javascript:;" onclick="showZSLS(\'' + 1 + '\', \'' + row.userId + '\')" class="ml-5" style="text-decoration: none;color:blue;">';
                            dom += '>>>分配跟进人>>></a><br/>';
                            dom += '<a title="分配" href="javascript:;" onclick="showXJ(\'' + 1 + '\', \'' + row.userId + '\')" class="ml-5" style="text-decoration: none;color:blue;">';
                            dom += '>>>分配校监　>>></a>';
                            return dom;
                        },
                        "targets" : 5
                    } ]
                });
            });

            var index;
            
            function showXJ(type, userId) {
            	var url = '/fans/toUDistributionPageXJ.do' + '?addType=' + type;
            	showPage(type, userId, url, '校监分配');
            }
            function showZSLS(type, userId) {
            	var url = '/fans/toUDistributionPage.do' + '?addType=' + type;
            	showPage(type, userId, url, '跟进人分配');
            }

            function showPage(type, userId, url, title) {
                if (type == '1') {
                    url += '&userIds=' + userId;
                } else {
                    var userIds = '';
                    $("input[name='userIds']:checked").each(function(index, data) {
  						var lId = $(this).val();                      
                   
                        if (lId && 'undefined' != lId && 'null' != lId) {
                            userIds += $(this).val() + ",";
                        }
                    });
                    if (userIds && userIds.length > 0) {
                        userIds = userIds.substring(0, userIds.length - 1);
                    } else {
                        layer.msg('请选择用户', {
                            icon : 5,
                            time : 1000
                        });
                        return false;
                    }
                    url += '&userIds=' + userIds;
                }
                layer_show(title, url, 1200, 810, function() {
                    myDataTable.fnDraw(false);
                }, true);
            }

            function _search() {
                $(":checked").prop("checked", false);
                myDataTable.fnDraw(true);
            }

            function search_data(pageData) {
                return {
                    inviteType : $("#inviteType").val() ? $("#inviteType").val() : '',
                    name : $("#name").val() ? $("#name").val() : '',
                    mobile : $("#mobile").val() ? $("#mobile").val() : '',
                    yzCode : $("#yzCode").val() ? $("#yzCode").val() : '',
                    idCard : $("#idCard").val() ? $("#idCard").val() : '',
                    sScholarship : $("#sScholarship").val() ? $("#sScholarship").val() : '',
                    start : pageData.start,
                    length : pageData.length
                };
            }
            
            function user(data) {
                
                var nickname = data.nickname ? data.nickname : '';
                var yzCode = data.yzCode ? data.yzCode : '';
                var name = data.name ? data.name : '';
                var mobile = data.mobile ? data.mobile : '';
                var idCard = data.idCard ? data.idCard : '';
                
               var dom = '<ul>';
               dom += '<li>昵称：' + nickname + '</li>';
               dom += '<li>远智编号：' + yzCode + '</li>';
               dom += '<li>真实姓名：' + name + '</li>';
               dom += '<li>手机号：' + mobile + '</li>';
               dom += '<li>身份证号：' + idCard + '</li>';
               dom += '</ul>';
               
               return dom;
            }
            
            function pUser(data) {
                if(data.pId && '' != data.pId) {
                 var nickname = data.pNickname ? data.pNickname : '';
                 var yzCode = data.pYzCode ? data.pYzCode : '';
                 var name = data.pName ? data.pName : '';
                 var mobile = data.pMobile ? data.pMobile : '';
                 var idCard = data.pIdCard ? data.pIdCard : '';
                 
                 var dom = '<ul>';
                 dom += '<li>昵称：' + nickname + '</li>';
                 dom += '<li>远智编号：' + yzCode + '</li>';
                 dom += '<li>真实姓名：' + name + '</li>';
                 dom += '<li>手机号：' + mobile + '</li>';
                 dom += '<li>身份证号：' + idCard + '</li>';
                 dom += '</ul>';
                 
                 return dom;
                } 
                return '无';
            }
            
            function source(data) {
                var scholarship = _findDict('scholarship', data.sScholarship);
                var regTime = data.regTime ? data.regTime : '无';
                
                var dom = '<ul>';
                dom += '<li>' + (scholarship ? scholarship : '无') + '</li>';
                dom += '<li>' + regTime + '</li>';
                dom += '</ul>';
                
                return dom;
            }
            
            function account(data) {
                if(data) {
                    var dom = '<ul>';
                    $.each(data, function(index, acc){
                        var accName = _findDict('accType', acc.accType);
                        accName = accName ? accName : '未知账户';
                        var amount = acc.accAmount ? acc.accAmount : '0.00';
                        if('2' == acc.accType) {
                        	dom += '<li><a href=\'javascript:void(0)\' onclick="showAccount(\'' + acc.userId + '\')" >' + accName + "：" + amount + '</a></li>';
                        } else {
                        	dom += '<li>' + accName + "：" + amount + '</li>';
                        }
                    });
                    dom += '</ul>';
                    return dom;
                } else {
                    return '无账户信息';
                }
            }
            
            function showAccount(userId){
    			layer_show('分配日志', '/invite_user/toAccount.do' + '?userId=' + userId, 1200, 600, function() {
                    myDataTable.fnDraw(false);
                }, false);
    		}