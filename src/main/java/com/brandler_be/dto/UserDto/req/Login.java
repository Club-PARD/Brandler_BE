package com.brandler_be.dto.UserDto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 로그인 요청 DTO
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "로그인 요청 정보")
public class Login {
    
    @Schema(description = "유저 식별 이메일", example = "1234@gmail.com")
    private String email;
    
    @Schema(description = "닉네임", example = "파드")
    private String name;
    
    @Schema(description = "장르", example = "아메카지")
    private String genre;
}
