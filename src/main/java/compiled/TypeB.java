package main.java.compiled;

/**
 * Created by Olaf on 2017-10-17.
 */
public class TypeB extends TypeA {

    public TypeB() {
        super();
    }

    public int answerToEverything() {
        return life * 2;
    }

    public static void main(String[] args) {
        TypeB b = new TypeB();
        System.out.println(b.answerToEverything());
    }
}
