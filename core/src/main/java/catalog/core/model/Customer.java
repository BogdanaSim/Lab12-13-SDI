package catalog.core.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.security.InvalidParameterException;
import java.util.Comparator;
import java.util.List;

@Entity
@Table(name = "customers", schema = "public")
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class Customer extends BaseEntity<Long>{
    @Column(name = "cname")
    private String name;
    @Column(name = "phonenumber")
    private String phoneNumber;

    public Customer(String name, String phoneNumber){
        this.name=name;
        this.phoneNumber=phoneNumber;
    }

    @OneToMany
    @JoinColumn(name = "idcustomer", referencedColumnName = "id")
    private List<Animal> animals;

    @Override
    public String toString() {
        return "Customer{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }

}

