package Calculator;

import java.util.Scanner;

class Main {

    // Массив арабских чисел
    private static final int[] ArabicNumbers = {100, 90, 50, 40, 10, 9, 5, 4, 1};
    // Массив римских чисел
    private static final String[] RomanNumbers = {"C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            while (true) {
                System.out.println("Введите арифметическое выражение (например, 2 + 2):");
                String input = scanner.nextLine();
                // Сравнивает без учета регистров
                if (input.equalsIgnoreCase("exit")) {
                    System.out.println("Выход из калькулятора.");
                    break;
                }

                String result = calc(input);
                System.out.println("Результат: " + result);
            }
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

    public static String calc(String input) {

        // Разделение строки по пробелу
        String[] parts = input.split(" ");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Неверный формат ввода");
        }

        // Проверить, что числа подходят под условия задачи
        String num1 = parts[0];
        String num2 = parts[2];
        boolean isNum1Roman = isRoman(num1);
        boolean isNum2Roman = isRoman(num2);
        if ((isNum1Roman && !isNum2Roman) || (!isNum1Roman && isNum2Roman)) {
            throw new IllegalArgumentException("Используются одновременно разные системы счисления");
        }

        // Преобразовать числа в int
        int operand1 = isNum1Roman ? romanToArabic(num1) : Integer.parseInt(num1);
        int operand2 = isNum2Roman ? romanToArabic(num2) : Integer.parseInt(num2);

        // Проверить, что числа находятся в диапазоне от 1 до 10
        if (operand1 < 1 || operand1 > 10 || operand2 < 1 || operand2 > 10) {
            throw new IllegalArgumentException("Числа должны быть от 1 до 10 включительно");
        }

        // Выполнить операцию
        String operator = parts[1];
        int result;
        switch (operator) {
            case "+":
                result = operand1 + operand2;
                break;
            case "-":
                result = operand1 - operand2;
                break;
            case "*":
                result = operand1 * operand2;
                break;
            case "/":
                if (operand2 == 0) {
                    throw new IllegalArgumentException("Деление на ноль");
                }
                result = operand1 / operand2;
                break;
            default:
                throw new IllegalArgumentException("Недопустимый оператор: " + operator);
        }

        // Вернуть результат в соответствующем формате
        return isNum1Roman ? arabicToRoman(result) : String.valueOf(result);
    }

    // Проверяет, содержит ли входная строка только символы, соответствующие римским числам (I, V, X, L, C, D, M)
    private static boolean isRoman(String input) {
        return input.matches("[IVXLCDM]+");
    }

    private static int romanToArabic(String input) {
        int result = 0;
        int i = 0;
        while (i < input.length()) {
            char currentChar = input.charAt(i);
            int currentValue = romanValue(currentChar);

            // Проверяем, если есть следующий символ и текущее значение меньше следующего
            if (i + 1 < input.length()) {
                char nextChar = input.charAt(i + 1);
                int nextValue = romanValue(nextChar);
                // Если текущее значение меньше следующего, значит это комбинация типа IV, IX и т.д.
                if (currentValue < nextValue) {
                    // Вычитаем текущее значение из следующего
                    result += (nextValue - currentValue);
                    // Переходим к символу после следующего, так как мы уже использовали его
                    i += 2;
                    continue;
                }
            }
            // Если не было комбинации типа IV, IX и т.д., просто добавляем текущее значение
            result += currentValue;
            // Переходим к следующему символу
            i++;
        }
        return result;
    }

    private static int romanValue(char romanChar) {
        switch (romanChar) {
            case 'I':
                return 1;
            case 'V':
                return 5;
            case 'X':
                return 10;
            case 'L':
                return 50;
            case 'C':
                return 100;
            default:
                throw new IllegalArgumentException("Неправильный символ римского числа: " + romanChar);
        }
    }

    private static String arabicToRoman(int number) {
        if (number < 1 || number > 100) {
            throw new IllegalArgumentException("В римской системе нет отрицательных чисел или чисел больше 100");
        }

        StringBuilder result = new StringBuilder();

        // Идем по римским числам
        for (int i = 0; i < ArabicNumbers.length; i++) {
            // Пока число больше или равно текущему арабскому числу
            while (number >= ArabicNumbers[i]) {
                // Добавляем соответствующее римское число в результат
                result.append(RomanNumbers[i]);
                // Вычитаем из числа значение текущего арабского числа
                number -= ArabicNumbers[i];
            }
        }
        return result.toString();
    }
}
