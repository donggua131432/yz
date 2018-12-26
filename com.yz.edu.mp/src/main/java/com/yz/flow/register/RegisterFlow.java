package com.yz.flow.register;

import com.yz.markting.AbstractFlow;

public class RegisterFlow extends AbstractFlow {

	public RegisterFlow() {
		super(new RegisterTrigger(), "RegisterFlow");
	}
}
