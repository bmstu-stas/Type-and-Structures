//BinarySearch


import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Random random = new Random();
        int size; // количество элементов в массиве
        int elem; // элемент массива
        ArrayList<Integer> arrayList = new ArrayList<>();

        System.out.print("Введите количество элементов в массиве: ");
        size = sc.nextInt(); // считывание количества элементов

        System.out.println("Как заполняем массив: 1 - случайные цифры, 2 - ввод вручную");
        int choice = sc.nextInt(); // выбор метода заполнения массива

        switch (choice) {
            case 1:
                for (int i = 0; i < size; i++) {
                    arrayList.add(random.nextInt(31)); // добавление случайного числа от 0 до 30 в список
                }
                break;
            case 2:
                for (int i = 0; i < size; i++) {
                    System.out.print("Введите элемент " + (i + 1) + ": ");
                    elem = sc.nextInt();
                    arrayList.add(elem); // добавление введённого элемента в список
                }
                break;
            default:
                System.out.println("Неверный выбор.");
                return;
        }

        // Сортировка массива
        Collections.sort(arrayList);
        System.out.println("Отсортированный массив: " + arrayList);

        // Реализация бинарного поиска
        System.out.print("Введите элемент для поиска: ");
        int searchValue = sc.nextInt(); // считывание элемента для поиска
        int index = binarySearch(arrayList, searchValue); // вызов метода бинарного поиска
        if (index != -1) {
            System.out.println("Элемент найден на позиции: " + index);
        } else {
            System.out.println("Элемент не найден.");
        }
    }

    // Метод для бинарного поиска
    public static int binarySearch(ArrayList<Integer> list, int value) {
        int low = 0; // начало диапазона поиска
        int high = list.size() - 1; // конец диапазона поиска

        while (low <= high) {
            int mid = (low + high) / 2; // средний индекс
            int guess = list.get(mid); // значение в средней позиции

            if (guess == value) {
                return mid; // значение найдено
            } else if (guess > value) {
                high = mid - 1; // сужение диапазона поиска к меньшим значениям
            } else {
                low = mid + 1; // сужение диапазона поиска к большим значениям
            }
        }
        return -1; // значение не найдено
    }
}
