package com.paylogic.paywalletlite.service.identity;

import com.paylogic.paywalletlite.domain.identity.Device;
import com.paylogic.paywalletlite.domain.identity.DeviceSession;
import com.paylogic.paywalletlite.domain.identity.User;
import com.paylogic.paywalletlite.domain.identity.enums.AccountStatus;
import com.paylogic.paywalletlite.domain.identity.enums.DeviceStatus;
import com.paylogic.paywalletlite.domain.identity.enums.SessionStatus;
import com.paylogic.paywalletlite.dto.request.LoginRequestDto;
import com.paylogic.paywalletlite.dto.response.AuthResponseDto;
import com.paylogic.paywalletlite.exception.BusinessException;
import com.paylogic.paywalletlite.repository.identity.DeviceRepository;
import com.paylogic.paywalletlite.repository.identity.DeviceSessionRepository;
import com.paylogic.paywalletlite.repository.identity.UserRepository;
import com.paylogic.paywalletlite.security.crypto.HashUtil;
import com.paylogic.paywalletlite.config.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final DeviceRepository deviceRepository;
    private final DeviceSessionRepository sessionRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    @Autowired
    public AuthenticationServiceImpl(UserRepository userRepository,
                                     DeviceRepository deviceRepository,
                                     DeviceSessionRepository sessionRepository,
                                     JwtTokenProvider jwtTokenProvider,
                                     PasswordEncoder passwordEncoder,
                                     UserService userService) {
        this.userRepository = userRepository;
        this.deviceRepository = deviceRepository;
        this.sessionRepository = sessionRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    @Override
    @Transactional
    public AuthResponseDto authenticate(LoginRequestDto request) throws BusinessException {
        User user = userRepository.findByPhoneNumber(request.getPhoneNumber())
                .orElseThrow(() -> new BusinessException("Invalid credentials"));

        if (user.getStatus() == AccountStatus.CLOSED) {
            throw new BusinessException("Account is closed");
        }

        if (userService.isAccountLocked(user.getUserId())) {
            throw new BusinessException("Account is locked. Try again later.");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            userService.recordFailedLogin(user.getUserId());
            throw new BusinessException("Invalid credentials");
        }

        Device device = deviceRepository.findByUserIdAndHardwareId(user.getUserId(), request.getHardwareId())
                .orElseThrow(() -> new BusinessException("Device not registered"));

        if (device.getStatus() != DeviceStatus.ACTIVE) {
            throw new BusinessException("Device is not active");
        }

        // Generate tokens
        String accessToken = jwtTokenProvider.generateAccessToken(user.getUserId().toString(), device.getDeviceId().toString());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getUserId().toString());

        // Create session
        DeviceSession session = new DeviceSession();
        session.setSessionId(UUID.randomUUID());
        session.setDeviceId(device.getDeviceId());
        session.setUserId(user.getUserId());
        session.setJwtTokenHash(HashUtil.sha256(accessToken));
        session.setCreatedAt(LocalDateTime.now());
        session.setExpiresAt(LocalDateTime.now().plusHours(24));
        session.setLastActivity(LocalDateTime.now());
        session.setStatus(SessionStatus.ACTIVE);

        sessionRepository.save(session);
        deviceRepository.updateLastSeen(device.getDeviceId());
        userService.recordSuccessfulLogin(user.getUserId());

        AuthResponseDto response = new AuthResponseDto();
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);
        response.setTokenType("Bearer");
        response.setExpiresIn(86400);
        response.setUserId(user.getUserId());
        response.setRole(user.getRole());

        return response;
    }

    @Override
    @Transactional
    public AuthResponseDto refreshToken(String refreshToken) throws BusinessException {
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new BusinessException("Invalid refresh token");
        }

        String userId = jwtTokenProvider.getUserIdFromToken(refreshToken);
        User user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new BusinessException("User not found"));

        String newAccessToken = jwtTokenProvider.generateAccessToken(userId, null);
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(userId);

        AuthResponseDto response = new AuthResponseDto();
        response.setAccessToken(newAccessToken);
        response.setRefreshToken(newRefreshToken);
        response.setTokenType("Bearer");
        response.setExpiresIn(86400);
        response.setUserId(user.getUserId());
        response.setRole(user.getRole());

        return response;
    }

    @Override
    @Transactional
    public void logout(String token) {
        String tokenHash = HashUtil.sha256(token);
        sessionRepository.findByTokenHash(tokenHash).ifPresent(session -> {
            sessionRepository.revokeSession(session.getSessionId());
        });
    }

    @Override
    @Transactional
    public void logoutAllDevices(String userId) {
        sessionRepository.revokeAllUserSessions(UUID.fromString(userId));
    }

    @Override
    public boolean validateToken(String token) {
        return jwtTokenProvider.validateToken(token);
    }
}