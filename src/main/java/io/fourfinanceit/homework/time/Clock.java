package io.fourfinanceit.homework.time;

import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
public class Clock {

    public LocalTime now(){
        return LocalTime.now();
    }
}
