package com.ferb.expenseMoneyTracker.entity;

import com.ferb.expenseMoneyTracker.abstracts.Auditable;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "used_refresh_tokens")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsedRefreshToken extends Auditable {
    @Id
    private UUID id;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date expiredAt;
}
