import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

public class DeadlockDemo {

    private static final Object lockA = new Object();
    private static final Object lockB = new Object();

    public static void main(String[] args) throws InterruptedException {
        Thread first = new Thread(() -> grab("A", lockA, "B", lockB), "Поток-1");
        Thread second = new Thread(() -> grab("B", lockB, "A", lockA), "Поток-2");

        first.start();
        second.start();

        Thread.sleep(2000);
        reportDeadlock();

        System.out.println("Потоки остались заблокированными навсегда, завершаем процесс принудительно.");
        System.exit(0);
    }

    private static void grab(String firstName, Object firstLock, String secondName, Object secondLock) {
        String me = Thread.currentThread().getName();

        synchronized (firstLock) {
            System.out.println(me + ": захватил " + firstName);

            sleep(100);

            System.out.println(me + ": жду " + secondName + "...");
            synchronized (secondLock) {
                System.out.println(me + ": захватил " + secondName + " (сюда выполнение не дойдёт)");
            }
        }
    }

    private static void reportDeadlock() {
        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        long[] deadlocked = bean.findDeadlockedThreads();

        if (deadlocked == null) {
            System.out.println("Deadlock не обнаружен.");
            return;
        }

        System.out.println("Обнаружен DEADLOCK, заблокированные потоки:");
        for (long id : deadlocked) {
            System.out.println("  - " + bean.getThreadInfo(id).getThreadName());
        }
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
