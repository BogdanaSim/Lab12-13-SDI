package catalog.web.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PreferenceDto extends BaseDto{
    private Long animal_id;
    private Long product_id;
    private String reason;
}
