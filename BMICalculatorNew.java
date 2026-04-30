import java.util.Scanner; // Used to read user input from the console
import java.util.Locale;  // Ensures correct decimal format (e.g., 1.5 instead of 1,5)

public class BMICalculatorNew {

    // "throws InterruptedException" is required because we use Thread.sleep()
    // Java forces us to handle or declare this exception when pausing a thread
    public static void main(String[] args) throws InterruptedException {

        Scanner scanner = new Scanner(System.in);
        scanner.useLocale(Locale.US); // Prevents input issues with decimal numbers

        String username = "";
        String password = "";

        String adminUser = "admin123";
        String adminPass = "admin123";

        String[] feedbackList = new String[10]; // Fixed-size storage (max 10 feedback entries)
        int feedbackCount = 0;

        boolean userExists = false; // Tracks if someone has signed up
        boolean running = true;     // Controls the main program loop

        // Main program loop (keeps running until user chooses Exit)
        while (running) {
            System.out.println("\n=== MAIN MENU ===");
            System.out.println("1. Sign Up");
            System.out.println("2. Login");
            System.out.println("3. BMI Calculator");
            System.out.println("4. Feedback");
            System.out.println("5. Exit");
            System.out.print("Choose option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Important: clears leftover newline from input buffer

            switch (choice) {

                case 1:
                    // SIGN UP
                    System.out.print("\nCreate username (8-16 chars): ");
                    username = scanner.nextLine();

                    if (username.length() < 8 || username.length() > 16) {
                        System.out.println("\nInvalid username length");
                        break; // Exit this case early if invalid
                    }

                    System.out.print("\nCreate password (8-16 chars): ");
                    password = scanner.nextLine();

                    if (password.length() < 8 || password.length() > 16) {
                        System.out.println("\nInvalid password length");
                        break;
                    }

                    userExists = true; // Now login is allowed
                    System.out.println("\n=== ACCOUNT CREATED SUCCESSFULLY ===");
                    break;

                case 2:
                    // LOGIN

                    // Prevent login if no account exists (fixes infinite loop issue)
                    if (!userExists) {
                        System.out.println("\nNo account found. Please sign up first.");
                        break;
                    }

                    int attempts = 0;
                    boolean loggedIn = false;

                    // Loop continues until login succeeds OR user exits
                    while (!loggedIn) {

                        // Lockout mechanism after 3 failed attempts
                        if (attempts >= 3) {
                            System.out.println("Too many attempts! Wait 30 seconds.");

                            // Countdown using a loop + Thread.sleep
                            for (int i = 30; i > 0; i--) {
                                System.out.println("Time left: " + i + " seconds");

                                // Thread.sleep pauses the program for 1000ms (1 second)
                                // This simulates a real login cooldown
                                Thread.sleep(1000);
                            }

                            attempts = 0; // Reset attempts after cooldown
                            System.out.println("Please re-enter your details");
                        }

                        System.out.println("Type 'exit' as username to return to menu.");

                        System.out.print("\nUsername: ");
                        String inputUser = scanner.nextLine();

                        // Allows user to break out of login loop manually
                        if (inputUser.equalsIgnoreCase("exit")) {
                            break;
                        }

                        System.out.print("\nPassword: ");
                        String inputPass = scanner.nextLine();

                        // ADMIN LOGIN (separate menu with extra privileges)
                        if (inputUser.equals(adminUser) && inputPass.equals(adminPass)) {

                            boolean adminRunning = true;

                            // Admin menu loop
                            while (adminRunning) {
                                System.out.println("\n=== ADMIN MENU ===");
                                System.out.println("1. View Feedback");
                                System.out.println("2. Logout");

                                int adminChoice = scanner.nextInt();
                                scanner.nextLine();

                                if (adminChoice == 1) {
                                    if (feedbackCount == 0) {
                                        System.out.println("\nNo feedback yet.");
                                    } else {
                                        // Loop through stored feedback and display it
                                        for (int i = 0; i < feedbackCount; i++) {
                                            System.out.println((i + 1) + ". " + feedbackList[i]);
                                        }
                                    }
                                } else {
                                    adminRunning = false; // Exit admin menu
                                }
                            }
                            break; // Exit login loop
                        }

                        // NORMAL USER LOGIN CHECK
                        if (inputUser.equals(username) && inputPass.equals(password)) {
                            System.out.println("\nUSER LOGIN SUCCESSFUL");
                            loggedIn = true; // Ends login loop
                        } else {
                            System.out.println("Incorrect details");
                            attempts++; // Increment failed attempts
                        }
                    }
                    break;

                case 3:
                    // BMI CALCULATOR

                    int unitChoice = getUnitChoice(scanner);

                    // Ternary operator chooses input based on unit system
                    double weight = (unitChoice == 1)
                            ? getValidInput(scanner, "Enter weight (kg): ", 10, 600)
                            : getValidInput(scanner, "Enter weight (lbs): ", 22, 1300);

                    double height = (unitChoice == 1)
                            ? getValidInput(scanner, "Enter height (m): ", 0.5, 2.5)
                            : getValidInput(scanner, "Enter height (in): ", 20, 100);

                    double bmi = calculateBMI(unitChoice, weight, height);
                    System.out.println("\nBMI: " + bmi);

                    // Determine BMI category using conditional logic
                    String category;
                    if (bmi < 18.5) category = "\nUnderweight";
                    else if (bmi < 25) category = "\nNormal";
                    else if (bmi < 30) category = "\nOverweight";
                    else if (bmi < 35) category = "\nObese";
                    else category = "\nSeverely Obese";

                    System.out.println(category);

                    // Simple advice logic based on category
                    if (category.equals("\nUnderweight")) {
                        System.out.println("Eat more & lift weights");
                    } else if (category.equals("\nNormal")) {
                        System.out.println("Maintain your routine");
                    } else {
                        System.out.println("\nExercise & eat clean");
                    }

                    double calories = calculateCalories(weight, height, unitChoice);
                    System.out.println("\nEstimated Calories: " + calories);

                    System.out.println(calculateMacros(calories, 1));
                    break;

                case 4:
                    // FEEDBACK SYSTEM
                    System.out.println("\nEnter feedback:");
                    String feedback = scanner.nextLine();

                    // Prevent array overflow
                    if (feedbackCount < feedbackList.length) {
                        feedbackList[feedbackCount++] = feedback;
                    } else {
                        System.out.println("\nStorage full");
                    }
                    break;

                case 5:
                    running = false; // Stops main loop → program ends
                    System.out.println("\nGoodBye " + username);
                    break;

                default:
                    System.out.println("\nInvalid option");
            }
        }
    }

    // Ensures user selects either Metric (1) or Imperial (2)
    public static int getUnitChoice(Scanner scanner) {
        while (true) {
            System.out.println("1. Metric | 2. Imperial");
            int choice = scanner.nextInt();

            if (choice == 1 || choice == 2) return choice;

            System.out.println("Invalid choice");
        }
    }

    // Reusable method to validate numeric input within a range
    public static double getValidInput(Scanner scanner, String prompt, double min, double max) {
        double value;

        while (true) {
            System.out.print(prompt);

            // hasNextDouble prevents crashes from invalid input (e.g., letters)
            if (scanner.hasNextDouble()) {
                value = scanner.nextDouble();

                if (value >= min && value <= max) return value;
            } else {
                scanner.next(); // Discard invalid input
            }

            System.out.println("Invalid input");
        }
    }

    // BMI formula differs depending on unit system
    public static double calculateBMI(int unitChoice, double weight, double height) {
        if (unitChoice == 1) {
            return weight / (height * height); // Metric
        } else {
            return (703 * weight) / (height * height); // Imperial
        }
    }

    // Simple calorie estimation (Mifflin-St Jeor approximation)
    public static double calculateCalories(double weight, double height, int unitChoice) {

        // Convert imperial to metric before calculation
        if (unitChoice == 2) {
            weight *= 0.453592;
            height *= 0.0254;
        }

        double heightCm = height * 100;

        return (10 * weight) + (6.25 * heightCm) - (5 * 20) + 5;
    }

    // Splits calories into macronutrients
    public static String calculateMacros(double calories, int goal) {

        double protein = 0.30, carbs = 0.40, fats = 0.30;

        // Convert calories → grams (protein/carbs = 4 cal/g, fats = 9 cal/g)
        double p = (protein * calories) / 4;
        double c = (carbs * calories) / 4;
        double f = (fats * calories) / 9;

        return "\nProtein: " + p + "g | Carbs: " + c + "g | Fats: " + f + "g";
    }
}
