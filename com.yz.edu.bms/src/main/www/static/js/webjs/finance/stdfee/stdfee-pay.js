function submitClick(type){
    $("#submitType").val(type);
    $("#form-std-pay").submit();
}

function getCoupons() {
    $.ajax({
        url: "/stdFee/stdCoupon.do",
        data: {
            learnId: $("#learnId").val(),
            page:1,
            rows:999
        },
        dataType : 'json',
        success: function (data) {
            if (data.code == _GLOBAL_SUCCESS) {
                var couponData=data.body;
                for(var i=0;i<couponData.length;i++){
                    var conpon={
                        amount:couponData[i].amount,
                        couponId:couponData[i].couponId,
                        couponName:couponData[i].couponName,
                        itemCodes:couponData[i].itemCodes,
                        scId:couponData[i].scId,
                    }
                    conpon=JSON.stringify(conpon)
                    var dom='<div class="couponDiv">';
                    dom+='<label for="couponId_'+i+'" class="checkBox" title="'+couponData[i].couponName+'">';
                    dom+='<input type="checkbox"  class="checkBox-input" value='+conpon+' id="couponId_'+i+'">';
                    dom+='<span class="checkBox-span"></span>'+couponData[i].couponName+'</label>';
                    dom+='</div>';
                    $("#couponId").append(dom);
                }
            }
        }
    });
}

// 勾选缴费科目事件
function yearChange(obj) {
    if ($(obj).prop('checked')) {
        $(obj).parent().siblings(".yzStudyPayInfo-count").find("input[type='checkbox']").each(function (i, e) {
            $(e).prop('checked', true);
            // 重新计算总共待缴
            checkItem();
        });
    } else {
        $(obj).parent().siblings(".yzStudyPayInfo-count").find("input[type='checkbox']").each(function (i, e) {
            $(e).prop('checked', false);
            // 重新计算总共待缴
            checkItem();
        });
    }

    // // 过滤可选优惠券
    $('.couponDiv').removeClass('show');
    $("#couponId .checkBox-input").each(function (i,e) {
        // debugger
        var code=JSON.parse($(e).val()).itemCodes;
        $('input:checkbox[name=itemCodes]:checked').each(function (n,v) {
            var itemCode = $(v).val();
            if(code.includes(itemCode)){
                // $(e).attr('disabled',true);
                $(e).parents('.couponDiv').addClass('show')
            }
        });
    });
    $("#couponCount").text($('.couponDiv.show').length)
}

function drawTable(rows, dom, chkId,grade) {
    for (var i = 0; i < rows.length; i++) {
        var payInfo = rows[i];
        /* dom += '<tr>';
         if(i==0){
         dom += '<td rowspan="'+rows.length+'" class="text-c"><input type="checkbox" name="years" onchange="yearChange(this)"/></td>';
         dom += '<td rowspan="'+rows.length+'">'
         dom += _findDict("itemYear",payInfo.year);
         dom += '</td>';
         }
         dom += '<td><input type="checkbox" style="display:none" onchange="checkItem()" value="'+ payInfo.itemCode + '" name="itemCodes"/><input type="hidden" id="'+ payInfo.itemCode +'" value="'+ payInfo.payable + '" />' + payInfo.itemCode + ':' + payInfo.itemName + '</td>';
         dom += '<td>' + payInfo.payable + '</td>';
         dom += '</tr>'; */
        if (i == 0) {
            var itemYear = '';
            if (payInfo.itemYear != null && payInfo.itemYear != '') {
                if('201803' == grade || '201703' == grade || '201709' == grade){
                    itemYear = _findDict("semester", payInfo.itemYear);
                }else{
                    itemYear = _findDict("itemYear", payInfo.itemYear);
                }
            }
            dom += '<div class="yzStudyPayInfo-m-c">';
            dom += '<div class="yzStudyPayInfo-check">';
            dom += '<input type="checkbox" class="YZSDcheck" id="check_' + chkId + '" name="years" value="' + payInfo.itemYear + '" onchange="yearChange(this)"/>';
            dom += '<label for="check_' + chkId + '" style="cursor: pointer"></label>';
            dom += '</div>';


            dom += ' <div class="yzStudyPayInfo-year">' + itemYear + '</div>';
            dom += ' <div class="yzStudyPayInfo-count">';
        }

        var itemName = getItemName(payInfo.itemName,grade);

        dom += '<div class="yzStudyPayInfo-count-1 cl">';
        dom += '<div><input type="checkbox" style="display:none" onchange="checkItem()" value="' + payInfo.itemCode + '" name="itemCodes"/><input type="hidden" id="' + payInfo.itemCode + '" value="' + payInfo.payable + '" />' + payInfo.itemCode + ':' + itemName + '</div>';
        dom += '<div>' + payInfo.payable + '</div>';
        dom += '</div>';
        var x = rows.length - 1;
        if (i == x) {
            dom += '</div>';
            dom += '</div>';
        }

    }
    return dom;
}

