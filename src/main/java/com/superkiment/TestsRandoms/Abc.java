package com.superkiment.TestsRandoms;

public class Abc implements InterfaceTest {
    Abc(String name) {

        System.out.println("Instance " + Abc.class.getName() + " : " + name);
    }

    @Override
    public void test() {
        System.out.println("lez go interface");
    }
}