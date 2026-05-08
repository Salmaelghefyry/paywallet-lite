paywallet-lite/
├── 📁 paywallet-lite-backend/                    # Module principal Spring Boot
│   ├── 📁 src/
│   │   ├── 📁 main/
│   │   │   ├── 📁 java/
│   │   │   │   └── 📁 com/
│   │   │   │       └── 📁 paylogic/
│   │   │   │           └── 📁 paywalletlite/
│   │   │   │               │
│   │   │   │               ├── 📁 PayWalletLiteApplication.java
│   │   │   │               │
│   │   │   │               ├── 📁 config/                          # Configuration Spring
│   │   │   │               │   ├── 📁 security/
│   │   │   │               │   │   ├── SecurityConfig.java
│   │   │   │               │   │   ├── JwtAuthenticationFilter.java
│   │   │   │               │   │   ├── JwtTokenProvider.java
│   │   │   │               │   │   └── PasswordEncoderConfig.java
│   │   │   │               │   ├── 📁 kafka/
│   │   │   │               │   │   ├── KafkaConfig.java
│   │   │   │               │   │   ├── KafkaProducerConfig.java
│   │   │   │               │   │   ├── KafkaConsumerConfig.java
│   │   │   │               │   │   └── KafkaTopicConfig.java
│   │   │   │               │   ├── 📁 database/
│   │   │   │               │   │   ├── OracleConfig.java
│   │   │   │               │   │   └── JpaConfig.java
│   │   │   │               │   ├── 📁 cache/
│   │   │   │               │   │   └── RedisConfig.java
│   │   │   │               │   ├── 📁 crypto/
│   │   │   │               │   │   ├── CryptoConfig.java
│   │   │   │               │   │   └── KeyManagementConfig.java
│   │   │   │               │   ├── 📁 swagger/
│   │   │   │               │   │   └── OpenApiConfig.java
│   │   │   │               │   └── 📁 scheduling/
│   │   │   │               │       └── ScheduledTasksConfig.java
│   │   │   │               │
│   │   │   │               ├── 📁 domain/                         # Entités métier (JPA)
│   │   │   │               │   ├── 📁 identity/
│   │   │   │               │   │   ├── User.java
│   │   │   │               │   │   ├── Device.java
│   │   │   │               │   │   ├── KYCProfile.java
│   │   │   │               │   │   └── enums/
│   │   │   │               │   │       ├── AccountStatus.java
│   │   │   │               │   │       ├── RoleType.java
│   │   │   │               │   │       ├── DeviceStatus.java
│   │   │   │               │   │       ├── DevicePlatform.java
│   │   │   │               │   │       └── KYCLevel.java
│   │   │   │               │   ├── 📁 wallet/
│   │   │   │               │   │   ├── Wallet.java
│   │   │   │               │   │   ├── WalletConfig.java
│   │   │   │               │   │   ├── WalletKeyPair.java
│   │   │   │               │   │   └── enums/
│   │   │   │               │   │       ├── WalletType.java
│   │   │   │               │   │       ├── WalletStatus.java
│   │   │   │               │   │       ├── KeyStorageType.java
│   │   │   │               │   │       ├── KeyStatus.java
│   │   │   │               │   │       └── WalletConfigStatus.java
│   │   │   │               │   ├── 📁 crypto/
│   │   │   │               │   │   ├── Certificate.java
│   │   │   │               │   │   ├── CertificateAuthority.java
│   │   │   │               │   │   ├── RevocationList.java
│   │   │   │               │   │   ├── ServerKey.java
│   │   │   │               │   │   └── enums/
│   │   │   │               │   │       ├── CertificateStatus.java
│   │   │   │               │   │       ├── CAStatus.java
│   │   │   │               │   │       ├── ServerKeyStatus.java
│   │   │   │               │   │       └── ServerKeyPurpose.java
│   │   │   │               │   ├── 📁 token/
│   │   │   │               │   │   ├── Token.java
│   │   │   │               │   │   ├── TokenSignature.java
│   │   │   │               │   │   ├── TokenTransferNode.java
│   │   │   │               │   │   ├── TokenDenomination.java
│   │   │   │               │   │   ├── TokenAllocationConfig.java
│   │   │   │               │   │   └── enums/
│   │   │   │               │   │       ├── TokenStatus.java
│   │   │   │               │   │       ├── AllocationMode.java
│   │   │   │               │   │       ├── TransferStatus.java
│   │   │   │               │   │       └── TokenAllocationConfigStatus.java
│   │   │   │               │   ├── 📁 transaction/
│   │   │   │               │   │   ├── Transaction.java
│   │   │   │               │   │   ├── TransactionRefund.java
│   │   │   │               │   │   ├── TransactionMetadata.java
│   │   │   │               │   │   ├── Ledger.java
│   │   │   │               │   │   ├── LedgerEntry.java
│   │   │   │               │   │   ├── ReconciliationReport.java
│   │   │   │               │   │   └── enums/
│   │   │   │               │   │       ├── TransactionType.java
│   │   │   │               │   │       ├── TransactionStatus.java
│   │   │   │               │   │       ├── OverpaymentStatus.java
│   │   │   │               │   │       ├── RefundStatus.java
│   │   │   │               │   │       ├── EntryType.java
│   │   │   │               │   │       ├── LedgerType.java
│   │   │   │               │   │       └── ReconciliationStatus.java
│   │   │   │               │   ├── 📁 credit/
│   │   │   │               │   │   ├── CreditConfig.java
│   │   │   │               │   │   ├── CreditLine.java
│   │   │   │               │   │   ├── CreditRepayment.java
│   │   │   │               │   │   └── enums/
│   │   │   │               │   │       ├── CreditStatus.java
│   │   │   │               │   │       ├── CreditConfigStatus.java
│   │   │   │               │   │       ├── RepaymentType.java
│   │   │   │               │   │       └── RepaymentStatus.java
│   │   │   │               │   ├── 📁 risk/
│   │   │   │               │   │   ├── RiskProfile.java
│   │   │   │               │   │   ├── FraudAlert.java
│   │   │   │               │   │   └── enums/
│   │   │   │               │   │       ├── RiskLevel.java
│   │   │   │               │   │       ├── AlertType.java
│   │   │   │               │   │       ├── AlertSeverity.java
│   │   │   │               │   │       └── AlertStatus.java
│   │   │   │               │   └── 📁 notification/
│   │   │   │               │       ├── Notification.java
│   │   │   │               │       └── enums/
│   │   │   │               │           ├── NotificationType.java
│   │   │   │               │           ├── NotificationChannel.java
│   │   │   │               │           └── NotificationStatus.java
│   │   │   │               │
│   │   │   │               ├── 📁 repository/                     # Couche d'accès données
│   │   │   │               │   ├── 📁 identity/
│   │   │   │               │   │   ├── UserRepository.java
│   │   │   │               │   │   ├── DeviceRepository.java
│   │   │   │               │   │   └── KYCProfileRepository.java
│   │   │   │               │   ├── 📁 wallet/
│   │   │   │               │   │   ├── WalletRepository.java
│   │   │   │               │   │   ├── WalletConfigRepository.java
│   │   │   │               │   │   └── WalletKeyPairRepository.java
│   │   │   │               │   ├── 📁 crypto/
│   │   │   │               │   │   ├── CertificateRepository.java
│   │   │   │               │   │   └── RevocationListRepository.java
│   │   │   │               │   ├── 📁 token/
│   │   │   │               │   │   ├── TokenRepository.java
│   │   │   │               │   │   ├── TokenTransferNodeRepository.java
│   │   │   │               │   │   └── TokenAllocationConfigRepository.java
│   │   │   │               │   ├── 📁 transaction/
│   │   │   │               │   │   ├── TransactionRepository.java
│   │   │   │               │   │   ├── TransactionRefundRepository.java
│   │   │   │               │   │   ├── LedgerEntryRepository.java
│   │   │   │               │   │   └── ReconciliationReportRepository.java
│   │   │   │               │   ├── 📁 credit/
│   │   │   │               │   │   ├── CreditConfigRepository.java
│   │   │   │               │   │   ├── CreditLineRepository.java
│   │   │   │               │   │   └── CreditRepaymentRepository.java
│   │   │   │               │   ├── 📁 risk/
│   │   │   │               │   │   ├── RiskProfileRepository.java
│   │   │   │               │   │   └── FraudAlertRepository.java
│   │   │   │               │   └── 📁 notification/
│   │   │   │               │       └── NotificationRepository.java
│   │   │   │               │
│   │   │   │               ├── 📁 service/                        # Couche métier
│   │   │   │               │   ├── 📁 identity/
│   │   │   │               │   │   ├── UserService.java
│   │   │   │               │   │   ├── UserServiceImpl.java
│   │   │   │               │   │   ├── AuthenticationService.java
│   │   │   │               │   │   ├── AuthenticationServiceImpl.java
│   │   │   │               │   │   ├── DeviceService.java
│   │   │   │               │   │   └── DeviceServiceImpl.java
│   │   │   │               │   ├── 📁 wallet/
│   │   │   │               │   │   ├── WalletService.java
│   │   │   │               │   │   ├── WalletServiceImpl.java
│   │   │   │               │   │   ├── WalletConfigService.java
│   │   │   │               │   │   └── WalletConfigServiceImpl.java
│   │   │   │               │   ├── 📁 token/
│   │   │   │               │   │   ├── TokenService.java
│   │   │   │               │   │   ├── TokenServiceImpl.java
│   │   │   │               │   │   ├── TokenAllocationService.java
│   │   │   │               │   │   ├── TokenAllocationServiceImpl.java
│   │   │   │               │   │   ├── TokenGenerationStrategy.java        # Interface
│   │   │   │               │   │   ├── AdaptiveDensityDistribution.java    # Implémentation
│   │   │   │               │   │   └── TokenSelectionOptimizer.java          # Algorithme offline
│   │   │   │               │   ├── 📁 transaction/
│   │   │   │               │   │   ├── TransactionService.java
│   │   │   │               │   │   ├── TransactionServiceImpl.java
│   │   │   │               │   │   ├── LedgerService.java
│   │   │   │               │   │   ├── LedgerServiceImpl.java
│   │   │   │               │   │   ├── ReconciliationService.java
│   │   │   │               │   │   ├── ReconciliationServiceImpl.java
│   │   │   │               │   │   └── OverpaymentHandler.java
│   │   │   │               │   ├── 📁 credit/
│   │   │   │               │   │   ├── CreditService.java
│   │   │   │               │   │   ├── CreditServiceImpl.java
│   │   │   │               │   │   ├── CreditRiskEvaluator.java
│   │   │   │               │   │   └── CreditRepaymentScheduler.java
│   │   │   │               │   ├── 📁 sync/
│   │   │   │               │   │   ├── SynchronizationService.java
│   │   │   │               │   │   ├── SynchronizationServiceImpl.java
│   │   │   │               │   │   ├── SyncValidationService.java
│   │   │   │               │   │   └── TokenRedemptionService.java
│   │   │   │               │   ├── 📁 security/
│   │   │   │               │   │   ├── CryptographicService.java
│   │   │   │               │   │   ├── CryptographicServiceImpl.java
│   │   │   │               │   │   ├── CertificateService.java
│   │   │   │               │   │   ├── CertificateServiceImpl.java
│   │   │   │               │   │   ├── KeyRotationService.java
│   │   │   │               │   │   └── SignatureVerificationService.java
│   │   │   │               │   ├── 📁 risk/
│   │   │   │               │   │   ├── RiskAssessmentService.java
│   │   │   │               │   │   ├── RiskAssessmentServiceImpl.java
│   │   │   │               │   │   └── FraudDetectionEngine.java
│   │   │   │               │   ├── 📁 notification/
│   │   │   │               │   │   ├── NotificationService.java
│   │   │   │               │   │   ├── NotificationServiceImpl.java
│   │   │   │               │   │   ├── PushNotificationService.java
│   │   │   │               │   │   └── SmsNotificationService.java
│   │   │   │               │   └── 📁 audit/
│   │   │   │               │       ├── AuditService.java
│   │   │   │               │       ├── AuditServiceImpl.java
│   │   │   │               │       └── AuditLogChainVerifier.java
│   │   │   │               │
│   │   │   │               ├── 📁 controller/                     # Couche REST API
│   │   │   │               │   ├── 📁 auth/
│   │   │   │               │   │   ├── AuthenticationController.java
│   │   │   │               │   │   └── DeviceController.java
│   │   │   │               │   ├── 📁 wallet/
│   │   │   │               │   │   └── WalletController.java
│   │   │   │               │   ├── 📁 token/
│   │   │   │               │   │   ├── TokenController.java
│   │   │   │               │   │   └── TokenAllocationController.java
│   │   │   │               │   ├── 📁 transaction/
│   │   │   │               │   │   └── TransactionController.java
│   │   │   │               │   ├── 📁 sync/
│   │   │   │               │   │   └── SynchronizationController.java
│   │   │   │               │   ├── 📁 credit/
│   │   │   │               │   │   └── CreditController.java
│   │   │   │               │   ├── 📁 admin/
│   │   │   │               │   │   ├── AdminWalletController.java
│   │   │   │               │   │   ├── AdminTransactionController.java
│   │   │   │               │   │   └── AdminRiskController.java
│   │   │   │               │   └── 📁 webhook/
│   │   │   │               │       └── WebhookController.java
│   │   │   │               │
│   │   │   │               ├── 📁 dto/                            # Data Transfer Objects
│   │   │   │               │   ├── 📁 request/
│   │   │   │               │   │   ├── LoginRequest.java
│   │   │   │               │   │   ├── RegisterRequest.java
│   │   │   │               │   │   ├── TokenAllocationRequest.java
│   │   │   │               │   │   ├── OfflinePaymentRequest.java
│   │   │   │               │   │   ├── SyncRequest.java
│   │   │   │               │   │   └── CreditRequest.java
│   │   │   │               │   ├── 📁 response/
│   │   │   │               │   │   ├── AuthResponse.java
│   │   │   │               │   │   ├── WalletResponse.java
│   │   │   │               │   │   ├── TokenResponse.java
│   │   │   │               │   │   ├── TransactionResponse.java
│   │   │   │               │   │   ├── SyncResponse.java
│   │   │   │               │   │   └── ApiErrorResponse.java
│   │   │   │               │   └── 📁 kafka/
│   │   │   │               │       ├── TransactionEventDto.java
│   │   │   │               │       ├── TokenRedemptionEventDto.java
│   │   │   │               │       ├── FraudAlertEventDto.java
│   │   │   │               │       └── AuditEventDto.java
│   │   │   │               │
│   │   │   │               ├── 📁 mapper/                         # MapStruct
│   │   │   │               │   ├── UserMapper.java
│   │   │   │               │   ├── WalletMapper.java
│   │   │   │               │   ├── TokenMapper.java
│   │   │   │               │   └── TransactionMapper.java
│   │   │   │               │
│   │   │   │               ├── 📁 exception/                      # Gestion erreurs
│   │   │   │               │   ├── GlobalExceptionHandler.java
│   │   │   │               │   ├── BusinessException.java
│   │   │   │               │   ├── InsufficientFundsException.java
│   │   │   │               │   ├── TokenExpiredException.java
│   │   │   │               │   ├── DoubleSpendException.java
│   │   │   │               │   └── InvalidSignatureException.java
│   │   │   │               │
│   │   │   │               ├── 📁 kafka/                          # Event-Driven Layer
│   │   │   │               │   ├── 📁 producer/
│   │   │   │               │   │   ├── TransactionEventProducer.java
│   │   │   │               │   │   ├── TokenRedemptionProducer.java
│   │   │   │               │   │   ├── FraudAlertProducer.java
│   │   │   │               │   │   ├── NotificationProducer.java
│   │   │   │               │   │   ├── AuditEventProducer.java
│   │   │   │               │   │   └── SynchronizationProducer.java
│   │   │   │               │   ├── 📁 consumer/
│   │   │   │               │   │   ├── TransactionEventConsumer.java
│   │   │   │               │   │   ├── TokenRedemptionConsumer.java
│   │   │   │               │   │   ├── FraudAlertConsumer.java
│   │   │   │               │   │   ├── NotificationConsumer.java
│   │   │   │               │   │   ├── AuditEventConsumer.java
│   │   │   │               │   │   └── SynchronizationConsumer.java
│   │   │   │               │   ├── 📁 event/
│   │   │   │               │   │   ├── TransactionCreatedEvent.java
│   │   │   │               │   │   ├── TokenRedeemedEvent.java
│   │   │   │               │   │   ├── FraudDetectedEvent.java
│   │   │   │               │   │   ├── NotificationEvent.java
│   │   │   │               │   │   ├── AuditEvent.java
│   │   │   │               │   │   └── SyncCompletedEvent.java
│   │   │   │               │   └── 📁 handler/
│   │   │   │               │       ├── TransactionEventHandler.java
│   │   │   │               │       ├── FraudAlertHandler.java
│   │   │   │               │       └── NotificationDispatcher.java
│   │   │   │               │
│   │   │   │               ├── 📁 security/                       # Sécurité métier
│   │   │   │               │   ├── 📁 jwt/
│   │   │   │               │   │   ├── JwtTokenUtil.java
│   │   │   │               │   │   └── JwtUserDetailsService.java
│   │   │   │               │   ├── 📁 crypto/
│   │   │   │               │   │   ├── EcdsaSignatureUtil.java
│   │   │   │               │   │   ├── AesEncryptionUtil.java
│   │   │   │               │   │   ├── KeyGeneratorUtil.java
│   │   │   │               │   │   └── HashUtil.java
│   │   │   │               │   └── 📁 certificate/
│   │   │   │               │       ├── CertificateValidator.java
│   │   │   │               │       ├── CertificateRevocationChecker.java
│   │   │   │               │       └── CertificateChainBuilder.java
│   │   │   │               │
│   │   │   │               ├── 📁 util/                           # Utilitaires
│   │   │   │               │   ├── DateTimeUtil.java
│   │   │   │               │   ├── UuidGenerator.java
│   │   │   │               │   ├── MoneyUtil.java
│   │   │   │               │   └── ValidationUtil.java
│   │   │   │               │
│   │   │   │               └── 📁 validation/                     # Bean Validation
│   │   │   │                   ├── PhoneNumberValidator.java
│   │   │   │                   ├── TokenAmountValidator.java
│   │   │   │                   └── TransactionLimitValidator.java
│   │   │   │
│   │   │   └── 📁 resources/
│   │   │       ├── 📁 db/
│   │   │       │   ├── 📁 migration/
│   │   │       │   │   ├── V1__init_schema.sql
│   │   │       │   │   ├── V2__add_token_system.sql
│   │   │       │   │   ├── V3__add_credit_system.sql
│   │   │       │       │   └── V4__add_audit_trail.sql
│   │   │       │   └── 📁 seed/
│   │   │       │       ├── denominations_seed.sql
│   │   │       │       └── wallet_configs_seed.sql
│   │   │       ├── 📁 kafka/
│   │   │       │   └── kafka-topics.json
│   │   │       ├── 📁 certs/
│   │   │       │   └── paylogic-ca.pem
│   │   │       ├── application.yml
│   │   │       ├── application-dev.yml
│   │   │       ├── application-prod.yml
│   │   │       ├── application-test.yml
│   │   │       └── logback-spring.xml
│   │   │
│   │   └── 📁 test/
│   │       ├── 📁 java/
│   │       │   └── 📁 com/paylogic/paywalletlite/
│   │       │       ├── 📁 unit/
│   │       │       │   ├── service/
│   │       │       │   ├── crypto/
│   │       │       │   └── util/
│   │       │       ├── 📁 integration/
│   │       │       │   ├── repository/
│   │       │       │   ├── kafka/
│   │       │       │   └── controller/
│   │       │       ├── 📁 e2e/
│   │       │       │   └── OfflinePaymentFlowTest.java
│   │       │       └── 📁 fixtures/
│   │       │           ├── TestDataFactory.java
│   │       │           └── MockTokenGenerator.java
│   │       └── 📁 resources/
│   │           ├── application-test.yml
│   │           └── 📁 sql/
│   │               ├── init-test-data.sql
│   │               └── cleanup.sql
│   │
│   └── 📁 pom.xml
│
├── 📁 paywallet-lite-mobile/                      # Module Flutter (Android/iOS)
│   ├── 📁 android/
│   │   ├── 📁 app/
│   │   │   ├── 📁 src/
│   │   │   │   ├── 📁 main/
│   │   │   │   │   ├── 📁 java/com/paylogic/paywalletlite/
│   │   │   │   │   │   ├── MainActivity.java
│   │   │   │   │   │   └── 📁 plugins/
│   │   │   │   │   │       ├── SecureStoragePlugin.java
│   │   │   │   │   │       ├── NfcPlugin.java
│   │   │   │   │   │       └── QrCodePlugin.java
│   │   │   │   │   └── 📁 res/
│   │   │   └── build.gradle
│   │   └── build.gradle
│   │
│   ├── 📁 ios/
│   │   ├── 📁 Runner/
│   │   │   ├── AppDelegate.swift
│   │   │   └── 📁 Plugins/
│   │   │       ├── SecureStoragePlugin.swift
│   │   │       └── QrCodePlugin.swift
│   │   └── 📁 Podfile
│   │
│   ├── 📁 lib/
│   │   ├── 📁 main.dart
│   │   ├── 📁 core/
│   │   │   ├── 📁 constants/
│   │   │   │   ├── api_constants.dart
│   │   │   │   ├── app_constants.dart
│   │   │   │   └── storage_constants.dart
│   │   │   ├── 📁 theme/
│   │   │   │   ├── app_theme.dart
│   │   │   │   └── app_colors.dart
│   │   │   ├── 📁 exceptions/
│   │   │   │   ├── app_exception.dart
│   │   │   │   ├── network_exception.dart
│   │   │   │   └── crypto_exception.dart
│   │   │   └── 📁 utils/
│   │   │       ├── date_utils.dart
│   │   │       ├── crypto_utils.dart
│   │   │       └── validation_utils.dart
│   │   │
│   │   ├── 📁 data/
│   │   │   ├── 📁 models/                          # Entités locales (SQLCipher)
│   │   │   │   ├── local_user.dart
│   │   │   │   ├── local_wallet.dart
│   │   │   │   ├── local_token.dart
│   │   │   │   ├── local_transaction.dart
│   │   │   │   └── local_sync_queue.dart
│   │   │   ├── 📁 repositories/
│   │   │   │   ├── local_user_repository.dart
│   │   │   │   ├── local_wallet_repository.dart
│   │   │   │   ├── local_token_repository.dart
│   │   │   │   ├── local_transaction_repository.dart
│   │   │   │   └── sync_queue_repository.dart
│   │   │   ├── 📁 datasources/
│   │   │   │   ├── 📁 local/
│   │   │   │   │   ├── sqlcipher_database.dart
│   │   │   │   │   ├── secure_storage.dart
│   │   │   │   │   └── shared_prefs.dart
│   │   │   │   └── 📁 remote/
│   │   │   │       ├── api_client.dart
│   │   │   │       ├── api_interceptor.dart
│   │   │   │       └── network_info.dart
│   │   │   └── 📁 dto/
│   │   │       ├── login_request_dto.dart
│   │   │       ├── token_allocation_response_dto.dart
│   │   │       ├── sync_request_dto.dart
│   │   │       └── sync_response_dto.dart
│   │   │
│   │   ├── 📁 domain/
│   │   │   ├── 📁 entities/
│   │   │   │   ├── user.dart
│   │   │   │   ├── wallet.dart
│   │   │   │   ├── token.dart
│   │   │   │   └── transaction.dart
│   │   │   ├── 📁 repositories/
│   │   │   │   ├── auth_repository.dart
│   │   │   │   ├── wallet_repository.dart
│   │   │   │   └── token_repository.dart
│   │   │   └── 📁 usecases/
│   │   │       ├── 📁 auth/
│   │   │       │   ├── login_usecase.dart
│   │   │       │   ├── logout_usecase.dart
│   │   │       │   └── verify_pin_usecase.dart
│   │   │       ├── 📁 wallet/
│   │   │       │   ├── get_balance_usecase.dart
│   │   │       │   ├── allocate_tokens_usecase.dart
│   │   │       │   └── sync_wallet_usecase.dart
│   │   │       ├── 📁 token/
│   │   │       │   ├── transfer_token_usecase.dart
│   │   │       │   ├── receive_token_usecase.dart
│   │   │       │   └── validate_token_usecase.dart
│   │   │       └── 📁 offline/
│   │   │           ├── execute_offline_payment_usecase.dart
│   │   │           ├── generate_qr_usecase.dart
│   │   │           └── scan_qr_usecase.dart
│   │   │
│   │   ├── 📁 presentation/
│   │   │   ├── 📁 blocs/                          # State Management
│   │   │   │   ├── auth_bloc/
│   │   │   │   │   ├── auth_bloc.dart
│   │   │   │   │   ├── auth_event.dart
│   │   │   │   │   └── auth_state.dart
│   │   │   │   ├── wallet_bloc/
│   │   │   │   │   ├── wallet_bloc.dart
│   │   │   │   │   ├── wallet_event.dart
│   │   │   │   │   └── wallet_state.dart
│   │   │   │   ├── token_bloc/
│   │   │   │   │   ├── token_bloc.dart
│   │   │   │   │   ├── token_event.dart
│   │   │   │   │   └── token_state.dart
│   │   │   │   └── sync_bloc/
│   │   │   │       ├── sync_bloc.dart
│   │   │   │       ├── sync_event.dart
│   │   │   │       └── sync_state.dart
│   │   │   ├── 📁 pages/
│   │   │   │   ├── splash_page.dart
│   │   │   │   ├── login_page.dart
│   │   │   │   ├── home_page.dart
│   │   │   │   ├── wallet_page.dart
│   │   │   │   ├── offline_payment_page.dart
│   │   │   │   ├── qr_scan_page.dart
│   │   │   │   ├── sync_page.dart
│   │   │   │   └── settings_page.dart
│   │   │   ├── 📁 widgets/
│   │   │   │   ├── balance_card.dart
│   │   │   │   ├── token_list.dart
│   │   │   │   ├── transaction_history.dart
│   │   │   │   ├── qr_display.dart
│   │   │   │   └── offline_indicator.dart
│   │   │   └── 📁 navigation/
│   │   │       ├── app_router.dart
│   │   │       └── route_names.dart
│   │   │
│   │   └── 📁 services/
│   │       ├── 📁 crypto/
│   │       │   ├── local_crypto_service.dart
│   │       │   ├── signature_service.dart
│   │       │   └── key_management_service.dart
│   │       ├── 📁 offline/
│   │       │   ├── offline_manager.dart
│   │       │   ├── connectivity_monitor.dart
│   │       │   └── offline_queue_processor.dart
│   │       ├── 📁 nfc/
│   │       │   ├── nfc_service.dart
│   │       │   └── nfc_android_impl.dart
│   │       ├── 📁 qr/
│   │       │   ├── qr_generator.dart
│   │       │   └── qr_scanner.dart
│   │       └── 📁 sync/
│   │           ├── sync_manager.dart
│   │           ├── sync_scheduler.dart
│   │           └── conflict_resolver.dart
│   │
│   ├── 📁 test/
│   │   ├── 📁 unit/
│   │   ├── 📁 widget/
│   │   ├── 📁 integration/
│   │   └── 📁 mocks/
│   │
│   ├── pubspec.yaml
│   └── analysis_options.yaml
│
├── 📁 paywallet-lite-shared/                      # Module commun (contrats API)
│   ├── 📁 src/
│   │   ├── 📁 main/
│   │   │   ├── 📁 java/
│   │   │   │   └── 📁 com/paylogic/paywalletlite/shared/
│   │   │   │       ├── 📁 api/
│   │   │   │       │   ├── ApiPaths.java
│   │   │   │       │   ├── ApiVersions.java
│   │   │   │       │   └── ApiConstants.java
│   │   │   │       ├── 📁 dto/
│   │   │   │       │   ├── TokenDto.java
│   │   │   │       │   ├── TransactionDto.java
│   │   │   │       │   └── SyncPayloadDto.java
│   │   │   │       └── 📁 events/
│   │   │   │           ├── KafkaTopics.java
│   │   │   │           └── EventSchemas.java
│   │   │   └── 📁 resources/
│   │   │       └── shared-config.yml
│   │   └── 📁 test/
│   └── pom.xml
│
├── 📁 paywallet-lite-infrastructure/              # IaC & DevOps
│   ├── 📁 docker/
│   │   ├── 📁 backend/
│   │   │   ├── Dockerfile
│   │   │   └── docker-entrypoint.sh
│   │   ├── 📁 oracle/
│   │   │   ├── Dockerfile
│   │   │   └── init-scripts/
│   │   ├── 📁 kafka/
│   │   │   ├── docker-compose.kafka.yml
│   │   │   └── 📁 scripts/
│   │   │       ├── create-topics.sh
│   │   │       └── topic-config.json
│   │   ├── 📁 redis/
│   │   │   └── docker-compose.redis.yml
│   │   └── 📁 monitoring/
│   │       ├── docker-compose.monitoring.yml
│   │       ├── 📁 prometheus/
│   │       │   └── prometheus.yml
│   │       └── 📁 grafana/
│   │           └── 📁 dashboards/
│   ├── 📁 kubernetes/
│   │   ├── 📁 base/
│   │   │   ├── deployment.yml
│   │   │   ├── service.yml
│   │   │   ├── configmap.yml
│   │   │   └── secret.yml
│   │   ├── 📁 overlays/
│   │   │   ├── 📁 dev/
│   │   │   ├── 📁 staging/
│   │   │   └── 📁 prod/
│   │   └── 📁 helm/
│   │       └── paywallet-lite/
│   ├── 📁 terraform/
│   │   ├── 📁 modules/
│   │   │   ├── oracle-db/
│   │   │   ├── kafka-cluster/
│   │   │   └── k8s-cluster/
│   │   ├── 📁 environments/
│   │   │   ├── dev/
│   │   │   ├── staging/
│   │   │   └── prod/
│   │   └── main.tf
│   └── 📁 scripts/
│       ├── deploy.sh
│       ├── backup-oracle.sh
│       └── health-check.sh
│
├── 📁 docs/                                       # Documentation
│   ├── 📁 architecture/
│   │   ├── c4-model/
│   │   ├── sequence-diagrams/
│   │   └── data-flow/
│   ├── 📁 api/
│   │   ├── openapi.yml
│   │   └── postman-collection.json
│   ├── 📁 security/
│   │   ├── threat-model.md
│   │   └── security-checklist.md
│   └── 📁 deployment/
│       ├── deployment-guide.md
│       └── runbook.md
│
├── 📁 .github/
│   ├── 📁 workflows/
│   │   ├── ci-backend.yml
│   │   ├── ci-mobile.yml
│   │   ├── cd-deploy.yml
│   │   └── security-scan.yml
│   └── 📁 pull_request_template.md
│
├── .gitignore
├── README.md
├── LICENSE
├── docker-compose.yml                             # Stack complet local
└── Makefile                                       # Commandes communes
