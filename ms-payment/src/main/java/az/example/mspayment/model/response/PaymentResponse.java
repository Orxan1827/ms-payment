package az.example.mspayment.model.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentResponse {

    long id;
    BigDecimal amount;
    String description;
    LocalDateTime responseAt;
}