function initItemDom() {
    var dom = '';
    var tutor = row.tutorPayInfos;
    var first = row.firstPayInfos;
    var second = row.secondPayInfos;
    var third = row.thirdPayInfos;
    var four = row.fourPayInfos;
    var other = row.otherPayInfos;

    var grade = row.grade;

    if (tutor != null && tutor.length > 0) {
        dom = drawTable(tutor, dom, 'tutor',grade);
    }
    if (first != null && first.length > 0) {
        dom = drawTable(first, dom, 'first',grade);
    }
    if (second != null && second.length > 0) {
        dom = drawTable(second, dom, 'second',grade);
    }
    if (third != null && third.length > 0) {
        dom = drawTable(third, dom, 'third',grade);
    }
    if (four != null && four.length > 0) {
        dom = drawTable(four, dom, 'four',grade);
    }
    if (other != null && other.length > 0) {
        dom = drawTable(other, dom, 'other',grade);
    }

    $("#feeItem").append(dom);
}


function checkItem() {
    // 计算总共待缴
    var amount = 0;
    $('input:checkbox[name=itemCodes]:checked').each(function (i) {
        var itemCode = $(this).val();
        amount += parseFloat($("#" + itemCode + "").val());
    });
    amount = amount.toFixed(2);
    $("#payableAmount").text(amount);
    countAmount();
}

function countAmount() {

    // 总共待缴
    var itemAmount = $("#payableAmount").text();
    // 滞留账户
    var accDeduction = $("#accDeduction").val();
    var zmScale = bmsParamJson['yz.zhimi.scale'];	// 智米抵扣比例
    // 智米
    var zmDeduction = $("#zmDeduction").val() / zmScale;
    var payable = itemAmount * 1 - zmDeduction * 1 - accDeduction * 1;
    payable = payable.toFixed(2);
    if (payable > 0) {
        // 实付金额
        $("#paidAmount").val(payable);
        $("#payableCount").val(payable);
    } else {
        $("#paidAmount").val('0.00');
        $("#payableCount").val('0.00');
    }
    couponChange();
}



