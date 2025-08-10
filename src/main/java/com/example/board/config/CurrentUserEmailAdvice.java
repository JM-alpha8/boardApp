package com.example.board.config;

// CurrentUserEmailAdvice.java
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class CurrentUserEmailAdvice {

    @ModelAttribute("principalEmail")
    public String principalEmail(Authentication auth) {
        if (auth == null) return null;
        Object p = auth.getPrincipal();

        if (p instanceof OidcUser ou) {                 // 구글 OIDC
            return ou.getEmail();
        }
        if (p instanceof OAuth2User ou2) {              // (대비) 일반 OAuth2
            Object email = ou2.getAttributes().get("email");
            return email != null ? email.toString() : auth.getName();
        }
        if (p instanceof UserDetails ud) {              // 폼 로그인
            return ud.getUsername();                    // username을 이메일로 쓰는 경우 OK
        }
        if (p instanceof String s) {                    // anonymousUser 등
            return s;
        }
        return auth.getName();
    }
}

