package com.bitharmony.comma.donation.repository;

import com.bitharmony.comma.donation.entity.Donation;
import com.bitharmony.comma.member.entity.Member;
import org.aspectj.apache.bcel.classfile.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DonationRespository extends JpaRepository<Donation,Long> {

    Optional<Donation> findById(Long id);

    Optional<Donation> findByPatron(Member member);

    Optional<Donation> findByArtistId(Long artistId);
}
