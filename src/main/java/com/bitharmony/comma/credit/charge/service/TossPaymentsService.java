package com.bitharmony.comma.credit.charge.service;

import com.bitharmony.comma.credit.charge.dto.ChargeConfirmResponse;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
@Transactional
public class TossPaymentsService {

    @Value("${custom.tossPayments.widget.secretKey}")
    private String tossPaymentsWidgetSecretKey;
    @Value("${custom.tossPayments.confirm.url}")
    private String TOSS_CONFIRM_URL;

    public ChargeConfirmResponse requestApprovalAndGetResponse(String orderId, String amount, String paymentKey) throws Exception {

        JSONObject paymentInfo = createPaymentInfo(orderId, amount, paymentKey);
        HttpURLConnection connection = makeAndSetConnetion();

        sendPaymentInfo(connection, paymentInfo);

        return getPaymentResult(connection);
    }

    public HttpURLConnection makeAndSetConnetion() throws Exception{
        URL url = new URL(TOSS_CONFIRM_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        setRequestHeaders(connection);

        return connection;
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

    private ChargeConfirmResponse getPaymentResult(HttpURLConnection connection) throws Exception {
        int code = connection.getResponseCode();
        boolean isSuccess = code == 200;

        InputStream responseStream = isSuccess ? connection.getInputStream() : connection.getErrorStream();

        JSONParser parser = new JSONParser();

        try (Reader reader = new InputStreamReader(responseStream, StandardCharsets.UTF_8)) {
            JSONObject paymentStatement = (JSONObject) parser.parse(reader);

            return ChargeConfirmResponse.builder()
                    .paymentStatement(paymentStatement)
                    .isApproved(isSuccess)
                    .build();
        }
    }
}