function couponChange() {
    // 优惠券单选
    // 优惠券id
    // var couponId = $("#couponId").val();
    // if (couponId != null && couponId != '') {
    //     $.ajax({
    //         type: "POST",
    //         url: "/stdFee/couponAmount.do",
    //         data: {
    //             couponId: couponId
    //         },
    //         dataType : 'json',
    //         success: function (data) {
    //             if (data.code == _GLOBAL_SUCCESS) {
    //                 var coupon = data.body;
    //                 var amount = coupon.amount;
    //                 var couponAmount = 0.00;
    //                 var $chkItems = $("input[name=itemCodes]:checked");
    //
    //                 // 遍历优惠券
    //                 for (var i = 0; i < coupon.itemCodes.length; i++) {
    //                     // 获取优惠券code
    //                     var itemCode = coupon.itemCodes[i];
    //                 // 遍历科目
    //                     $chkItems.each(function () {
    //                         // 获取科目code
    //                         var chkItem = $(this).val();
    //                         if (chkItem == itemCode) {
    //                             var chkAmount = $("#" + chkItem).val();
    //                             if(amount * 1 > 0){
    //                             	// 如果待缴大于优惠券金额
    //                                if (chkAmount * 1 > amount * 1) {
    //                                	couponAmount = couponAmount + (amount * 1).toFixed(2) * 1;
    //                                	amount = 0;
    //                                } else {
    //                                	couponAmount = couponAmount + (chkAmount * 1).toFixed(2) * 1;
    //                                    amount = (amount * 1 - chkAmount * 1).toFixed(2) * 1;
    //                                }
    //                             }
    //                         }
    //                     });
    //                 }
    //                 // 待缴金额
    //                 var itemAmount = $("#payableAmount").text();
    //                 // 滞留金额
    //                 var accDeduction = $("#accDeduction").val();
    //                 var zmScale = bmsParamJson['yz.zhimi.scale'];	// 智米抵扣比例
    //                 // 智米金额
    //                 var zmDeduction = $("#zmDeduction").val() / zmScale;
    //
    //                 // 实付金额
    //                 var payable = itemAmount * 1 - accDeduction * 1 - zmDeduction*1 - couponAmount;
    //                 if (payable > 0) {
    //                     $("#paidAmount").val(payable.toFixed(2));
    //                     $("#payableCount").val(payable.toFixed(2));
    //                 } else {
    //                     $("#paidAmount").val('0.00');
    //                     $("#payableCount").val('0.00');
    //                 }
    //             }
    //         }
    //     });
    // } else {
    // }

    // 优惠券多选
    // 选中的优惠项
    var selectedCoupons = [];
    $(".couponDiv.show input:checked").each(function (i,e) {
        var value=$(e).val();
        var coupons = value=JSON.parse(value);
        selectedCoupons.push(value)
    });
    // 收费项
    var selectedItem = [];
    $("input[name=itemCodes]:checked").each(function (i,e) {
        var siblings=$(e).siblings('input');
        var itemCodes={
            payable:siblings.val(),
            itemCode:siblings.attr('id'),
        }
        selectedItem.push(itemCodes)
    });
    // 排序后的收费项目
    selectedItem=getParInfos(selectedItem)

    var setCouponsDetails=[];
    var couponAmount=0;
    // 设置抵扣详细
    setDeduction(selectedCoupons,selectedItem,setCouponsDetails);

    $("#coupons").val(JSON.stringify(this_couponsDetail));

    // 计算抵扣金额
    couponAmount=deduction();
    // 待缴金额
    var itemAmount = $("#payableAmount").text();
    // 滞留金额
    var accDeduction = $("#accDeduction").val();
    var zmScale = bmsParamJson['yz.zhimi.scale'];	// 智米抵扣比例
    // 智米金额
    var zmDeduction = $("#zmDeduction").val() / zmScale;
    var payable = itemAmount * 1 - accDeduction * 1 - zmDeduction*1 - couponAmount;

    if (payable > 0) {
        $("#paidAmount").val(payable.toFixed(2));
        $("#payableCount").val(payable.toFixed(2));
    } else {
        $("#paidAmount").val('0.00');
        $("#payableCount").val('0.00');
    }
}

$(function () {
    var recruitType = row.recruitType;
    var stdName = row.stdName;
    var grade = row.grade;
    var unvsName = row.unvsName;
    var pfsnCode = row.pfsnCode;
    var pfsnName = row.pfsnName;
    var pfsnLevel = row.pfsnLevel;

    // 优惠详情
    var this_couponsDetail=[];

    var stdInfo = stdName + ' - ' + grade + '级 - [' + _findDict("recruitType", recruitType) + ']' + unvsName + ':(' + pfsnCode + ')' + pfsnName + '[' + _findDict("pfsnLevel", pfsnLevel) + ']';

    $("#stdInfo").text(stdInfo);

    $("#paymentType").select2({
        placeholder: "--请选择--",
        allowClear: true
    });

    // _simple_ajax_select({
    //     selectId: "couponId",
    //     searchUrl: "/stdFee/stdCoupon.do",
    //     sData: {
    //         learnId: function () {
    //             return $("#learnId").val();
    //         }
    //     },
    //     showText: function (item) {
    //         var text = item.couponName;
    //         return text;
    //     },
    //     showId: function (item) {
    //         return item.couponId;
    //     },
    //     placeholder: '--请选择--'
    // });

    getCoupons();

    // 初始化缴费科目
    initItemDom();

    //    全选事件
    $('.YZSDcheckAll').on('change', function () {
        var isChecked = $('.YZSDcheckAll').prop('checked');
        $('.YZSDcheck').each(function (i, e) {
            var checked = $(e).prop('checked');
            if (checked != isChecked) {
                $(e).click();
            }
        });
    })

    //  缴费复选事件
    $('.YZSDcheck').on('change', function () {
        if ($('.YZSDcheck:checked').length >= $('.YZSDcheck').length) {
            $('.YZSDcheckAll').prop('checked', true);
        } else {
            $('.YZSDcheckAll').prop('checked', false);
        }
    });

    //        动态设置高度
    $('.yzStudyPayInfo-m-c').each(function (i, e) {

        var yzStudyPayInfo_m_c_height = $(e).find('.yzStudyPayInfo-count').children().length * 40;
        $(e).css('height', yzStudyPayInfo_m_c_height).find('.yzStudyPayInfo-year').css('line-height', yzStudyPayInfo_m_c_height + 'px').siblings('.yzStudyPayInfo-check').css('line-height', yzStudyPayInfo_m_c_height + 3 + 'px')
    })

    var itemDom = '';

    getCouponCount($("#learnId").val());

    // 优惠券选项框改变
    $("#couponId").on('change','.checkBox-input',function () {
        couponChange();
    })

    $("#form-std-pay").validate({
        rules: {
            accDeduction: {
                min: 0,
                max: function () {
                    return parseFloat($("#accAmount").text());
                },
                isStdFee: true
            },
            zmDeduction: {
                min: 0,
                max: function () {
                    return parseFloat($("#zmAmount").text());
                },
                digits: true
            },
            paidAmount: {
                min: function () {
                    return parseFloat($("#payableCount").val());
                },
                isStdFee: true
            },
            itemCodes: {
                required: true
            },
            paymentType: {
                required: true
            }
        },
        messages: {},
        onkeyup: false,
        focusCleanup: true,
        success: "valid",
        submitHandler: function (form) {
            var type = $("#submitType").val();
            var url = '';

            if('1' == type){
                url = "/stdFee/pay.do";
            } else if('2' == type){
                url = "/stdFee/payAndPrint.do";
            } else {
                return ;
            }

            var itemLength = $(".yzStudyPayInfo-check :input[type='checkbox']:checked").length;
            if(itemLength <= 0){
                layer.msg('缴费科目不能为空', {icon: 5, time: 1000}, function () {
                });
                return ;
            }
            var printUrl = '';
            $(form).ajaxSubmit({
                type: "post", //提交方式
                dataType: "json", //数据类型
                url: url, //请求url
                // contentType:"application/json",
                success: function (data) { //提交成功的回调函数
                    if (data.code == _GLOBAL_SUCCESS) {
                        layer.msg('缴费成功！', {icon: 1, time: 1000}, function () {
                            var type = $("#submitType").val();
                            if('2' == type){
                                printUrl = "/rept/reptPrint.do";
                                printUrl += '?reptId=' + data.body.reptId;
                                window.location.href= printUrl;
                            }else{
                                window.parent.myDataTable.fnDraw(false);
                                layer_close();
                            }
                        });
                    }
                }
            });
        }
    });
});


