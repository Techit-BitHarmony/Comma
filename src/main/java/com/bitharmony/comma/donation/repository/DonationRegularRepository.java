package com.bitharmony.comma.donation.repository;

import com.bitharmony.comma.donation.entity.Donation;
import com.bitharmony.comma.donation.entity.DonationRegular;
import org.quartz.JobKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DonationRegularRepository extends JpaRepository<DonationRegular, Long> {

    List<DonationRegular> findAllByPatronName(String patronUsername);

    Optional<DonationRegular> findByJobKey(JobKey jobKey);
}
