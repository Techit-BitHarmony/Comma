package com.bitharmony.comma.domain.credit.charge.controller;

import com.bitharmony.comma.domain.credit.charge.dto.ChargeCreateRequest;
import com.bitharmony.comma.domain.credit.charge.dto.ChargeCreateResponse;
import com.bitharmony.comma.domain.credit.charge.dto.ChargeGetListResponse;
import com.bitharmony.comma.domain.credit.charge.dto.ChargeGetResponse;
import com.bitharmony.comma.domain.credit.charge.entity.Charge;
import com.bitharmony.comma.domain.credit.charge.service.ChargeService;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/credit")
public class ChargeController {

    private final ChargeService chargeService;

    @Value("${custom.tossPayments.widget.secretKey}")
    private String tossPaymentsWidgetSecretKey;

    @GetMapping("/charges/{id}")
    public ChargeGetResponse getCharge(@PathVariable long id) {
            Charge charge = this.chargeService.getChargeById(id);

        return ChargeGetResponse.builder()
                .chargeAmount(charge.getChargeAmount())
                .createDate(charge.getCreateDate())
                .payDate(charge.getPayDate())
                .paymentKey(charge.getPaymentKey())
                .build();
    }

    @GetMapping("/charges")
    public ResponseEntity<ChargeGetListResponse> getChargeList() {
        List<Charge> charges = this.chargeService.getChargeList();
        ChargeGetListResponse chargeGetListResponse = ChargeGetListResponse.toDtoList(charges);

        return new ResponseEntity<>(chargeGetListResponse, HttpStatus.OK); 
    }


    // POST 발송을 위해 임의로 생성한 템플릿
    // 추후 프론트 구현시 삭제 예정
    // 금액(chargeAmount) 입력 후 '/charges'로 POST 발송
    @GetMapping("/charge_form")
    public String charge(){
        return "domain/credit/charge/charge_form";
    }


    // Charge 객체 생성 & 저장 후 해당 객체를 Response에 실어 보냄
    // Response의 chargeId 값으로 "/charge/pay/{id}"로 리다이렉트하여 결제 진행
    @PostMapping("/charges")
    public ResponseEntity<ChargeCreateResponse> createCharge(
            @RequestBody ChargeCreateRequest chargeCreateRequest){

    Charge charge = this.chargeService.createCharge(chargeCreateRequest.chargeAmount());
    ChargeCreateResponse chargeCreateResponse = new ChargeCreateResponse(charge);

    return new ResponseEntity<>(chargeCreateResponse, HttpStatus.CREATED);
    }

    @GetMapping("/charges/pay/{id}")
    public String payCharge(@PathVariable long id, Model model){
        Charge charge = this.chargeService.getChargeById(id);
        model.addAttribute("charge", charge);

        return "/domain/credit/charge/charge";
    }

    @GetMapping("/success")
    public String showSuccess() {
        return "domain/credit/charge/success";
    }

    @GetMapping("/fail")
    public String showFail(Model model, String failCode, String failMessage) {
        model.addAttribute("code", failCode);
        model.addAttribute("message", failMessage);

        return "domain/credit/charge/fail";
    }

    @PostMapping(value = "/confirm")
    public ResponseEntity<JSONObject> confirmPayment(@RequestBody String jsonBody) throws Exception {

        JSONParser parser = new JSONParser();
        String orderId;
        String amount;
        String paymentKey;
        try {
            // 클라이언트에서 받은 JSON 요청 바디입니다.
            JSONObject requestData = (JSONObject) parser.parse(jsonBody);
            paymentKey = (String) requestData.get("paymentKey");
            orderId = (String) requestData.get("orderId");
            amount = (String) requestData.get("amount");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        };


        // orderId와 amount 맞는지 체크하는 로직
        chargeService.checkValidity(orderId, Long.parseLong(amount));


        JSONObject obj = new JSONObject();
        obj.put("orderId", orderId);
        obj.put("amount", amount);
        obj.put("paymentKey", paymentKey);


        // TODO: 개발자센터에 로그인해서 내 결제위젯 연동 키 > 시크릿 키를 입력하세요. 시크릿 키는 외부에 공개되면 안돼요.
        // @docs https://docs.tosspayments.com/reference/using-api/api-keys
        String widgetSecretKey = tossPaymentsWidgetSecretKey;

        // 토스페이먼츠 API는 시크릿 키를 사용자 ID로 사용하고, 비밀번호는 사용하지 않습니다.
        // 비밀번호가 없다는 것을 알리기 위해 시크릿 키 뒤에 콜론을 추가합니다.
        // @docs https://docs.tosspayments.com/reference/using-api/authorization#%EC%9D%B8%EC%A6%9D
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] encodedBytes = encoder.encode((widgetSecretKey + ":").getBytes("UTF-8"));
        String authorizations = "Basic " + new String(encodedBytes, 0, encodedBytes.length);

        // 결제 승인 API를 호출하세요.
        // 결제를 승인하면 결제수단에서 금액이 차감돼요.
        // @docs https://docs.tosspayments.com/guides/payment-widget/integration#3-결제-승인하기
        URL url = new URL("https://api.tosspayments.com/v1/payments/confirm");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Authorization", authorizations);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);


        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(obj.toString().getBytes("UTF-8"));

        int code = connection.getResponseCode();
        boolean isSuccess = code == 200 ? true : false;

        InputStream responseStream = isSuccess ? connection.getInputStream() : connection.getErrorStream();

        // TODO: 결제 성공 및 실패 비즈니스 로직을 구현하세요.
        if (isSuccess) {
            chargeService.addCredit(orderId, Long.parseLong(amount), paymentKey);
        } else {
            throw new RuntimeException("결제 승인 실패");
        }


        Reader reader = new InputStreamReader(responseStream, StandardCharsets.UTF_8);
        JSONObject jsonObject = (JSONObject) parser.parse(reader);
        responseStream.close();

        return ResponseEntity.status(code).body(jsonObject);
    }

}
