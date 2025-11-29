package threads;

public class Integrator extends Thread {
    private Task task;
    private Semaphore semaphore;
    
    public Integrator(Task task, Semaphore semaphore) {
        this.task = task;
        this.semaphore = semaphore;
    }

    @Override
    public void run() {
        System.out.println("Интегратор запущен");
        
        try {
            for (int i = 0; i < task.getTasksCount(); i++) {
                // Проверка прерывания текущего потока напрямую
                if (Thread.currentThread().isInterrupted()) {
                    throw new InterruptedException();
                }
                
                // Запрашиваем разрешение на чтение данных
                semaphore.startRead();
                
                // Проверка прерывания после получения разрешения
                if (Thread.currentThread().isInterrupted()) {
                    throw new InterruptedException();
                }
                
                double leftBorder, rightBorder, step, result;
                synchronized (task) {
                    leftBorder = task.getLeftBorder();
                    rightBorder = task.getRightBorder();
                    step = task.getStep();
                    result = task.execute();
                }
                
                System.out.println("Result " + leftBorder + " " + rightBorder + " " + step + " " + result);
                semaphore.endRead();
                
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    throw new InterruptedException();
                }
            }
        } catch (InterruptedException e) {
            System.out.println("Интегратор прерван");
            Thread.currentThread().interrupt();
        } finally {
            System.out.println("Интегратор завершил работу");
        }
    }
}