package threads;

public class SimpleIntegrator implements Runnable {
    
    // Ссылка на объект для взаимодействия между потоками 
    private Task task;
    
    // Конструктор класса SimpleIntegrator
    public SimpleIntegrator(Task task) {
        this.task = task;
    }

    @Override
    public void run() {
        //Цикл выполняется для всех заданий
        for (int i = 0; i < task.getTasksCount(); i++) {
            double leftBorder, rightBorder, step, result;
            
            // Блок синхронизации для безопасного доступа к общему объекту Task
            synchronized (task) {
                
                // Проверка (что задание готово для вычисления)
                if (task.getFunction() != null) {
                    
                    //Получение параметров задания
                    leftBorder = task.getLeftBorder();
                    rightBorder = task.getRightBorder();
                    step = task.getStep();
                    
                    // Вычисление интеграла
                    result = task.execute();
                } else {
                    continue;
                }
            }
            
            System.out.println("Result " + leftBorder + " " + rightBorder + " " + step + " " + result);

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                System.out.println("Интегратор прерван");
                break;
            }
        }
        System.out.println("Интегратор завершил работу");
    }
}