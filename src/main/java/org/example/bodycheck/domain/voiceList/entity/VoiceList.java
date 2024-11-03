package org.example.bodycheck.domain.voiceList.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.bodycheck.common.util.StringUtil;
import org.example.bodycheck.domain.enums.VoiceCode;

@Entity
@Getter
@Builder
@Table(name = "voiceList")
@AllArgsConstructor
@NoArgsConstructor
public class VoiceList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "voice_code")
    @Enumerated(value = EnumType.STRING)
    private VoiceCode voiceCode;

}
