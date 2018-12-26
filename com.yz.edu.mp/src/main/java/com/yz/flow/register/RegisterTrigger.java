package com.yz.flow.register;

import com.yz.flow.register.trigger.RT_AF_2017_06;
import com.yz.markting.Triggers;

public class RegisterTrigger extends Triggers {

	public RegisterTrigger() {
		add(new RT_AF_2017_06());
	}
}
