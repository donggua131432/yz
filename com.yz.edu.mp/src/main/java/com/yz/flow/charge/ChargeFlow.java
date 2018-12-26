package com.yz.flow.charge;

import com.yz.markting.AbstractFlow;

public class ChargeFlow extends AbstractFlow {
	
	public ChargeFlow() {
		super(new ChargeTrigger(), "ChargeFlow");
	}
}
