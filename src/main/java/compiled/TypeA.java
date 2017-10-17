package main.java.compiled;

/**
 * Created by Olaf on 2017-10-17.
 */
public class TypeA {
    public static final int life = 21;

    public TypeA() {
    }

    public int addTwoNumbers(int a, int b) {
        return a + b;
    }

    public int[] doubleArrayValues(int[] numbers) {
        for(int i=0; i< numbers.length; i++) {
            numbers[i] = numbers[i]*2;
        }
        return numbers;
    }

    public static String sayHello(String name) {
        return "Hello " + name + "!";
    }
    public static void main(String[] args) {
        TypeA a = new TypeA();
        int[] arrayNumbers = {1,2,3};
        int[] values = a.doubleArrayValues(arrayNumbers);
        for (int i = 0; i < values.length; i++) {
            System.out.print(values[i] + " ");
        }
        System.out.println(TypeA.sayHello("Olaf"));
        System.out.println(a.addTwoNumbers(2,4));
    }
}