function getCouponCount(learnId){
    var url = "/stdFee/stdCouponCount.do";

    $.ajax({
        type: "POST",
        url: url,
        data: {
            learnId: learnId
        },
        dataType : 'json',
        success: function (data) {
            if (data.code == _GLOBAL_SUCCESS) {
                // $("#couponCount").text(data.body);
            }
        }
    });
}



function _toConsumableArray(arr) { if (Array.isArray(arr)) { for (var i = 0, arr2 = Array(arr.length); i < arr.length; i++) { arr2[i] = arr[i]; } return arr2; } else { return Array.from(arr); } }

// 计算抵扣值
function deduction() {
    var count = 0;
    if (this_couponsDetail.length > 0) {
        var _iteratorNormalCompletion = true;
        var _didIteratorError = false;
        var _iteratorError = undefined;

        try {
            for (var _iterator = this_couponsDetail[Symbol.iterator](), _step; !(_iteratorNormalCompletion = (_step = _iterator.next()).done); _iteratorNormalCompletion = true) {
                var item = _step.value;
                var _iteratorNormalCompletion2 = true;
                var _didIteratorError2 = false;
                var _iteratorError2 = undefined;

                try {
                    for (var _iterator2 = item.couponDetails[Symbol.iterator](), _step2; !(_iteratorNormalCompletion2 = (_step2 = _iterator2.next()).done); _iteratorNormalCompletion2 = true) {
                        var detail = _step2.value;

                        count += Number(detail.price);
                    }
                } catch (err) {
                    _didIteratorError2 = true;
                    _iteratorError2 = err;
                } finally {
                    try {
                        if (!_iteratorNormalCompletion2 && _iterator2.return) {
                            _iterator2.return();
                        }
                    } finally {
                        if (_didIteratorError2) {
                            throw _iteratorError2;
                        }
                    }
                }
            }
        } catch (err) {
            _didIteratorError = true;
            _iteratorError = err;
        } finally {
            try {
                if (!_iteratorNormalCompletion && _iterator.return) {
                    _iterator.return();
                }
            } finally {
                if (_didIteratorError) {
                    throw _iteratorError;
                }
            }
        }
    }
    return count;
}

