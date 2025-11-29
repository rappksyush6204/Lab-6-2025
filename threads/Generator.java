package threads;

import functions.basic.Log;
import java.util.Random;

public class Generator extends Thread {
    private Task task;
    private Semaphore semaphore;
    private Random random = new Random();
    
    public Generator(Task task, Semaphore semaphore) {
        this.task = task;
        this.semaphore = semaphore;
    }
    
    @Override
    public void run() {
        System.out.println("Генератор запущен");
        
        try {
            for (int i = 0; i < task.getTasksCount(); i++) {
                // Проверка прерывания текущего потока напрямую
                if (Thread.currentThread().isInterrupted()) {
                    throw new InterruptedException();
                }
                semaphore.startWrite();
                
                // Проверка прерывания после получения разрешения
                if (Thread.currentThread().isInterrupted()) {
                    throw new InterruptedException();
                }
                
                // Генерация параметров задания
                double base = 1 + random.nextDouble() * 9;
                Log logFunc = new Log(base);
                double leftBorder = random.nextDouble() * 100;
                double rightBorder = 100 + random.nextDouble() * 100;
                double step = random.nextDouble();

                // Установка задания 
                synchronized (task) {
                    task.setTask(logFunc, leftBorder, rightBorder, step);
                }
                
                System.out.println("Source " + leftBorder + " " + rightBorder + " " + step);
                semaphore.endWrite();
                
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    throw new InterruptedException();
                }
            }
        } catch (InterruptedException e) {
            System.out.println("Генератор прерван");
            Thread.currentThread().interrupt();
        } finally {
            System.out.println("Генератор завершил работу");
        }
    }
}