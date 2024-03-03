import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.DefaultXYDataset;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class MethodsMatrix {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Random random = new Random();

    public static int[][] matrix(int m, int n) {
        int[][] matrix = new int[m][n];
        System.out.println("Выберите режим заполнения матрицы:");
        System.out.println("1. Вручную");
        System.out.println("2. Случайные значения");
        int mode = scanner.nextInt();

        switch (mode) {
            case 1:
                manualFill(matrix);
                break;
            case 2:
                randomFill(matrix);
                break;
            default:
                System.out.println("Неверный режим. Используются случайные значения.");
                randomFill(matrix);
                break;
        }
        return matrix;
    }

    private static void manualFill(int[][] matrix) {
        System.out.println("Введите элементы матрицы:");
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                matrix[i][j] = scanner.nextInt();
            }
        }
    }

    private static void randomFill(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                matrix[i][j] = random.nextInt(10);
            }
        }
    }

    public static void printMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            for (int element : row) {
                System.out.printf("%5d", element);
            }
            System.out.println();
        }
    }

    public static int[][] mulitMatrix(int[][] A, int[][] B) {
        int mA = A.length;
        int nA = A[0].length;
        int mB = B.length;
        int nB = B[0].length;

        if (nA != mB) {
            System.out.println("Матрицы не подходят для операции умножения по размеру.");
            return null;
        }

        int[][] C = new int[mA][nB];
        for (int i = 0; i < mA; i++) {
            for (int j = 0; j < nB; j++) {
                for (int k = 0; k < nA; k++) {
                    C[i][j] += A[i][k] * B[k][j];
                }
            }
        }
        return C;
    }

    public static int[][] mulitMatrixVinograd(int[][] A, int[][] B) {
        int mA = A.length;
        int nA = A[0].length;
        int mB = B.length;
        int nB = B[0].length;

        if (nA != mB) {
            System.out.println("Матрицы не подходят для операции умножения по размеру.");
            return null;
        }

        int[][] C = new int[mA][nB];
        int[] mulH = new int[mA];
        int[] mulV = new int[nB];

        for (int i = 0; i < mA; i++) {
            for (int j = 0; j < nA / 2; j++) {
                mulH[i] += A[i][2 * j] * A[i][2 * j + 1];
            }
        }

        for (int i = 0; i < nB; i++) {
            for (int j = 0; j < mB / 2; j++) {
                mulV[i] += B[2 * j][i] * B[2 * j + 1][i];
            }
        }

        for (int i = 0; i < mA; i++) {
            for (int j = 0; j < nB; j++) {
                int buf = -(mulH[i] + mulV[j]);
                for (int k = 0; k < nA / 2; k++) {
                    buf += (A[i][2 * k + 1] + B[2 * k][j]) * (A[i][2 * k] + B[2 * k + 1][j]);
                }
                C[i][j] = buf;
            }
        }

        if (nA % 2 != 0) {
            for (int i = 0; i < mA; i++) {
                for (int j = 0; j < nB; j++) {
                    C[i][j] += A[i][nA - 1] * B[nA - 1][j];
                }
            }
        }

        return C;
    }

    public static void saveTimeToFile(long duration, int method, int[][] A, int[][] B) {
        String methodName = (method == 1) ? "NormalMethod" : "VinogradMethod";
        try {
            FileWriter writer = new FileWriter(methodName + "Time.txt", true);
            writer.write("Method: " + methodName + ", Duration: " + duration + "ns, Matrix A size: " + A.length + "x" + A[0].length + ", Matrix B size: " + B.length + "x" + B[0].length + "\n");
            writer.close();
            System.out.println("Время выполнения и размеры матриц сохранены в файл " + methodName + "Time.txt");
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении времени выполнения и размеров матриц в файл.");
            e.printStackTrace();
        }
    }

    public static void plotGraphs() {
        try {
            String fileName = "NormalMethodTime.txt";
            double[][] dataNormal = readDataFromFile(fileName);
            fileName = "VinogradMethodTime.txt";
            double[][] dataVinograd = readDataFromFile(fileName);

            if (dataNormal != null && dataVinograd != null) {
                DefaultXYDataset dataset = new DefaultXYDataset();
                dataset.addSeries("Обычный способ", dataNormal);
                dataset.addSeries("Алгоритм Винограда", dataVinograd);

                JFreeChart chart = ChartFactory.createXYLineChart(
                        "Графики времени выполнения",
                        "Размер матрицы",
                        "Время выполнения (нс)",
                        dataset
                );

                JFrame frame = new JFrame("Графики");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                ChartPanel chartPanel = new ChartPanel(chart);
                frame.add(chartPanel);
                frame.pack();
                frame.setVisible(true);
            }
        } catch (Exception e) {
            System.out.println("Ошибка при построении графика: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static double[][] readDataFromFile(String fileName) {
        int Npoints = 3;
        double[][] data = new double[2][Npoints]; // Предполагаем, что у нас есть не более 10 точек для построения графика
        String line;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            int index = 0;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(", ");
                if (parts.length == 4 && parts[0].startsWith("Method:") && parts[1].startsWith("Duration:") && parts[2].startsWith("Matrix A size:") && parts[3].startsWith("Matrix B size:")) {
                    String[] durationParts = parts[1].split(" ");
                    String[] matrixAParts = parts[2].split(" ");
                    String[] matrixBParts = parts[3].split(" ");
                    data[0][index] = Integer.parseInt(matrixAParts[3].substring(0, matrixAParts[3].indexOf("x"))) /** Integer.parseInt(matrixBParts[3].substring(0, matrixBParts[3].indexOf("x")))*/; // Размер матрицы A * B
                    data[1][index] = Long.parseLong(durationParts[1].substring(0, durationParts[1].indexOf("ns"))); // Продолжительность в наносекундах
                    index++;
                } else {
                    System.out.println("Некорректный формат данных в файле: " + fileName);
                    return null;
                }
            }
            reader.close();
            return data;
        } catch (IOException | NumberFormatException e) {
            System.out.println("Ошибка при чтении данных из файла: " + fileName);
            e.printStackTrace();
            return null;
        }
    }



}

