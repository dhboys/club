package org.dongho.club.repository;

import org.dongho.club.entity.ClubMember;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface ClubMemberRepository extends JpaRepository<ClubMember, String> {
    // entityGraph는 lazy를 eager로 바꾼다
    @EntityGraph(attributePaths = "roleSet" , type = EntityGraph.EntityGraphType.FETCH)
    @Query("select m from ClubMember m where m.fromSocial = :social and m.email = :email")
    Optional<ClubMember> findByEmail(String email , boolean social);
}
