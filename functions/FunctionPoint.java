package functions;

import java.io.Serializable;

public class FunctionPoint implements Serializable {
    // Приватные поля для инкапсуляции данных
    private double x;  // Координата x точки
    private double y;  // Координата y точки

    // КОНСТРУКТОРЫ
    public FunctionPoint(double x, double y) { //Создаёт объект точки с заданными координатами
        this.x = x;
        this.y = y;
    }

    public FunctionPoint(FunctionPoint point) {
        this.x = point.x;  // Копируем x из переданной точки
        this.y = point.y;  // Копируем y из переданной точки
    }

    public FunctionPoint() { // Создаём точку с координатами (0; 0)
        this(0.0, 0.0);
    }

    // ГЕТТЕРЫ (методы для чтения значений)
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    // СЕТТЕРЫ (методы для изменения значений)
    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }
    
    // переопределенные методы object
    
    @Override
    public String toString() {
        return "(" + x + "; " + y + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FunctionPoint)) return false;
        
        FunctionPoint that = (FunctionPoint) o;
        
        // Сравнение с учетом погрешности для чисел с плавающей точкой
        return Math.abs(this.x - that.x) < 1e-10 && 
               Math.abs(this.y - that.y) < 1e-10;
    }

    @Override
    public int hashCode() {
        long xBits = Double.doubleToLongBits(x);
        long yBits = Double.doubleToLongBits(y);
        
        int xHash = (int)(xBits ^ (xBits >>> 32));
        int yHash = (int)(yBits ^ (yBits >>> 32));
        
        return xHash ^ yHash;
    }

    @Override
    public Object clone() {
        return new FunctionPoint(this.x, this.y);
    }
}