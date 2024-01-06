package com.kurt.gym.auth.model.user;

public enum UserRole {
    ADMIN(1),
    COACH(2),
    FRONT_DESK(3),
    CUSTOMER(4);

    private final int role;

    UserRole(int role) {
        this.role = role;
    }

    public int getStatus() {
        return this.role;

    }

    public static UserRole getRole(int role) {
        switch (role) {
            case 1:
                return ADMIN;
            case 2:
                return COACH;
            case 3:
                return FRONT_DESK;
            case 4:
                return CUSTOMER;
            default:
                return null;
        }
    }

}
