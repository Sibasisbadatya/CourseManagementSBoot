package com.project.CourseManagement.practice.multithreading.deadlock;

class Paper {
    public synchronized void writeWithPaperAndPen(Pen pen) {
        System.out.println("writting with paper and pen");
        pen.finishedWithPaper();
    }

    public synchronized void finishedWithPen() {
        System.out.println("writting finished with pen");
    }

}

class Pen {
    public synchronized void writeWithPenAndPaper(Paper paper) {
        System.out.println("writting with pen and paper");
        paper.finishedWithPen();
    }

    public synchronized void finishedWithPaper() {
        System.out.println("writting finished with paper");
    }
}

class Thread1 extends Thread {
    Pen pen;
    Paper paper;

    Thread1(Pen pen, Paper paper) {
        this.pen = pen;
        this.paper = paper;
    }

    @Override
    public void run() {
        pen.writeWithPenAndPaper(paper);
    }
}

class Thread2 extends Thread {
    Pen pen;
    Paper paper;

    Thread2(Pen pen, Paper paper) {
        this.pen = pen;
        this.paper = paper;
    }

    @Override
    public void run() {
        synchronized (pen){ // to avoid deadlock we must do synchronised in one thread for the object that thread wants to acquire.
            paper.writeWithPaperAndPen(pen);
        }

    }
}


public class Deadlock {


    public static void main(String[] args) {
        Paper paper = new Paper();
        Pen pen = new Pen();
        Thread1 t1 = new Thread1(pen, paper);
        Thread2 t2 = new Thread2(pen, paper);
        t1.start();
        t2.start();
    }


}
