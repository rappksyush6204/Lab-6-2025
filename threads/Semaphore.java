package threads;

//обеспечивает корректное взаимодействие между генератором и интегратором
public class Semaphore {
    
    // флаг (данные готовы для чтения (интегрирования)) 
    private boolean dataReady = false;
    
    // флаг (данные обработаны и можно генерировать новые)
    private boolean dataProcessed = true;
    
    // метод для начала генерации данных
    // блокирует поток пока предыдущие данные не будут обработаны
    public synchronized void startWrite() throws InterruptedException {
        while (!dataProcessed) {
            wait();
        }
        // сбрасываем флаги для новой операции 
        dataReady = false;
        dataProcessed = false;
    }
    
    // для завершения генерации данных
    public synchronized void endWrite() {
        dataReady = true; 
        notifyAll(); 
    }
    
    // метод для начала чтения (интегрирования) 
    public synchronized void startRead() throws InterruptedException {
        while (!dataReady) {
            wait(); 
        }
    }
    
    // для завершения интегрирования данных
    public synchronized void endRead() {
        dataReady = false; 
        dataProcessed = true;
        notifyAll();
    }
}