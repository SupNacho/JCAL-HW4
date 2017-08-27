package ru.supernacho.mt;

public class Mfu {
    private static final Object printerMon = new Object();
    private static final Object scannerMon = new Object();


    public void start() {
        Thread printProc = new Thread(() -> printDoc(8));
        Thread printProc2 = new Thread(() -> printDoc(6));
        Thread printProc3 = new Thread(() -> printDoc(12));
        Thread scanProc = new Thread(() -> scannDoc(5));
        Thread scanProc2 = new Thread(() -> scannDoc(8));
        Thread scanProc3 = new Thread(() -> scannDoc(9));
        printProc.setName("| Печать 1 |");
        printProc2.setName("| Печать 2 |");
        printProc3.setName("| Печать 3 |");
        scanProc.setName("| Сакан  1 |");
        scanProc2.setName("| Сакан  2 |");
        scanProc3.setName("| Сакан  3 |");
        printProc.start();
        printProc2.start();
        printProc3.start();
        scanProc.start();
        scanProc2.start();
        scanProc3.start();
        try {
            printProc.join();
            printProc2.join();
            printProc3.join();
            scanProc.join();
            scanProc2.join();
            scanProc3.join();
        } catch (InterruptedException e){
            e.printStackTrace();
        }
        System.out.println("Все задачи МФУ выполнены.");


    }

    public void printDoc(int pages) {
        synchronized (printerMon) {
            for (int i = 1; i <= pages; i++) {
                System.out.println(Thread.currentThread().getName() + " отпечатано страниц: " + i);
                if ( i == pages) {
                    System.out.println("          --> Задание " + Thread.currentThread().getName() + " завершено");
                }
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void scannDoc(int frames) {
        synchronized (scannerMon) {
            for (int i = 1; i <= frames; i++) {
                System.out.println(Thread.currentThread().getName() + " отсканировано старниц: " + i);
                    if ( i == frames) {
                    System.out.println("          --> Задание " + Thread.currentThread().getName() + " завершено");
                }
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
