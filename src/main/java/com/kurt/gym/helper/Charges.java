package com.kurt.gym.helper;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Charges {

    // It's arranged by the chance of user will pay for gym
    FREE_TRIAL(0),
    MONTHLY(1),
    ANNUALLY(2),
    WEEKLY(3),
    QUERTERLY(4),
    SEMI_ANNUAL(5);

    final int value;

}
