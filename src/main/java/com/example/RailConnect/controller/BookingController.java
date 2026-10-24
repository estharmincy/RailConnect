package com.example.RailConnect.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.example.RailConnect.entity.Booking;
import com.example.RailConnect.repository.BookingRepository;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "*")
public class BookingController {

    private final BookingRepository bookingRepository;

    public BookingController(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    // Book a ticket
    @PostMapping
    public Booking bookTicket(@RequestBody Booking booking) {

        booking.setPnr(UUID.randomUUID().toString()
                .replace("-", "").substring(0, 10).toUpperCase());

        booking.setStatus("CONFIRMED");

        return bookingRepository.save(booking);
    }

    // View all bookings
    @GetMapping
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    // Check PNR status
    @GetMapping("/pnr/{pnr}")
    public Booking checkPNR(@PathVariable String pnr) {

        return bookingRepository.findByPnr(pnr)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "PNR not found"));
    }

    // Cancel ticket
    @PutMapping("/cancel/{pnr}")
    public Booking cancelTicket(@PathVariable String pnr) {

        Booking booking = bookingRepository.findByPnr(pnr)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "PNR not found"));

        if ("CANCELLED".equalsIgnoreCase(booking.getStatus())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Booking is already cancelled");
        }

        booking.setStatus("CANCELLED");

        return bookingRepository.save(booking);
    }

    // Delete booking permanently
    @DeleteMapping("/{id}")
    public String deleteBooking(@PathVariable Long id) {

        if (!bookingRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Booking not found with ID: " + id);
        }

        bookingRepository.deleteById(id);

        return "Booking deleted successfully. ID: " + id;
    }
}