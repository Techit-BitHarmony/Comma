package com.bitharmony.comma.domain.credit.creditLog.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CreditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    추후 멤버 기능 통합시 연동
//    @ManyToOne
//    private Member member;

    @Enumerated(EnumType.STRING)
    private EventType eventType;

    @NotNull
    private long creditChangeAmount;

//    추후 멤버 기능 통합시 멤버 크레딧 값과 연동
//    private long restCredit;

    private LocalDateTime createDate;

    @NotNull
    public enum EventType {
        충전__토스페이먼츠,
        정산__음원판매,
        정산__아티스트후원,
        사용__음원구매,
        사용__아티스트후원,
        출금신청__통장입금,
        출금신청취소
    }
}
