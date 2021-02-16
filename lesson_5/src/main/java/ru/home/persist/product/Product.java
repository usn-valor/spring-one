package ru.home.persist.product;

import javax.persistence.*;

@Entity
@Table(name = "products")
@NamedQueries({
        @NamedQuery(name = "productByName", query = "from Product p where p.productName=:productName"),
        @NamedQuery(name = "allProducts", query = "from Product")
})
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 128, unique = true, nullable = false)
    private String productName;

    @Column(length = 512, nullable = false)
    private String description;

    @Column
    private int price;

    public Product() {
    }

    public Product(String productName, String description, int price) {
        this.productName = productName;
        this.description = description;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
