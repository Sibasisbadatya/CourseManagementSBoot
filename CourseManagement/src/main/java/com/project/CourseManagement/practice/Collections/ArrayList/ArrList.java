package com.project.CourseManagement.practice.Collections.ArrayList;
//Unlike Arrays ArrayList can change the size dynamically
//By default capacity of ArrayList is 10
//if its full it creates new capacity of 1.5x following copying of elements for O(n) TC.

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ArrList {

    public void printValues(ArrayList<Integer> list) {
        for (int x : list) {
            System.out.print(x + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        ArrList a = new ArrList();
//        Creating arrays
        ArrayList<Integer> list = new ArrayList<>();
//        ArrayList<Integer> list = new ArrayList<>(100); to give initial size

        List<String> list1 = Arrays.asList("Sibasis", "Ansuman"); // instant creation
        String[] sarray = {"Sibasis", "Ansuman"};
        List<String> list2 = Arrays.asList(sarray); //by primitive array
//        This List made by Arrays has fixed size so set() [to modify] only valid not add() [to add] to add elements

        List<Integer> integers = List.of(1, 2, 4, 5, 6);
//        By this method we can't even use .set() to modify


//        To make modifiable we can pass this array into ArrayList
        ArrayList<Integer> mlist = new ArrayList<>(integers);
        mlist.set(1,97);
        a.printValues(mlist);
//        by hovering the constructor eg ArrayList<>(here) you will see in intelliJ we can give capacity or another list


        list.add(10);//adding of elements
        list.add(20);
        System.out.println(list.get(1));

//          looping
        a.printValues(list);

//        Contains for existence
        System.out.println(list.contains(30));

//        removing elements
        list.remove(1); //takes index to remove
        a.printValues(list);

//        .remove() accepts index and object ,for string List it can work fine if we give string it takes a object
//        and if we give integer it takes as index .
//        But for integer array how it will know if that is object or index so to remove miscommunication we can use wrapper object
        mlist.remove(Integer.valueOf(6));
                a.printValues(mlist);

//        adding at index
        list.add(1, 80);
        a.printValues(list);

//        setting(updating) elements by index
        list.set(1, 91);
        a.printValues(list);
        System.out.println(list); //it automatically has toString method to print the list

//        get size of list
        System.out.println(list.size());


//        adding a new list to another
        list.addAll(mlist);
        a.printValues(list);


//        to convert list to array
        Object[] array = mlist.toArray(); //Here simple array of Object formed because we didn't have the data type
        Integer[] array1 = mlist.toArray(new Integer[0]);  //of type integer having size 0


//        Sorting of elements
        Collections.sort(mlist);
        a.printValues(mlist);

        mlist.sort(null); //here in sort we have to pass comparator but here for default we pass null (Natural Ordering)
//        But we can pass custom comparator
//        Comperator returns integer
    }


}
