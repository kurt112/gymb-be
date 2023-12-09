package com.kurt.gym.auth.model.user;

public enum UserRole {
    ADMIN(1),
    COACH(2),
    FRONT_DESK(3),
    CUSTOMER(4);

    private final int role;

    UserRole(int role){
        this.role = role;
    }

    public int getStatus(){
        return  this.role;
    }

}
