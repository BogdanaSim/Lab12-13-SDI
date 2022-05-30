package catalog.core.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.security.InvalidParameterException;
import java.util.Comparator;
import java.util.Objects;
@Entity
@Table(name = "preferences", schema = "public")
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class Preference extends BaseEntity<Long> {
    @Column(name = "animalid")
    private Long animal_id;
    @Column(name = "productid")
    private Long product_id;
    @Column(name = "reason")
    private String reason;



//    @ManyToOne
//    @JoinColumn(name = "animalid", referencedColumnName = "id")
//    private Animal animal;
//
//    @ManyToOne
//    @JoinColumn(name = "productid", referencedColumnName = "id")
//    private Product product;

    public Preference(long ida, long idp, String reason){
        this.animal_id=ida;
        this.product_id=idp;
        this.reason=reason;
    }

    @Override
    public String toString() {
        return "Preference{" +
                "id= "+id+
                ", animal id = " + animal_id + '\'' +
                ", product id = " + product_id + '\'' +
                ", reason = " + reason + '\'' +
                '}';
    }
}