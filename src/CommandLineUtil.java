import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CommandLineUtil {
    private static CommandLineUtil instance = null;

    /**
     * Constructor
     */
    private CommandLineUtil() {
        //
    }

    /**
     * Get static instance
     * @return static instance
     */
    public static CommandLineUtil getInstance() {
        if (instance == null) {
            instance = new CommandLineUtil();
        }

        return instance;
    }

    /**
     * Run a command and return callback after success
     * @param command String command
     * @param commandLineUtilCallback callback after done
     */
    public void run(String command, CommandLineUtilCallback commandLineUtilCallback) {
        // Create new thread to run command
        Thread thread = new Thread(() -> {
            // List string response
            List<String> response = new ArrayList<>();

            // get current runtime
            Runtime runtime = Runtime.getRuntime();

            // process
            Process process = null;

            // String line
            String line = null;

            try {
                // exec command
                process = runtime.exec(command);

                // Get input stream
                BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));

                // Setting response
                while ((line = input.readLine()) != null) {
                    response.add(line);
                }
                // wait exec command success
                process.waitFor();

                // callback after success
                commandLineUtilCallback.onResponse(response);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        // start thread
        thread.start();
    }

    public interface CommandLineUtilCallback {
        void onResponse(List<String> response);
    }
}
