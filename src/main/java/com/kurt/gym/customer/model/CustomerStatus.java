package com.kurt.gym.customer.model;


public enum CustomerStatus {
    MEMBER(1),
    NON_MEMBER(0),
    FREEZE(2),
    FOR_DELETION(-1),
    IN_ACTIVE(-2);

    private final int status;

    CustomerStatus(int status){
        this.status = status;
    }

    public int getStatus(){
        return  this.status;
    }
}
