package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @SequenceGenerator(name = "my_seq", sequenceName = "seq1", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "my_seq")
    private Long id;

    @NotNull
    @Size(min = 1)
    @Column(name = "order_number")
    private String orderNumber;

    @Valid
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "order_rows",
            joinColumns=@JoinColumn(name = "orders_id",
                    referencedColumnName = "id")
    )
    private List<OrderRow> orderRows;

}
