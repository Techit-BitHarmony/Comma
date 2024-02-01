package com.bitharmony.comma.domain.credit.charge.controller;

import com.bitharmony.comma.domain.credit.charge.dto.*;
import com.bitharmony.comma.domain.credit.charge.entity.Charge;
import com.bitharmony.comma.domain.credit.charge.service.ChargeService;
import com.bitharmony.comma.member.entity.Member;
import com.bitharmony.comma.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/credit")
public class ChargeController {

    private final ChargeService chargeService;
    private final MemberService memberService;

    @GetMapping("/charges/{id}")
    public ResponseEntity<ChargeGetResponse> getCharge(@PathVariable long id) {

        Charge charge = chargeService.getChargeById(id);

        return new ResponseEntity<>(
                ChargeGetResponse.builder()
                        .username(charge.getCharger().getUsername())
                        .chargeAmount(charge.getChargeAmount())
                        .createDate(charge.getCreateDate())
                        .payDate(charge.getPayDate())
                        .paymentKey(charge.getPaymentKey())
                        .build(),
                HttpStatus.OK);
    }

    @GetMapping("/charges/mine")
    public ResponseEntity<ChargeGetListResponse> getMyChargeList() {

        Member member = memberService.getMemberByUsername("user1");
        List<Charge> charges = chargeService.getChargeListByMemberId(member.getId());

        ChargeGetListResponse chargeGetListResponse = ChargeGetListResponse.builder()
                .chargeDtos(charges.stream().map(ChargeDto::new).toList())
                .build();

        return new ResponseEntity<>(chargeGetListResponse, HttpStatus.OK);
    }

    @GetMapping("/charges")
    public ResponseEntity<ChargeGetListResponse> getAllChargeList() {

        List<Charge> charges = chargeService.getChargeList();

        ChargeGetListResponse chargeGetListResponse = ChargeGetListResponse.builder()
                .chargeDtos(charges.stream().map(ChargeDto::new).toList())
                .build();

        return new ResponseEntity<>(chargeGetListResponse, HttpStatus.OK);
    }

    // Charge 객체 생성 & 저장 후 해당 객체를 Response에 실어 보냄
    // Response의 chargeId 값으로 "/charge/pay/{id}"로 리다이렉트하여 결제 진행
    @PostMapping("/charges")
    public ResponseEntity<ChargeCreateResponse> createCharge(
            @RequestBody ChargeCreateRequest chargeCreateRequest) {

        // TODO : 멤버 가져오는 메서드 추가시 수정 (임시로 user1 사용)
        Member member = memberService.getMemberByUsername("user1");
        Charge charge = chargeService.createCharge(member, chargeCreateRequest.chargeAmount());

        return new ResponseEntity<>(
                ChargeCreateResponse.builder()
                        .chargeId(charge.getId())
                        .build(),
                HttpStatus.CREATED
        );
    }

    // POST 발송을 위해 임의로 생성한 템플릿
    // 추후 프론트 구현시 삭제 예정
    // 금액(chargeAmount) 입력 후 '/charges'로 POST 발송
    @GetMapping("/charge_form")
    public String charge() {
        return "domain/credit/charge/charge_form";
    }

    @GetMapping("/charges/pay/{id}")
    public String payCharge(@PathVariable long id, Model model) {
        Charge charge = chargeService.getChargeById(id);
        model.addAttribute("charge", charge);

        return "/domain/credit/charge/charge";
    }

    @PostMapping(value = "/confirm")
    public ResponseEntity<ChargeConfirmResponse> confirmPayment(@RequestBody ChargeConfirmRequest chargeConfirmRequest) {

        String orderId = chargeConfirmRequest.orderId();
        String amount = chargeConfirmRequest.amount();
        String paymentKey = chargeConfirmRequest.paymentKey();

        ChargeConfirmResponse chargeConfirmResponse =
                chargeService.confirmPayment(orderId, amount, paymentKey);

        return new ResponseEntity<>(chargeConfirmResponse, HttpStatus.OK);
    }

    @GetMapping("/success")
    public String showSuccess() {
        return "domain/credit/charge/success";
    }

    @GetMapping("/fail")
    public String showFail(Model model, @RequestParam String code, @RequestParam String message) {
        model.addAttribute("code", code);
        model.addAttribute("message", message);

        return "domain/credit/charge/fail";
    }
}
