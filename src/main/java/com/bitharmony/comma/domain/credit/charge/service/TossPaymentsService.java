package com.bitharmony.comma.domain.credit.charge.service;

import com.bitharmony.comma.domain.credit.charge.dto.ChargeConfirmResponse;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class TossPaymentsService {

    @Value("${custom.tossPayments.widget.secretKey}")
    private String tossPaymentsWidgetSecretKey;
    @Value("${custom.tossPayments.confirm.url}")
    private String TOSS_CONFIRM_URL;
    private JSONObject paymentStatement;

    public ChargeConfirmResponse requestApprovalAndGetResponse(String orderId, String amount, String paymentKey) throws Exception {

        JSONObject paymentInfo = createPaymentInfo(orderId, amount, paymentKey);

        URL url = new URL(TOSS_CONFIRM_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        setRequestHeaders(connection);
        sendPaymentInfo(connection, paymentInfo);

        boolean isSuccess = getPaymentResult(connection);


        return ChargeConfirmResponse.builder()
                .paymentStatement(paymentStatement)
                .isApproved(isSuccess)
                .build();
    }


    private JSONObject createPaymentInfo(String orderId, String amount, String paymentKey){
        JSONObject paymentInfo = new JSONObject();
        paymentInfo.put("orderId", orderId);
        paymentInfo.put("amount", amount);
        paymentInfo.put("paymentKey", paymentKey);

        return paymentInfo;
    }

    private void setRequestHeaders(HttpURLConnection connection) throws Exception {
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] encodedBytes = encoder.encode((tossPaymentsWidgetSecretKey + ":").getBytes("UTF-8"));
        String authorizations = "Basic " + new String(encodedBytes, 0, encodedBytes.length);

        connection.setRequestProperty("Authorization", authorizations);
        connection.setRequestProperty("Content-Type", "application/json");
    }

    private void sendPaymentInfo(HttpURLConnection connection, JSONObject paymentInfo) throws IOException {
        try (OutputStream outputStream = connection.getOutputStream()) {
            outputStream.write(paymentInfo.toString().getBytes("UTF-8"));
        }
    }

    private boolean getPaymentResult(HttpURLConnection connection) throws Exception {
        int code = connection.getResponseCode();
        boolean isSuccess = code == 200;

        InputStream responseStream = isSuccess ? connection.getInputStream() : connection.getErrorStream();

        JSONParser parser = new JSONParser();

        try (Reader reader = new InputStreamReader(responseStream, StandardCharsets.UTF_8)) {
            paymentStatement = (JSONObject) parser.parse(reader);
        }

        return isSuccess;
    }

}
