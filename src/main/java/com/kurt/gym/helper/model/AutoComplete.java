package com.kurt.gym.helper.model;

import lombok.Getter;

@Getter
public class AutoComplete {

    private long id;
    private String label;

    public AutoComplete(long id, String label) {
        this.id = id;
        this.label = label;
    }
    
}
