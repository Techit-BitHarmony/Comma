package com.bitharmony.comma.donation.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/donation")
public class DonationController {

    @GetMapping("/test")
    public void test(){

    }

}
