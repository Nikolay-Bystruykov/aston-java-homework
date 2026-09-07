public class ChainDemo {
    public static void main(String[] args) {
        OrderHandler chain = new StockHandler();
        chain.setNext(new AddressHandler())
             .setNext(new LimitHandler());

        System.out.println("--- Заказ 1 ---");
        chain.handle(new OrderRequest("Ноутбук", true, "Москва, Арбат 1", 50000));

        System.out.println("--- Заказ 2 ---");
        chain.handle(new OrderRequest("Монитор", false, "СПб, Невский 10", 20000));

        System.out.println("--- Заказ 3 ---");
        chain.handle(new OrderRequest("Мышка", true, null, 1500));

        System.out.println("--- Заказ 4 ---");
        chain.handle(new OrderRequest("Сервер", true, "Казань, Баумана 5", 500000));
    }
}

class OrderRequest {
    private final String product;
    private final boolean inStock;
    private final String address;
    private final double amount;

    OrderRequest(String product, boolean inStock, String address, double amount) {
        this.product = product;
        this.inStock = inStock;
        this.address = address;
        this.amount = amount;
    }

    String getProduct()  { return product; }
    boolean isInStock()  { return inStock; }
    String getAddress()  { return address; }
    double getAmount()   { return amount; }
}

abstract class OrderHandler {
    private OrderHandler next;

    OrderHandler setNext(OrderHandler next) {
        this.next = next;
        return next;
    }

    abstract void handle(OrderRequest request);

    protected void passToNext(OrderRequest request) {
        if (next != null) {
            next.handle(request);
        } else {
            System.out.println("Заказ принят: " + request.getProduct());
        }
    }
}

class StockHandler extends OrderHandler {
    @Override
    void handle(OrderRequest request) {
        if (!request.isInStock()) {
            System.out.println("Отказ: товара нет на складе");
            return;
        }
        System.out.println("Проверка склада пройдена");
        passToNext(request);
    }
}

class AddressHandler extends OrderHandler {
    @Override
    void handle(OrderRequest request) {
        if (request.getAddress() == null || request.getAddress().isEmpty()) {
            System.out.println("Отказ: не указан адрес доставки");
            return;
        }
        System.out.println("Проверка адреса пройдена");
        passToNext(request);
    }
}

class LimitHandler extends OrderHandler {
    private static final double LIMIT = 100000;

    @Override
    void handle(OrderRequest request) {
        if (request.getAmount() > LIMIT) {
            System.out.println("Отказ: сумма превышает лимит " + LIMIT);
            return;
        }
        System.out.println("Проверка суммы пройдена");
        passToNext(request);
    }
}
