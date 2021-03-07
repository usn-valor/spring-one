package ru.home.service.product;

import ru.home.persist.product.Product;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;

//DataTransferObject - DTO
public class ProductRepr {

    private Long id;

    @NotEmpty
    private String productName;

    @NotEmpty
    private String description;

    //@NotEmpty
    private BigDecimal price;

    public ProductRepr() {
    }

    public ProductRepr(Product product) {
        this.id = product.getId();
        this.productName = product.getProductName();
        this.description = product.getDescription();
        this.price = product.getPrice();
    }

    public ProductRepr(String productName) {
        this.productName = productName;
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
