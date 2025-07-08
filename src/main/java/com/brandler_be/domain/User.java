package com.brandler_be.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 사용자 엔티티
 * 사용자의 기본 정보와 브랜드 스크랩, 방문 기록 관계를 관리합니다.
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Schema(description = "사용자 정보")
public class User {
    @Id
    @Schema(description = "사용자 이메일")
    private String email;

    @Schema(description = "닉네임")
    private String name;

    @Schema(description = "선호하는 패션 장르")
    private String genre;


    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Visit> visits = new ArrayList<>();



    /**
     * 사용자 정보 업데이트
     */
    public void updateUserInfo(String name, String genre) {
        if (name != null) {
            this.name = name;
        }
        if (genre != null) {
            this.genre = genre;
        }
    }
}
