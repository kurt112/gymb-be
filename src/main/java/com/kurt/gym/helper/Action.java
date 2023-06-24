package com.kurt.gym.helper;

import lombok.Getter;

@Getter
public enum Action {
    DELETED(1), CREATED(2), UPDATED(3), TOP_UP(4);

    private final int value;

    private Action(int value) {
        this.value = value;
    }
}
