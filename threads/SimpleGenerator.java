package threads;

import functions.basic.Log;
import java.util.Random;

public class SimpleGenerator implements Runnable {
    
    // Ссылка на объект для взаимодействия между потоками 
    private Task task;
    
    // Генератор случайных чисел для создания параметров заданий 
    private Random random = new Random();
    
    //Конструктор
    public SimpleGenerator(Task task) {
        this.task = task;
    }
    
    @Override
    public void run() {
        // Пока есть задания для генерации
        for (int i = 0; i < task.getTasksCount(); i++) {
            double base, leftBorder, rightBorder, step;
            
            // Блок синхронизации для безопасного доступа к общему объекту Task
            synchronized (task) {
                
                // Создание логарифмической функции со случайным основанием от 1 до 10
                base = 1 + random.nextDouble() * 9;
                Log logFunc = new Log(base);
                
                // Генерация параметров задания согласно
                leftBorder = random.nextDouble() * 100;           // от 0 до 100
                rightBorder = 100 + random.nextDouble() * 100;    // от 100 до 200
                step = random.nextDouble();                       // от 0 до 1
                
                // Установка параметров в объект задания
                task.setTask(logFunc, leftBorder, rightBorder, step);
            }
            
            //Сообщение о сгенерированном задании 
            System.out.println("Source " + leftBorder + " " + rightBorder + " " + step);

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                System.out.println("Генератор прерван");
                break;
            }
        }
        System.out.println("Генератор завершил работу");
    }
}