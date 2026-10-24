package com.example.RailConnect.repository;

import com.example.RailConnect.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByPnr(String pnr);

    boolean existsByPnrAndPaymentStatus(String pnr, String paymentStatus);
}