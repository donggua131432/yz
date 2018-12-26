package com.yz.flow.stage;

import com.yz.markting.AbstractFlow;

public class StageFlow extends AbstractFlow {
	
	public StageFlow() {
		super(new StageTrigger(), "StageFlow");
	}

}
