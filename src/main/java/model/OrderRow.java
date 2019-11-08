package model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
@Table(name = "order_rows")
public class OrderRow {

//    @Id
//    @GeneratedValue
//    private Long id;

//    private Long orderId;

    @NonNull
    @Column(name = "item_name")
    private String itemName;

    @NotNull
    @Min(1)
    private Integer price;

    @NotNull
    @Min(1)
    private Integer quantity;

}
