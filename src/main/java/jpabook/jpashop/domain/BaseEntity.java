package jpabook.jpashop.domain;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@Data
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @LastModifiedBy
    private String auditId;

    @CreatedBy
    private String rgstId;

    @LastModifiedDate
    private LocalDateTime auditDtm;

    @CreatedDate
    private LocalDateTime rgstDtm;
}
