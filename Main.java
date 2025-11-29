import functions.*;
import functions.basic.*;
import threads.*;

public class Main {
    public static void main(String[] args) {
        // задание 1: тестирование интегрирования экспоненты
        Exp exp = new Exp();
        double theory = Math.E - 1;
        System.out.println("Теоретическое значение интеграла exp(x) от 0 до 1: " + theory);
        // подбор шага для точности 7 знаков после запятой
        double[] stepSizes = {1.0, 0.1, 0.01, 0.001, 0.0001, 0.00001, 0.000001, 0.0000001};
        System.out.println("Поиск шага для точности 1e-7:");
        
        for (double s : stepSizes) {
            double value = Functions.integrate(exp, 0, 1, s);
            double diff = Math.abs(value - theory);
            System.out.printf("Шаг: %.8f | Значение: %.10f | Разница: %.10f%n", s, value, diff);

            // проверка достижения требуемой точности
            if (diff < 1e-7) {
                System.out.printf("Требуемая точность достигнута при шаге: %.8f%n", s);
                break;
            }
        }

        System.out.println();
        // запуск трех версий программы
        runSequential();
        runSimpleThreads();
        runComplexThreads();
    }

    public static void runSequential() {
        // задание 2: последовательная версия
        System.out.println("Последовательная версия:");
        // создаем задание на 100 вычислений
        Task t = new Task(100);

        for (int i = 0; i < t.getTasksCount(); i++) {
            // генерируем случайные параметры согласно заданию
            // основание логарифма от 1 до 10
            double b = 1 + Math.random() * 9;
            // левая граница от 0 до 100
            double l = Math.random() * 100;
            // правая граница от 100 до 200
            double r = 100 + Math.random() * 100;
            // шаг от 0 до 1
            double st = Math.random();

            // создаем логарифмическую функцию
            Log func = new Log(b);
            // выводим параметры в формате задания
            System.out.printf("Source %.4f %.4f %.4f%n", l, r, st);

            try {
                // вычисляем интеграл
                double res = Functions.integrate(func, l, r, st);
                // выводим результат в формате задания
                System.out.printf("Result %.4f %.4f %.4f %.6f%n", l, r, st, res);
            } catch (Exception ex) {
                // обрабатываем ошибки интегрирования
                System.out.println("Error: " + ex.getMessage());
            }
        }
        System.out.println();
    }

    public static void runSimpleThreads() {
        // задание 3: многопоточная версия
        System.out.println("Простая многопоточная версия:");

        // общее задание для двух потоков
        Task t = new Task(100);

        // создаем и запускаем потоки
        Thread gen = new Thread(new SimpleGenerator(t));
        Thread integ = new Thread(new SimpleIntegrator(t));

        gen.start();
        integ.start();

        try {
            // ожидаем завершения потоков
            gen.join();
            integ.join();
        } catch (InterruptedException ex) {
            System.out.println("Главный поток прерван");
        }
        System.out.println();
    }

    public static void runComplexThreads() {
        // задание 4: усложненная версия с семафором
        System.out.println("Усложненная многопоточная версия:");

        // создаем общие объекты
        Task t = new Task(100);
        threads.Semaphore sem = new threads.Semaphore();

        // создаем специализированные потоки
        Generator gen = new Generator(t, sem);
        Integrator integ = new Integrator(t, sem);

        // запускаем потоки
        gen.start();
        integ.start();

        try {
            // ждем 50 мс
            Thread.sleep(50);

            // прерываем потоки
            gen.interrupt();
            integ.interrupt();

            // ожидаем завершения потоков
            gen.join();
            integ.join();
        } catch (InterruptedException ex) {
            System.out.println("Главный поток прерван");
        }
        System.out.println();
    }
}