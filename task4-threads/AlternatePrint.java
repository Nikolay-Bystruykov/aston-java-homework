public class AlternatePrint {

    private static final Object lock = new Object();

    private static int turn = 1;

    private static final long PAUSE_MS = 300;

    public static void main(String[] args) {
        Thread first = new Thread(() -> printForever("1", 1, 2), "Поток-1");
        Thread second = new Thread(() -> printForever("2", 2, 1), "Поток-2");

        first.start();
        second.start();
    }

    private static void printForever(String value, int myTurn, int nextTurn) {
        while (true) {
            synchronized (lock) {
                while (turn != myTurn) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }

                System.out.println(value);

                turn = nextTurn;
                lock.notifyAll();
            }

            try {
                Thread.sleep(PAUSE_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
}