// 优惠券抵扣详细
function setDeduction(this_selectedCoupons, this_selectedItem) {
    this_couponsDetail = []; // 清空抵扣详细
    var parInfos = this_selectedItem; //排序好的收费项目
    var coupons = this_selectedCoupons.map(function (item) {
        return {
            couponId: item.couponId,
            scId: item.scId,
            amount: Number(item.amount),
            itemCodes: [].concat(_toConsumableArray(item.itemCodes))
        };
    });

    var _iteratorNormalCompletion3 = true;
    var _didIteratorError3 = false;
    var _iteratorError3 = undefined;

    try {
        var _loop = function _loop() {
            var itemFee = _step3.value;

            var availableCoupons = coupons.filter(function (item) {
                return item.itemCodes.includes(itemFee.itemCode);
            });
            // 排序优惠券
            availableCoupons = availableCoupons.sort(function (a, b) {
                return a.itemCodes.length - b.itemCodes.length;
            });
            var _iteratorNormalCompletion4 = true;
            var _didIteratorError4 = false;
            var _iteratorError4 = undefined;

            try {
                for (var _iterator4 = availableCoupons[Symbol.iterator](), _step4; !(_iteratorNormalCompletion4 = (_step4 = _iterator4.next()).done); _iteratorNormalCompletion4 = true) {
                    var itemCoupon = _step4.value;

                    // 如果优惠券金额大于等于实付金额
                    if (Number(itemCoupon.amount) >= Number(itemFee.payable)) {
                        setCouponsDetail(itemCoupon.couponId, itemCoupon.scId, itemFee.itemCode, itemFee.payable);
                        itemCoupon.amount -= itemFee.payable;
                        itemFee.payable = 0;

                        if (itemCoupon.amount === 0) {
                            coupons.splice(coupons.indexOf(itemCoupon), 1);
                        }

                        // 过滤剩下优惠的当前科目code值
                        coupons = coupons.map(function (item) {
                            if (item.itemCodes.includes(itemFee.itemCode)) {
                                item.itemCodes.splice(item.itemCodes.indexOf(itemFee.itemCode), 1);
                            }
                            return item;
                        });
                        break;
                    } else {
                        setCouponsDetail(itemCoupon.couponId, itemCoupon.scId, itemFee.itemCode, itemCoupon.amount);
                        itemFee.payable -= itemCoupon.amount;
                        coupons.splice(coupons.indexOf(itemCoupon), 1);
                    }
                }
            } catch (err) {
                _didIteratorError4 = true;
                _iteratorError4 = err;
            } finally {
                try {
                    if (!_iteratorNormalCompletion4 && _iterator4.return) {
                        _iterator4.return();
                    }
                } finally {
                    if (_didIteratorError4) {
                        throw _iteratorError4;
                    }
                }
            }
        };

        for (var _iterator3 = parInfos[Symbol.iterator](), _step3; !(_iteratorNormalCompletion3 = (_step3 = _iterator3.next()).done); _iteratorNormalCompletion3 = true) {
            _loop();
        }
    } catch (err) {
        _didIteratorError3 = true;
        _iteratorError3 = err;
    } finally {
        try {
            if (!_iteratorNormalCompletion3 && _iterator3.return) {
                _iterator3.return();
            }
        } finally {
            if (_didIteratorError3) {
                throw _iteratorError3;
            }
        }
    }
}

// 记录优惠券使用详细
function setCouponsDetail(couponId, scId, itemCode, fee) {
    fee = Number(fee).toFixed(2);
    var couponDetail = this_couponsDetail.find(function (item) {
        return item.scId === scId;
    });
    if (couponDetail) {
        couponDetail.couponDetails.push({ itemCode: itemCode, price: fee });
    } else {
        var _couponDetail = [{ itemCode: itemCode, price: fee }];
        this_couponsDetail.push({ couponId: couponId, scId: scId, couponDetails: _couponDetail });
    }
};

// 获取缴费科目
function getParInfos(this_selectedItem) {
    var parInfos = this_selectedItem;
    // 排序，code为Y的排在最前面
    /*parInfos.sort((a, b) => {
      let val1 = a.itemCode.indexOf('Y') === 0 ? Number(a.itemCode.substr(1)) : 10;
      let val2 = b.itemCode.indexOf('Y') === 0 ? Number(b.itemCode.substr(1)) : 10;
      return val1 - val2;
    });*/
    return parInfos.filter(function (item) {
        return item.itemCode.indexOf('Y') === 0;
    }).concat(parInfos.filter(function (item) {
        return item.itemCode.indexOf('Y') !== 0;
    }));
}
