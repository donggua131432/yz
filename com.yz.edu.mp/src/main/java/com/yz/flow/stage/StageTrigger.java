package com.yz.flow.stage;

import com.yz.flow.stage.trigger.ST_AF_2017_06;
import com.yz.flow.stage.trigger.ST_AF_2016_08_17_12H;
import com.yz.markting.Triggers;

public class StageTrigger extends Triggers{
	
	public StageTrigger() {
		add(new ST_AF_2016_08_17_12H());
		add(new ST_AF_2017_06());
	}

}
