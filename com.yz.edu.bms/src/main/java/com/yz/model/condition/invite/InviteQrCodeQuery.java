package com.yz.model.condition.invite;

import com.yz.model.common.PageCondition;

public class InviteQrCodeQuery extends PageCondition {
	private String invitations;
	private String createName;

	public String getInvitations() {
		return invitations;
	}

	public void setInvitations(String invitations) {
		this.invitations = invitations;
	}

	public String getCreateName() {
		return createName;
	}

	public void setCreateName(String createName) {
		this.createName = createName;
	}

}
