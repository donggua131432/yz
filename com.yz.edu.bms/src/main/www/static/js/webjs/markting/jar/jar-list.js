var myDataTable;
      $(function () {
          myDataTable = $('.table-sort').dataTable({
              "serverSide": true,
              "dom": 'rtilp',
              "ajax": {
                  url: "/mpjar/getList.do",
                  type: "post"
              },
              "pageLength": 10,
              "pagingType": "full_numbers",
              "ordering": false,
              "searching": false,
              "lengthMenu": [10, 20],
              "createdRow": function (row, data, dataIndex) {
                  $(row).addClass('text-c');
              },
              "language": _my_datatables_language,
              columns: [
                  {"mData": "jarName"},
                  {"mData": null},
                  {"mData": "jarDesc"},
                  {"mData": null},
                  {"mData": "uploadUser"},
                  {"mData": null},
                  {"mData": "updateUser"},
                  {"mData": null},
                  {"mData": null}
              ],
              "columnDefs": [
                  {"targets": 1,"render": function (data, type, row, meta) {
                      var url = _FILE_URL + row.jarUrl;
                      var dom = '<a href="' + url + ' ">' + url + '</a>';
                      return dom;
                  }},
                  {"targets": 2, "class": "text-l"},
                  {"targets": 3, "class": "text-l no-warp","render": function (data, type, row, meta) {
                      var dateTime = row.uploadTime;
                      if (!dateTime) { return '-'}
                      var date = dateTime.substring(0, 10)
                      var time = dateTime.substring(11)
                      return date + '<br>' + time
                  }},
                  {"targets": 5, "class": "text-l no-warp","render": function (data, type, row, meta) {
                      var dateTime = row.updateTime;
                      if (!dateTime) {
                          return '-'
                      }
                      var date = dateTime.substring(0, 10)
                      var time = dateTime.substring(11)
                      return date + '<br>' + time
                  }},
                  {"targets": 7,"render": function (data, type, row, meta) {
                      var isAllow = row.isAllow;
                      if ('1' == isAllow) {
                          return '<span class="label label-success radius">已启用</span>';
                      } else {
                          return '<span class="label label-danger radius">已禁用</span>';
                      }
                  }},
                  {"targets": 8,"render": function (data, type, row, meta) {
                      var dom = '';
                      dom += '<a onClick="toggle(\'' + row.jarName
                          + '\')" href="javascript:;" title="启用" style="text-decoration:none"><i class="iconfont icon-qiyong"></i></a>';
                      dom += ' &nbsp;';
                      dom += '<a title="删除" href="javascript:void(0)" onclick="del(\'' + row.jarName + '\')" class="ml-5" style="text-decoration:none">';
                      dom += '<i class="iconfont icon-shanchu"></i></a>';
                      return dom;
                  }
              }]
          });
      });

      function add(type, jarName) {
          layer_show('添加JAR包信息', '/mpjar/toAdd.do', 1200, null, function () {
              myDataTable.fnDraw(true);
          });
      }

      function del(jarName) {
          $.ajax({
              type: 'POST',
              url: '/mpjar/delete.do',
              data: {
                  'jarName': jarName
              },
              dataType: 'json',
              success: function (data) {
                  if (_GLOBAL_SUCCESS == data.code) {
                      layer.msg('删除成功', {
                          icon: 1,
                          time: 1000
                      }, function () {
                          _search();
                      });
                  }
              }
          });
      }

      function toggle(jarName) {
          $.ajax({
              type: 'POST',
              url: '/mpjar/start.do',
              data: {
                  'jarName': jarName
              },
              dataType: 'json',
              success: function (data) {
                  if (_GLOBAL_SUCCESS == data.code) {
                      layer.msg('启用成功', {
                          icon: 1,
                          time: 1000
                      }, function () {
                          _search();
                      });
                  }
              }
          });
      }

      function _search() {
          myDataTable.fnDraw(true);
      }