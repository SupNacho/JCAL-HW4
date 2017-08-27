package ru.supernacho.mt;

import java.io.*;
import java.util.concurrent.*;

public class Main {
    private static final Object monitor = new Object();
    private static final int CNT = 5;
    private static final int TSK_TWO_CNT = 10;
    private static final int THS_DONE = 3;
    private static volatile char state = 'A';
    private  static ExecutorService executorService = null;
    private static DataOutputStream dos = null;
    private static int recState = 0;


    public static void main(String[] args){

        executorService = Executors.newFixedThreadPool(3);

//        task1();  // Задача№1 - вывод в одном методе, на вход подаются два символа (Символ потока и символ след. потока,
                    // можно было обойтись только символом этого потока, но пришлось бы писать if/else,
                    // решил обойтись 2-мя входными данными).

//        task2();            // Задача№2 - пишем в три потока по 10 записей в файл.
                              // Метод записи попробовал синхронизед и нет.
                              // Как я понял, суть задания убедиться в потокобезопасности потоков ввода/вывода.

        executorService.shutdown();

        task3(); // Задача№3 - Одновременно печатает и сканирует по одному заданию каждого типа,
                 // дополнительные задачи на сканирование и печать ожидают освобождения соответствующих мониторов,
                 // после чего начинается печать и сканирование следующих запущенных задач(не более 1 однотипной задачи
                 // одновременно).



    }

    private static void task3() {
        Mfu printer = new Mfu();
        printer.start();
    }

    private static void task2() {

        try {
            dos = new DataOutputStream(new FileOutputStream("task2.txt", true));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        executorService.execute(() -> fileWrite());
        executorService.execute(() -> fileWrite());
        executorService.execute(() -> fileWrite());

    }

    private synchronized static void fileWrite() { // C synchronized идет запись 123..10 123..10 123..10,
                                                   // без синхронизед 111 222 333 ... при этом порядок записи потоков
                                                   // не гарантирован, кто первый пришел тот и записал.

        for (int i = 0; i < TSK_TWO_CNT; i++) {
            try {
                dos.writeUTF(Thread.currentThread().getName() + ": record# " + i + "\n");
                Thread.sleep(20);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        ++recState;
        if (recState == THS_DONE){
            System.out.println("Record done!");
            try {
                dos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void task1() {
        executorService.execute(() -> printChar('A','B'));
        executorService.execute(() -> printChar('B','C'));
        executorService.execute(() -> printChar('C','A'));

    }

    private static void printChar(char ch, char ch2) {
        synchronized (monitor) {
            try {
                for (int i = 0; i < CNT; i++) {
                    while (ch != state) {
                        monitor.wait();
                    }
                    System.out.print(ch);
                    state = ch2;
                    monitor.notifyAll();

                }
            } catch (InterruptedException e) {
                 e.printStackTrace();
                 }
        }
    }
}
