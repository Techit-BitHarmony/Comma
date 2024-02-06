package com.bitharmony.comma.credit.creditLog.entity;

import com.bitharmony.comma.member.entity.Member;
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

    @ManyToOne
    private Member member;

    @Enumerated(EnumType.STRING)
    private EventType eventType;

    @NotNull
    private long creditChangeAmount;

    private long restCredit;

    private LocalDateTime createDate;

    @NotNull
    public enum EventType {
        충전__토스페이먼츠,
        정산__음원판매,
        정산__아티스트후원,
        사용__음원구매,
        사용__아티스트후원,
        출금신청__통장입금,
        출금신청__수정,
        출금신청__취소
    }
}
