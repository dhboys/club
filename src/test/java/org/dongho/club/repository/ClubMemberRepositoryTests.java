package org.dongho.club.repository;

import lombok.extern.log4j.Log4j2;
import org.dongho.club.entity.ClubMember;
import org.dongho.club.entity.ClubMemberRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class ClubMemberRepositoryTests {
    @Autowired
    private ClubMemberRepository memberRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    // 100개의 계정 넣기
    @Test
    public void testInsertMembers(){
        IntStream.rangeClosed(1 , 100).forEach(i -> {
            ClubMember clubMember = ClubMember.builder()
                    .email("user" + i + "@google.com")
                    .name("사용자" + i)
                    .password(passwordEncoder.encode("1111"))
                    .fromSocial(false)
                    .build();
            // 권한 주기 -> USER 권한은 다 주기
            clubMember.addMemberRole(ClubMemberRole.USER);
            // i 가 80 넘으면 MEMBER 권한
            if( i >= 80){
                clubMember.addMemberRole(ClubMemberRole.MEMBER);
            }
            // i 가 90 넘으면 ADMIN 권한
            if( i>= 90){
                clubMember.addMemberRole(ClubMemberRole.ADMIN);
            }
            memberRepository.save(clubMember);
        });
    }
    // clubMember 가져오기
    @Test
    public void testRead(){
        Optional<ClubMember> result = memberRepository.findByEmail("user99@google.com" , false);

        ClubMember clubMember = result.get();

        log.info(clubMember);
        log.info(clubMember.getRoleSet());
    }
}
