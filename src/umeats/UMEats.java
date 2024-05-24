package umeats;

import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

public class UMEats {

    private static final Map<String, Map<String, Integer>> meals = new HashMap<>();
    private static int servedMeals = 0;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("--------------- UMEats ---------------\n");
            displayStatistics();
            System.out.println("1. Add Meal");
            System.out.println("2. Take Meal");
            System.out.println("3. Exit");

            System.out.println("\n" + "-".repeat(38));
            System.out.print("Choose An Option : ");
            int choice = scanner.nextInt();
            System.out.println("-".repeat(38));

            try {
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        addMeal(scanner);
                        break;
                    case 2:
                        takeMeal(scanner);
                        break;
                    case 3:
                        System.out.println("\nThank You For Sharing Your \nMeal With UMEats. Goodbye!");
                        System.out.println("\n" + "-".repeat(38));
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.nextLine(); // Clear the invalid input
            }
        }
    }

    private static void addMeal(Scanner scanner) {
        System.out.println("\n----------- Add Your Meal ------------");
        String area = selectArea(scanner);
        String block = selectBlock(scanner, area);
        System.out.println("-".repeat(38) + "\n");
        System.out.print("Enter Meal Quantity To Add : ");
        try {
            int quantity = scanner.nextInt();
            updateMealCount(area, block, quantity);

            System.out.println("Your Meal Added Successfully");
            displayStatistics();
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a valid number.");
            scanner.nextLine(); // Clear the invalid input
        }
    }

    private static void takeMeal(Scanner scanner) {
        System.out.println("\n------------ Take A Meal -------------");
        String area = selectArea(scanner);
        String block = selectBlock(scanner, area);
        System.out.println("-".repeat(38) + "\n");
        System.out.print("Select Meal Quantity (1-4): ");
        try {
            int quantity = scanner.nextInt();

            if (quantity < 1 || quantity > 4) {
                System.out.println("Invalid quantity. Please select between 1 and 4.");
                return;
            }

            int availableMeals = meals.getOrDefault(area, new HashMap<>()).getOrDefault(block, 0);
            if (availableMeals < quantity) {
                System.out.println("Not enough meals available in Block " + block + ".\nPlease try again.");
                System.out.println("\n" + "-".repeat(38) + "\n");
                return;
            }

            updateMealCount(area, block, -quantity);
            servedMeals += quantity;  // Update served meals count

            System.out.println("Your Meal Is Ready.");
            displayStatistics();
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a valid number.");
            scanner.nextLine(); // Clear the invalid input
        }
    }

    private static String selectArea(Scanner scanner) {
        System.out.println("\nSelect An Area~\n");
        System.out.println("Kolej Kediaman 01"
                + "\nKolej Kediaman 02"
                + "\nKolej Kediaman 03"
                + "\nKolej Kediaman 04"
                + "\nKolej Kediaman 05"
                + "\nKolej Kediaman 06"
                + "\nKolej Kediaman 07"
                + "\nKolej Kediaman 08"
                + "\nKolej Kediaman 09"
                + "\nKolej Kediaman 10"
                + "\nKolej Kediaman 11"
                + "\nKolej Kediaman 12"
                + "\nKolej Kediaman 13");

        System.out.println("\n" + "-".repeat(38));
        System.out.print("Enter Selected Area : ");
        return scanner.nextLine().toUpperCase();
    }

    private static String selectBlock(Scanner scanner, String area) {
        System.out.println("-".repeat(38) + "\n");
        System.out.println("Select A Block~\n");
        System.out.println("Block A"
                + "\nBlock B"
                + "\nBlock C"
                + "\nBlock D");

        System.out.println("\n" + "-".repeat(38));
        System.out.print("Enter Selected Block : ");
        return scanner.nextLine().toUpperCase();
    }

    private static void updateMealCount(String area, String block, int quantity) {
        meals.putIfAbsent(area, new HashMap<>());
        meals.get(area).merge(block, quantity, Integer::sum);
    }

    private static void displayStatistics() {
        int totalAvailableMeals = meals.values().stream()
                .mapToInt(area -> area.values().stream().mapToInt(Integer::intValue).sum())
                .sum();

        System.out.println("Available Meals : " + totalAvailableMeals);
        System.out.println("Served Meals : " + servedMeals);
        System.out.println("\n" + "-".repeat(38) + "\n");
    }
}
