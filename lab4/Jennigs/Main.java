// Импорт класса File для работы с файлами.
import java.io.File;
// Импорт класса для обработки исключений при отсутствии файла.
import java.io.FileNotFoundException;
// Импорт класса ArrayList для использования динамических массивов.
import java.util.ArrayList;
// Импорт класса Scanner для чтения данных из файла.
import java.util.Scanner;

// Определение класса SparseMatrix для представления разреженной матрицы.
class SparseMatrix {
    // Количество строк матрицы.
    int rows;
    // Количество столбцов матрицы.
    int cols;
    // Список элементов матрицы, хранящий объекты Element.
    ArrayList<Element> elements;

    // Конструктор класса SparseMatrix.
    SparseMatrix(int rows, int cols) {
        this.rows = rows;  // Инициализация числа строк.
        this.cols = cols;  // Инициализация числа столбцов.
        this.elements = new ArrayList<>();  // Инициализация списка для хранения элементов.
    }

    // Метод для добавления элемента в матрицу.
    void  addElement(int row, int col, int value) {
        for (Element elem : elements) {  // Перебор всех элементов в списке.
            if (elem.row == row && elem.col == col) {  // Проверка совпадения позиции элемента.
                elem.value += value;  // Увеличение значения элемента, если он уже существует.
                return;  // Выход из метода.
            }
        }
        // Добавление элемента, если он новый.
        if (value != 0) {
            elements.add(new Element(row, col, value));
        } else {
            // Проверка условий для добавления нулевого элемента.
            boolean hasNonZeroLeft = false, hasNonZeroRight = false;
            for (Element e : elements) {
//                if ((e.row == row && e.value != 0)|| ()) {
//                    if (e.col < col) hasNonZeroLeft = true;
//                    if (e.col > col) hasNonZeroRight = true;
//                }

                if (e.row == row) {
                    if (e.col < col && e.value != 0) hasNonZeroLeft = true;
                    if (e.col > col /*&& (e.value != 0 ||
                            e.value==0 )*/) hasNonZeroRight = true;
                }
            }
            if (hasNonZeroLeft && hasNonZeroRight) {
                elements.add(new Element(row, col, 0));
            }
        }
    }

    // Метод для сложения двух разреженных матриц.
    static SparseMatrix add(SparseMatrix a, SparseMatrix b) {
        SparseMatrix result = new SparseMatrix(a.rows, a.cols);  // Создание новой матрицы для результатов.
        for (Element el : a.elements) {
            result.addElement(el.row, el.col, el.value);  // Добавление элементов первой матрицы.
        }
        for (Element el : b.elements) {
            result.addElement(el.row, el.col, el.value);  // Добавление элементов второй матрицы.
        }
        return result;  // Возвращение результата сложения.
    }

    // Метод для вывода сжатой матрицы.
    void printCompressed() {
        System.out.println("Сжатая матрица:");
        for (Element el : elements) {
            System.out.println(el.row + " " + el.col + " " + el.value);  // Вывод каждого элемента.
        }
    }

    // Метод для вывода полной матрицы.
    void printFull() {
        System.out.println("Полная матрица:");
        int[][] full = new int[rows][cols];  // Создание полной матрицы.
        for (Element el : elements) {
            full[el.row][el.col] = el.value;  // Заполнение матрицы значениями.
        }
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(full[i][j] + " ");  // Вывод значений матрицы.
            }
            System.out.println();
        }
    }
}

// Класс Element для хранения элементов матрицы.
class Element {
    int row;  // Индекс строки элемента.
    int col;  // Индекс столбца элемента.
    int value;  // Значение элемента.

    // Конструктор класса Element.
    Element(int row, int col, int value) {
        this.row = row;  // Инициализация строки.
        this.col = col;  // Инициализация столбца.
        this.value = value;  // Инициализация значения.
    }
}

// Основной класс программы.
public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        // Чтение матриц из файла.
        SparseMatrix matrix1 = readMatrixFromFile("D:\\TempProjects\\Jennings\\src\\matrix1.txt");
        SparseMatrix matrix2 = readMatrixFromFile("D:\\TempProjects\\Jennings\\src\\matrix2.txt");

        // Вывод матриц и результатов.
        System.out.println("Матрица 1:");
        matrix1.printCompressed();
        System.out.println("Матрица 2:");
        matrix2.printCompressed();

        SparseMatrix result = SparseMatrix.add(matrix1, matrix2);
        System.out.println("Результат (Сжатая):");
        result.printCompressed();
        System.out.println("Результат (Полная):");
        result.printFull();
    }

    // Метод для чтения матрицы из файла.
    static SparseMatrix readMatrixFromFile(String filename) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(filename));  // Создание объекта Scanner для чтения файла.
        int rows = scanner.nextInt();  // Чтение количества строк.
        int cols = scanner.nextInt();  // Чтение количества столбцов.
        SparseMatrix matrix = new SparseMatrix(rows, cols);  // Создание матрицы.
        while (scanner.hasNextInt()) {
            int row = scanner.nextInt();  // Чтение строки элемента.
            int col = scanner.nextInt();  // Чтение столбца элемента.
            int value = scanner.nextInt();  // Чтение значения элемента.
            matrix.addElement(row, col, value);  // Добавление элемента в матрицу.
        }
        scanner.close();  // Закрытие сканера.
        return matrix;  // Возвращение считанной матрицы.
    }
}
