package org.dongho.club.repository;

import org.dongho.club.entity.Note;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface NoteRepository extends JpaRepository<Note, Long> {

    // 하나의 노트가져오기 , writer가 LAZY이므로 ENTITY 그래프로 EAGER로 바꾼다
    @EntityGraph(attributePaths = "writer"  , type = EntityGraph.EntityGraphType.FETCH)
    @Query("select n from Note n where n.num = :num")
    Optional<Note> getWithWriter(@Param("num") Long num);

    // email을 주면 그 작성자가 쓴 모든 note 가져오기 , writer는 EAGER 로딩으로 처리
    @EntityGraph(attributePaths = "writer"  , type = EntityGraph.EntityGraphType.FETCH)
    @Query("select n from Note n where n.writer.email = :email")
    List<Note> getList(@Param("email") String email);
}
