package com.example.service;

import com.example.dto.ClientCreditProductDto;
import com.example.dto.ClientInfoDto;
import com.example.entity.PaymentRegistry;
import com.example.entity.ProductRegistry;
import com.example.repository.PaymentScheduleRepo;
import com.example.repository.ProductRegistryRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CreditProductService {

    private final ProductRegistryRepo productRegistryRepo;
    private final PaymentScheduleRepo paymentScheduleRepo;
    private final RestTemplate restTemplate;



    @Value("${credit.max-limit}")
    private BigDecimal maxCreditLimit;

    public void processCreditRequest(ClientCreditProductDto dto) {

        List<ProductRegistry> existingProducts = productRegistryRepo.findByClientId(dto.getClientId());

        BigDecimal totalCredit = existingProducts.stream()
                .map(p -> getTotalProductAmount(p)) // сумма по продукту
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        ClientInfoDto clientInfo = restTemplate.getForObject(
                "http://ms1:8081/clients/" + dto.getClientId(),
                ClientInfoDto.class
        );

        if (clientInfo == null) throw new RuntimeException("Client info not found");

        boolean hasOverdue = existingProducts.stream()
                .anyMatch(p -> checkOverduePayments(p));

        if (totalCredit.add(dto.getCreditAmount()).compareTo(maxCreditLimit) > 0 || hasOverdue) {
            saveRejected(dto);
            return;
        }

        ProductRegistry product = ProductRegistry.builder()
                .clientId(dto.getClientId())
                .accountId(dto.getAccountId())
                .producerId(dto.getProducerId())
                .interestRate(dto.getInterestRate())
                .openDate(LocalDate.now())
                .mountNumber(dto.getMountNumber())
                .paymentDate(LocalDateTime.now())
                .build();

        productRegistryRepo.save(product);

        generatePaymentSchedule(product, dto.getCreditAmount());
    }

    private BigDecimal getTotalProductAmount(ProductRegistry product) {
        List<PaymentRegistry> payments = paymentScheduleRepo.findByProductRegistryId(product.getId());
        return payments.stream()
                .map(PaymentRegistry::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private boolean checkOverduePayments(ProductRegistry product) {
        List<PaymentRegistry> payments = paymentScheduleRepo.findByProductRegistryId(product.getId());
        return payments.stream().anyMatch(PaymentRegistry::getExpired);
    }

    private void saveRejected(ClientCreditProductDto dto) {
        ProductRegistry rejected = ProductRegistry.builder()
                .clientId(dto.getClientId())
                .accountId(dto.getAccountId())
                .producerId(dto.getProducerId())
                .interestRate(dto.getInterestRate())
                .openDate(LocalDate.now())
                .mountNumber(dto.getMountNumber())
                .paymentDate(LocalDateTime.now())
                .build();

        productRegistryRepo.save(rejected);
    }

    private void generatePaymentSchedule(ProductRegistry product, BigDecimal totalAmount) {
        BigDecimal monthlyRate = BigDecimal.valueOf(product.getInterestRate() / 12 / 100);
        int n = product.getMountNumber();

        BigDecimal numerator = monthlyRate.multiply((BigDecimal.ONE.add(monthlyRate)).pow(n));
        BigDecimal denominator = (BigDecimal.ONE.add(monthlyRate)).pow(n).subtract(BigDecimal.ONE);
        BigDecimal annuityPayment = totalAmount.multiply(numerator)
                .divide(denominator, 2, RoundingMode.HALF_UP);

        BigDecimal remainingDebt = totalAmount;

        for (int month = 1; month <= n; month++) {
            BigDecimal interest = remainingDebt.multiply(monthlyRate).setScale(2, RoundingMode.HALF_UP);
            BigDecimal principal = annuityPayment.subtract(interest).setScale(2, RoundingMode.HALF_UP);
            remainingDebt = remainingDebt.subtract(principal);

            PaymentRegistry payment = PaymentRegistry.builder()
                    .productRegistryId(product.getId())
                    .paymentDate(LocalDate.now().plusMonths(month))
                    .amount(annuityPayment)
                    .interestRateAmount(interest)
                    .debtAmount(principal)
                    .expired(false)
                    .paymentExpirationDate(LocalDate.now().plusMonths(month))
                    .build();

            paymentScheduleRepo.save(payment);
        }
    }
}

