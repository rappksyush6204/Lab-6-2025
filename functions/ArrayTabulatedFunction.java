package functions;

import java.io.*;

public class ArrayTabulatedFunction implements TabulatedFunction, Serializable {
    private static final long serialVersionUID = 1L;
    private FunctionPoint[] points;
    private int pointsCount;

    // Конструкторы
    public ArrayTabulatedFunction() {
        this.points = new FunctionPoint[2];
        this.pointsCount = 0;
    }

    public ArrayTabulatedFunction(double leftX, double rightX, int pointsCount) throws IllegalArgumentException {
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница не может быть больше или равна правой");
        }
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Количество точек не может быть меньше двух");
        }

        this.pointsCount = pointsCount;
        this.points = new FunctionPoint[pointsCount + 2];

        double step = (rightX - leftX) / (pointsCount - 1);
        
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            points[i] = new FunctionPoint(x, 0.0);
        }
    }

    public ArrayTabulatedFunction(double leftX, double rightX, double[] values) throws IllegalArgumentException {
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница области не может быть больше или равна правой");
        }
        if (values.length < 2) {
            throw new IllegalArgumentException("Количество точек не может быть меньше двух");
        }
        
        this.pointsCount = values.length;
        this.points = new FunctionPoint[pointsCount + 2];

        double step = (rightX - leftX) / (pointsCount - 1);
        
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            points[i] = new FunctionPoint(x, values[i]);
        }
    }

    public ArrayTabulatedFunction(FunctionPoint[] points) throws IllegalArgumentException {
        if (points.length < 2) {
            throw new IllegalArgumentException("Количество точек не может быть меньше двух");
        }
        
        for (int i = 0; i < points.length - 1; i++) {
            if (points[i + 1].getX() - points[i].getX() < 1e-10) {
                throw new IllegalArgumentException("Точки должны быть строго упорядочены по возрастанию X");
            }       
        }
        
        this.pointsCount = points.length;
        this.points = new FunctionPoint[pointsCount + 2];
        
        for (int i = 0; i < pointsCount; i++) {
            this.points[i] = new FunctionPoint(points[i]);
        }
    }

    // Методы
    public double getLeftDomainBorder() {
        return points[0].getX();
    }

    public double getRightDomainBorder() {
        return points[pointsCount - 1].getX();
    }

    public double getFunctionValue(double x) {
        if (x < getLeftDomainBorder() - 1e-10 || x > getRightDomainBorder() + 1e-10) {
            return Double.NaN;
        }

        for (int i = 0; i < pointsCount; i++) {
            if (Math.abs(x - points[i].getX()) < 1e-10) {
                return points[i].getY(); 
            }
        }

        for (int i = 0; i < pointsCount - 1; i++) {
            double x1 = points[i].getX();
            double x2 = points[i + 1].getX();
            
            if (x >= x1 - 1e-10 && x <= x2 + 1e-10) {
                double y1 = points[i].getY();
                double y2 = points[i + 1].getY();
                return y1 + (y2 - y1) * (x - x1) / (x2 - x1);
            }
        }
        return Double.NaN;
    }

    public int getPointsCount() {
        return pointsCount;
    }

    public FunctionPoint getPoint(int index) throws FunctionPointIndexOutOfBoundsException {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс " + index + " выходит за границы набора точек");
        }
        return new FunctionPoint(points[index]);
    }

    public void setPoint(int index, FunctionPoint point) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс " + index + " выходит за границы набора точек");
        }

        if (index > 0 && point.getX() <= points[index - 1].getX() + 1e-10) {
            throw new InappropriateFunctionPointException("X точки должен быть больше предыдущего");
        }
        if (index < pointsCount - 1 && point.getX() >= points[index + 1].getX() - 1e-10) {
            throw new InappropriateFunctionPointException("X точки должен быть меньше следующего");
        }

        points[index] = new FunctionPoint(point);
    }

    public double getPointX(int index) throws FunctionPointIndexOutOfBoundsException {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс " + index + " выходит за границы набора точек");
        }
        return points[index].getX();
    }

    public void setPointX(int index, double x) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Точка с индексом " + index + " не существует");
        }

        if (index > 0 && x <= points[index - 1].getX() + 1e-10) {
            throw new InappropriateFunctionPointException("Новый X должен быть больше X левого соседа");
        }
        if (index < pointsCount - 1 && x >= points[index + 1].getX() - 1e-10) {
            throw new InappropriateFunctionPointException("Новый X должен быть меньше X правого соседа");
        }

        double oldY = points[index].getY();
        points[index] = new FunctionPoint(x, oldY);
    }

    public double getPointY(int index) throws FunctionPointIndexOutOfBoundsException {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс " + index + " выходит за границы набора точек");
        }
        return points[index].getY();
    }

    public void setPointY(int index, double y) throws FunctionPointIndexOutOfBoundsException {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс " + index + " выходит за границы набора точек");
        }
        
        double oldX = points[index].getX();
        points[index] = new FunctionPoint(oldX, y);
    }

    public void deletePoint(int index) throws FunctionPointIndexOutOfBoundsException, IllegalStateException {
        if (pointsCount <= 2) {
            throw new IllegalStateException("Нельзя удалять точки: минимальное количество точек - 2");
        }
        
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс " + index + " выходит за границы набора точек");
        }

        System.arraycopy(points, index + 1, points, index, pointsCount - index - 1);
        pointsCount--;
        points[pointsCount] = null;
    }

    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        if (point == null) {
            throw new IllegalArgumentException("Точка не может быть null");
        }
        
        int insertIndex = 0;
        
        while (insertIndex < pointsCount && points[insertIndex].getX() < point.getX() - 1e-10) {
            insertIndex++;
        }

        if (insertIndex < pointsCount && Math.abs(points[insertIndex].getX() - point.getX()) < 1e-10) {
            throw new InappropriateFunctionPointException("Точка с x=" + point.getX() + " уже существует в функции");
        }

        if (pointsCount >= points.length) {
            FunctionPoint[] newPoints = new FunctionPoint[points.length * 2];
            System.arraycopy(points, 0, newPoints, 0, pointsCount);
            points = newPoints;
        }

        System.arraycopy(points, insertIndex, points, insertIndex + 1, pointsCount - insertIndex);
        
        points[insertIndex] = new FunctionPoint(point);
        pointsCount++;
    }
    
    // Возвращаем строковое представление функции в формате {(x1; y1), (x2; y2), и тд.}
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("{");
        for (int i = 0; i < pointsCount; i++) {
            result.append(points[i].toString());
            if (i < pointsCount - 1) {
                result.append(", ");
            }
        }
        result.append("}");
        return result.toString();
    }

    /**
     * Сравнивает эту функцию с другим объектом
     * Возвращает true если функции имеют одинаковые точки
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TabulatedFunction)) return false;
        
        TabulatedFunction that = (TabulatedFunction) o;
        if (this.pointsCount != that.getPointsCount()) return false;
        
        // Оптимизация для ArrayTabulatedFunction
        if (o instanceof ArrayTabulatedFunction) {
            ArrayTabulatedFunction arrayThat = (ArrayTabulatedFunction) o;
            for (int i = 0; i < pointsCount; i++) {
                if (!this.points[i].equals(arrayThat.points[i])) {
                    return false;
                }
            }
        } else {
            // Общий случай для любого TabulatedFunction
            for (int i = 0; i < pointsCount; i++) {
                FunctionPoint thisPoint = this.getPoint(i);
                FunctionPoint thatPoint = that.getPoint(i);
                if (!thisPoint.equals(thatPoint)) {
                    return false;
                }
            }
        }
        
        return true;
    }

    // Возвращаем хэш-код функции на основе всех точек и их количества
    @Override
    public int hashCode() {
        int result = pointsCount;
        for (int i = 0; i < pointsCount; i++) {
            result ^= points[i].hashCode();
        }
        return result;
    }

    // Создаем копию функции
    @Override
    public Object clone() throws CloneNotSupportedException {
        FunctionPoint[] clonedPoints = new FunctionPoint[pointsCount];
        for (int i = 0; i < pointsCount; i++) {
            clonedPoints[i] = (FunctionPoint) points[i].clone();
        }
        return new ArrayTabulatedFunction(clonedPoints);
    }
}