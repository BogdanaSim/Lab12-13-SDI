package catalog.web.converter;

import catalog.core.model.Product;
import catalog.web.dto.ProductDto;
import org.springframework.stereotype.Component;

@Component
public class ProductConverter extends BaseConverter<Product, ProductDto> {
    @Override
    public Product convertDtoToModel(ProductDto dto) {
        Product model = new Product();
        model.setId(dto.getId());
        model.setType(dto.getType());
        model.setPrice(dto.getPrice());
        return model;
    }

    @Override
    public ProductDto convertModelToDto(Product product) {
        ProductDto dto = new ProductDto(product.getType(), product.getPrice());
        dto.setId(product.getId());
        return dto;
    }
}
