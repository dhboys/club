package org.dongho.club.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;
// Entity의 슈퍼클래스가 되기 위함을 표시
@MappedSuperclass
// 자동 감시를 위한 어노테이션
@EntityListeners(value = {AuditingEntityListener.class})
@Getter
// 수정시간을 위한 상속 클래스
abstract class BaseEntity {
    // Entity가 등록되는 시간
    @CreatedDate
    // 수정 불가하게 설정
    @Column(name = "regdate" , updatable = false)
    private LocalDateTime regDate;
    // 마지막으로 수정되는 시간
    @LastModifiedDate
    @Column(name = "moddate")
    private LocalDateTime modDate;

}
