package ru.home.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.home.persist.product.Product;
import ru.home.persist.product.ProductRepository;
import ru.home.service.product.ProductRepr;
import ru.home.service.product.ProductService;
import ru.home.service.user.UserRepr;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Controller // аналог сервлета, обрабатывающий соответствующий URL
@RequestMapping("/product")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    private ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String listPage(Model model, @RequestParam("productNameFilter") Optional<String> productNameFilter,
                                        @RequestParam("description") Optional<String> description,
                                        @RequestParam("priceMinFilter") Optional<BigDecimal> priceMinFilter,
                                        @RequestParam("priceMaxFilter") Optional<BigDecimal> priceMaxFilter) {
        logger.info("List page requested");

        List<ProductRepr> products = productService.findWithFilter(
                productNameFilter.filter(s -> !s.isBlank()).orElse(null),
                description.filter(s -> !s.isBlank()).orElse(null),
                priceMinFilter.orElse(null),
                priceMaxFilter.orElse(null)
        );
        model.addAttribute("products", products);
        return "product";
    }

    @GetMapping("/{id}")
    public String editPage(@PathVariable("id") Long id, Model model) {
        logger.info("Edit page for id {} requested", id);

        model.addAttribute("product", productService.findById(id).orElseThrow(NotFoundException::new));
        return "product_form";
    }

    @PostMapping("/update")
    public String update(@Valid @ModelAttribute ProductRepr product, BindingResult result) {
        logger.info("Update endpoint requested");

        if (result.hasErrors())
            return "product_form";
        logger.info("Updating user with id {}", product.getId());
        productService.save(product);
        return "redirect:/product";
    }

    @GetMapping("/new")
    public String create(Model model) {
        logger.info("Creating new product");

        model.addAttribute("product", new ProductRepr());
        return "product_form";
    }

    @DeleteMapping("/{id}")
    public String remove(@PathVariable("id") Long id) {
        logger.info("Deleting product for id {} requested", id);

        productService.delete(id);
        return "redirect:/product";
    }
}
