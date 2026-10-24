package com.example.RailConnect.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.example.RailConnect.entity.Train;
import com.example.RailConnect.repository.TrainRepository;

@RestController
@RequestMapping("/api/trains")
@CrossOrigin(origins = "*")
public class TrainController {

    private final TrainRepository trainRepository;

    public TrainController(TrainRepository trainRepository) {
        this.trainRepository = trainRepository;
    }

    @GetMapping
    public List<Train> getAllTrains() {
        return trainRepository.findAll();
    }
    @GetMapping("/search")
    public List<Train> searchTrains(
            @RequestParam String source,
            @RequestParam String destination) {

        if (source.equalsIgnoreCase(destination)) {
            return java.util.Collections.emptyList();
        }

        return trainRepository
                .findBySourceIgnoreCaseAndDestinationIgnoreCase(
                        source.trim(),
                        destination.trim()
                );
    }
   

    @PostMapping
    public Train addTrain(@RequestBody Train train) {
        return trainRepository.save(train);
    }

    @GetMapping("/{id}")
    public Train getTrainById(@PathVariable Long id) {
        return trainRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Train not found"));
    }

    @DeleteMapping("/{id}")
    public void deleteTrain(@PathVariable Long id) {
        trainRepository.deleteById(id);
    }
    @PutMapping("/{id}")
    public Train updateTrain(
            @PathVariable Long id,
            @RequestBody Train train) {

        Train existingTrain = trainRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Train not found"));

        existingTrain.setTrainName(train.getTrainName());
        existingTrain.setSource(train.getSource());
        existingTrain.setDestination(train.getDestination());

        return trainRepository.save(existingTrain);
    }
}