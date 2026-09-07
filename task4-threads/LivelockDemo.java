import java.util.concurrent.atomic.AtomicInteger;

public class LivelockDemo {

    static final int MAX_HANDOFFS = 20;

    public static void main(String[] args) throws InterruptedException {
        Diner husband = new Diner("Муж");
        Diner wife = new Diner("Жена");
        Spoon spoon = new Spoon(husband);

        Thread t1 = new Thread(() -> husband.eatWith(spoon, wife), "Поток-Муж");
        Thread t2 = new Thread(() -> wife.eatWith(spoon, husband), "Поток-Жена");

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println();
        System.out.println("Ложку передавали " + spoon.getHandoffs() + " раз, оба остались голодны:");
        System.out.println("  " + husband.getName() + " голоден: " + husband.isHungry());
        System.out.println("  " + wife.getName() + " голодна: " + wife.isHungry());
        System.out.println("Потоки всё время работали, но прогресса не было — это LIVELOCK.");
    }
}

class Spoon {

    private Diner owner;
    private final AtomicInteger handoffs = new AtomicInteger();

    Spoon(Diner owner) {
        this.owner = owner;
    }

    synchronized Diner getOwner() {
        return owner;
    }

    synchronized void passTo(Diner diner) {
        this.owner = diner;
        handoffs.incrementAndGet();
    }

    int getHandoffs() {
        return handoffs.get();
    }

    synchronized void use(Diner diner) {
        System.out.println(diner.getName() + " поел(а).");
    }
}

class Diner {

    private final String name;
    private volatile boolean hungry = true;

    Diner(String name) {
        this.name = name;
    }

    String getName() {
        return name;
    }

    boolean isHungry() {
        return hungry;
    }

    void eatWith(Spoon spoon, Diner other) {
        while (hungry && spoon.getHandoffs() < LivelockDemo.MAX_HANDOFFS) {
            if (spoon.getOwner() != this) {
                sleep(10);
                continue;
            }

            if (other.isHungry()) {
                System.out.println(name + ": ты голоден(на), возьми ложку, " + other.getName() + ".");
                spoon.passTo(other);
                sleep(10);
                continue;
            }

            spoon.use(this);
            hungry = false;
            spoon.passTo(other);
        }
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
