/**
 * author：xyzsyx@163.com 在使用中遇到问题或发现bug更或者有技术交流爱好的朋友请发邮件到我的email，大家一起学习，一起进步！
 *
 *
 * 初始化调用方法  在js的onload事件或jq的$(document).ready()里面调用stepBar.init(id, option)即可。
 * 第一个参数为stepBar容器的id，必填，允许传入的值包括如下：
 *     jQuery对象
 *     javascript对象
 *     DOM元素（可转化为ID的字符串，如 “stepBar” || “#stepBar”） 纠错：误把jQuery对象的“#”写成“.”也同样能识别出来，但是必须保证次参数能转化成元素ID
 * 第二个参数为一个对象直接量，选填，包含如下的零个或多个
 *     step                string number   目标进度  默认为1（第一步），选填
 *     change              boolean    设置插件是否可被操作，选填  默认false
 *     animation           boolean    设置插件是否采用动画形式（前提stepBar.change为true），选填  默认false
 *     speed               number     动画速度（前提，change和animation为true） 选填   默认1000ms
 *     stepEasingForward   string     从当前步数往前过渡动画（前提，change和animation为true） 选填  默认 "easeOutExpo"  更多参数请参照 jquery.easing.js
 *     stepEasingBackward  string     从当前步数往后过渡动画（前提，change和animation为true） 选填  默认 "easeOutElastic"  更多参数请参照 jquery.easing.js
 *
 *     PS：不合法的参数将强行使用默认值
 */
