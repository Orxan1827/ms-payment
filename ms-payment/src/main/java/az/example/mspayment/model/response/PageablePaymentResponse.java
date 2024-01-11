package az.example.mspayment.model.response;

import liquibase.pro.packaged.B;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class PageablePaymentResponse {
    List<PaymentResponse> payments;
    long totalElements;
    int totalPage;
    boolean hasNextText;
}
