package com.example.vote.service;

import com.example.vote.domain.Member;
import com.example.vote.dto.LoginRequestDTO;
import com.example.vote.dto.LoginResponseDTO;
import com.example.vote.repository.MemberRepository;
import com.example.vote.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider; // JWT 생성 로직을 위한 클래스 (예제)

    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
        Member member = memberRepository.findByUsername(loginRequestDTO.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));

        if (!passwordEncoder.matches(loginRequestDTO.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("Invalid username or password");
        }

        String token = jwtTokenProvider.createToken(member.getUsername()); // JWT 생성

        return new LoginResponseDTO(member.getUsername(), token);
    }
}
