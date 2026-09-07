public class StrategyDemo {
    public static void main(String[] args) {
        PaymentContext context = new PaymentContext();

        context.setStrategy(new CardPayment("1234-5678-9012-3456"));
        context.checkout(1500.0);

        context.setStrategy(new CashPayment());
        context.checkout(890.0);

        context.setStrategy(new CryptoPayment("bc1qxy2k"));
        context.checkout(12000.0);

        context.setStrategy(amount -> System.out.println("Оплачено бонусами: " + amount + " руб."));
        context.checkout(300.0);
    }
}

interface PaymentStrategy {
    void pay(double amount);
}

class CardPayment implements PaymentStrategy {
    private final String cardNumber;

    CardPayment(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    @Override
    public void pay(double amount) {
        System.out.println("Оплачено картой " + cardNumber + ": " + amount + " руб.");
    }
}

class CashPayment implements PaymentStrategy {
    @Override
    public void pay(double amount) {
        System.out.println("Оплачено наличными: " + amount + " руб.");
    }
}

class CryptoPayment implements PaymentStrategy {
    private final String wallet;

    CryptoPayment(String wallet) {
        this.wallet = wallet;
    }

    @Override
    public void pay(double amount) {
        System.out.println("Оплачено криптовалютой с кошелька " + wallet + ": " + amount + " руб.");
    }
}

class PaymentContext {
    private PaymentStrategy strategy;

    void setStrategy(PaymentStrategy strategy) {
        this.strategy = strategy;
    }

    void checkout(double amount) {
        if (strategy == null) {
            throw new IllegalStateException("Способ оплаты не выбран");
        }
        strategy.pay(amount);
    }
}
