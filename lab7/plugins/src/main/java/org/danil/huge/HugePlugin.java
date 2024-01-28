package org.danil.huge;

import org.danil.Plugin;

public class HugePlugin implements Plugin {
    final char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    @Override
    public void doUsefull() {
        for(int i = 0; i < 100; ++i) {
            System.out.print(alphabet[i % alphabet.length]);
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                break;
            }
        }
        System.out.print("\r We are done             ");
    }
}
