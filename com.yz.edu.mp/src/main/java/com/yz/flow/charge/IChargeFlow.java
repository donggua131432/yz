package com.yz.flow.charge;

import com.yz.markting.AbstractFlow;

public class IChargeFlow extends AbstractFlow {
	
	public IChargeFlow() {
		super(new IChargeTrigger(), "IChargeFlow");
	}
}
