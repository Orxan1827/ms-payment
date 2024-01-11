package az.example.mspayment.controller;

import az.example.mspayment.model.request.PaymentCriteria;
import az.example.mspayment.model.request.PaymentRequest;
import az.example.mspayment.model.response.PageablePaymentResponse;
import az.example.mspayment.model.response.PaymentResponse;
import az.example.mspayment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/payments")
public class PaymentController {
    private final PaymentService paymentService;

    @GetMapping("/payments")
    public String getMessage(){
        return "hello i am payments";
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void savePayment(@Valid @RequestBody PaymentRequest request){
        paymentService.savePayment(request);
    }

    @GetMapping
    public PageablePaymentResponse getAllPayments(@RequestParam int page, @RequestParam int count, PaymentCriteria paymentCriteria){
        System.out.println(paymentService.getAllPayments(page,count,paymentCriteria));
        return paymentService.getAllPayments(page,count,paymentCriteria);
    }

    @GetMapping("/{id}")
    public PaymentResponse getPayment(@PathVariable long id){
       return paymentService.getPaymentById(id);
    }

    @PutMapping("/edit/{id}")
    public void updatePayment(@PathVariable long id,@RequestBody PaymentRequest request){
        paymentService.updatePayment(id,request);
    }

    @DeleteMapping("/delete/{id}")
    public void deletePayment(@PathVariable long id){
        paymentService.deletePayment(id);
    }


}
