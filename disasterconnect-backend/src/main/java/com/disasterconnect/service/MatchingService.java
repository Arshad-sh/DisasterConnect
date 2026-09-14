package com.disasterconnect.service;

import com.disasterconnect.enums.Urgency;
import com.disasterconnect.model.MatchingCandidate;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class MatchingService {

    public double calculateUrgencyScore(Urgency urgency) {

        if (urgency == null) {
            return 0;
        }

        return switch (urgency) {
            case LOW -> 25;
            case MEDIUM -> 50;
            case HIGH -> 80;
            case CRITICAL -> 100;
        };
    }

    public double calculateDistanceScore(double distanceInKm) {

        if (distanceInKm < 0) {
            return 0;
        }

        if (distanceInKm <= 2) {
            return 100;
        }

        if (distanceInKm <= 5) {
            return 80;
        }

        if (distanceInKm <= 10) {
            return 60;
        }

        if (distanceInKm <= 20) {
            return 40;
        }

        if (distanceInKm <= 50) {
            return 20;
        }

        return 0;
    }

    public double calculateAvailabilityScore(boolean available) {

        return available ? 100 : 0;
    }

    // Day 32.2 - Handle unavailable candidates
    public boolean isCandidateAvailable(boolean available) {

        return available;
    }

    // Day 31.6 - Calculate capacity score
    public double calculateCapacityScore(
            double availableCapacity,
            double requiredCapacity) {

        if (availableCapacity < 0 || requiredCapacity <= 0) {
            return 0;
        }

        double capacityRatio =
                availableCapacity / requiredCapacity;

        if (capacityRatio >= 1.0) {
            return 100;
        }

        if (capacityRatio >= 0.75) {
            return 75;
        }

        if (capacityRatio >= 0.50) {
            return 50;
        }

        if (capacityRatio >= 0.25) {
            return 25;
        }

        return 0;
    }

    // Day 32.3 - Handle insufficient capacity
    public boolean hasSufficientCapacity(
            double availableCapacity,
            double requiredCapacity) {

        if (availableCapacity < 0 || requiredCapacity <= 0) {
            return false;
        }

        return availableCapacity >= requiredCapacity;
    }

    public double calculateReliabilityScore(double reliability) {

        if (reliability < 0) {
            return 0;
        }

        if (reliability > 100) {
            return 100;
        }

        return reliability;
    }

    public double calculateFinalScore(
            double urgencyScore,
            double distanceScore,
            double availabilityScore,
            double capacityScore,
            double reliabilityScore) {

        return (urgencyScore * 0.35)
                + (distanceScore * 0.25)
                + (availabilityScore * 0.20)
                + (capacityScore * 0.15)
                + (reliabilityScore * 0.05);
    }

    // Day 32.8 - Rank candidates with tie-breakers
    public List<MatchingCandidate> rankCandidates(
            List<MatchingCandidate> candidates) {

        if (candidates == null || candidates.isEmpty()) {
            return List.of();
        }

        return candidates.stream()
                .sorted(
                        Comparator
                                .comparingDouble(
                                        MatchingCandidate::getFinalScore
                                )
                                .reversed()

                                // Tie-breaker 1:
                                // Higher availability wins
                                .thenComparing(
                                        Comparator.comparingDouble(
                                                MatchingCandidate::getAvailability
                                        ).reversed()
                                )

                                // Tie-breaker 2:
                                // Shorter distance wins
                                .thenComparingDouble(
                                        MatchingCandidate::getDistance
                                )

                                // Tie-breaker 3:
                                // Higher reliability wins
                                .thenComparing(
                                        Comparator.comparingDouble(
                                                MatchingCandidate::getReliability
                                        ).reversed()
                                )
                )
                .toList();
    }

    // Day 32.9 - Filter invalid candidates
    public List<MatchingCandidate> filterEligibleCandidates(
            List<MatchingCandidate> candidates) {

        if (candidates == null || candidates.isEmpty()) {
            return List.of();
        }

        return candidates.stream()
                .filter(candidate -> candidate != null)
                .filter(candidate -> candidate.getAvailability() > 0)
                .filter(candidate -> candidate.getFinalScore() > 0)
                .toList();
    }

    public Optional<MatchingCandidate> selectBestMatch(
            List<MatchingCandidate> candidates) {

        if (candidates == null || candidates.isEmpty()) {
            return Optional.empty();
        }

        return candidates.stream()
                .max(
                        Comparator
                                .comparingDouble(
                                        MatchingCandidate::getFinalScore
                                )
                                .thenComparingDouble(
                                        MatchingCandidate::getAvailability
                                )
                                .thenComparing(
                                        Comparator.comparingDouble(
                                                MatchingCandidate::getDistance
                                        ).reversed()
                                )
                                .thenComparingDouble(
                                        MatchingCandidate::getReliability
                                )
                );
    }

    public MatchingCandidate createCandidate(
            Long candidateId,
            String candidateType,
            double urgencyScore,
            double distanceScore,
            double availabilityScore,
            double capacityScore,
            double reliabilityScore) {

        double finalScore = calculateFinalScore(
                urgencyScore,
                distanceScore,
                availabilityScore,
                capacityScore,
                reliabilityScore
        );

        MatchingCandidate candidate =
                new MatchingCandidate(
                        candidateId,
                        candidateType,
                        finalScore
                );

        candidate.setAvailability(availabilityScore);
        candidate.setDistance(distanceScore);
        candidate.setReliability(reliabilityScore);

        return candidate;
    }
}