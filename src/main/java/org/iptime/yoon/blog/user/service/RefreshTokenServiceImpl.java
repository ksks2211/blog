package org.iptime.yoon.blog.user.service;

import lombok.RequiredArgsConstructor;
import org.iptime.yoon.blog.security.auth.JwtUser;
import org.iptime.yoon.blog.security.exception.ExpiredRefreshTokenException;
import org.iptime.yoon.blog.security.exception.InvalidBlogUserDataException;
import org.iptime.yoon.blog.security.exception.InvalidRefreshTokenException;
import org.iptime.yoon.blog.user.BlogUserMapper;
import org.iptime.yoon.blog.user.entity.BlogUser;
import org.iptime.yoon.blog.user.entity.RefreshToken;
import org.iptime.yoon.blog.user.repository.BlogUserRepository;
import org.iptime.yoon.blog.user.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;



/**
 * @author rival
 * @since 2023-08-17
 */
@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService{

    private final RefreshTokenRepository refreshTokenRepository;
    private final BlogUserRepository blogUserRepository;
    private final BlogUserMapper blogUserMapper;

    @Value("${auth.refresh-token.refresh-exp-hours}")
    private int REFRESH_EXP_HOURS;

    private LocalDateTime getExpiryDate(){
        return LocalDateTime.now().plusHours(REFRESH_EXP_HOURS);
    }

    @Override
    @Transactional
    public String createToken(Long userId){

        if(!blogUserRepository.existsById(userId)){
            throw new InvalidBlogUserDataException("ID : "+userId + " AuthUser Not Found");
        }

        BlogUser user = BlogUser.builder().id(userId).build();
        RefreshToken token;
        Optional<RefreshToken> optional = refreshTokenRepository.findByUser(user);

        if (optional.isPresent() && !optional.get().isExpired()) {
            token = optional.get();
        } else {
            optional.ifPresent(refreshToken -> refreshTokenRepository.deleteById(refreshToken.getId()));
            token = RefreshToken.builder()
                .expiryDate(getExpiryDate())
                .user(BlogUser.builder().id(userId).build())
                .build();
        }
        refreshTokenRepository.save(token);
        return token.getId();
    }

    @Override
    @Transactional
    public JwtUser validateTokenAndGetJwtUser(String id) throws InvalidRefreshTokenException, ExpiredRefreshTokenException {

        RefreshToken refreshToken = refreshTokenRepository.findById(id).orElseThrow(() -> new InvalidRefreshTokenException("Invalid Refresh Token"));
        if(refreshToken.isExpired()){
            refreshTokenRepository.delete(refreshToken);
            throw new ExpiredRefreshTokenException("Token Expired");
        }
        BlogUser blogUser = refreshToken.getUser();
        return blogUserMapper.blogUserToJwtUser(blogUser);
    }

    @Override
    @Transactional
    public void removeExpiredTokens() {
        refreshTokenRepository.deleteAllExpiredTokens();
    }

    @Override
    public void removeTokenByUserId(Long id) {
        BlogUser user = BlogUser.builder().id(id).build();
        refreshTokenRepository.deleteByUser(user);
    }


}
