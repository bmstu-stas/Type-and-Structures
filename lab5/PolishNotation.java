import java.util.ArrayList;
import java.util.Scanner;

public class PolishNotation { // Объявляем главный класс PolishNotation

    // Проверка оператора является ли символ оператором
    public static boolean isOperator(char c) { // Метод для проверки, является ли символ оператором
        return c == '+' || c == '-' || c == '*' || c == '/'; // Возвращаем true, если символ - оператор (+, -, *, /)
    }

    // Приоритет оператора
    public static int precedence(char c) { // Метод для определения приоритета оператора
        switch (c) { // Используем switch для определения приоритета оператора
            case '+':
            case '-':
                return 1; // Приоритет для + и - равен 1
            case '*':
            case '/':
                return 2; // Приоритет для * и / равен 2
            default:
                return -1; // Возвращаем -1 для других символов (например, скобок)
        }
    }

    // Преобразование инфиксного выражения в постфиксное
    public static String infixToPostfix(String expression) { // Метод для преобразования инфиксного выражения в постфиксное
        StringBuilder result = new StringBuilder(); // Строка для хранения результата
        ArrayList<Character> stack = new ArrayList<>(); // Динамический массив для хранения операторов
        for (int i = 0; i < expression.length(); i++) { // Проходим по каждому символу выражения
            char c = expression.charAt(i);

            if (Character.isLetterOrDigit(c)) { // Если символ - буква или цифра
                result.append(c); // Добавляем его к результату
            } else if (c == '(') { // Если символ - открывающая скобка
                stack.add(c); // Помещаем её в стек (ArrayList)
            } else if (c == ')') { // Если символ - закрывающая скобка
                while (!stack.isEmpty() && stack.get(stack.size() - 1) != '(') { // Пока не найдём открывающую скобку
                    result.append(stack.remove(stack.size() - 1)); // Извлекаем операторы из стека и добавляем к результату
                }
                stack.remove(stack.size() - 1); // Удаляем открывающую скобку из стека
            } else if (isOperator(c)) { // Если символ - оператор
                while (!stack.isEmpty() && precedence(stack.get(stack.size() - 1)) >= precedence(c)) { // Пока в стеке операторы с большим или равным приоритетом
                    result.append(stack.remove(stack.size() - 1)); // Извлекаем их из стека и добавляем к результату
                }
                stack.add(c); // Помещаем текущий оператор в стек
            }
        }
        while (!stack.isEmpty()) { // Извлекаем все оставшиеся операторы из стека
            result.append(stack.remove(stack.size() - 1));
        }
        return result.toString(); // Возвращаем результат
    }

    // Вычисление значения постфиксного выражения
    public static int evaluatePostfix(String expression) { // Метод для вычисления значения постфиксного выражения
        ArrayList<Integer> stack = new ArrayList<>(); // Динамический массив для хранения промежуточных значений
        for (int i = 0; i < expression.length(); i++) { // Проходим по каждому символу выражения
            char c = expression.charAt(i);

            if (Character.isDigit(c)) { // Если символ - цифра
                stack.add(c - '0'); // Преобразуем его в число и помещаем в стек (ArrayList)
            } else if (isOperator(c)) { // Если символ - оператор
                int val2 = stack.remove(stack.size() - 1); // Извлекаем два верхних значения из стека
                int val1 = stack.remove(stack.size() - 1);
                switch (c) { // Выполняем операцию в зависимости от типа оператора
                    case '+':
                        stack.add(val1 + val2); // Складываем и помещаем результат в стек
                        break;
                    case '-':
                        stack.add(val1 - val2); // Вычитаем и помещаем результат в стек
                        break;
                    case '*':
                        stack.add(val1 * val2); // Умножаем и помещаем результат в стек
                        break;
                    case '/':
                        stack.add(val1 / val2); // Делим и помещаем результат в стек
                        break;
                }
            }
        }
        return stack.remove(stack.size() - 1); // Возвращаем итоговое значение, оставшееся в стеке
    }

    // Построение таблицы значений f(x)
    public static void buildFunctionTable(String expression, int start, int end, int step) { // Метод для построения таблицы значений функции
        System.out.println(" x | f(x)"); // Заголовок таблицы
        System.out.println("----------"); // Разделитель
        for (int x = start; x <= end; x += step) { // Проходим по каждому значению x в заданном диапазоне
            String modifiedExpression = expression.replace("x", String.valueOf(x)); // Заменяем x на текущее значение
            String postfix = infixToPostfix(modifiedExpression); // Преобразуем выражение в постфиксное
            int result = evaluatePostfix(postfix); // Вычисляем значение выражения
            System.out.printf("%2d | %3d%n", x, result); // Выводим x и f(x)
        }
    }

    public static void main(String[] args) { // Главный метод программы
        Scanner scanner = new Scanner(System.in); // Создаём объект Scanner для ввода данных
        System.out.println("Введите выражение (с 'x' вместо переменной): "); // Просим пользователя ввести выражение
        String expression = scanner.nextLine(); // Считываем введённое выражение

        System.out.println("Введите начальное значение x: "); // Просим ввести начальное значение x
        int start = scanner.nextInt(); // Считываем начальное значение

        System.out.println("Введите конечное значение x: "); // Просим ввести конечное значение x
        int end = scanner.nextInt(); // Считываем конечное значение

        System.out.println("Введите шаг: "); // Просим ввести шаг изменения x
        int step = scanner.nextInt(); // Считываем шаг

        buildFunctionTable(expression, start, end, step); // Вызываем метод для построения таблицы значений функции
    }
}
