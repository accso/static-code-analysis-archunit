package de.accso.archunitsonar;

public class MyMath {
    // explicit dependency to MyString (only to let the ArchUnit dependency test fail)
    // MyString myString;

    public MyMath() {}

    public int add(int i, int j) {
        return i+j;
    }

    public int multiply(int i, int j) {
        return i*j;
    }
}
