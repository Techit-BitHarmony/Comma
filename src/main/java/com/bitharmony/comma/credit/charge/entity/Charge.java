package com.bitharmony.comma.credit.charge.entity;

import com.bitharmony.comma.member.entity.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;

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

    @ManyToOne
    private Member charger;

    @NotNull
    private long chargeAmount;

    @Builder.Default
    private LocalDateTime createDate = LocalDateTime.now();

    private String paymentKey;

    private LocalDateTime payDate;

    // 토스페이먼츠 결제 요청시 orderId 생성을 위한 코드
    // 생성일자__ChargeId 형태로 생성 (예시)202401311557__11
    public String getCode() {
        // yyyy-MM-dd 형식의 DateTimeFormatter 생성
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
        // LocalDateTime 객체를 문자열로 변환
        return getCreateDate().format(formatter) + "__" + getId();
    }
}
