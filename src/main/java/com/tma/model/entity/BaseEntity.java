package com.tma.model.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

@Data
@MappedSuperclass
public abstract class BaseEntity implements Serializable {

    @CreationTimestamp
    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    protected Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    protected Date updatedAt;

    @Column(name = "deleted", columnDefinition="boolean default false")
    protected boolean deleted;

    @PrePersist
    public void onInsert() {
        createdAt = Timestamp.from(ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")).toInstant());
    }

    @PreUpdate
    public void onUpdate() {
        updatedAt = Timestamp.from(ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")).toInstant());
    }

}
