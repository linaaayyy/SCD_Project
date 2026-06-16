package database;
import model.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
public class FileHandler {
    private static final String FILE_NAME = "expenses.txt";
    public static void saveTransactionToFile(Transaction t) {
        try (FileWriter fw = new FileWriter(FILE_NAME, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(t.getTransactionType() + "," + t.getAmount() + "," +
                    t.getCategory() + "," + t.getDescription() + "," + t.getDate());
            System.out.println("Data successfully written to text file!");
        } catch (IOException e) {
            System.err.println("Error writing data to flat text file: " + e.getMessage());
        }
    }
    public static List<Transaction> loadTransactionsFromFile() {
        List<Transaction> list = new ArrayList<>();
        File file = new File(FILE_NAME);
        if (!file.exists()) return list;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(",");
                if (tokens.length < 5) continue;
                String type = tokens[0];
                double amt = Double.parseDouble(tokens[1]);
                String cat = tokens[2];
                String desc = tokens[3];
                String date = tokens[4];
                if (type.equals("EXPENSE")) {
                    list.add(new Expense(amt, cat, desc, date));
                } else {
                    list.add(new Income(amt, cat, desc, date));
                }
            }
        } catch (Exception e) {
            System.err.println("Error reading or parsing flat text file: " + e.getMessage());
        }
        return list;
    }
}