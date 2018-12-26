$(function () {

    $("select").select2({
        placeholder: '--请选择--'
    })

    //初始化年度下拉框
    _init_select("year", dictJson.year);

    //初始化年级下拉框
    _init_select("grade", dictJson.grade);
    _init_select("pfsnLevel", dictJson.pfsnLevel);
    //初始化学期
    _init_select("semester", dictJson.semester);

    //初始化院校名称下拉框
    _simple_ajax_select({
        selectId: "unvsId",
        searchUrl: '/bdUniversity/findAllKeyValue.do',
        sData: {},
        showText: function (item) {
            return item.unvs_name;
        },
        showId: function (item) {
            return item.unvs_id;
        },
        placeholder: '--请选择--'
    });

    $("#semester").change(function () {
        initCourse();
    })

    $("#unvsId").append(new Option("", "", false, true));

    $("#unvsId").change(function () {
        $("#pfsnId").removeAttr("disabled");
        init_pfsn_select();
        initCourse();
        $("#unvsName").val($(this).find('>option:selected').text());
    });
    $("#grade").change(function () {
        $("#pfsnId").removeAttr("disabled");
        init_pfsn_select();
    });
    $("#pfsnLevel").change(function () {
        $("#pfsnId").removeAttr("disabled");
        init_pfsn_select();
        initCourse();
    });
    $("#pfsnId").append(new Option("", "", false, true));
    $("#pfsnId").select2({
        placeholder: "--请先选择院校--"
    });

    $("#pfsnId").change(function () {
        $("#courseId").removeAttr("disabled");
        initCourse();
    })

    $("#courseId").change(function () {
        $("#courseName").val($(this).find('>option:selected').text());
    })

    $("#form-room-assign-edit").validate({
        rules: {
            year: {
                required: true,
            },
            semester: {
                required: true,
            },
            grade: {
                required: true,
            },
            unvsId: {
                required: true,
            },
            pfsnLevel: {
                required: true,
            },
            pfsnId: {
                required: true,
            },
            courseId: {
                required: true
            }
        },
        onkeyup: false,
        focusCleanup: true,
        success: "valid",
        submitHandler: function (form) {
            var url = '/course/teachTaskBookExport.do?unvsName=';
            $('<form method="post" action="' + url + $("#unvsName").val() + '&pfsnName=' + $("#pfsnId>option:selected").text() + '&courseName=' + $("#courseId>option:selected").text() + '">'
                + '<input type="text" name="unvsId" value="' + $("#unvsId").val() + '"/>'
                + '<input type="text" name="pfsnId" value="' + $("#pfsnId").val() + '"/>'
                + '<input type="text" name="courseId" value="' + $("#courseId").val() + '"/>'
                + '<input type="text" name="grade" value="' + $("#grade").val() + '"/>'
                + '<input type="text" name="pfsnLevel" value="' + $("#pfsnLevel").val() + '"/>'
                + '<input type="text" name="semester" value="' + $("#semester").val() + '"/>'
                + '<input type="text" name="year" value="' + $("#year").val() + '"/>'
                + '</form>').appendTo('body').submit().remove();
        }
    });
});

function init_pfsn_select() {
    _simple_ajax_select({
        selectId: "pfsnId",
        searchUrl: '/baseinfo/sPfsn.do',
        sData: {
            sId: function () {
                return $("#unvsId").val() ? $("#unvsId").val() : '';
            },
            ext1: function () {
                return $("#pfsnLevel").val() ? $("#pfsnLevel").val() : '';
            },
            ext2: function () {
                return $("#grade").val() ? $("#grade").val() : '';
            }
        },
        showText: function (item) {
            var text = '(' + item.pfsnCode + ')' + item.pfsnName;
            text += '[' + _findDict('pfsnLevel', item.pfsnLevel) + ']';
            return text;
        },
        showId: function (item) {
            return item.pfsnId;
        },
        placeholder: '--请选择专业--'
    });
    $("#pfsnId").append(new Option("", "", false, true));
}

function initCourse() {
    _simple_ajax_select({
        selectId: "courseId",
        searchUrl: '/course/getCourseByPfsnId.do',
        sData: {
            pfsnId: function () {
                return $("#pfsnId").val() ? $("#pfsnId").val() : '';
            },
            semester: function () {
                return $("#semester").val() ? $("#semester").val() : '';
            }
        },
        showText: function (item) {
            return item.course_name;
        },
        showId: function (item) {
            return item.course_id;
        },
        placeholder: '--请选择--'
    });
}