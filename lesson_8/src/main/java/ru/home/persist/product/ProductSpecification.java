package ru.home.persist.product;

import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public final class ProductSpecification {

    public static Specification<Product> productNameLike(String name) {
        return ((root, query, cb) -> cb.like(root.get("productName"), "%" + name + "%"));
    }

    public static Specification<Product> descriptionLike(String description) {
        return ((root, query, cb) -> cb.like(root.get("description"), "%" + description + "%"));
    }

    public static Specification<Product> priceMinFilter(BigDecimal priceMinFilter) {
        return ((root, query, cb) -> cb.ge(root.get("price"), priceMinFilter));
    }

    public static Specification<Product> priceMaxFilter(BigDecimal priceMaxFilter) {
        return ((root, query, cb) -> cb.le(root.get("price"), priceMaxFilter));
    }
}
