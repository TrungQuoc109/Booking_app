package vn.com.booking.enumclass;

public enum Role {
	Admin(0), User(1);

	private final int value;

	Role(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
	public static Role fromValue(int value) {
		for (Role role : Role.values()) {
			if (role.value == value) {
				return role;
			}
		}
		return null;
	}
}
