package com.example.RailConnect.controller;

import com.example.RailConnect.entity.Booking;
import com.example.RailConnect.entity.Payment;
import com.example.RailConnect.entity.Train;
import com.example.RailConnect.repository.BookingRepository;
import com.example.RailConnect.repository.PaymentRepository;
import com.example.RailConnect.repository.TrainRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "*")
public class PaymentController {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final TrainRepository trainRepository;

    public PaymentController(
            PaymentRepository paymentRepository,
            BookingRepository bookingRepository,
            TrainRepository trainRepository) {

        this.paymentRepository = paymentRepository;
        this.bookingRepository = bookingRepository;
        this.trainRepository = trainRepository;
    }

    // Create Payment
    @PostMapping
    public ResponseEntity<?> createPayment(@RequestBody Payment payment) {

        // 1. Validate PNR
        if (payment.getPnr() == null || payment.getPnr().isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "PNR is required"));
        }

        Booking booking = bookingRepository.findByPnr(payment.getPnr())
                .orElse(null);

        if (booking == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid PNR. Booking not found"));
        }

        if ("CANCELLED".equalsIgnoreCase(booking.getStatus())) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Cannot pay for a cancelled booking"));
        }

        // 2. Validate Amount
        if (payment.getAmount() == null || payment.getAmount() <= 0) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Amount must be greater than zero"));
        }

        // 3. Find Train and validate fare
        Train train = trainRepository
                .findByTrainNumber(booking.getTrainNumber())
                .orElse(null);

        if (train == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Train not found"));
        }

        if (Math.abs(payment.getAmount() - train.getFare()) > 0.001) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Payment amount does not match train fare"));
        }

        // 4. Validate Payment Method
        if (payment.getPaymentMethod() == null
                || payment.getPaymentMethod().isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Payment method is required"));
        }

        // 5. Prevent duplicate successful payments
        if (paymentRepository.existsByPnrAndPaymentStatus(
                payment.getPnr(), "SUCCESS")) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Payment already exists for this PNR"));
        }

        // Demo only: this records a payment; it does not process real money.
        payment.setPaymentStatus("SUCCESS");

        Payment savedPayment = paymentRepository.save(payment);

        return ResponseEntity.ok(savedPayment);
    }

    // Get all payments
    @GetMapping
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    // Get payments by PNR
    @GetMapping("/pnr/{pnr}")
    public List<Payment> getPaymentsByPnr(@PathVariable String pnr) {
        return paymentRepository.findByPnr(pnr);
    }
}