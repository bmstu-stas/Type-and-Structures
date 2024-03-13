public class IntegrationMethods {
    // Расчет интеграла методом левых прямоугольников
    public static double leftRectangleMethod(double a, double b, int n) {
        double h = (b - a) / n; // Вычисление ширины каждого прямоугольника
        double sum = 0; // Инициализация суммы для накопления результата
        //

        for (int i = 0; i < n; i++) { //

            double x = a + i * h;// Суммирование значений функции в левых точках прямоугольников
            if (x < a) x = a; // Корректировка последней точки, если вышли за пределы интервала
            sum += Math.exp(x);
        }
        return sum * h+ Math.exp((a + h * (n-1))) * (b - (a + h * (n-1)))
                ; // Возвращение результата, умноженного на ширину прямоугольника
    }

    // Расчет интеграла методом правых прямоугольников с учетом последнего неполного отрезка
    public static double rightRectangleMethod(double a, double b, int n) {
        double h = (b - a) / n; // Вычисление ширины каждого прямоугольника
        double sum = 0; // Инициализация суммы для накопления результата
        //double x = a;
        for (int i = 1; i < n /*не доходя одного шага до правой гр.*/; i++) {
            double x = a + i * h; // Вычисление правой точки прямоугольника
            //x += h;
            if (x > b) x = b; // Корректировка последней точки, если вышли за пределы интервала
            sum += Math.exp(x); // Суммирование значений функции в правых точках прямоугольников
        }
        // в цикле рассчитали sum как все площади без одной, пока что не домноженные на h
        return sum * h + /*последняя площадь*/ Math.exp(b) * (b - (a + h * (n-1)))
        //                                                 h* = b минус предпоследняя точка на отрезке
                ; // Возвращение результата, умноженного на ширину прямоугольника
    }

    // Расчет интеграла методом трапеций
    public static double trapezoidalMethod(double a, double b, int n) {
        double h = (b - a) / n; // Вычисление ширины основания трапеций
        double sum = 0.5 * (Math.exp(a) + Math.exp(b)); // Инициализация суммы с учетом значений на концах интервала
        for (int i = 1; i < n; i++) {
            sum += Math.exp(a + i * h); // Суммирование значений функции во всех промежуточных точках
        }
        return sum * h; // Возвращение результата, умноженного на ширину основания трапеции
    }
}
