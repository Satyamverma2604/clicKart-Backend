
package com.clicKart.config;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.clicKart.category.entity.Category;
import com.clicKart.category.repository.CategoryRepository;
import com.clicKart.product.entity.Product;
import com.clicKart.product.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DemoDataInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    @Override
    public void run(String... args) {

        Map<String, Category> categories = seedCategories();

        seedProducts(categories);

        System.out.println("==================================");
        System.out.println("clicKart demo data initialization completed!");
        System.out.println("Categories: " + categoryRepository.count());
        System.out.println("Products: " + productRepository.count());
        System.out.println("==================================");
    }

    private Map<String, Category> seedCategories() {

        Map<String, Category> categories = new HashMap<>();

        List<CategoryData> categoryData = List.of(
            new CategoryData("Electronics", "Electronic gadgets and devices"),
            new CategoryData("Mobiles", "Smartphones and mobile devices"),
            new CategoryData("Laptops", "Laptops for work, study and gaming"),
            new CategoryData("Fashion", "Trendy clothing for men and women"),
            new CategoryData("Footwear", "Shoes, sneakers and casual footwear"),
            new CategoryData("Home & Kitchen", "Home appliances and kitchen essentials"),
            new CategoryData("Accessories", "Everyday lifestyle accessories"),
            new CategoryData("Gaming", "Gaming consoles and gaming accessories"),
            new CategoryData("Beauty & Personal Care", "Beauty and personal care products"),
            new CategoryData("Sports & Fitness", "Sports and fitness equipment")
        );

        for (CategoryData data : categoryData) {

            Category category = categoryRepository
                    .findByNameIgnoreCase(data.name())
                    .orElseGet(() -> {
                        Category newCategory = new Category();
                        newCategory.setName(data.name());
                        newCategory.setDescription(data.description());
                        newCategory.setActive(true);

                        return categoryRepository.save(newCategory);
                    });

            categories.put(data.name(), category);
        }

        return categories;
    }

    private void seedProducts(Map<String, Category> categories) {

        List<ProductData> products = List.of(

            // ELECTRONICS
            new ProductData("Electronics", "Sony Wireless Headphones",
                "Premium wireless headphones with noise cancellation and deep bass.",
                "5999", 25, "Sony",
                "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=600", true),

            new ProductData("Electronics", "JBL Bluetooth Speaker",
                "Portable Bluetooth speaker with powerful sound and long battery life.",
                "3499", 40, "JBL",
                "https://images.unsplash.com/photo-1608043152269-423dbba4e7e1?w=600", true),

            new ProductData("Electronics", "Logitech Wireless Mouse",
                "Ergonomic wireless mouse suitable for office and everyday use.",
                "1299", 50, "Logitech",
                "https://images.unsplash.com/photo-1527864550417-7fd91fc51a46?w=600", true),

            new ProductData("Electronics", "Mechanical Keyboard",
                "RGB mechanical keyboard with responsive keys and durable construction.",
                "2999", 0, "Redragon",
                "https://images.unsplash.com/photo-1587829741301-dc798b83add3?w=600", true),

            // MOBILES
            new ProductData("Mobiles", "Samsung Galaxy S24",
                "Premium smartphone with an advanced camera and AMOLED display.",
                "64999", 15, "Samsung",
                "https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?w=600", true),

            new ProductData("Mobiles", "Apple iPhone 15",
                "Apple smartphone featuring a powerful processor and advanced camera system.",
                "69999", 12, "Apple",
                "https://images.unsplash.com/photo-1592750475338-74b7b21085ab?w=600", true),

            new ProductData("Mobiles", "OnePlus Nord CE",
                "Feature-packed smartphone with smooth performance and fast charging.",
                "24999", 20, "OnePlus",
                "https://images.unsplash.com/photo-1598327105666-5b89351aff97?w=600", true),

            new ProductData("Mobiles", "Redmi Note 13",
                "Affordable smartphone with a vibrant display and reliable performance.",
                "15999", 30, "Xiaomi",
                "https://images.unsplash.com/photo-1598327105854-c8674faddf9e?w=600", false),

            // LAPTOPS
            new ProductData("Laptops", "Dell Inspiron 15",
                "Everyday laptop suitable for students, office work and productivity.",
                "54999", 10, "Dell",
                "https://images.unsplash.com/photo-1496181133206-80ce9b88a853?w=600", true),

            new ProductData("Laptops", "HP Pavilion 14",
                "Lightweight laptop with a modern design and dependable performance.",
                "62999", 8, "HP",
                "https://images.unsplash.com/photo-1517336714731-489689fd1ca8?w=600", true),

            new ProductData("Laptops", "Lenovo IdeaPad Slim",
                "Slim and portable laptop designed for everyday computing.",
                "47999", 14, "Lenovo",
                "https://images.unsplash.com/photo-1588872657578-7efd1f1555ed?w=600", true),

            new ProductData("Laptops", "ASUS TUF Gaming Laptop",
                "Performance-oriented gaming laptop with dedicated graphics.",
                "89999", 5, "ASUS",
                "https://images.unsplash.com/photo-1603302576837-37561b2e2302?w=600", true),

            // FASHION
            new ProductData("Fashion", "Men's Casual T-Shirt",
                "Comfortable cotton casual T-shirt for everyday wear.",
                "599", 100, "Roadster",
                "https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?w=600", true),

            new ProductData("Fashion", "Men's Denim Jacket",
                "Classic denim jacket with a stylish casual fit.",
                "1999", 35, "Levis",
                "https://images.unsplash.com/photo-1551028719-00167b16eac5?w=600", true),

            new ProductData("Fashion", "Women's Summer Dress",
                "Lightweight and elegant summer dress for casual occasions.",
                "1499", 28, "DressBerry",
                "https://images.unsplash.com/photo-1539109136881-3be0616acf4b?w=600", true),

            new ProductData("Fashion", "Classic Men's Hoodie",
                "Soft and comfortable hoodie for everyday styling.",
                "1299", 45, "H&M",
                "https://images.unsplash.com/photo-1556821840-3a63f95609a7?w=600", true),

            // FOOTWEAR
            new ProductData("Footwear", "Nike Running Shoes",
                "Lightweight running shoes designed for comfort and daily training.",
                "4999", 22, "Nike",
                "https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=600", true),

            new ProductData("Footwear", "Adidas Sports Sneakers",
                "Stylish sneakers suitable for sports and casual outfits.",
                "3999", 30, "Adidas",
                "https://images.unsplash.com/photo-1608231387042-66d1773070a5?w=600", true),

            new ProductData("Footwear", "Puma Casual Shoes",
                "Comfortable casual shoes with a modern sporty design.",
                "2799", 18, "Puma",
                "https://images.unsplash.com/photo-1495555961986-6d4c1ecb7be3?w=600", true),

            new ProductData("Footwear", "Classic White Sneakers",
                "Minimal white sneakers that complement everyday outfits.",
                "1899", 0, "Campus",
                "https://images.unsplash.com/photo-1495555961986-6d4c1ecb7be3?w=600", true),

            // HOME & KITCHEN
            new ProductData("Home & Kitchen", "Coffee Maker",
                "Compact coffee maker for preparing fresh coffee at home.",
                "2499", 16, "Philips",
                "https://images.unsplash.com/photo-1495474472287-4d71bcdd2085?w=600", true),

            new ProductData("Home & Kitchen", "Modern Table Lamp",
                "Elegant table lamp for bedrooms, study rooms and living spaces.",
                "1499", 25, "Home Centre",
                "https://images.unsplash.com/photo-1507473885765-e6ed057f782c?w=600", true),

            new ProductData("Home & Kitchen", "Kitchen Cookware Set",
                "Essential cookware set for everyday home cooking.",
                "3299", 12, "Prestige",
                "https://images.unsplash.com/photo-1556911220-bff31c812dba?w=600", true),

            new ProductData("Home & Kitchen", "Decorative Indoor Plant",
                "Beautiful indoor plant to add greenery to your living space.",
                "499", 40, "Ugaoo",
                "https://images.unsplash.com/photo-1485955900006-10f4d324d411?w=600", true),

            // ACCESSORIES
            new ProductData("Accessories", "Leather Wallet",
                "Compact wallet with multiple compartments for cards and cash.",
                "999", 35, "WildHorn",
                "https://images.unsplash.com/photo-1627123424574-724758594e93?w=600", true),

            new ProductData("Accessories", "Classic Wrist Watch",
                "Elegant wristwatch suitable for formal and casual occasions.",
                "2499", 20, "Fossil",
                "https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=600", true),

            new ProductData("Accessories", "Premium Sunglasses",
                "Stylish sunglasses with UV protection for everyday use.",
                "1299", 27, "Ray-Ban",
                "https://images.unsplash.com/photo-1511499767150-a48a237f0083?w=600", true),

            // GAMING
            new ProductData("Gaming", "PlayStation 5 Controller",
                "Wireless gaming controller with immersive haptic feedback.",
                "5999", 14, "Sony",
                "https://images.unsplash.com/photo-1606144042614-b2417e99c4e3?w=600", true),

            new ProductData("Gaming", "Gaming Headset",
                "Over-ear gaming headset with microphone and immersive audio.",
                "2999", 25, "HyperX",
                "https://images.unsplash.com/photo-1599669454699-248893623440?w=600", true),

            new ProductData("Gaming", "RGB Gaming Mouse",
                "High-precision gaming mouse with customizable RGB lighting.",
                "1799", 32, "Razer",
                "https://images.unsplash.com/photo-1615663245857-ac93bb7c39e7?w=600", true),

            new ProductData("Gaming", "Gaming Keyboard",
                "Responsive gaming keyboard designed for competitive gameplay.",
                "3499", 18, "Corsair",
                "https://images.unsplash.com/photo-1541140532154-b024d705b90a?w=600", false),

            // BEAUTY & PERSONAL CARE
            new ProductData("Beauty & Personal Care", "Face Moisturizer",
                "Daily moisturizer designed to keep skin feeling soft and hydrated.",
                "499", 45, "Nivea",
                "https://images.unsplash.com/photo-1608248543803-ba4f8c70ae0b?w=600", true),

            new ProductData("Beauty & Personal Care", "Perfume for Men",
                "Refreshing fragrance suitable for daily and special occasions.",
                "1499", 30, "Wild Stone",
                "https://images.unsplash.com/photo-1541643600914-78b084683601?w=600", true),

            new ProductData("Beauty & Personal Care", "Hair Care Shampoo",
                "Everyday shampoo for gentle hair cleansing and care.",
                "349", 50, "Dove",
                "https://images.unsplash.com/photo-1608248543803-ba4f8c70ae0b?w=600", true),

            // SPORTS & FITNESS
            new ProductData("Sports & Fitness", "Yoga Mat",
                "Non-slip exercise mat for yoga, stretching and workouts.",
                "899", 40, "Boldfit",
                "https://images.unsplash.com/photo-1592432678016-e910b452f9a2?w=600", true),

            new ProductData("Sports & Fitness", "Adjustable Dumbbells",
                "Strength training equipment suitable for home workouts.",
                "2999", 15, "Lifelong",
                "https://images.unsplash.com/photo-1583454110551-21f2fa2afe61?w=600", true),

            new ProductData("Sports & Fitness", "Sports Water Bottle",
                "Reusable water bottle for gym, sports and everyday hydration.",
                "499", 60, "Milton",
                "https://images.unsplash.com/photo-1602143407151-7111542de6e8?w=600", true),

            new ProductData("Sports & Fitness", "Fitness Resistance Bands",
                "Resistance bands for strength training and mobility exercises.",
                "699", 35, "Boldfit",
                "https://images.unsplash.com/photo-1598289431512-b97b0917affc?w=600", true)
        );

        for (ProductData data : products) {

            boolean alreadyExists = productRepository
                    .existsByNameIgnoreCaseAndBrandIgnoreCase(
                            data.name(), data.brand()
                    );

            if (alreadyExists) {
                continue;
            }

            Category category = categories.get(data.categoryName());

            if (category == null) {
                throw new IllegalStateException(
                        "Category not found: " + data.categoryName()
                );
            }

            Product product = new Product();

            product.setName(data.name());
            product.setDescription(data.description());
            product.setPrice(new BigDecimal(data.price()));
            product.setStockQuantity(data.stockQuantity());
            product.setBrand(data.brand());
            product.setImageUrl(data.imageUrl());
            product.setActive(data.active());
            product.setCategory(category);

            productRepository.save(product);
        }
    }

    private record CategoryData(
            String name,
            String description
    ) {}

    private record ProductData(
            String categoryName,
            String name,
            String description,
            String price,
            Integer stockQuantity,
            String brand,
            String imageUrl,
            Boolean active
    ) {}
}
