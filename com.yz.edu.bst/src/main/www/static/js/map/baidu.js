/**
 * 在divName的div上创建中心点为(lon,lat)的地图控件
 */
var BaiduMap = function(divName,inputId,disableMapClick,lon,lat){
    var map;

    if(disableMapClick){
        map = new BMap.Map(divName, {enableMapClick:false});
    }else{
        map = new BMap.Map(divName);
    }

    if(lon != null && lat !=null && lon != "" && lon != ""){
        var point = new BMap.Point(lon,lat);  // 创建点坐标
        map.centerAndZoom(point, 15);         // 初始化地图，设置中心点坐标和地图级别
    }

    map.enableDoubleClickZoom();//启用鼠标双击放大，默认启用(可不写)
    map.enableDragging();//启用地图拖拽事件，默认启用(可不写)
    map.enableScrollWheelZoom();//启用地图滚轮放大缩小
    map.enableKeyboard();//启用键盘上下左右键移动地图

    map.addControl(new BMap.NavigationControl());
    map.addControl(new BMap.ScaleControl());
    map.addControl(new BMap.OverviewMapControl());
    map.addControl(new BMap.GeolocationControl());

    map.addEventListener("click",function(e){
        map.province = null;
        map.city = null;
        map.marker = null;
        map.clearOverlays();
        var point = new BMap.Point(e.point.lng,e.point.lat);
        var marker = new BMap.Marker(point);// 创建标注
        getPositionName(map,point,marker,inputId);
        map.addOverlay(marker);             // 将标注添加到地图中
        marker.disableDragging();           // 不可拖拽
        map.marker = marker;
        alert("当前点的坐标:("+point.lng+","+point.lat+")");
    });
    return map;
};



/**
 * 地图搜索
 * @param map       map句柄
 * @param place     地点字符串
 * @param resultPanel   结果集放置div的id
 */
var searchLocal = function(map,place,resultPanel){
    var local = new BMap.LocalSearch(map, {
        renderOptions:{
            map: map,
            autoViewport: true,
            panel: resultPanel
        },
        pageCapacity: 1
    });
    local.search(place);
};

/**
 * 获取选中的点的坐标(lng,lat)
 */
var getPointOfMarker = function (map) {
    var marker = map.marker;
    if(marker){
        return marker.point;
    }else{
        return null;
    }
};

/**
 * 获取某point的文字地址
 */
var getPositionName222 = function(map,point,marker,divName){
    var geoc = new BMap.Geocoder();
    geoc.getLocation(point, function(rs){
        var addComp = rs.addressComponents;
        var address = addComp.province + addComp.city + addComp.district + addComp.street + addComp.streetNumber;
        if(marker){
            var label = new BMap.Label(address,
                {offset:new BMap.Size(-20,-20)});
            marker.setLabel(label);
        }
        alert(address);
        if(divName){
            // $("#"+divName).val(address);
        }
        map.province = addComp.province;
        map.city = addComp.city;
    });
};


/**
 * 搜索从place1到place2的公交
 */
var searchTransit = function(map,place1,place2,resultPanel){
    var transit = new BMap.TransitRoute(map, {
        renderOptions: {map: map, panel: resultPanel}
    });
    transit.search(place1, place2);
};

/**
 * 导航方式:最短时间,最短距离,避免高速
 */
var routePolicy = [BMAP_DRIVING_POLICY_LEAST_TIME,
                    BMAP_DRIVING_POLICY_LEAST_DISTANCE,
                    BMAP_DRIVING_POLICY_AVOID_HIGHWAYS];

/**
 * 搜索从start到end的开车路线
 */
var searchDriving = function(map,start,end,policyIndex,resultPanel){
    map.clearOverlays();
    search(start,end,routePolicy[policyIndex]);
    function search(start,end,route){
        var driving = new BMap.DrivingRoute(map, {renderOptions:
        {map: map,panel: resultPanel, autoViewport: true},policy: route});
        driving.search(start,end);
    }
};

/**
 * 搜索从start到end的步行路线
 */
var searchWalk = function (map,start,end,resultPanel) {
    var walking = new BMap.WalkingRoute(map, {renderOptions:{map: map, panel: resultPanel, autoViewport: true}});
    walking.search(start,end);
}

/**
 * 添加用户自定义点到地图
 */
var addUserMarker = function(title,content,lon,lat){
    var markerArr = [{
        title:title,
        content:content,
        point:lon+'|'+lat,
        isOpen:1,
        icon:{w:23,h:25,l:46,t:21,x:9,lb:12}
    }];
    addMarkerToMap(markerArr)
};

//公司标注点
var mimiMark = [
    {
        title:"远智教育",
        content:"深圳市南山区深南大道9998号万利达大厦2210",
        point:"113.940322|22.546235",
        isOpen:1,icon:{w:23,h:25,l:46,t:21,x:9,lb:12}
    }];

/**
 * 添加用户的marker,mimiMarker已经可用
 */
var addMarkerToMap = function(markerArr){
    if(markerArr == "" || markerArr == null){
        markerArr = mimiMark;
    }
    for(var i=0;i<markerArr.length;i++){
        var json = markerArr[i];
        var p0 = json.point.split("|")[0];
        var p1 = json.point.split("|")[1];
        var point = new BMap.Point(p0,p1);
        var iconImg = createIcon(json.icon);
        var marker = new BMap.Marker(point,{icon:iconImg});
        var label = new BMap.Label(json.title,{"offset":new BMap.Size(json.icon.lb-json.icon.x+10,-20)});
        marker.setLabel(label);
        map.addOverlay(marker);
        label.setStyle({
            borderColor:"#808080",
            color:"#333",
            cursor:"pointer"
        });
        (function(){
            var _iw = createInfoWindow(i,markerArr);
            var _marker = marker;
            _marker.addEventListener("click",function(){
                this.openInfoWindow(_iw);
            });
            _iw.addEventListener("open",function(){
                _marker.getLabel().hide();
            })
            _iw.addEventListener("close",function(){
                _marker.getLabel().show();
            })
            label.addEventListener("click",function(){
                _marker.openInfoWindow(_iw);
            })
            if(!!json.isOpen){
                label.hide();
                _marker.openInfoWindow(_iw);
            }
        })()
    }
}

//创建InfoWindow
var createInfoWindow = function(i,markerArr){
    var json = markerArr[i];
    var iw = new BMap.InfoWindow("<b class='iw_poi_title' title='" + json.title + "'>" + json.title + "</b><div class='iw_poi_content'>"+json.content+"</div>");
    return iw;
}

//创建一个Icon
var createIcon = function(json){
    var icon = new BMap.Icon("http://app.baidu.com/map/images/us_mk_icon.png", new BMap.Size(json.w,json.h),{imageOffset: new BMap.Size(-json.l,-json.t),infoWindowOffset:new BMap.Size(json.lb+5,1),offset:new BMap.Size(json.x,json.h)})
    return icon;
}

