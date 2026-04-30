import java.util.Scanner; import java.util.Locale;

public class BMICalculatorNew {

    public static void main(String[] args) throws InterruptedException {

        Scanner scanner = new Scanner(System.in);
        scanner.useLocale(Locale.US);

        String username = "";
        String password = "";
        String securityAnswer = "blue";

        String adminUser = "admin123";
        String adminPass = "admin123";

        String[] feedbackList = new String[10];
        int feedbackCount = 0;

        boolean running = true;

        while (running) {
            System.out.println("\n=== MAIN MENU ===");
            System.out.println("1. Sign Up");
            System.out.println("2. Login");
            System.out.println("3. BMI Calculator");
            System.out.println("4. Feedback");
            System.out.println("5. Exit");
            System.out.print("Choose option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {

                case 1:
                    System.out.print("\nCreate username (8-16 chars): ");
                    username = scanner.nextLine();

                    if (username.length() < 8 || username.length() > 16) {
                        System.out.println("\nInvalid username length");
                        break;
                    }

                    System.out.print("\nCreate password (8-16 chars): ");
                    password = scanner.nextLine();

                    if (password.length() < 8 || password.length() > 16) {
                        System.out.println("\nInvalid password length");
                        break;
                    }

                    System.out.println("\n==========================================ACCOUNT CREATED SUCCESSFULLY==========================================");
                    break;

                case 2:
                    int attempts = 0;
                    boolean loggedIn = false;

                 while (!loggedIn) {
						
                        if (attempts >= 3) {
                            System.out.println(" Too many attempts! Wait 30 seconds.");

                            for (int i = 30; i > 0; i--) {
                                System.out.println("Time left: " + i + " seconds");
                                Thread.sleep(1000);
                            }
                            attempts = 0;
							System.out.println("Please re-enter your details");
                        }

                        System.out.print("\nUsername: ");
                        String inputUser = scanner.nextLine();

                        System.out.print("\nPassword: ");
                        String inputPass = scanner.nextLine();

                        if (inputUser.equals(adminUser) && inputPass.equals(adminPass)) {

                            boolean adminRunning = true;

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
                                        for (int i = 0; i < feedbackCount; i++) {
                                            System.out.println((i + 1) + ". " + feedbackList[i]);
                                        }
                                    }
                                } else {
                                    adminRunning = false;
                                }
                            }
                            break;
                        }

                        if (inputUser.equals(username) && inputPass.equals(password)) {
                            System.out.println("\nUSER LOGIN SUCCESSFUL");
                            loggedIn = true;
                        } else {
                            System.out.println("Incorrect details");
                            attempts++;
                        }
                    }
                    break;

                case 3:
                    int unitChoice = getUnitChoice(scanner);

                    double weight = (unitChoice == 1)
                            ? getValidInput(scanner, "Enter weight (kg): ", 10, 600)
                            : getValidInput(scanner, "Enter weight (lbs): ", 22, 1300);

                    double height = (unitChoice == 1)
                            ? getValidInput(scanner, "Enter height (m): ", 0.5, 2.5)
                            : getValidInput(scanner, "Enter height (in): ", 20, 100);

                    double bmi = calculateBMI(unitChoice, weight, height);

                    System.out.println("\nBMI: " + bmi);

                    String category;

                    if (bmi < 18.5) category = "\nUnderweight";
                    else if (bmi < 25) category = "\nNormal";
                    else if (bmi < 30) category = "\nOverweight";
                    else if (bmi < 35) category = "\nObese";
                    else category = "\nSeverely Obese";

                    System.out.println(category);
					
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

                   // advice(scanner, category);

                    break;

                case 4:
                    System.out.println("\nEnter feedback:");
                    String feedback = scanner.nextLine();

                    if (feedbackCount < feedbackList.length) {
                        feedbackList[feedbackCount++] = feedback;
                        System.out.println("\nFeedback saved!");
                    } else {
                        System.out.println("\nStorage full");
                    }
                    break;

                case 5:
                    running = false;
					System.out.println("\nGoodBye" + " " + username);
                    break;

                default:
                    System.out.println("\nInvalid option");
            }
        }
		
    }
	    

    public static int getUnitChoice(Scanner scanner) {
        while (true) {
            System.out.println("1. Metric | 2. Imperial");
            int choice = scanner.nextInt();

            if (choice == 1 || choice == 2) return choice;

            System.out.println("Invalid choice");
        }
    }

    public static double getValidInput(Scanner scanner, String prompt, double min, double max) {
        double value;

        while (true) {
            System.out.print(prompt);

            if (scanner.hasNextDouble()) {
                value = scanner.nextDouble();

                if (value >= min && value <= max) return value;
            } else {
                scanner.next();
            }

            System.out.println("Invalid input");
        }
    }

    public static double calculateBMI(int unitChoice, double weight, double height) {
        if (unitChoice == 1) {
            return weight / (height * height);
        } else {
            return (703 * weight) / (height * height);
        }
    }

    public static double calculateCalories(double weight, double height, int unitChoice) {

        if (unitChoice == 2) {
            weight *= 0.453592;
            height *= 0.0254;
        }

        double heightCm = height * 100;

        return (10 * weight) + (6.25 * heightCm) - (5 * 20) + 5;
    }

    public static String calculateMacros(double calories, int goal) {

        double protein = 0.30, carbs = 0.40, fats = 0.30;

        double p = (protein * calories) / 4;
        double c = (carbs * calories) / 4;
        double f = (fats * calories) / 9;

        return "\nProtein: " + p + "g | Carbs: " + c + "g | Fats: " + f + "g";
    }



}
