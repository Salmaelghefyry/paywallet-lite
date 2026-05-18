package com.paylogic.paywalletlite.config.web;

import com.paylogic.paywalletlite.dto.request.CreateWalletRequestDto;
import com.paylogic.paywalletlite.dto.response.WalletResponseDto;
import com.paylogic.paywalletlite.service.wallet.WalletService;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

class WebConfigMappingTest {

    @Test
    void registersAdminPendingWalletsEndpoint() {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.setServletContext(new MockServletContext());
        context.register(WebConfig.class, TestWalletServiceConfig.class);
        context.refresh();

        try {
            RequestMappingHandlerMapping mappings = context.getBean(RequestMappingHandlerMapping.class);

            boolean endpointRegistered = mappings.getHandlerMethods().keySet().stream()
                    .anyMatch(mapping -> mapping.toString().contains("{GET [/api/admin/wallets/pending]}"));

            assertTrue(endpointRegistered, "Expected GET /api/admin/wallets/pending to be registered");
        } finally {
            context.close();
        }
    }

    @Configuration
    static class TestWalletServiceConfig {

        @Bean
        WalletService walletService() {
            return new WalletService() {
                @Override
                public WalletResponseDto requestWalletCreation(UUID userId, CreateWalletRequestDto request) {
                    return null;
                }

                @Override
                public WalletResponseDto getWalletById(UUID walletId) {
                    return null;
                }

                @Override
                public List<WalletResponseDto> getWalletsByUserId(UUID userId) {
                    return Collections.emptyList();
                }

                @Override
                public List<WalletResponseDto> getPendingWallets() {
                    return Collections.emptyList();
                }

                @Override
                public WalletResponseDto approveWallet(UUID walletId) {
                    return null;
                }

                @Override
                public WalletResponseDto rejectWallet(UUID walletId, String reason) {
                    return null;
                }
            };
        }
    }
}
