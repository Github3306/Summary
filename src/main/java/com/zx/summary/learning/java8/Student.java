package com.zx.summary.learning.java8;

import lombok.Data;

@Data
public class Student {
    private String aa;
    private Integer age;
    private Integer no;

    public Student(String aa, Integer age, Integer no) {
        this.aa = aa;
        this.age = age;
        this.no = no;
    }
}
