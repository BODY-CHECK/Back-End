package org.example.bodycheck.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.bodycheck.domain.common.BaseEntity;
import org.example.bodycheck.domain.mapping.MemberAgree;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Terms extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String title;

    private String body;

    private Boolean optional;

    @OneToMany(mappedBy = "terms", cascade = CascadeType.ALL)
    private List<MemberAgree> memberAgreeList = new ArrayList<>();
}
