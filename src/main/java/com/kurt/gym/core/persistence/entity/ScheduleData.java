package com.kurt.gym.core.persistence.entity;


import java.sql.Time;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.kurt.gym.helper.SqlTimeDeserializer;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ScheduleData implements Comparable<ScheduleData> {
    private short day;

    @JsonFormat(pattern = "hh:mm")
    @JsonDeserialize(using = SqlTimeDeserializer.class)
    private Time startTime;

    @JsonFormat(pattern = "hh:mm")
    @JsonDeserialize(using = SqlTimeDeserializer.class)
    private Time endTime;

    @Override
    public int compareTo(ScheduleData o) {

        return this.day - o.day;
        
    }
}
