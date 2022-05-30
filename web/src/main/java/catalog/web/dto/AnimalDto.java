package catalog.web.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AnimalDto extends BaseDto{
    long customerID;

    int age;
    String breed;
    String color;
}
