package com.bitharmony.comma.domain.credit.charge.controller;

import com.bitharmony.comma.domain.credit.charge.dto.ChargeCreateRequest;
import com.bitharmony.comma.domain.credit.charge.dto.ChargeCreateResponse;
import com.bitharmony.comma.domain.credit.charge.dto.ChargeGetResponse;
import com.bitharmony.comma.domain.credit.charge.entity.Charge;
import com.bitharmony.comma.domain.credit.charge.service.ChargeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/credit")
public class ChargeController {

    private final ChargeService chargeService;

    @GetMapping("/charges/{id}")
    public ResponseEntity<ChargeGetResponse> getCharge(@PathVariable long id) {
        try {
            Charge charge = this.chargeService.getChargeById(id);
            ChargeGetResponse chargeGetResponse = new ChargeGetResponse(charge);

            return new ResponseEntity<>(chargeGetResponse, HttpStatus.OK);
        }  catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/charges")
    public ResponseEntity<ChargeCreateResponse> createCharge(
            @RequestBody ChargeCreateRequest chargeCreateRequest){

        Charge charge = this.chargeService.createCharge(chargeCreateRequest.chargeAmount());
        ChargeCreateResponse chargeCreateResponse = new ChargeCreateResponse(charge);

        return new ResponseEntity<>(chargeCreateResponse, HttpStatus.CREATED);
    }


}
