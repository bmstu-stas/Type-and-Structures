import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

// схема Рейнбольдта-Местеньи
class RingMatrix {
    int[] AN; // Массив ненулевых элементов
    int[] NR; // Индексы следующих элементов по строке
    int[] NC; // Индексы следующих элементов по столбцу
    int[] JR; // Точки входа в строки
    int[] JC; // Точки входа в столбцы
    int rows; // Количество строк в матрице
    int cols; // Количество столбцов в матрице
    int nnz; // Количество ненулевых элементов

    // Конструктор для инициализации разреженной матрицы
    RingMatrix(int rows, int cols, int maxNnz) {
        this.rows = rows; // Установка количества строк
        this.cols = cols; // Установка количества столбцов
        this.nnz = 0; // Инициализация количества ненулевых элементов
        AN = new int[maxNnz]; // Создание массива ненулевых элементов с максимальным размером
        NR = new int[maxNnz]; // Создание массива индексов следующих элементов по строке с максимальным размером
        NC = new int[maxNnz]; // Создание массива индексов следующих элементов по столбцу с максимальным размером
        JR = new int[rows]; // Создание массива точек входа в строки с размером, равным количеству строк
        JC = new int[cols]; // Создание массива точек входа в столбцы с размером, равным количеству столбцов

        // Инициализация массива JR значениями -1 (отсутствие элементов в строках)
        for (int i = 0; i < rows; i++) {
            JR[i] = -1;
        }
        // Инициализация массива JC значениями -1 ( отсутствие элементов в столбцах)
        for (int i = 0; i < cols; i++) {
            JC[i] = -1;
        }
    }

    // Добавления элемента в матрицу
    void addElement(int row, int col, int value) {
        if (value == 0) return; // Пропуск нулевых элементов

        AN[nnz] = value; // Добавление значения в массив ненулевых элементов
        int nnzIndex = nnz++; // Увеличение счетчика ненулевых элементов и сохранение текущего индекса

        // Обработка строк
        if (JR[row] == -1) { // Если строка пустая
            JR[row] = nnzIndex; // Установка точки входа в строку на текущий индекс ненулевого элемента
            NR[nnzIndex] = nnzIndex; // Замыкание на самого себя (кольцо)
        } else { // Если строка не пустая
            int curIndex = JR[row]; // Текущий индекс элемента строки
            while (NR[curIndex] != JR[row]) { // Поиск последнего элемента строки
                curIndex = NR[curIndex];
            }
            NR[curIndex] = nnzIndex; // Установка следующего элемента на текущий индекс
            NR[nnzIndex] = JR[row]; // Замыкание на начальный элемент строки
        }

        // Обработка столбцов
        if (JC[col] == -1) { // Если столбец пустой
            JC[col] = nnzIndex; // Установка точки входа в столбец на текущий индекс ненулевого элемента
            NC[nnzIndex] = nnzIndex; // Замыкание на самого себя
        } else { // Если столбец не пустой
            int curIndex = JC[col]; // Текущий индекс элемента столбца
            while (NC[curIndex] != JC[col]) { // Поиск последнего элемента столбца
                curIndex = NC[curIndex];
            }
            NC[curIndex] = nnzIndex; // Установка следующего элемента на текущий индекс
            NC[nnzIndex] = JC[col]; // Замыкание на начальный элемент столбца
        }
    }

    // Метод для печати матрицы
    void printMatrix() {
        for (int i = 0; i < rows; i++) { // Проход по всем строкам
            if (JR[i] != -1) { // Если строка не пустая
                int curIndex = JR[i]; // Установка текущего индекса на точку входа в строку
                do {
                    System.out.println("Элемент на (" + i + "," + getColumnIndex(curIndex) + ") = " + AN[curIndex]); // Печать элемента
                    curIndex = NR[curIndex]; // Переход к следующему элементу в строке
                } while (curIndex != JR[i]); // Пока не вернемся к началу строки
            }
        }
    }

    // Получение индекса столбца по индексу ненулевого элемента
    int getColumnIndex(int nnzIndex) {
        for (int i = 0; i < cols; i++) { // Проход по всем столбцам
            if (JC[i] == nnzIndex || NC[JC[i]] == nnzIndex) { // Если найден индекс ненулевого элемента
                return i; // Возвращаем индекс столбца
            }
        }
        return -1; // Возврат -1, если индекс не найден
    }

    // Метод для получения индекса строки по индексу ненулевого элемента
    int getRowIndex(int nnzIndex) {
        for (int i = 0; i < rows; i++) { // Проход по всем строкам
            if (JR[i] == nnzIndex || NR[JR[i]] == nnzIndex) { // Если найден индекс ненулевого элемента
                return i; // Возвращаем индекс строки
            }
        }
        return -1; // Возврат -1, если индекс не найден
    }

