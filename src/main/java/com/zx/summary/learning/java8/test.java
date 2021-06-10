package com.zx.summary.learning.java8;


import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class test {
    @Test
    public void test01() {
        List<Integer> test = new ArrayList<Integer>();
        test.add(1);
        test.add(2);
        test.add(3);
        test.add(4);
        test.add(5);
        test.add(6);
        test.add(7);
        test.add(8);
        test.add(9);
        test.add(10);
        test.add(11);
        test.add(12);

        List<Integer> collect = test.stream() //1
                .filter(a -> a > 1
                        && a < 10) //2
                .collect(Collectors.toList()); //3
        System.out.println(test);
        System.out.println(collect);
    }

    @Test
    public void test02(){

        List<Student> test = new ArrayList<>();
        test.add(new Student("aa", 11,15));
        test.add(new Student("bb", 13,10));
        test.add(new Student("cc", 13,11));
        test.add(new Student("dd", 15,20));
        test.add(new Student("ee", 11,16));
        List<Student> collect = test.stream()
                .sorted(Comparator.comparing(Student::getAge).thenComparing(Student::getNo))
                .collect(Collectors.toList());
        System.out.println(collect);
    }

    @Test
    public void streamDemo1(){
        List<Student> test = new ArrayList<>();
        test.add(new Student("aa", 11,15));
        test.add(new Student("bb", 13,10));
        test.add(new Student("cc", 13,11));
        test.add(new Student("dd", 15,20));
        test.add(new Student("ee", 11,16));
        test.stream().forEach(student -> student.setAge(10));
        System.out.println(test.toString());
    }
}
