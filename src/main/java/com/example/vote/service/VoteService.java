package com.example.vote.service;

import com.example.vote.domain.Candidate;
import com.example.vote.domain.Member;
import com.example.vote.domain.Vote;
import com.example.vote.dto.VoteResponseDto;
import com.example.vote.repository.CandidateRepository;
import com.example.vote.repository.MemberRepository;
import com.example.vote.repository.VoteRepository;
import com.example.vote.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VoteService {
    private final MemberRepository memberRepository;
    private final CandidateRepository candidateRepository;
    private final VoteRepository voteRepository;

    public List<VoteResponseDto> getResult(Member.Part part) {
        List<Vote> votes = voteRepository.findByPart(part);

        List<VoteResponseDto> voteResponseDtos = new ArrayList<VoteResponseDto>();

        for (Vote vote : votes) {
            VoteResponseDto voteResponseDto = VoteResponseDto.builder()
                    .voteCount(vote.getCandidate().getVoteCount())
                    .candidateName(vote.getCandidate().getName())
                    .build();

            voteResponseDtos.add(voteResponseDto);
        }

        voteResponseDtos = voteResponseDtos.stream().sorted(Comparator.comparing(VoteResponseDto::getVoteCount)).collect(Collectors.toList());

        return voteResponseDtos;


//        List<Posts> all = postsRepository.findByTitle(title); // 1
//
//        List<PostsResponseDto> postsResponseDtoList = new ArrayList<>(); // 2
//
//        for(Posts posts : all){ // 3
//            PostsResponseDto dto = PostsResponseDto.builder()
//                    .id(posts.getId())
//                    .title(posts.getTitle())
//                    .author(posts.getAuthor())
//                    .content(posts.getContent())
//                    .build();
//
//            postsResponseDtoList.add(dto); // 4
//        }
//
//        return postsResponseDtoList;

    }

    public List<VoteResponseDto> vote(CustomUserDetails userDetails, Long id){
        final Member member = memberRepository.findByUsername(userDetails.getUsername()).orElseThrow(
                () -> new IllegalArgumentException("Member not found"));

        final Candidate candidate = candidateRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Candidate not found"));

        // 투표 저장
        final Vote vote = Vote.builder()
                .member(member)
                .candidate(candidate).
                build();
        voteRepository.save(vote);

        // 후보자 득표수 증가
        candidate.increaseVoteCount();
        candidateRepository.save(candidate);

        return getResult(member.getPart());
    }
}
