$(function () {
    var learnId= $('.learnId').html();
    var url = '/certificate/getStudyInfoZS.do' + '?learnId=' + learnId;
    var printUrl = '/certificate/printViewZS.do';
    $.ajax({
        type: 'POST',
        url: url,
        dataType: 'json',
        success: function (data) {
            var res=data.body;

            var dom="";
            var name=res.std_name;
            var idCard=res.id_card;
            var sex=res.sex;
            var grade=res.grade;
            var gradeZ=ArabiSimplified(grade)+'年';
            var gradeT=grade+'';
            gradeT='<span>'+gradeT.substring(0,4)+' </span>年'+gradeT.substring(5,6)+'月';
            // gradeT=ArabiSimplified(gradeT);
            var payTime=new Date(res.pay_time).format('yyyy年MM月dd');
            payTime='<span>'+payTime.substring(0,4)+' </span>年'+payTime.substring(5,7)+'月';
            // console.log(payTime);

            var gradeYear='<span>'+(grade-1)+' </span>年';
            var unvs=res.unvs_name;
            var pfsn_name=res.pfsn_name;
            var pfsn_level=res.pfsn_level;
            var recruit_type=res.recruit_type;

            var time=new Date(res.now).format('yyyy年MM月dd');
            var year=parseInt(time.substring(0,4));
            var mouth=parseInt(time.substring(5,7)) ;
            var day=parseInt(time.substring(8,10));
            var timeZ='<span>'+year+' </span>年'+mouth+'月'+day+'日'

            time=ArabiSimplified(time)+'日';
            // console.log(timeZ);

            if(recruit_type==1){
                dom="兹有学生 "+name+'，'+sex+'，身份证号码为：<span class="idCard">'+idCard+'</span>，'+'于'+payTime+'报考成人高考，'+'报读院校为'+unvs+'，'+pfsn_name+'专业。'+pfsn_level+'层次。';
            }
            if(recruit_type==2){
                dom="兹有学生"+name+'，'+sex+'，身份证号码为：<span class="idCard">'+idCard+'</span>，'+'于'+payTime+'报考开放教育，'+'报读院校为'+unvs+'，'+pfsn_name+'专业。'+pfsn_level+'层次。';
            }

            $('.info').html(dom);
            $('.time').html(timeZ);

            window.print();
        }
    });
});

function ArabiSimplified(Num) {
    for (var i = Num.length - 1; i >= 0; i--) {
        Num = Num.replace(",", "")//替换Num中的“,”
        Num = Num.replace(" ", "")//替换Num中的空格
    }
    // if (isNaN(Num)) { //验证输入的字符是否为数字
    //     //alert("请检查小写金额是否正确");
    //     return;
    // }
    //字符处理完毕后开始转换，采用前后两部分分别转换
    var part = String(Num).split(".");
    var newchar = "";
    //小数点前进行转化
    for (var i = part[0].length - 1; i >= 0; i--) {
        if (part[0].length > 10) {
            //alert("位数过大，无法计算");
            return "";
        }//若数量超过拾亿单位，提示
        tmpnewchar = ""
        perchar = part[0].charAt(i);
        switch (perchar) {
            case "0":  tmpnewchar = "零" + tmpnewchar;break;
            case "1": tmpnewchar = "一" + tmpnewchar; break;
            case "2": tmpnewchar = "二" + tmpnewchar; break;
            case "3": tmpnewchar = "三" + tmpnewchar; break;
            case "4": tmpnewchar = "四" + tmpnewchar; break;
            case "5": tmpnewchar = "五" + tmpnewchar; break;
            case "6": tmpnewchar = "六" + tmpnewchar; break;
            case "7": tmpnewchar = "七" + tmpnewchar; break;
            case "8": tmpnewchar = "八" + tmpnewchar; break;
            case "9": tmpnewchar = "九" + tmpnewchar; break;
            case "年": tmpnewchar = "年" + tmpnewchar; break;
            case "月": tmpnewchar = "月" + tmpnewchar; break;
            case "日": tmpnewchar = "日" + tmpnewchar; break;
        }
        newchar = tmpnewchar + newchar;
    }

    //替换以“一十”开头的，为“十”
    return newchar;
}

function ArabiSimplifiedTwo(Num) {
    for (var i = Num.length - 1; i >= 0; i--) {
        Num = Num.replace(",", "")//替换Num中的“,”
        Num = Num.replace(" ", "")//替换Num中的空格
    }
    if (isNaN(Num)) { //验证输入的字符是否为数字
        //alert("请检查小写金额是否正确");
        return;
    }
    //字符处理完毕后开始转换，采用前后两部分分别转换
    var part = String(Num).split(".");
    var newchar = "";
    //小数点前进行转化
    for (var i = part[0].length - 1; i >= 0; i--) {
        if (part[0].length > 10) {
            //alert("位数过大，无法计算");
            return "";
        }//若数量超过拾亿单位，提示
        tmpnewchar = ""
        perchar = part[0].charAt(i);
        switch (perchar) {
            case "0":  tmpnewchar = "零" + tmpnewchar;break;
            case "1": tmpnewchar = "一" + tmpnewchar; break;
            case "2": tmpnewchar = "二" + tmpnewchar; break;
            case "3": tmpnewchar = "三" + tmpnewchar; break;
            case "4": tmpnewchar = "四" + tmpnewchar; break;
            case "5": tmpnewchar = "五" + tmpnewchar; break;
            case "6": tmpnewchar = "六" + tmpnewchar; break;
            case "7": tmpnewchar = "七" + tmpnewchar; break;
            case "8": tmpnewchar = "八" + tmpnewchar; break;
            case "9": tmpnewchar = "九" + tmpnewchar; break;
        }
        switch (part[0].length - i - 1) {
            case 0: tmpnewchar = tmpnewchar; break;
            case 1: if (perchar != 0) tmpnewchar = tmpnewchar + "十"; break;
            case 2: if (perchar != 0) tmpnewchar = tmpnewchar + "百"; break;
            case 3: if (perchar != 0) tmpnewchar = tmpnewchar + "千"; break;
            case 4: tmpnewchar = tmpnewchar + "万"; break;
            case 5: if (perchar != 0) tmpnewchar = tmpnewchar + "十"; break;
            case 6: if (perchar != 0) tmpnewchar = tmpnewchar + "百"; break;
            case 7: if (perchar != 0) tmpnewchar = tmpnewchar + "千"; break;
            case 8: tmpnewchar = tmpnewchar + "亿"; break;
            case 9: tmpnewchar = tmpnewchar + "十"; break;
        }
        newchar = tmpnewchar + newchar;
    }
    //替换所有无用汉字，直到没有此类无用的数字为止
    while (newchar.search("零零") != -1 || newchar.search("零亿") != -1 || newchar.search("亿万") != -1 || newchar.search("零万") != -1) {
        newchar = newchar.replace("零亿", "亿");
        newchar = newchar.replace("亿万", "亿");
        newchar = newchar.replace("零万", "万");
        newchar = newchar.replace("零零", "零");
    }
    //替换以“一十”开头的，为“十”
    if (newchar.indexOf("一十") == 0) {
        newchar = newchar.substr(1);
    }
    //替换以“零”结尾的，为“”
    if (newchar.lastIndexOf("零") == newchar.length - 1) {
        newchar = newchar.substr(0, newchar.length - 1);
    }
    return newchar;
}