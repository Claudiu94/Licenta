package com.common;

import kotlin.reflect.jvm.internal.impl.javax.inject.Inject;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class App
{
    @Inject
    private HelloWorld helloWorld;

    @PostConstruct
    public void init() {
        System.out.println("Enters init!!!!");
        helloWorld.printMessage();
    }
}
