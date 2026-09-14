package com.disasterconnect.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class RbacTestController {

    // =========================================================
    // ADMIN TEST
    // =========================================================

    @GetMapping("/api/admin/test")
    public String adminTest() {
        return "ADMIN access granted";
    }

    // =========================================================
    // CITIZEN TEST
    // =========================================================

    @GetMapping("/api/citizen/test")
    public String citizenTest() {
        return "CITIZEN access granted";
    }

    // =========================================================
    // NGO TEST
    // =========================================================

    @GetMapping("/api/ngo/test")
    public String ngoTest() {
        return "NGO access granted";
    }

    // =========================================================
    // VOLUNTEER TEST
    // =========================================================

    @GetMapping("/api/volunteer/test")
    public String volunteerTest() {
        return "VOLUNTEER access granted";
    }
}