import java.util.HashMap;
import java.util.Map;

public class ProxyDemo {
    public static void main(String[] args) {
        Catalog catalog = new CatalogProxy();

        System.out.println(catalog.getProduct(1));
        System.out.println(catalog.getProduct(1));
        System.out.println(catalog.getProduct(2));
        System.out.println(catalog.getProduct(1));
    }
}

interface Catalog {
    String getProduct(int id);
}

class RealCatalog implements Catalog {
    @Override
    public String getProduct(int id) {
        System.out.println("  [обращение к базе данных...]");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return "Товар №" + id;
    }
}

class CatalogProxy implements Catalog {
    private RealCatalog realCatalog;
    private final Map<Integer, String> cache = new HashMap<>();

    @Override
    public String getProduct(int id) {
        if (cache.containsKey(id)) {
            System.out.println("  [взято из кеша]");
            return cache.get(id);
        }

        if (realCatalog == null) {
            realCatalog = new RealCatalog();
        }

        String product = realCatalog.getProduct(id);
        cache.put(id, product);
        return product;
    }
}
