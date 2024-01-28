package org.danil;

public class HugePlugin implements Plugin {
    @Override
    public void doUsefull() {
        for(int i = 0; i < 100; ++i) {
            System.out.print("\r<");
            for(int j = 0; j < i; ++j)
                System.out.print("=");
            for(int j = i; j < 100; ++j)
                System.out.print(" ");
            System.out.print(">");
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                break;
            }
        }
        System.out.print("\r We are done             ");
    }
}
