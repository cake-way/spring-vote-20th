package com.example.vote.service;

import com.example.vote.domain.Candidate;
import com.example.vote.domain.Member;
import com.example.vote.dto.CandidateResponseDto;
import com.example.vote.repository.CandidateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CandidateService {
    private final CandidateRepository candidateRepository;

    public List<CandidateResponseDto> getLeader(Member.Part part) {
        List<Candidate> candidates = candidateRepository.findByPart(part);

        List<CandidateResponseDto> candidateResponseDtos = new ArrayList<>();

        for (Candidate candidate : candidates) {
            CandidateResponseDto candidateResponseDto = CandidateResponseDto.builder()
                    .name(candidate.getName())
                    .id(candidate.getId())
                    .build();

            candidateResponseDtos.add(candidateResponseDto);
        }
    }
}
