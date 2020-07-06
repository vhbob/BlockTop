package com.vhbob.blocktop.contests;

public enum ContestType {

	DAILY("Daily"), WEEKLY("Weekly"), MONTHLY("Monthly"), CUSTOM("Custom");

	private String type;

	private ContestType(String type) {
		this.type = type;
	}

	public String toString() {
		return type;
	}

	public static boolean contains(String test) {

		for (ContestType c : ContestType.values()) {
			if (c.name().equals(test)) {
				return true;
			}
		}

		return false;
	}
}
