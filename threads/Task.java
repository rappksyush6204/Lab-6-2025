package threads;

import functions.Function;
import functions.Functions;
import functions.basic.Log;

public class Task {
    // Поля для хранения параметров задания
    private Function function;      // Интегрируемая функция
    private double leftBorder;      // Левая граница интегрирования
    private double rightBorder;     // Правая граница интегрирования  
    private double step;            // Шаг дискретизации
    private int tasksCount;         // Общее количество заданий
    private int currentTask = 0;    // Текущий номер задания
    
    // Конструктор, принимающий общее количество заданий
    public Task(int tasksCount) {
        this.tasksCount = tasksCount;
    }
    
    // Синхронизированный метод для установки параметров задания
    public synchronized void setTask(Function function, double leftBorder, double rightBorder, double step) {
        this.function = function;
        this.leftBorder = leftBorder;
        this.rightBorder = rightBorder;
        this.step = step;
    }
    public synchronized int getTasksCount() {
        return tasksCount;
    }
    // Проверка, остались ли еще задания для выполнения
    public synchronized boolean hasMoreTasks() {
        return currentTask < tasksCount;
    }
    
    // Увеличение счетчика выполненных заданий
    public synchronized void incrementTask() {
        currentTask++;
    }
    
    // Геттеры для получения параметров задания
    public synchronized Function getFunction() {
        return function;
    }
    
    public synchronized double getLeftBorder() {
        return leftBorder;
    }
    
    public synchronized double getRightBorder() {
        return rightBorder;
    }
    
    public synchronized double getStep() {
        return step;
    }
    
    // Выполнение вычисления интеграла с текущими параметрами
    public double execute() {
        return Functions.integrate(function, leftBorder, rightBorder, step);
    }
}