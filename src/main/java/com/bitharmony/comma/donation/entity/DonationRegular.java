package com.bitharmony.comma.donation.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.quartz.JobKey;

import java.io.Serializable;

@Entity
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class DonationRegular implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @NotNull
    String patronName;

    @NotNull
    String artistName;

    @NotNull
    Long amount;

    @NotNull
    Integer executeDay;

    @NotNull
    JobKey jobKey;

    @Builder.Default
    boolean anonymous = false;
}
