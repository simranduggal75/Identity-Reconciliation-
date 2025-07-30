package com.bitespeed.identity.controller;

import com.bitespeed.identity.service.IdentifyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/identify")
@RequiredArgsConstructor
public class IdentifyController {
    private final IdentifyService service;

    @PostMapping
    public ResponseEntity<Map<String, Object>> identify(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String phoneNumber = body.get("phoneNumber");
        if (email == null && phoneNumber == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "email or phoneNumber is required"));
        }
        return ResponseEntity.ok(service.identify(email, phoneNumber));
    }
}