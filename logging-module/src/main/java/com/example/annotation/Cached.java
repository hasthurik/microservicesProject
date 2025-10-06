package com.example.annotation;


public @interface Cached {
    String cacheName() default "defaultCache";
}
