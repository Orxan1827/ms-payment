package az.example.mspayment.client;
import az.example.mspayment.model.client.CountryDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "ms-country", url = "${client.ms-country.endpoint}")
public interface CountryClient {
    @GetMapping("/api/countries/{currency}")
    List<CountryDto>getAvailableCountries(@RequestParam String currency);
}
