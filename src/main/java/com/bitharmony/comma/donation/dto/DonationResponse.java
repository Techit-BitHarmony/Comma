package com.bitharmony.comma.donation.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DonationResponse<T> {

    private String msg;
    private T data;

    public static <T> DonationResponse<T> of(String msg) {
        DonationResponse<T> donationResponse = new DonationResponse<T>();

        donationResponse.setMsg(msg);

        return donationResponse;
    }

    public static <T> DonationResponse<T> of(String msg, T data) {
        DonationResponse<T> donationResponse = new DonationResponse<T>();

        donationResponse.setMsg(msg);
        donationResponse.setData(data);

        return donationResponse;
    }
}
