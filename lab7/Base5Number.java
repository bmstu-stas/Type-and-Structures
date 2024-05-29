import java.util.Arrays;

public class Base5Number {
    private static final int BASE = 5; // База системы счисления (5)
    private static final int LENGTH = 5; // Количество разрядов (5)
    private int[] digits; // Массив для хранения цифр числа

    // преобразования из десятичного числа в пятиричное
    public Base5Number(int number) {
        if(number<0 || number>=Math.pow(5,5))
        {
            throw new IllegalArgumentException("Некорректное число для пятиричной системы: ");
        }
//        String base5String = Integer.toString(number);
//        for (char digitChar : base5String.toCharArray()) {
//            int digit = Character.getNumericValue(digitChar);
//            if (digit < 0 || digit >= BASE) {
//                throw new IllegalArgumentException("Некорректное число для пятиричной системы: " + number);
//            }
        //}

        digits = new int[LENGTH]; // Инициализация массива длиной 5
        for (int i = 0; i < LENGTH; i++) { // Цикл для заполнения массива цифрами числа в пятиричной системе
//            if (number < 0) {
//                throw new IllegalArgumentException("Число должно быть неотрицательным"); // Исключение, если число отрицательное
//            }
            digits[i] = number % BASE; // Записываем текущую цифру (остаток от деления на 5)
            number /= BASE; // Делим число на 5 для получения следующей цифры
        }
    }

    // Конструктор, принимающий массив цифр
    public Base5Number(int[] digits) {
        if (digits.length != LENGTH) { // Проверка длины массива
            throw new IllegalArgumentException("Массив цифр должен быть длиной " + LENGTH); // Исключение, если длина массива неверна
        }
        for (int digit : digits) { // Проверка каждого элемента массива
            if (digit < 0 || digit >= BASE) { // Если цифра меньше 0 или больше или равна 5
                throw new IllegalArgumentException("Цифры должны быть в диапазоне от 0 до " + (BASE - 1)); // Исключение, если цифра некорректна
            }
        }
        this.digits = digits; // Инициализация объекта переданным массивом
    }

    // Метод для сложения двух чисел в пятиричной системе
    public Base5Number add(Base5Number other) {
        int carry = 0; // Переменная для хранения переноса при сложении
        int[] result = new int[LENGTH]; // Массив для хранения результата сложения
        for (int i = 0; i < LENGTH; i++) { // Цикл по всем разрядам
            int sum = this.digits[i] + other.digits[i] + carry; // Суммируем соответствующие разряды и перенос
            result[i] = sum % BASE; // Записываем текущую цифру результата (остаток от деления на 5)
            carry = sum / BASE; // Обновляем перенос (целая часть от деления на 5)
        }
        return new Base5Number(result); // Возвращаем новый объект с результатом сложения
    }

    // Метод для вычитания двух чисел в пятиричной системе
    public Base5Number subtract(Base5Number other) {
        int borrow = 0; // Переменная для хранения займа при вычитании
        int[] result = new int[LENGTH]; // Массив для хранения результата вычитания
        for (int i = 0; i < LENGTH; i++) { // Цикл по всем разрядам
            int diff = this.digits[i] - other.digits[i] - borrow; // Вычитаем соответствующие разряды и займ
            if (diff < 0) { // Если результат отрицательный, делаем займ
                diff += BASE; // Увеличиваем разряд на базу (5)
                borrow = 1; // Устанавливаем займ
            } else {
                borrow = 0; // Обнуляем займ, если результат не отрицательный
            }
            result[i] = diff; // Записываем текущую цифру результата
        }
        return new Base5Number(result); // Возвращаем новый объект с результатом вычитания
    }

    // Метод для умножения двух чисел в пятиричной системе
    public Base5Number multiply(Base5Number other) {
        int[] result = new int[LENGTH]; // Массив для хранения результата умножения
        for (int i = 0; i < LENGTH; i++) { // Цикл по разрядам первого числа
            int carry = 0; // Переменная для хранения переноса при умножении
            for (int j = 0; j + i < LENGTH; j++) { // Цикл по разрядам второго числа
                int product = this.digits[i] * other.digits[j] + result[i + j] + carry; // Умножаем разряды, добавляем текущий результат и перенос
                result[i + j] = product % BASE; // Записываем текущую цифру результата (остаток от деления на 5)
                carry = product / BASE; // Обновляем перенос (целая часть от деления на 5)
            }
        }
        return new Base5Number(result); // Возвращаем новый объект с результатом умножения
    }

