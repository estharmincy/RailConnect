package com.example.RailConnect;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.RailConnect.entity.Train;
import com.example.RailConnect.repository.TrainRepository;

@Component
public class TrainDataLoader implements CommandLineRunner {

    private final TrainRepository trainRepository;

    public TrainDataLoader(TrainRepository trainRepository) {
        this.trainRepository = trainRepository;
    }

    @Override
    public void run(String... args) {

        addTrain("12602", "Chennai Express",
                "Chennai", "Bangalore", "06:00", "11:00", 450, 120);

        addTrain("12603", "Coimbatore Express",
                "Chennai", "Coimbatore", "07:00", "14:00", 500, 100);

        addTrain("12604", "Madurai Express",
                "Chennai", "Madurai", "08:00", "16:00", 550, 100);

        addTrain("12605", "Kanyakumari Express",
                "Chennai", "Nagercoil", "17:00", "06:00", 650, 150);

        addTrain("12606", "Kerala Express",
                "Chennai", "Thiruvananthapuram", "18:00", "08:00", 700, 120);

        addTrain("12607", "Mysore Express",
                "Bangalore", "Mysore", "07:30", "10:30", 250, 100);

        addTrain("12608", "Hyderabad Express",
                "Chennai", "Hyderabad", "19:00", "07:00", 750, 100);

        addTrain("12609", "Mumbai Express",
                "Chennai", "Mumbai", "20:00", "12:00", 1000, 120);

        addTrain("12610", "Delhi Express",
                "Chennai", "New Delhi", "18:00", "20:00", 1500, 150);

        addTrain("12611", "Kochi Express",
                "Bangalore", "Kochi", "21:00", "08:00", 600, 100);

        addTrain("12612", "Pondicherry Express",
                "Chennai", "Pondicherry", "09:00", "12:00", 250, 80);

        addTrain("12613", "Salem Express",
                "Chennai", "Salem", "10:00", "16:00", 350, 100);

        addTrain("12614", "Tirunelveli Express",
                "Chennai", "Tirunelveli", "19:30", "07:30", 600, 120);

        addTrain("12615", "Trichy Express",
                "Chennai", "Trichy", "06:30", "12:00", 400, 100);

        addTrain("12616", "Erode Express",
                "Coimbatore", "Chennai", "15:00", "22:00", 500, 100);

        addTrain("12617", "Bangalore Express",
                "Coimbatore", "Bangalore", "08:00", "14:00", 450, 100);

        addTrain("12618", "Madurai Express",
                "Nagercoil", "Chennai", "18:00", "06:00", 650, 120);

        addTrain("12619", "Trivandrum Express",
                "Nagercoil", "Thiruvananthapuram", "09:00", "12:00", 250, 80);

        addTrain("12620", "Mumbai Express",
                "Bangalore", "Mumbai", "20:00", "12:00", 900, 120);

        addTrain("12621", "Hyderabad Express",
                "Bangalore", "Hyderabad", "18:00", "07:00", 700, 100);

        System.out.println("Train data loading completed!");
    }

    private void addTrain(
            String trainNumber,
            String trainName,
            String source,
            String destination,
            String departureTime,
            String arrivalTime,
            double fare,
            int availableSeats) {

        if (trainRepository.findByTrainNumber(trainNumber).isPresent()) {
            System.out.println("Already exists: " + trainNumber);
            return;
        }

        Train train = new Train();

        train.setTrainNumber(trainNumber);
        train.setTrainName(trainName);
        train.setSource(source);
        train.setDestination(destination);
        train.setDepartureTime(departureTime);
        train.setArrivalTime(arrivalTime);
        train.setFare(fare);
        train.setAvailableSeats(availableSeats);

        trainRepository.save(train);

        System.out.println("Added: " + trainName);
    }
}