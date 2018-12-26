$(function () {
    var learnId= $('.learnId').html();
    var url = '/certificate/getStudyInfo.do' + '?learnId=' + learnId;
    var printUrl = '/certificate/printView.do';
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
            gradeT=gradeT.substring(0,4)+'年'+gradeT.substring(5,6)+'月';
            gradeT=ArabiSimplified(gradeT);

            var gradeYear=ArabiSimplified(grade-1)+'年';
            var unvs=res.unvs_name;
            var pfsn_name=res.pfsn_name;
            var pfsn_level=res.pfsn_level;
            var recruit_type=res.recruit_type;

            var time=new Date(res.now).format('yyyy年MM月dd');
            var year=parseInt(time.substring(0,4));
            var mouth=parseInt(time.substring(5,7)) ;
            var day=parseInt(time.substring(8,10));
            var timeZ=ArabiSimplified(year)+'年'+ArabiSimplifiedTwo(mouth)+'月'+ArabiSimplifiedTwo(day)+'日'

            time=ArabiSimplified(time)+'日';
            console.log(timeZ);

            if(recruit_type==1){
                dom="兹有学生 "+name+'，'+sex+'，身份证号码：<span class="idCard">'+idCard+'</span>，'+'于'+gradeYear+'参加成人高考，'+'被'+unvs+'继续教育学院录取，'+'于'+gradeZ+'三月就读 '+pfsn_level+' 层次'+pfsn_name+'专业。';
            }
            if(recruit_type==2){
                dom="兹有学生"+name+'，'+sex+'，身份证号码：<span class="idCard">'+idCard+'</span>，'+'被'+unvs+'录取，'+'于'+gradeT+'就读 '+pfsn_level+' 层次'+pfsn_name+'专业。';
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