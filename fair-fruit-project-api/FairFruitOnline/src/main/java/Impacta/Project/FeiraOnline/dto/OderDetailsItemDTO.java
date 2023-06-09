package Impacta.Project.FeiraOnline.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OderDetailsItemDTO {
    private String productName;
    private String productImage;
    private BigDecimal productPrice;
    private BigDecimal productTotal;
    private Integer productQuantity;
}
