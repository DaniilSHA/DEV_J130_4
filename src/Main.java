import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        ExecutorService fixedPoolOfClients = Executors.newFixedThreadPool(2);
        for (int i = 0; i < 2; i++) {
            fixedPoolOfClients.submit(new Runnable() {
                @Override
                public void run() {
                    new SimpleChat();
                }
            });
        }
    }
}