function Progress(initValue, id, option) {
    this.initValue = initValue;
    this.id = id;
    this.option = option;

    this.init = function () {
        this.initValue.curStep = 0;
        if (typeof this.id == "object" || this.id.indexOf("#") == 0) {
            this.initValue.bar = $(this.id);
        } else {
            if (this.id.indexOf(".") == 0) {
                this.id = this.id.substring(1, this.id.length);
            }
            this.initValue.bar = $("#" + this.id);
        }
        this.initValue.width = this.option.width;
        this.initValue.change = this.option.change ? true : false;
        this.initValue.animation = this.initValue.change && this.option.animation ? true : false;
        this.layout(this.option.isCheck);
        this.initValue.item = this.initValue.bar.find(".ui-stepInfo");
        if (this.initValue.item.length < 2) {
            return;
        }
        this.initValue.bar.show();
        this.initValue.itemCount = this.initValue.item.length;
        this.initValue.step = !isNaN(this.option.step) && this.option.step <= this.initValue.itemCount && this.option.step > 0 ? this.option.step : 1;
        this.initValue.triggerStep = this.initValue.step;
        if (!isNaN(this.initValue.speed) && this.initValue.speed > 0) {
            this.initValue.speed = parseInt(this.initValue.speed);
        }
        this.stepEasing(this.initValue.stepEasingForward, false);
        this.stepEasing(this.initValue.stepEasingBackward, true);
        this.stepInfoWidthFun(this.option.isCheck);
    };

    this.stepEasing = function (stepEasing, backward) {
        if (typeof jQuery.easing[stepEasing] === "function") {
            if (backward) {
                this.option.stepEasingBackward = stepEasing;
            } else {
                this.option.stepEasingForward = stepEasing;
            }
        }
    };

    this.layout = function (isCheck) {
        this.initValue.bar.find(".ui-stepInfo .ui-stepSequence").addClass("judge-stepSequence-hind");
        this.initValue.bar.find(".ui-stepInfo:first-child .ui-stepSequence").addClass("judge-stepSequence-pre");
    };

    this.classHover = function () {
        if (this.initValue.change) {
            this.initValue.bar.find(".ui-stepInfo .judge-stepSequence-pre").removeClass("judge-stepSequence-hind-change").addClass("judge-stepSequence-pre-change");
            this.initValue.bar.find(".ui-stepInfo .judge-stepSequence-hind").removeClass("judge-stepSequence-pre-change").addClass("judge-stepSequence-hind-change");
        }
    };

    this.stepInfoWidthFun = function (isCheck) {
        if (this.initValue.itemCount > 0) {
            this.initValue.barWidth = this.initValue.width;
            this.initValue.itemWidth = Math.floor((this.initValue.barWidth * 0.5) / (this.initValue.itemCount - 1));
            this.initValue.bar.find(".ui-stepLayout").width(Math.floor(this.initValue.barWidth * 0.7 + this.initValue.itemWidth));
            this.initValue.item.width(this.initValue.itemWidth);
            this.initValue.bar.find(".ui-stepLayout").css({"margin-left": -Math.floor(this.initValue.itemWidth / 2) + 10});
            if (this.initValue.change) {
                this._event(isCheck);
            }
            this.percent(isCheck);
        }
    };

    this._event = function (isCheck) {
        var _this = this.initValue;
        _this.bar.on("click", ".ui-stepSequence", function () {
            var triggerStep = _this.triggerStep;
            if (!isNaN(parseInt(triggerStep)) && triggerStep > 0 && triggerStep <= _this.itemCount && triggerStep != _this.curStep) {
                _this.triggerStep = triggerStep;
                this.percent(isCheck);
            }
        });
    };

    this.percent = function (isCheck) {
        var temp_this = this;
        var _this = this.initValue;
        var calc = 100 / (_this.itemCount - 1);
        _this.processWidth = calc * (_this.triggerStep - 1) + "%";
        if (_this.animation) {
            if (_this.triggerStep < _this.curStep) {
                this._animate();
                _this.curStep--;
            } else {
                _this.curStep++;
            }
            _this.curProcessWidth = calc * (_this.curStep - 1) + "%";
            _this.bar.find(".ui-stepProcess").stop(true).animate({"width": _this.curProcessWidth}, _this.speed, function () {
                temp_this._animate(isCheck);
                if (_this.processWidth != _this.curProcessWidth) {
                    temp_this.percent(isCheck);
                }
            });
        } else {
            if (_this.curProcessWidth != _this.processWidth) {
                _this.curProcessWidth = _this.processWidth;
                _this.bar.find(".ui-stepProcess").width(_this.processWidth);
                this.jump();
            }
        }
    };

    this.jump = function () {
        this.initValue.bar.find(".ui-stepInfo .ui-stepSequence").removeClass("judge-stepSequence-pre").addClass("judge-stepSequence-hind");
        this.initValue.bar.find(".ui-stepInfo:nth-child(-n+" + this.initValue.triggerStep + ") .ui-stepSequence").removeClass("judge-stepSequence-hind").addClass("judge-stepSequence-pre");
        this.classHover();
    };

    this._animate = function (isCheck) {
        var stepSequence_size = {},
            easing = this.initValue.stepEasingBackward,
            preClass,
            hindClass;
        if (this.initValue.triggerStep < this.initValue.curStep) {
            stepSequence_size.padding = "0px";
            preClass = "judge-stepSequence-pre";
            hindClass = "judge-stepSequence-hind";
            easing = this.initValue.stepEasingForward;
        } else {
            stepSequence_size.padding = "0px";
            preClass = "judge-stepSequence-hind";
            if (this.initValue.curStep == this.initValue.step && !isCheck) {
                hindClass = "judge-stepSequence-pre2";
            } else if (this.initValue.curStep == this.initValue.step && isCheck) {
                hindClass = "judge-stepSequence-pre3";
            } else {
                hindClass = "judge-stepSequence-pre";
            }
        }
        this.initValue.bar.find(".ui-stepInfo:nth-child(" + this.initValue.curStep + ") .ui-stepSequence").removeClass(preClass).addClass(hindClass);
        this.initValue.bar.find(".ui-stepInfo:nth-child(" + this.initValue.curStep + ") .ui-stepSequence").animate(stepSequence_size, 300, easing);
        this.classHover();
    };
}
