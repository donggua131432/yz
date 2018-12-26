智米赠送营销JAR包

1.编写 Flow 继承 com.yz.markting.AbstractFlow  -- 在构造方法中 实例化 自身的trigger、calculator、flowName(使用类名即可)

2.编写 Trigger 实现 com.yz.markting.Trigger -- 触发器 ：根据传入的条件参数来匹配选择器 ， 

3.编写 Switcher 继承 com.yz.markting.MpSwticher -- 选择器可以根据相应条件来做扩展并不做限制

4.编写 Calculator 实现 com.yz.markting.Calculator -- 计算器 ： 根据选择器 来选择相应奖励信息 (com.yz.markting.MpResult 如果有多条奖励记录时使用 targets)



5.使用方式

MpCondition condition = new MpCondition();

condition.put("a", 0);

MpResult result = MpContext.getStageFlow().match(condition);

