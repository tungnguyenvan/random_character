import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

public class main {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_BOLD = "\033[0;1m";

    private static int index = 0;
    private static int rightCount = 0;
    private static int wrongCount = 0;
    private static int totalCount = 0;
    private static Scanner scanner = new Scanner(System.in);
    private static List<DictionaryModel> dictionaryList;

    public static void run() {
        if (dictionaryList.size() > 0) {
            System.out.println("---------------------------");
            System.out.println(ANSI_BOLD + "Total: " + totalCount + ANSI_RESET);
            System.out.println(ANSI_GREEN + "Đúng: " + rightCount + ANSI_RESET + " \t\t" + ANSI_RED + "Sai: " + wrongCount + ANSI_RESET);

            DictionaryModel dictionaryModel = dictionaryList.get(index);
            System.out.print(ANSI_YELLOW + dictionaryModel.getKey() + " : " + ANSI_RESET);
            String input = scanner.next();
            if (input.compareTo(dictionaryModel.getValue()) == 0) {
                System.out.println(ANSI_GREEN + "Đúng rồi: " + dictionaryModel.getValue() + ANSI_RESET);
                rightCount++;
            } else {
                System.out.println(ANSI_RED + "Sai rồi: " + dictionaryModel.getValue() + ANSI_RESET);
                wrongCount++;
            }

            index++;
            totalCount++;
            if (index >= dictionaryList.size()) {
                index = 0;
                Collections.shuffle(dictionaryList, new Random());
            }
            execRun();
        } else {
            System.out.println(ANSI_RED + "Error: dictionary empty" + ANSI_RESET);
        }
    }

    public static void execRun() {
        CommandLineUtil.getInstance().run("clear", response -> {
            run();
        });
    }

    public static void main(String[] args) {
        Gson gson = new Gson();
        dictionaryList = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(System.getProperty("user.dir") + "/Dictionary.json"));
            Type type = new TypeToken<List<DictionaryModel>>() {}.getType();
            List<DictionaryModel> models = gson.fromJson(br, type);
            List<DictionaryModel> dictionaryModels = new ArrayList<>();

            for (DictionaryModel model: models) {
                if (model.getIsEnable()) dictionaryModels.add(model);
            }

            System.out.println("Chọn một:");
            System.out.println("1. Bắt đầu.");
            System.out.println("2. Theo số record mới nhât.");
            System.out.print("Chọn: ");

            int answer = scanner.nextInt();
            int recordCount = 0;
            while (!(answer == 1 || answer == 2)) {
                System.out.print("Thử lại: ");
                answer = scanner.nextInt();
            }

            if (answer == 2) {
                System.out.print("Chọn số record: ");
                recordCount = scanner.nextInt();
            }

            for (int i = 0; i < dictionaryModels.size(); i++) {
                DictionaryModel model = dictionaryModels.get(i);
                boolean canAdd = true;

                if (answer == 2 && i < (dictionaryModels.size() - recordCount)) {
                    canAdd = false;
                }

                if (canAdd) dictionaryList.add(model);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        
        System.out.println(ANSI_BLUE + "----------[START]----------" + ANSI_RESET);
        execRun();
    }
}
