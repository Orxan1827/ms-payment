package az.example.mspayment.service;

import az.example.mspayment.client.CountryClient;
import az.example.mspayment.entity.Payment;
import az.example.mspayment.exception.NotFoundException;
import az.example.mspayment.mapper.PaymentMapper;
import az.example.mspayment.model.request.PaymentCriteria;
import az.example.mspayment.model.request.PaymentRequest;
import az.example.mspayment.model.response.PageablePaymentResponse;
import az.example.mspayment.model.response.PaymentResponse;
import az.example.mspayment.repository.PaymentRepository;
import az.example.mspayment.service.specification.PaymentSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static az.example.mspayment.mapper.PaymentMapper.mapEntityToResponse;
import static az.example.mspayment.mapper.PaymentMapper.mapRequestToEntity;
import static az.example.mspayment.model.constant.ExceptionConstants.COUNTRY_NOT_FOUND_CODE;
import static az.example.mspayment.model.constant.ExceptionConstants.COUNTRY_NOT_FOUND_MESSAGE;
import static org.springframework.data.domain.Sort.Direction.DESC;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final CountryClient countryClient;

    @Transactional
    public void savePayment(PaymentRequest paymentRequest) {
        log.info("savePayment.started");
        countryClient.getAvailableCountries(paymentRequest.getCurrency())
                .stream()
                .filter(country -> country.getRemainingLimit().compareTo(paymentRequest.getAmount()) > 0)
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format(COUNTRY_NOT_FOUND_MESSAGE, paymentRequest.getAmount(),
                        paymentRequest.getCurrency(), COUNTRY_NOT_FOUND_CODE)));

        paymentRepository.save(mapRequestToEntity(paymentRequest));
        log.info("savePayment.success");
    }

    public PaymentResponse getPaymentById(long id) {
        log.info("getPayment.start id: {}", id);
        Payment payment = fetchPaymentIFExist(id);
        log.info("getPayment.success id: {}", id);
        return mapEntityToResponse(payment);
    }

    public PageablePaymentResponse getAllPayments(int page, int count, PaymentCriteria paymentCriteria) {
        PageRequest pageRequest = PageRequest.of(page, count, Sort.by(DESC, "id"));
        Page<Payment> pageablePayment = paymentRepository.findAll(new PaymentSpecification(paymentCriteria), pageRequest);
        List<PaymentResponse> payments = pageablePayment.getContent()
                .stream()
                .map(PaymentMapper::mapEntityToResponse).collect(Collectors.toList());

        return  PageablePaymentResponse.builder()
                .payments(payments)
                .hasNextText(pageablePayment.hasNext())
                .totalElements(pageablePayment.getTotalElements())
                .totalPage(pageablePayment.getTotalPages())
                .build();
    }


    public void updatePayment(long id, PaymentRequest request) {
        log.info("updatePayment.start id: {}", id);
        Payment payment = fetchPaymentIFExist(id);
        payment.setAmount(request.getAmount());
        payment.setDescription(request.getDescription());
        paymentRepository.save(payment);
        log.info("updatePayment.success id: {}", id);
    }

    public void updatePaymentSome(long id, PaymentRequest request) {
        log.info("updateSome.start id: {}", id);
        Payment payment = fetchPaymentIFExist(id);
        if (request.getAmount() != null)
            payment.setAmount(request.getAmount());
        if (request.getDescription() != null)
            payment.setDescription(request.getDescription());
        paymentRepository.save(payment);
    }


    public void deletePayment(long id) {
        log.info("deletePayment.start id: {}", id);
        fetchPaymentIFExist(id);
        paymentRepository.deleteById(id);
        log.info("deletePayment.success id: {}", id);
    }

    private Payment fetchPaymentIFExist(long id) {
        return paymentRepository.findById(id).orElseThrow(() -> new RuntimeException());
    }

}
