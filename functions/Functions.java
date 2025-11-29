package functions;

import functions.meta.*;

public class Functions {

    private Functions() {}; //конструктор чтобы нельзя было создать объект класса
    
    public static Function shift(Function f, double shiftX, double shiftY) {
        return new Shift(f, shiftX, shiftY);
    }

    public static Function scale(Function f, double scaleX, double scaleY) {
        return new Scale(f, scaleX, scaleY);
    }

    public static Function power(Function f, double power){
        return new Power(f, power);
    }
    public static Function sum(Function f1, Function f2) {
        return new Sum(f1, f2);
    }
    public static Function mult(Function f1, Function f2){
        return new Mult(f1, f2);
    }
    public static Function composition(Function f1, Function f2){
        return new Composition(f1, f2);
    }
    
    public static double integrate(Function f, double left, double right, double step) {
        if (left < f.getLeftDomainBorder() || right > f.getRightDomainBorder()) {
            throw new IllegalArgumentException("Границы интегрирования выходят за область определения функции");
        }
        if (left >= right) {
            throw new IllegalArgumentException("Левая граница должна быть меньше правой");
        }
        if (step <= 0) {
            throw new IllegalArgumentException("Шаг интегрирования должен быть положительным");
        }
        
        double integralValue = 0.0;
        double currentX = left;
        
        while (currentX < right) {
            double nextX = Math.min(currentX + step, right);
            double y1 = f.getFunctionValue(currentX);
            double y2 = f.getFunctionValue(nextX);
            integralValue += (y1 + y2) * (nextX - currentX) / 2;
            currentX = nextX;
        }
        
        return integralValue;
    }
}