package catalog.core.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.security.InvalidParameterException;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
@Entity
@Table(name = "products", schema = "public")
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class Product extends BaseEntity<Long>  {
    @Column(name = "type")
    private String type;
    @Column(name = "price")
    private Float price;

    @OneToMany
    @JoinColumn(name = "productid", referencedColumnName = "id")
    private List<Preference> preferences;

    public Product(String type, float price){
        this.type=type;
        this.price=price;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id ="+id+
                ", type = " + type +
                ", price = " + price +
                '}';
    }
}