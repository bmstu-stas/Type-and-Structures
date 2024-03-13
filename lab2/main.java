import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Создание объекта Scanner для считывания ввода пользователя
        Scanner scanner = new Scanner(System.in);

        // Запрос у пользователя о необходимости рассчитать интеграл с заданной точностью
        System.out.println("Вы хотите рассчитать интеграл с точностью? (да/нет):");
        String response = scanner.next();
        // Преобразование ответа пользователя в логическую переменную
        boolean withPrecision = response.equalsIgnoreCase("да");

        // Запрос начала и конца интервала интегрирования
        System.out.print("Введите начало интервала: ");
        double a = scanner.nextDouble(); // Чтение начала интервала
        System.out.print("Введите конец интервала: ");
        double b = scanner.nextDouble(); // Чтение конца интервала

        // Запрос количества разбиений для расчета интеграла
        System.out.println("Количество разбиений для расчетов без точности: ");
        int n = scanner.nextInt(); // Чтение количества разбиений

        // Запрос заданной точности для расчетов (не используется в данной версии кода)
        System.out.print("Заданная точность для расчетов: ");
        double precision = scanner.nextDouble(); // Чтение заданной точности

        // Вычисление интеграла тремя методами: левых прямоугольников, правых прямоугольников и трапеций
        double resultLeft = IntegrationMethods.leftRectangleMethod(a, b, n);
        double resultRight = IntegrationMethods.rightRectangleMethod(a, b, n);
        double resultTrapezoidal = IntegrationMethods.trapezoidalMethod(a, b, n);

        // Вывод результатов с округлением до трех знаков после запятой
        System.out.println("Метод левых прямоугольников: " + Math.round(resultLeft * 1000) / 1000.0);
        System.out.println("Метод правых прямоугольников: " + Math.round(resultRight * 1000) / 1000.0);
        System.out.println("Метод трапеций: " + Math.round(resultTrapezoidal * 1000) / 1000.0);

        // Закрытие сканера
        scanner.close();
    }
}
