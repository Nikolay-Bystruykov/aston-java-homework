public class AdapterDemo {
    public static void main(String[] args) {
        DeliveryService ownService = new OwnDelivery();
        ownService.deliver("Москва", "Арбат 1", 5.0);

        DeliveryService external = new ExternalDeliveryAdapter(new ExternalShippingApi());
        external.deliver("Москва", "Арбат 1", 5.0);
    }
}

interface DeliveryService {
    void deliver(String city, String street, double weightKg);
}

class OwnDelivery implements DeliveryService {
    @Override
    public void deliver(String city, String street, double weightKg) {
        System.out.println("Своя доставка: " + city + ", " + street + ", " + weightKg + " кг");
    }
}

class ExternalShippingApi {
    void ship(String fullAddress, double weightPounds) {
        System.out.println("External API: адрес [" + fullAddress
                + "], вес " + weightPounds + " фунтов");
    }
}

class ExternalDeliveryAdapter implements DeliveryService {
    private final ExternalShippingApi api;

    ExternalDeliveryAdapter(ExternalShippingApi api) {
        this.api = api;
    }

    @Override
    public void deliver(String city, String street, double weightKg) {
        String fullAddress = city + ", " + street;
        double weightPounds = weightKg * 2.20462;
        api.ship(fullAddress, weightPounds);
    }
}
