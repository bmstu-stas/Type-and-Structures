 import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Выберите действие:");
        System.out.println("1. Умножить матрицы");
        System.out.println("2. Построить графики");
        int choice = scanner.nextInt();

        if (choice == 1) {
            System.out.println("Введите размеры матрицы A:");
            int m = scanner.nextInt();
            int n = scanner.nextInt();
            int[][] A = MethodsMatrix.matrix(m, n);

            System.out.println("Введите размеры матрицы B:");
            m = scanner.nextInt();
            n = scanner.nextInt();
            int[][] B = MethodsMatrix.matrix(m, n);

            System.out.println("Матрица A: ");
            MethodsMatrix.printMatrix(A);
            System.out.println("Матрица B: ");
            MethodsMatrix.printMatrix(B);

            System.out.println("Выберите метод умножения матриц:");
            System.out.println("1. Обычный способ");
            System.out.println("2. Алгоритм Винограда");
            int method = scanner.nextInt();

            long startTime = System.nanoTime();

            int[][] C;
            if (method == 1) {
                C = MethodsMatrix.mulitMatrix(A, B);
            } else if (method == 2) {
                C = MethodsMatrix.mulitMatrixVinograd(A, B);
            } else {
                System.out.println("Неверный метод. Используется обычный способ умножения.");
                C = MethodsMatrix.mulitMatrix(A, B);
            }

            long endTime = System.nanoTime();
            long duration = (endTime - startTime);

            if (C != null) {
                System.out.println("Матрица C (результат умножения A и B):");
                MethodsMatrix.printMatrix(C);

                MethodsMatrix.saveTimeToFile(duration, method, A, B);
            } else {
                System.out.println("Матрицы A и B не могут быть умножены из-за несоответствия их размеров.");
            }
        } else if (choice == 2) {
            MethodsMatrix.plotGraphs();
        } else {
            System.out.println("Неверный выбор. Программа завершена.");
        }
    }
}
