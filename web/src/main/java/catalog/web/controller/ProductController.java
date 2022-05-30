package catalog.web.controller;

import catalog.core.model.Product;
import catalog.core.service.IProductService;
import catalog.web.converter.ProductConverter;
import catalog.web.dto.ProductDto;
import catalog.web.dto.ProductsDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProductController {
    public static final Logger log = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private IProductService productService;

    @Autowired
    private ProductConverter productConverter;

//    @RequestMapping(value = "/products")
//    List<Product> getAllProducts() {
//        //todo: logs
//        return productService.getAllProducts();
//
//    }

    @RequestMapping(value = "/products")
    ProductsDto getAllProducts() {
        log.trace("getAllProducts --- method entered");

        ProductsDto p=new ProductsDto(
                productConverter.convertModelsToDtos(
                        productService.getAllProducts()));
        log.trace("getAllProducts - method finished: result={}", p);

        return p;
//        return new ProductsDto(
//                productConverter.convertModelsToDtos(
//                        productService.getAllProducts()));
    }

    @RequestMapping(value = "/products/{filter}")
    ProductsDto filterProducts(@PathVariable String filter) {
        log.trace("filterProducts --- method entered, type = {}",filter);

        ProductsDto p=new ProductsDto(
                productConverter.convertModelsToDtos(
                        productService.filterProductsByType(filter)));
        log.trace("filterProducts - method finished: result={}", p);

        return p;
//        return new ProductsDto(
//                productConverter.convertModelsToDtos(
//                        productService.filterProductsByType(filter)));
    }

    @RequestMapping(value = "/products", method = RequestMethod.POST)
    ProductDto addProduct(@RequestBody ProductDto productDto) {
        log.trace("addProduct - method entered: product={}", productDto);

        Product product = productConverter.convertDtoToModel(productDto);

        Product result = productService.saveProduct(product);

        log.trace("addProduct - method finished: result ={}",result);
        return productConverter.convertModelToDto(result);
    }

    @RequestMapping(value = "/products/{id}", method = RequestMethod.PUT)
    ProductDto updateProduct(@PathVariable Long id,
                             @RequestBody ProductDto dto) {
        log.trace("updateProduct - method entered: product={}", dto);
        Long getId=id;
        ProductDto p =productConverter.convertModelToDto(
                productService.updateProduct(
                        productConverter.convertDtoToModel(dto)
                ));
        log.trace("updateProduct - method finished: result ={}",p);

        return p;
//        return
//                productConverter.convertModelToDto(
//                        productService.updateProduct(
//                                productConverter.convertDtoToModel(dto)
//                        ));
    }

    @RequestMapping(value = "/products/{id}", method = RequestMethod.DELETE)
    ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        log.trace("deleteProduct - method entered: id={}", id);

        productService.deleteProduct(id);
        log.trace("deleteProduct - method finished");

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
