package com.disasterconnect.service;

import com.disasterconnect.enums.Urgency;
import com.disasterconnect.model.MatchingCandidate;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class MatchingServiceTest {

    private final MatchingService matchingService =
            new MatchingService();

    @Test
    void shouldCalculateUrgencyScore() {

        assertEquals(
                25,
                matchingService.calculateUrgencyScore(Urgency.LOW)
        );

        assertEquals(
                50,
                matchingService.calculateUrgencyScore(Urgency.MEDIUM)
        );

        assertEquals(
                80,
                matchingService.calculateUrgencyScore(Urgency.HIGH)
        );

        assertEquals(
                100,
                matchingService.calculateUrgencyScore(Urgency.CRITICAL)
        );

        assertEquals(
                0,
                matchingService.calculateUrgencyScore(null)
        );
    }

    @Test
    void shouldCalculateDistanceScore() {

        assertEquals(
                100,
                matchingService.calculateDistanceScore(2)
        );

        assertEquals(
                80,
                matchingService.calculateDistanceScore(5)
        );

        assertEquals(
                60,
                matchingService.calculateDistanceScore(10)
        );

        assertEquals(
                40,
                matchingService.calculateDistanceScore(20)
        );

        assertEquals(
                20,
                matchingService.calculateDistanceScore(50)
        );

        assertEquals(
                0,
                matchingService.calculateDistanceScore(51)
        );

        assertEquals(
                0,
                matchingService.calculateDistanceScore(-1)
        );
    }

    @Test
    void shouldCalculateAvailabilityScore() {

        assertEquals(
                100,
                matchingService.calculateAvailabilityScore(true)
        );

        assertEquals(
                0,
                matchingService.calculateAvailabilityScore(false)
        );
    }

    @Test
    void shouldCalculateCapacityScore() {

        assertEquals(
                100,
                matchingService.calculateCapacityScore(100, 100)
        );

        assertEquals(
                100,
                matchingService.calculateCapacityScore(150, 100)
        );

        assertEquals(
                75,
                matchingService.calculateCapacityScore(75, 100)
        );

        assertEquals(
                50,
                matchingService.calculateCapacityScore(50, 100)
        );

        assertEquals(
                25,
                matchingService.calculateCapacityScore(25, 100)
        );

        assertEquals(
                0,
                matchingService.calculateCapacityScore(10, 100)
        );

        assertEquals(
                0,
                matchingService.calculateCapacityScore(-10, 100)
        );

        assertEquals(
                0,
                matchingService.calculateCapacityScore(100, 0)
        );
    }

    @Test
    void shouldCalculateReliabilityScore() {

        assertEquals(
                80,
                matchingService.calculateReliabilityScore(80)
        );

        assertEquals(
                0,
                matchingService.calculateReliabilityScore(-10)
        );

        assertEquals(
                100,
                matchingService.calculateReliabilityScore(150)
        );
    }

    @Test
    void shouldCalculateFinalScore() {

        double score = matchingService.calculateFinalScore(
                100,
                80,
                100,
                100,
                80
        );

        assertEquals(
                94,
                score,
                0.001
        );
    }

    @Test
    void shouldRankCandidatesByFinalScore() {

        MatchingCandidate candidate1 =
                new MatchingCandidate(1L, "NGO", 70);

        MatchingCandidate candidate2 =
                new MatchingCandidate(2L, "NGO", 90);

        MatchingCandidate candidate3 =
                new MatchingCandidate(3L, "NGO", 80);

        List<MatchingCandidate> candidates =
                List.of(
                        candidate1,
                        candidate2,
                        candidate3
                );

        List<MatchingCandidate> ranked =
                matchingService.rankCandidates(candidates);

        assertEquals(3, ranked.size());

        assertEquals(
                2L,
                ranked.get(0).getCandidateId()
        );

        assertEquals(
                90,
                ranked.get(0).getFinalScore()
        );

        assertEquals(
                3L,
                ranked.get(1).getCandidateId()
        );

        assertEquals(
                80,
                ranked.get(1).getFinalScore()
        );

        assertEquals(
                1L,
                ranked.get(2).getCandidateId()
        );

        assertEquals(
                70,
                ranked.get(2).getFinalScore()
        );
    }

    // Day 33.9 - Test null candidate list for ranking
    @Test
    void shouldHandleNullCandidatesWhenRanking() {

        List<MatchingCandidate> rankedCandidates =
                matchingService.rankCandidates(null);

        assertTrue(
                rankedCandidates.isEmpty()
        );
    }

    // Day 33.9 - Test empty candidate list for ranking
    @Test
    void shouldHandleEmptyCandidatesWhenRanking() {

        List<MatchingCandidate> rankedCandidates =
                matchingService.rankCandidates(List.of());

        assertTrue(
                rankedCandidates.isEmpty()
        );
    }

    @Test
    void shouldSelectBestMatch() {

        MatchingCandidate candidate1 =
                new MatchingCandidate(1L, "NGO", 75);

        MatchingCandidate candidate2 =
                new MatchingCandidate(2L, "NGO", 94.5);

        MatchingCandidate candidate3 =
                new MatchingCandidate(3L, "NGO", 85);

        List<MatchingCandidate> candidates =
                List.of(
                        candidate1,
                        candidate2,
                        candidate3
                );

        Optional<MatchingCandidate> bestMatch =
                matchingService.selectBestMatch(candidates);

        assertTrue(bestMatch.isPresent());

        assertEquals(
                2L,
                bestMatch.get().getCandidateId()
        );

        assertEquals(
                94.5,
                bestMatch.get().getFinalScore()
        );
    }

    // Day 33.10 - Test best match using availability tie-breaker
    @Test
    void shouldSelectBestMatchUsingAvailabilityTieBreaker() {

        MatchingCandidate candidate1 =
                new MatchingCandidate(1L, "NGO", 90);

        candidate1.setAvailability(70);
        candidate1.setDistance(5);
        candidate1.setReliability(80);

        MatchingCandidate candidate2 =
                new MatchingCandidate(2L, "NGO", 90);

        candidate2.setAvailability(90);
        candidate2.setDistance(5);
        candidate2.setReliability(80);

        List<MatchingCandidate> candidates =
                List.of(
                        candidate1,
                        candidate2
                );

        Optional<MatchingCandidate> bestMatch =
                matchingService.selectBestMatch(candidates);

        assertTrue(bestMatch.isPresent());

        assertEquals(
                2L,
                bestMatch.get().getCandidateId()
        );
    }

    // Day 33.10 - Test best match using distance tie-breaker
    @Test
    void shouldSelectBestMatchUsingDistanceTieBreaker() {

        MatchingCandidate candidate1 =
                new MatchingCandidate(1L, "NGO", 90);

        candidate1.setAvailability(90);
        candidate1.setDistance(10);
        candidate1.setReliability(80);

        MatchingCandidate candidate2 =
                new MatchingCandidate(2L, "NGO", 90);

        candidate2.setAvailability(90);
        candidate2.setDistance(5);
        candidate2.setReliability(80);

        List<MatchingCandidate> candidates =
                List.of(
                        candidate1,
                        candidate2
                );

        Optional<MatchingCandidate> bestMatch =
                matchingService.selectBestMatch(candidates);

        assertTrue(bestMatch.isPresent());

        assertEquals(
                2L,
                bestMatch.get().getCandidateId()
        );
    }

    @Test
    void shouldReturnEmptyWhenNoCandidatesExist() {

        Optional<MatchingCandidate> bestMatch =
                matchingService.selectBestMatch(List.of());

        assertTrue(bestMatch.isEmpty());
    }

    @Test
    void shouldCreateCandidateWithCalculatedFinalScore() {

        MatchingCandidate candidate =
                matchingService.createCandidate(
                        10L,
                        "NGO",
                        100,
                        80,
                        100,
                        100,
                        80
                );

        assertNotNull(candidate);

        assertEquals(
                10L,
                candidate.getCandidateId()
        );

        assertEquals(
                "NGO",
                candidate.getCandidateType()
        );

        assertEquals(
                94,
                candidate.getFinalScore(),
                0.001
        );
    }

    // Day 33.2 - Test invalid candidate filtering
    @Test
    void shouldFilterInvalidCandidates() {

        MatchingCandidate validCandidate =
                new MatchingCandidate(1L, "NGO", 80);

        validCandidate.setAvailability(100);
        validCandidate.setDistance(5);
        validCandidate.setReliability(80);

        MatchingCandidate unavailableCandidate =
                new MatchingCandidate(2L, "NGO", 90);

        unavailableCandidate.setAvailability(0);
        unavailableCandidate.setDistance(2);
        unavailableCandidate.setReliability(90);

        MatchingCandidate zeroScoreCandidate =
                new MatchingCandidate(3L, "NGO", 0);

        zeroScoreCandidate.setAvailability(100);
        zeroScoreCandidate.setDistance(5);
        zeroScoreCandidate.setReliability(50);

        List<MatchingCandidate> candidates =
                List.of(
                        validCandidate,
                        unavailableCandidate,
                        zeroScoreCandidate
                );

        List<MatchingCandidate> eligibleCandidates =
                matchingService.filterEligibleCandidates(
                        candidates
                );

        assertEquals(
                1,
                eligibleCandidates.size()
        );

        assertEquals(
                1L,
                eligibleCandidates.get(0).getCandidateId()
        );
    }

    // Day 33.2 - Test null candidate list
    @Test
    void shouldHandleNullCandidatesWhenFiltering() {

        List<MatchingCandidate> eligibleCandidates =
                matchingService.filterEligibleCandidates(null);

        assertTrue(
                eligibleCandidates.isEmpty()
        );
    }

    // Day 33.2 - Test empty candidate list
    @Test
    void shouldHandleEmptyCandidatesWhenFiltering() {

        List<MatchingCandidate> eligibleCandidates =
                matchingService.filterEligibleCandidates(
                        List.of()
                );

        assertTrue(
                eligibleCandidates.isEmpty()
        );
    }
}