package com.bitharmony.comma.domain.credit.charge.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Charge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    추후 멤버 기능 구현시 추가
//    @ManyToOne
//    private Member charger;

    @NotNull
    private long chargeAmount;

    private LocalDateTime createDate;

    private String paymentKey;

    private LocalDateTime payDate;

    public String getCode() {
        // yyyy-MM-dd 형식의 DateTimeFormatter 생성
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");

        // LocalDateTime 객체를 문자열로 변환
        return getCreateDate().format(formatter) + "__" + getId();
    }
}
