package com.yz.flow.charge;

import com.yz.flow.charge.trigger.IT_BF_2017;
import com.yz.flow.charge.trigger.IT_BF_2018_03_01;
import com.yz.markting.Triggers;

public class IChargeTrigger extends Triggers {
	
	public IChargeTrigger() {
		add(new IT_BF_2018_03_01());
		add(new IT_BF_2017());
	}
}
