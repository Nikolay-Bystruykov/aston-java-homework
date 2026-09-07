public class BuilderDemo {
    public static void main(String[] args) {
        Order order = new Order.Builder("Ноутбук", 1)
                .address("Москва, Арбат 1")
                .urgent(true)
                .giftWrap(true)
                .comment("Позвонить за час")
                .build();

        System.out.println(order);

        Order simple = new Order.Builder("Мышка", 2)
                .address("СПб, Невский 10")
                .build();

        System.out.println(simple);

        try {
            new Order.Builder("Клавиатура", 1).build();
        } catch (IllegalStateException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
}

class Order {
    private final String product;
    private final int quantity;
    private final String address;
    private final String comment;
    private final boolean urgent;
    private final boolean giftWrap;

    private Order(Builder builder) {
        this.product = builder.product;
        this.quantity = builder.quantity;
        this.address = builder.address;
        this.comment = builder.comment;
        this.urgent = builder.urgent;
        this.giftWrap = builder.giftWrap;
    }

    @Override
    public String toString() {
        return "Order{product='" + product + "', quantity=" + quantity
                + ", address='" + address + "', comment='" + comment
                + "', urgent=" + urgent + ", giftWrap=" + giftWrap + "}";
    }

    static class Builder {
        private final String product;
        private final int quantity;
        private String address;
        private String comment;
        private boolean urgent;
        private boolean giftWrap;

        Builder(String product, int quantity) {
            this.product = product;
            this.quantity = quantity;
        }

        Builder address(String address) {
            this.address = address;
            return this;
        }

        Builder comment(String comment) {
            this.comment = comment;
            return this;
        }

        Builder urgent(boolean urgent) {
            this.urgent = urgent;
            return this;
        }

        Builder giftWrap(boolean giftWrap) {
            this.giftWrap = giftWrap;
            return this;
        }

        Order build() {
            if (address == null) {
                throw new IllegalStateException("Адрес доставки обязателен");
            }
            return new Order(this);
        }
    }
}
