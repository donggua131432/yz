var annexListTable;
    $(function () {
        annexListTable = $('#annexListTable').dataTable({
            "serverSide": true,
            "dom": 'rtp',
            "ajax": {
                url: "/" + varUrl,
                type: "post",
                data: {
                    empId: varEmpId
                }
            },
            "paging": false,
            "ordering": false,
            "searching": false,
            "createdRow": function (row, data, dataIndex) {
                $(row).addClass('text-c');
            },
            "language": _my_datatables_language,
            "columns": [{
                "mData": "annexName"
            }, {
                "mData": null
            }, {
                "mData": null
            }, {
                "mData": "updateUser"
            }, {
                "mData": "updateTime"
            }],
            "columnDefs": [{
                "render": function (data, type, row, meta) {
                    var dom = '<form method="post" enctype="multipart/form-data">';
                    dom += '<div class="addPhoto">';
                    if (row.annexUrl && row.annexUrl != '') {
                        dom += '<p class="c-666 dropPhoto" style="display:none;">点击 <a href="javascript:;" onclick="addPhoto(this)">添加</a>或将图片拖拽到此处上传</p>';
                        dom += '<input type="file" class="getPhoto" name="annexFile" style="display: none">';
                        dom += '<div>';
                        dom += '<span onclick="openPhoto(event,this)"><img src="' + _FILE_URL + row.annexUrl + '" onerror="nofind(this)" /></span>';
                        dom += '<i class="iconfont icon-shanchu deletePhoto2 deletePhoto2" onclick="deletePhoto2(this)" ></i>';
                        dom += '</div>';
                    } else {
                        dom += '<p class="c-666 dropPhoto">点击 <a href="javascript:;" onclick="addPhoto(this)">添加</a>或将图片拖拽到此处上传</p>';
                        dom += '<input type="file" class="getPhoto" name="annexFile" style="display: none">';
                        dom += '<div>';
                        dom += '<span></span>';
                        dom += '<i class="iconfont icon-shanchu deletePhoto2 deletePhoto2" onclick="deletePhoto2(this)"></i>';
                        dom += '</div>';
                    }
                    dom += '<input type="hidden" name="annexUrl" value="' + (row.annexUrl ? row.annexUrl : '') + '" />';
                    dom += '<input type="hidden" name="annexId" value="' + row.annexId + '" />';
                    dom += '<input type="hidden" name="empId" value="' + row.empId + '" />';
                    dom += '</div>';
                    dom += '</form>';
                    return dom;
                },
                "targets": 1
            }, {
                "render": function (data, type, row, meta) {
                    var dom = '';
                    if ('1' === row.isRequire) {
                        dom = '<i class="Hui-iconfont Hui-iconfont-xuanze"></i>';
                    } else {
                        dom = '<i class="Hui-iconfont Hui-iconfont-close"></i>';
                    }
                    return dom;
                },
                "targets": 2
            }],
            "drawCallback": function (oSetting, json) {
                //上传图片
                document.documentElement.addEventListener('dragover', function (e) {
                    e.preventDefault();
                });
                document.documentElement.addEventListener('drop', function (e) {
                    e.preventDefault();
                });

                $('.addPhoto img').on('load', function () {
                    var height = this.height;
                    var width = this.width;
                    if (height > width) {
                        $(this).css('height', '60px');
                    } else {
                        $(this).css('width', '60px');
                    }
                });

                $('.dropPhoto').on('drop', function (e) {
                    var formData = new FormData($(this).closest('form')[0]);
                    if(!formData.get('annexFile')){
                        layer.msg('该浏览器不支持拖拽上传，请使用点击上传', {
                            icon: 2,
                            time: 2000
                        });
                        return
                    }
                    e = e.originalEvent;
                    var box = $(this).siblings('div').find('span');
                    upPhoto(box, e);
                    $(this).hide().siblings('div').find('span,i').show();
                });

                $('.getPhoto').on('change', function () {
                    upPhoto($(this).siblings('div').find('span'), this);
                    $(this).siblings('p').hide().siblings('div').find('span,i').show();
                });
            }
        });

        $("#bt_submit").click(function () {
            layer_close();
        });
    });

    //    图片放大预览
    function openPhoto(e,obj) {
        e.stopPropagation();
        var src = $(obj).find('img').attr('src');
        $('#photoPreview').fadeIn(500).find('img').attr('src', src);
    }

    $(document).on('click',function () {
        $('#photoPreview').fadeOut(500)
    });

    function addPhoto(obj, url, annexType) {
        $(obj).parent().siblings('.getPhoto').click();
    }

    function upPhoto(box, e) {
        var file;
        if (e.dataTransfer) {
            file = e.dataTransfer.files[0];
        } else {
            file = e.files[0];
        }
        var type = file.type;
        var fr = new FileReader();
        fr.readAsDataURL(file);

        fr.addEventListener('load', function () {
            var result = fr.result;
            if (type.indexOf('image/') > -1) {
                createPhoto(box, result);
                ajaxFile('/' + varUpDateUrl, box, '1', file);
            } else {
                layer.msg('请选择正确的图片文件格式！', {
                    icon: 1,
                    time: 1000
                });
            }
        });
    }

    //    删除图片
    function deletePhoto2(obj) {
        $(obj).hide();
        $(obj).siblings('span').html('');
        $(obj).parent().siblings('p').show();
        $(obj).parent().siblings('input[name="annexFile"]').val(null);

        ajaxFile('/' + varDeleteUrl, obj, '2');
    }

    function createPhoto(box, imgSrc) {
        $(box).html('');
        var img = document.createElement('img');
        $(img).on('load', function () {
            var height = this.height;
            var width = this.width;
            if (height > width) {
                $(img).css('height', '60px');
            } else {
                $(img).css('width', '60px');
            }
        });
        img.src = imgSrc;
        $(box).html(img);
    }

    function ajaxFile(url, obj, type, file) {
        var $form = $(obj).closest('form');

        var success = function (data) {
            if ('00' == data.code) {
                if ('1' == type) {
                    layer.msg('附件上传成功', {
                        icon: 1,
                        time: 1000
                    });
                } else {
                    layer.msg('附件删除成功', {
                        icon: 1,
                        time: 1000
                    });
                }
                annexListTable.fnDraw(true);
            }
        };

        if (window.FormData) {
            var formData = new FormData($form[0]);
            if (type === '1' && !formData.get('annexFile').size) {
                formData.set('annexFile', file);
            }

            $.ajax({
                type: 'post',
                url: url,
                data: formData,
                dataType: 'json',
                processData: false,
                contentType: false,
                success: success
            });
        } else {
            $form.ajaxSubmit({
                type: 'post',
                dataType: 'json',
                url: url,
                success: success
            });
        }

    }

    function nofind(t){
        var errorPng="/images/ucnter/avatar-default-S.gif"
        // if(window.location.href.indexOf('bms')==-1){
        //     errorPng="/images/ucnter/avatar-default-S.gif"
        // }
        t.src=errorPng;
        t.onerror=null;
    }