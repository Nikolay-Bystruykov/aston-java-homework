public class DecoratorDemo {
    public static void main(String[] args) {
        DeliveryOrder order = new BasicOrder(1000);
        System.out.println(order.getDescription() + " = " + order.getCost());

        order = new GiftWrapDecorator(order);
        order = new InsuranceDecorator(order);
        order = new ExpressDecorator(order);

        System.out.println(order.getDescription() + " = " + order.getCost());
    }
}

interface DeliveryOrder {
    double getCost();
    String getDescription();
}

class BasicOrder implements DeliveryOrder {
    private final double cost;

    BasicOrder(double cost) {
        this.cost = cost;
    }

    @Override
    public double getCost() {
        return cost;
    }

    @Override
    public String getDescription() {
        return "Базовый заказ";
    }
}

abstract class OrderDecorator implements DeliveryOrder {
    protected final DeliveryOrder order;

    OrderDecorator(DeliveryOrder order) {
        this.order = order;
    }
}

class GiftWrapDecorator extends OrderDecorator {
    GiftWrapDecorator(DeliveryOrder order) {
        super(order);
    }

    @Override
    public double getCost() {
        return order.getCost() + 200;
    }

    @Override
    public String getDescription() {
        return order.getDescription() + " + подарочная упаковка";
    }
}

class InsuranceDecorator extends OrderDecorator {
    InsuranceDecorator(DeliveryOrder order) {
        super(order);
    }

    @Override
    public double getCost() {
        return order.getCost() * 1.05;
    }

    @Override
    public String getDescription() {
        return order.getDescription() + " + страховка";
    }
}

class ExpressDecorator extends OrderDecorator {
    ExpressDecorator(DeliveryOrder order) {
        super(order);
    }

    @Override
    public double getCost() {
        return order.getCost() + 500;
    }

    @Override
    public String getDescription() {
        return order.getDescription() + " + срочная доставка";
    }
}
