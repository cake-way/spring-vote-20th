package com.example.vote.repository;

import com.example.vote.domain.Candidate;
import com.example.vote.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {
}