    // Метод для сложения двух разреженных матриц
    static RingMatrix add(RingMatrix a, RingMatrix b) {
        if (a.rows != b.rows || a.cols != b.cols) { // Проверка на совпадение размеров матриц
            throw new IllegalArgumentException("Размеры матриц не совпадают."); // Исключение, если размеры не совпадают
        }
        RingMatrix result = new RingMatrix(a.rows, a.cols, a.nnz + b.nnz); // Создание результирующей матрицы
        for (int i = 0; i < a.nnz; i++) { // Проход по всем ненулевым элементам первой матрицы
            int row = a.getRowIndex(i); // Получение индекса строки элемента
            int col = a.getColumnIndex(i); // Получение индекса столбца элемента
            result.addElement(row, col, a.AN[i]); // Добавление элемента в результирующую матрицу
        }
        for (int i = 0; i < b.nnz; i++) { // Проход по всем ненулевым элементам второй матрицы
            int row = b.getRowIndex(i); // Получение индекса строки элемента
            int col = b.getColumnIndex(i); // Получение индекса столбца элемента
            int existingValue = result.getValueAt(row, col); // Получение текущего значения в результирующей матрице
            result.addElement(row, col, existingValue + b.AN[i]); // Сложение значений и добавление в результирующую матрицу
        }
        return result; // Возврат результирующей матрицы
    }

    // Метод для умножения двух разреженных матриц
    static RingMatrix multiply(RingMatrix a, RingMatrix b) {
        if (a.cols != b.rows) { // Проверка на совместимость размеров для умножения
            throw new IllegalArgumentException("Размеры матриц несовместимы для умножения."); // Исключение, если размеры не совместимы
        }
        RingMatrix result = new RingMatrix(a.rows, b.cols, a.nnz * b.nnz); // Создание результирующей матрицы
        for (int i = 0; i < a.nnz; i++) { // Проход по всем ненулевым элементам первой матрицы
            int rowA = a.getRowIndex(i); // Получение индекса строки элемента
            int colA = a.getColumnIndex(i); // Получение индекса столбца элемента
            for (int j = 0; j < b.nnz; j++) { // Проход по всем ненулевым элементам второй матрицы
                int rowB = b.getRowIndex(j); // Получение индекса строки элемента
                int colB = b.getColumnIndex(j); // Получение индекса столбца элемента
                if (colA == rowB) { // Условие умножения: индекс столбца первого элемента равен индексу строки второго
                    int productValue = a.AN[i] * b.AN[j]; // Вычисление произведения
                    int existingValue = result.getValueAt(rowA, colB); // Получение текущего значения в результирующей матрице
                    result.addElement(rowA, colB, existingValue + productValue); // Добавление результата умножения в результирующую матрицу
                }
            }
        }
        return result; // Возврат результирующей матрицы
    }

    // Метод для получения значения элемента по его строке и столбцу
    int getValueAt(int row, int col) {
        if (JR[row] != -1) { // Если строка не пустая
            int curIndex = JR[row]; // Установка текущего индекса на точку входа в строку
            do {
                if (getColumnIndex(curIndex) == col) { // Если индекс столбца элемента совпадает с искомым
                    return AN[curIndex]; // Возвращение значения элемента
                }
                curIndex = NR[curIndex]; // Переход к следующему элементу в строке
            } while (curIndex != JR[row]); // Пока не вернемся к началу строки
        }
        return 0; // Возврат 0, если элемент не найден
    }
}

// Главный класс с методом main
public class Main {
    public static void main(String[] args) {
        try {
            RingMatrix matrix1 = readMatrixFromFile("D:\\TempProjects\\Rendbolt3_final\\src\\matrix1.txt"); // Чтение первой матрицы из файла
            RingMatrix matrix2 = readMatrixFromFile("D:\\TempProjects\\Rendbolt3_final\\src\\matrix2.txt"); // Чтение второй матрицы из файла

            // Отображение исходных матриц
            System.out.println("Исходная матрица 1:");
            matrix1.printMatrix(); // Печать первой матрицы
            System.out.println("Исходная матрица 2:");
            matrix2.printMatrix(); // Печать второй матрицы

            RingMatrix sumMatrix = RingMatrix.add(matrix1, matrix2); // Сложение матриц
            RingMatrix productMatrix = RingMatrix.multiply(matrix1, matrix2); // Умножение матриц

            // Отображение результирующих матриц
            System.out.println("Суммарная матрица:");
            sumMatrix.printMatrix(); // Печать суммарной матрицы
            System.out.println("Произведение матриц:");
            productMatrix.printMatrix(); // Печать произведения матриц
        } catch (FileNotFoundException e) { // Обработка исключения, если файл не найден
            System.err.println("Ошибка: Файл не найден - " + e.getMessage()); // Печать сообщения об ошибке
        } catch (Exception e) { // Обработка других исключений
            System.err.println("Ошибка: " + e.getMessage()); // Печать сообщения об ошибке
        }
    }

    // Метод для чтения матрицы из файла
    static RingMatrix readMatrixFromFile(String filename) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(filename)); // Создание сканера для чтения файла
        int rows = scanner.nextInt(); // Чтение количества строк
        int cols = scanner.nextInt(); // Чтение количества столбцов
        int maxNnz = scanner.nextInt(); // Чтение максимального количества ненулевых элементов
        RingMatrix matrix = new RingMatrix(rows, cols, maxNnz); // Создание матрицы
        while (scanner.hasNextInt()) { // Пока в файле есть целые числа
            int row = scanner.nextInt(); // Чтение строки элемента
            int col = scanner.nextInt(); // Чтение столбца элемента
            int value = scanner.nextInt(); // Чтение значения элемента
            matrix.addElement(row, col, value); // Добавление элемента в матрицу
        }
        scanner.close(); // Закрытие сканера
        return matrix; // Возврат матрицы
    }
}
