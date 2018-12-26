package com.yz.flow.charge;

import com.yz.flow.charge.trigger.CT_AF_2017_06;
import com.yz.flow.charge.trigger.CT_AF_2016_08_17_12H;
import com.yz.flow.charge.trigger.CT_AF_2017_11_2;
import com.yz.flow.charge.trigger.CT_BF_2018_03_01;
import com.yz.markting.Triggers;

public class ChargeTrigger extends Triggers {
	
	public ChargeTrigger() {
		add(new CT_BF_2018_03_01());
		add(new CT_AF_2017_06());
		add(new CT_AF_2016_08_17_12H());
		add(new CT_AF_2017_11_2());
	}
}
