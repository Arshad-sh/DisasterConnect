package com.disasterconnect.model;

public class MatchingCandidate {

    private Long candidateId;
    private String candidateType;
    private double finalScore;

    // Day 32.8 - Tie-breaker fields
    private double availability;
    private double distance;
    private double reliability;

    public MatchingCandidate() {
    }

    public MatchingCandidate(
            Long candidateId,
            String candidateType,
            double finalScore) {

        this.candidateId = candidateId;
        this.candidateType = candidateType;
        this.finalScore = finalScore;
    }

    public Long getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(Long candidateId) {
        this.candidateId = candidateId;
    }

    public String getCandidateType() {
        return candidateType;
    }

    public void setCandidateType(String candidateType) {
        this.candidateType = candidateType;
    }

    public double getFinalScore() {
        return finalScore;
    }

    public void setFinalScore(double finalScore) {
        this.finalScore = finalScore;
    }

    public double getAvailability() {
        return availability;
    }

    public void setAvailability(double availability) {
        this.availability = availability;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getReliability() {
        return reliability;
    }

    public void setReliability(double reliability) {
        this.reliability = reliability;
    }
}