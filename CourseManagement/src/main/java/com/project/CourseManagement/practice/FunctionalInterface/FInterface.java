package com.project.CourseManagement.practice.FunctionalInterface;

interface MathOperation {
    // In interface there are certain rules like variables are automatically or by default public static final
    // and mehtods are public abstract

    //    and the interface havinf one absttract method is called functional interface
    int operate(int a, int b);
}

class Add implements MathOperation {

    @Override
    public int operate(int a, int b) {
        return a + b;
    }
}

class Subtract implements MathOperation {

    @Override
    public int operate(int a, int b) {
        return a + b;
    }
}


public class FInterface {

    public static void main(String[] args) {
        Add add = new Add();
        System.out.println(add.operate(5, 10));
        MathOperation addOperation = ((a, b) -> a + b);
        System.out.println(addOperation.operate(10,20));
    }

}
