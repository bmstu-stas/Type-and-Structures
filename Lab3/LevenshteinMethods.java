// Импорт библиотеки JFreeChart для генерации графиков
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import javax.swing.JFrame;

// Импорт классов для ввода-вывода, чтобы читать и записывать файлы
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class LevenshteinMethods {
    // Реализация алгоритма Левенштейна итеративно
    public static int levensteinDistanceIterative(String s1, String s2) {
        // Создаем матрицу
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];

        // Инициализация первого столбца и строки
        for (int i = 0; i <= s1.length(); i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= s2.length(); j++) {
            dp[0][j] = j;
        }

        // Заполняем матрицу, вычисляя минимальные затраты на операции (вставку, удаление, замену)
        for (int i = 1; i <= s1.length(); i++) {
            for (int j = 1; j <= s2.length(); j++) {
                int cost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;
                dp[i][j] = Math.min(Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1), dp[i - 1][j - 1] + cost);
            }
        }

        // Возвращаем расстояние Левенштейна, которое находится в нижнем правом углу матрицы
        return dp[s1.length()][s2.length()];
    }

    // Реализация алгоритма Левенштейна рекурсивно
    public static int levensteinDistanceRecursive(String s1, String s2) {
        // Базовые случаи
        if (s1.isEmpty()) {
            return s2.length();
        }
        if (s2.isEmpty()) {
            return s1.length();
        }

        // Вычисляем стоимость замены
        int substitutionCost = (s1.charAt(0) == s2.charAt(0)) ? 0 : 1;

        // Выбираем наименьшую стоимость из трех возможных операций
        return Math.min(Math.min(
                        levensteinDistanceRecursive(s1.substring(1), s2) + 1,
                        levensteinDistanceRecursive(s1, s2.substring(1)) + 1),
                levensteinDistanceRecursive(s1.substring(1), s2.substring(1)) + substitutionCost);
    }

    // Реализация алгоритма Левенштейна рекурсивно с кэш
    public static int levensteinDistanceRecursiveMemo(String s1, String s2, Integer[][] memo) {
        // Проверяем, сохранен ли уже рассчитанный результат в кеше
        if (memo[s1.length()][s2.length()] != null) {
            return memo[s1.length()][s2.length()];
        }

        // Базовые случаи
        if (s1.isEmpty()) {
            memo[s1.length()][s2.length()] = s2.length();
            return s2.length();
        }
        if (s2.isEmpty()) {
            memo[s1.length()][s2.length()] = s1.length();
            return s1.length();
        }

        // Вычисляем стоимость замены
        int substitutionCost = (s1.charAt(0) == s2.charAt(0)) ? 0 : 1;

        // Вычисляем наименьшую стоимость из трех возможных операций и сохраняем результат в кеш
        memo[s1.length()][s2.length()] = Math.min(Math.min(
                        levensteinDistanceRecursiveMemo(s1.substring(1), s2, memo) + 1,
                        levensteinDistanceRecursiveMemo(s1, s2.substring(1), memo) + 1),
                levensteinDistanceRecursiveMemo(s1.substring(1), s2.substring(1), memo) + substitutionCost);

        // Возвращаем сохраненный в кеше результат
        return memo[s1.length()][s2.length()];
    }

    // Реализация алгоритма Дамерау-Левенштейна
    public static int damerauLevenshteinDistance(String s1, String s2) {
        // Создаем матрицу для динамического программирования
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];

        // Инициализация первого столбца и строки
        for (int i = 0; i <= s1.length(); i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= s2.length(); j++) {
            dp[0][j] = j;
        }

        // Заполняем матрицу, вычисляя минимальные затраты на операции
        for (int i = 1; i <= s1.length(); i++) {
            for (int j = 1; j <= s2.length(); j++) {
                int cost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;
                dp[i][j] = Math.min(Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1), dp[i - 1][j - 1] + cost);

                // Обрабатываем возможную транспозицию
                if (i > 1 && j > 1 && s1.charAt(i - 1) == s2.charAt(j - 2) && s1.charAt(i - 2) == s2.charAt(j - 1)) {
                    dp[i][j] = Math.min(dp[i][j], dp[i - 2][j - 2] + cost);
                }
            }
        }

        // Возвращаем результат из нижнего правого угла матрицы
        return dp[s1.length()][s2.length()];
    }

    // Метод для замера времени выполнения алгоритма и сохранения результатов в файл
    public static void computeAndSaveDistance(String s1, String s2, int algorithmChoice) {
        long startTime = System.nanoTime(); // Начало отсчета времени для замера
        int distance = 0; // Переменная для хранения расстояния между строками

        // Выбор и выполнение алгоритма в соответствии с выбором пользователя
        switch (algorithmChoice) {
            case 1:
                distance = levensteinDistanceIterative(s1, s2);
                break;
            case 2:
                distance = levensteinDistanceRecursive(s1, s2);
                break;
            case 3:
                Integer[][] memo = new Integer[s1.length() + 1][s2.length() + 1];
                distance = levensteinDistanceRecursiveMemo(s1, s2, memo);
                break;
            case 4:
                distance = damerauLevenshteinDistance(s1, s2);
                break;
            default:
                System.out.println("Неверный выбор алгоритма. Выберите число от 1 до 4.");
                return;
        }

        long endTime = System.nanoTime(); // Окончание замера времени
        long duration = endTime - startTime; // Вычисление затраченного времени

        // Выводим информацию о замере в консоль
        System.out.println("Выполнен: " + getMethodName(algorithmChoice) +
                "\nРасстояние: " + distance +
                "\nВремя выполнения (наносекунды): " + duration);

        // Сохраняем результаты в файл для последующего построения графика
        saveTimeToFile(duration, getMethodName(algorithmChoice), s1, s2, distance);
    }

    // Вспомогательный метод для получения строки с именем метода
    private static String getMethodName(int choice) {
        switch (choice) {
            case 1: return "LevenshteinIterative";
            case 2: return "LevenshteinRecursive";
            case 3: return "LevenshteinRecursiveMemo";
            case 4: return "DamerauLevenshtein";
            default: return "UnknownMethod";
        }
    }

    // Метод для сохранения замера времени в файл
    private static void saveTimeToFile(long duration, String methodName, String s1, String s2, int distance) {
        // Определяем имя файла на основе метода
        String fileName = methodName + "Time.txt";

        try (FileWriter writer = new FileWriter(fileName, true)) {
            // Форматируем строку с результатами и записываем ее в файл
            writer.write(String.format("%s, %d, %d, %d\n",
                    methodName, s1.length() + s2.length(), distance, duration));
        } catch (IOException e) {
            System.err.println("Ошибка при записи в файл: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Метод для построения графиков на основе записанных данных
    public static void plotGraphs() {
        // Создаем набор данных для графика
        DefaultCategoryDataset dataset =  new DefaultCategoryDataset();

        // Определяем имена методов, для которых строим графики
        String[] methods = {"LevenshteinIterative", "LevenshteinRecursive", "LevenshteinRecursiveMemo", "DamerauLevenshtein"};

        // Находим минимальное значение времени выполнения среди всех методов
        long minTime = Long.MAX_VALUE;
        for (String method : methods) {
            String fileName = method + "Time.txt";

            try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
                String line;

                // Чтение файла построчно
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(", ");
                    long time = Long.parseLong(parts[3]);
                    minTime = Math.min(minTime, time); // Обновляем минимальное значение времени
                }
            } catch (IOException e) {
                System.err.println("Ошибка при чтении из файла: " + e.getMessage());
                e.printStackTrace();
            }
        }

        // Читаем данные из файлов и добавляем их в набор данных
        for (String method : methods) {
            String fileName = method + "Time.txt";

            try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
                String line;

                // Чтение файла построчно
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(", ");
                    // Преобразуем длину строк и время выполнения из строки в числа
                    int length = Integer.parseInt(parts[1]);
                    long time = Long.parseLong(parts[3]);
                    // Отнимаем минимальное значение времени выполнения
                    time -= minTime;
                    // Добавляем данные в набор для графика
                    dataset.addValue(time, method, Integer.toString(length));
                }
            } catch (IOException e) {
                System.err.println("Ошибка при чтении из файла: " + e.getMessage());
                e.printStackTrace();
            }
        }

        // Создаем линейный график с использованием библиотеки JFreeChart
        JFreeChart chart = ChartFactory.createLineChart(
                "Сравнение времени выполнения алгоритмов", // Заголовок графика
                "Суммарная длина строк", // Подпись оси X
                "Время выполнения (наносекунды)", // Подпись оси Y
                dataset, // Источник данных
                PlotOrientation.VERTICAL, // Ориентация графика
                true, // Показывать легенду
                true, // Tooltips
                false // URLs
        );

        // Отображаем график в окне
        JFrame frame = new JFrame("Графики времени выполнения");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Установка операции при закрытии
        frame.add(new ChartPanel(chart)); // Добавление графика в панель
        frame.pack(); // Автоматическая установка размера окна
        frame.setLocationRelativeTo(null); // Расположение окна по центру экрана
        frame.setVisible(true); // Отображение окна
    }
}
