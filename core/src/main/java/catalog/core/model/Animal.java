package catalog.core.model;

import lombok.*;
import org.hibernate.Hibernate;


import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "animals", schema = "public")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class Animal extends BaseEntity<Long> {

    @Column(name = "idcustomer")
    private Long customerID;

    @Column(name = "age")
    private Integer age;

    @Column(name = "breed")
    private String breed;

    @Column(name = "color")
    private String color;

    @OneToMany
    @JoinColumn(name = "animalid", referencedColumnName = "id")
    private List<Preference> preferences;

    public Animal(long id,int age,String breed,String color){
        this.customerID=id;
        this.age=age;
        this.breed=breed;
        this.color=color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Animal animal = (Animal) o;
        return id != null && Objects.equals(id, animal.id);
    }

    @Override
    public String toString() {
        return "Animal{" +
                " id=" + id +
                ", customerID=" + customerID +
                ", age=" + age +
                ", breed='" + breed + '\'' +
                ", color='" + color + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