    // Метод для деления двух чисел в пятиричной системе
    public Base5Number divide(Base5Number other) {
        int thisDecimal = this.toDecimal(); // Преобразуем текущее число в десятичную систему
        int otherDecimal = other.toDecimal(); // Преобразуем другое число в десятичную систему
        int resultDecimal = thisDecimal / otherDecimal; // Выполняем целочисленное деление в десятичной системе
        return new Base5Number(resultDecimal); // Преобразуем результат обратно в пятиричную систему и возвращаем новый объект
    }



    // Метод для нахождения остатка от деления двух чисел в пятиричной системе
    public Base5Number mod(Base5Number other) {
        int thisDecimal = this.toDecimal(); // Преобразуем текущее число в десятичную систему
        int otherDecimal = other.toDecimal(); // Преобразуем другое число в десятичную систему
        int resultDecimal = thisDecimal % otherDecimal; // Выполняем нахождение остатка от деления в десятичной системе
        return new Base5Number(resultDecimal); // Преобразуем результат обратно в пятиричную систему и возвращаем новый объект
    }

    // Метод для преобразования числа из пятиричной системы в десятичную
    public int toDecimal() {
        int result = 0; // Переменная для хранения результата
        for (int i = LENGTH - 1; i >= 0; i--) { // Цикл по всем разрядам в обратном порядке
            result = result * BASE + digits[i]; // Умножаем текущий результат на базу и добавляем текущий разряд
        }
        return result; // Возвращаем результат в десятичной системе
    }

    // Метод для вывода числа в пятиричной системе в виде строки
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(); // Строитель строк для формирования результата
        for (int i = LENGTH - 1; i >= 0; i--) { // Цикл по всем разрядам в обратном порядке
            sb.append(digits[i]); // Добавляем текущий разряд к строке
        }
        return sb.toString(); // Возвращаем сформированную строку
    }

    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Base5Number other = (Base5Number) obj;
        return Arrays.equals(digits, other.digits);
    }

    public int compareTo(Base5Number other) {
        return Integer.compare(this.toDecimal(), other.toDecimal());
    }

    public static void main(String[] args) {
        try {
            Base5Number num1 = new Base5Number(123); // Создаем объект с числом 666 в десятичной системе
            Base5Number num2 = new Base5Number(123); // Создаем объект с числом 123 в десятичной системе

            // Вывод чисел в пятиричной системе
            System.out.println("Число 1: " + num1); // Вывод числа 1
            System.out.println("Число 2: " + num2); // Вывод числа 2



            // Сложение чисел
            Base5Number sum = num1.add(num2); // Сложение чисел
            System.out.println("Сумма: " + sum); // Вывод результата сложения

            // Вычитание чисел
            Base5Number difference = num1.subtract(num2); // Вычитание чисел
            System.out.println("Разность: " + difference); // Вывод результата вычитания

            //Сравнение

            // Умножение чисел
            Base5Number product = num1.multiply(num2); // Умножение чисел
            System.out.println("Произведение: " + product); // Вывод результата умножения

            //



            // Деление чисел
            Base5Number quotient = num1.divide(num2); // Деление чисел
            System.out.println("Частное: " + quotient); // Вывод результата деления

            // Остаток от деления чисел
            Base5Number remainder = num1.mod(num2); // Нахождение остатка от деления чисел
            System.out.println("Остаток: " + remainder); // Вывод результата нахождения остатка

            System.out.println("Число " + num1 + " равно "+ num2 +" "+ num1.equals(num2));
            System.out.println("Число " + num1 + " больше "+ num2 +" " + (num1.compareTo(num2) > 0));
            System.out.println("Число " + num1 + " больше "+ num2 +" " + (num1.compareTo(num2) < 0));

        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage()); // Вывод сообщения об ошибке
        }
    }


}
