// Импортируем класс Scanner для считывания ввода с консоли
import java.util.Scanner;

// Основной класс программы
public class Main {
    // Главный метод программы
    public static void main(String[] args) {
        // Создаем объект Scanner для чтения ввода пользователя
        Scanner scanner = new Scanner(System.in);

        // Выводим меню выбора действия пользователю
        System.out.println("Выберите действие:");
        System.out.println("1. Вычислить расстояния между строками");
        System.out.println("2. Построить графики");

        // Считываем выбор пользователя
        int choice = scanner.nextInt();
        scanner.nextLine(); // Очищаем буфер после ввода числа

        // В зависимости от выбора пользователя вызываем соответствующий метод
        if (choice == 1) {
            // Если выбрано вычисление расстояния
            computeDistances(scanner);
        } else if (choice == 2) {
            // Если выбрано построение графиков
            LevenshteinMethods.plotGraphs();
        } else {
            // Если выбор не соответствует предложенным опциям
            System.out.println("Неверный выбор. Программа завершена.");
        }
    }

    // Метод для вычисления расстояний между строками
    private static void computeDistances(Scanner scanner) {
        // Запрашиваем у пользователя ввод двух строк
        System.out.println("Введите первую строку:");
        String s1 = scanner.nextLine(); // Считываем первую строку

        System.out.println("Введите вторую строку:");
        String s2 = scanner.nextLine(); // Считываем вторую строку

        // Предлагаем пользователю выбрать алгоритм для вычисления расстояния
        System.out.println("Выберите алгоритм:");
        System.out.println("1. Левенштейн (цикл)");
        System.out.println("2. Левенштейн (рекурсия)");
        System.out.println("3. Левенштейн (рекурсия с кэшом)");
        System.out.println("4. Дамерау-Левенштейн");

        // Считываем выбор алгоритма
        int algorithmChoice = scanner.nextInt();

        // Вызываем метод из класса LevenshteinMethods для вычисления расстояния и замера времени выполнения
        LevenshteinMethods.computeAndSaveDistance(s1, s2, algorithmChoice);
    }
}
