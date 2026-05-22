paywallet-lite-backend/
├── 📁 src/
│   ├── 📁 main/
│   │   ├── 📁 java/
│   │   │   └── 📁 com/
│   │   │       └── 📁 paylogic/
│   │   │           └── 📁 paywalletlite/
│   │   │               │
│   │   │               ├── 📁 PayWalletLiteApplication.java          # Main class avec AnnotationConfigApplicationContext
│   │   │               │
│   │   │               ├── 📁 config/                                  # Configuration Spring Framework (XML + Java Config)
│   │   │               │   ├── 📁 root/
│   │   │               │   │   ├── RootConfig.java                     # @Configuration racine (services, repositories)
│   │   │               │   │   ├── AppConfig.java                      # Configuration applicative générale
│   │   │               │   │   └── PropertyConfig.java                 # Chargement properties
│   │   │               │   ├── 📁 web/
│   │   │               │   │   ├── WebConfig.java                      # @Configuration Web (controllers, view resolvers)
│   │   │               │   │   ├── WebAppInitializer.java                # WebApplicationInitializer (remplace web.xml)
│   │   │               │   │   ├── ServletConfig.java                    # DispatcherServlet configuration
│   │   │               │   │   └── CORSConfig.java                       # Configuration CORS
│   │   │               │   ├── 📁 security/
│   │   │               │   │   ├── SecurityConfig.java                   # Spring Security Configuration
│   │   │               │   │   ├── SecurityWebApplicationInitializer.java  # Initializer sécurité
│   │   │               │   │   ├── JwtAuthenticationFilter.java          # Filtre JWT personnalisé
│   │   │               │   │   ├── JwtTokenProvider.java                 # Génération/validation JWT
│   │   │               │   │   ├── JwtUserDetailsService.java            # UserDetailsService
│   │   │               │   │   ├── PasswordEncoderConfig.java            # BCryptPasswordEncoder
│   │   │               │   │   └── AccessDeniedHandlerImpl.java          # Gestion 403
│   │   │               │   ├── 📁 database/
│   │   │               │   │   ├── DataSourceConfig.java                 # Configuration Oracle DataSource
│   │   │               │   │   ├── JpaConfig.java                        # EntityManagerFactory, TransactionManager
│   │   │               │   │   ├── HibernateProperties.java              # Propriétés Hibernate
│   │   │               │   │   └── DatabaseMigrationConfig.java          # Flyway ou Liquibase
│   │   │               │   ├── 📁 kafka/
│   │   │               │   │   ├── KafkaConfig.java                      # ProducerFactory, ConsumerFactory
│   │   │               │   │   ├── KafkaProducerConfig.java                # Template Kafka
│   │   │               │   │   ├── KafkaConsumerConfig.java                # Listener Container Factory
│   │   │               │   │   └── KafkaTopicConfig.java                   # Création topics
│   │   │               │   ├── 📁 crypto/
│   │   │               │   │   ├── CryptoConfig.java                       # Beans cryptographiques
│   │   │               │   │   └── KeyManagementConfig.java                # Gestion clés serveur
│   │   │               │   └── 📁 scheduling/
│   │   │               │       ├── TaskSchedulerConfig.java                # ScheduledExecutorService
│   │   │               │       └── QuartzConfig.java                       # Jobs planifiés (token expiration, sync)
│   │   │               │
│   │   │               ├── 📁 domain/                                    # Entités JPA (même structure)
│   │   │               │   ├── 📁 identity/
│   │   │               │   │   ├── User.java
│   │   │               │   │   ├── Device.java
│   │   │               │   │   ├── KYCProfile.java
│   │   │               │   │   └── enums/
│   │   │               │   │       ├── AccountStatus.java
│   │   │               │   │       ├── RoleType.java
│   │   │               │   │       ├── DeviceStatus.java
│   │   │               │   │       ├── DevicePlatform.java
│   │   │               │   │       └── KYCLevel.java
│   │   │               │   ├── 📁 wallet/
│   │   │               │   │   ├── Wallet.java
│   │   │               │   │   ├── WalletConfig.java
│   │   │               │   │   ├── WalletKeyPair.java
│   │   │               │   │   └── enums/
│   │   │               │   │       ├── WalletType.java
│   │   │               │   │       ├── WalletStatus.java
│   │   │               │   │       ├── KeyStorageType.java
│   │   │               │   │       ├── KeyStatus.java
│   │   │               │   │       └── WalletConfigStatus.java
│   │   │               │   ├── 📁 crypto/
│   │   │               │   │   ├── Certificate.java
│   │   │               │   │   ├── CertificateAuthority.java
│   │   │               │   │   ├── RevocationList.java
│   │   │               │   │   ├── ServerKey.java
│   │   │               │   │   └── enums/
│   │   │               │   │       ├── CertificateStatus.java
│   │   │               │   │       ├── CAStatus.java
│   │   │               │   │       ├── ServerKeyStatus.java
│   │   │               │   │       └── ServerKeyPurpose.java
│   │   │               │   ├── 📁 token/
│   │   │               │   │   ├── Token.java
│   │   │               │   │   ├── TokenSignature.java
│   │   │               │   │   ├── TokenTransferNode.java
│   │   │               │   │   ├── TokenDenomination.java
│   │   │               │   │   ├── TokenAllocationConfig.java
│   │   │               │   │   └── enums/
│   │   │               │   │       ├── TokenStatus.java
│   │   │               │   │       ├── AllocationMode.java
│   │   │               │   │       ├── TransferStatus.java
│   │   │               │   │       └── TokenAllocationConfigStatus.java
│   │   │               │   ├── 📁 transaction/
│   │   │               │   │   ├── Transaction.java
│   │   │               │   │   ├── TransactionRefund.java
│   │   │               │   │   ├── TransactionMetadata.java
│   │   │               │   │   ├── Ledger.java
│   │   │               │   │   ├── LedgerEntry.java
│   │   │               │   │   ├── ReconciliationReport.java
│   │   │               │   │   └── enums/
│   │   │               │   │       ├── TransactionType.java
│   │   │               │   │       ├── TransactionStatus.java
│   │   │               │   │       ├── OverpaymentStatus.java
│   │   │               │   │       ├── RefundStatus.java
│   │   │               │   │       ├── EntryType.java
│   │   │               │   │       ├── LedgerType.java
│   │   │               │   │       └── ReconciliationStatus.java
│   │   │               │   ├── 📁 credit/
│   │   │               │   │   ├── CreditConfig.java
│   │   │               │   │   ├── CreditLine.java
│   │   │               │   │   ├── CreditRepayment.java
│   │   │               │   │   └── enums/
│   │   │               │   │       ├── CreditStatus.java
│   │   │               │   │       ├── CreditConfigStatus.java
│   │   │               │   │       ├── RepaymentType.java
│   │   │               │   │       └── RepaymentStatus.java
│   │   │               │   ├── 📁 risk/
│   │   │               │   │   ├── RiskProfile.java
│   │   │               │   │   ├── FraudAlert.java
│   │   │               │   │   └── enums/
│   │   │               │   │       ├── RiskLevel.java
│   │   │               │   │       ├── AlertType.java
│   │   │               │   │       ├── AlertSeverity.java
│   │   │               │   │       └── AlertStatus.java
│   │   │               │   └── 📁 notification/
│   │   │               │       ├── Notification.java
│   │   │               │       └── enums/
│   │   │               │           ├── NotificationType.java
│   │   │               │           ├── NotificationChannel.java
│   │   │               │           └── NotificationStatus.java
│   │   │               │
│   │   │               ├── 📁 repository/                                # Couche d'accès données
│   │   │               │   ├── 📁 identity/
│   │   │               │   │   ├── UserRepository.java                     # Interface
│   │   │               │   │   ├── UserRepositoryImpl.java                 # Implémentation JPA
│   │   │               │   │   ├── DeviceRepository.java
│   │   │               │   │   ├── DeviceRepositoryImpl.java
│   │   │               │   │   └── KYCProfileRepository.java
│   │   │               │   ├── 📁 wallet/
│   │   │               │   │   ├── WalletRepository.java
│   │   │               │   │   ├── WalletRepositoryImpl.java
│   │   │               │   │   ├── WalletConfigRepository.java
│   │   │               │   │   └── WalletConfigRepositoryImpl.java
│   │   │               │   ├── 📁 crypto/
│   │   │               │   │   ├── CertificateRepository.java
│   │   │               │   │   └── CertificateRepositoryImpl.java
│   │   │               │   ├── 📁 token/
│   │   │               │   │   ├── TokenRepository.java
│   │   │               │   │   ├── TokenRepositoryImpl.java
│   │   │               │   │   ├── TokenTransferNodeRepository.java
│   │   │               │   │   └── TokenAllocationConfigRepository.java
│   │   │               │   ├── 📁 transaction/
│   │   │               │   │   ├── TransactionRepository.java
│   │   │               │   │   ├── TransactionRepositoryImpl.java
│   │   │               │   │   ├── TransactionRefundRepository.java
│   │   │               │   │   ├── LedgerEntryRepository.java
│   │   │               │   │   └── ReconciliationReportRepository.java
│   │   │               │   ├── 📁 credit/
│   │   │               │   │   ├── CreditConfigRepository.java
│   │   │               │   │   ├── CreditConfigRepositoryImpl.java
│   │   │               │   │   ├── CreditLineRepository.java
│   │   │               │   │   └── CreditRepaymentRepository.java
│   │   │               │   ├── 📁 risk/
│   │   │               │   │   ├── RiskProfileRepository.java
│   │   │               │   │   └── FraudAlertRepository.java
│   │   │               │   └── 📁 notification/
│   │   │               │       └── NotificationRepository.java
│   │   │               │
│   │   │               ├── 📁 service/                                   # Couche métier
│   │   │               │   ├── 📁 identity/
│   │   │               │   │   ├── UserService.java                        # Interface
│   │   │               │   │   ├── UserServiceImpl.java                    # @Service
│   │   │               │   │   ├── AuthenticationService.java
│   │   │               │   │   ├── AuthenticationServiceImpl.java
│   │   │               │   │   ├── DeviceService.java
│   │   │               │   │   └── DeviceServiceImpl.java
│   │   │               │   ├── 📁 wallet/
│   │   │               │   │   ├── WalletService.java
│   │   │               │   │   ├── WalletServiceImpl.java
│   │   │               │   │   ├── WalletConfigService.java
│   │   │               │   │   └── WalletConfigServiceImpl.java
│   │   │               │   ├── 📁 token/
│   │   │               │   │   ├── TokenService.java
│   │   │               │   │   ├── TokenServiceImpl.java
│   │   │               │   │   ├── TokenAllocationService.java
│   │   │               │   │   ├── TokenAllocationServiceImpl.java
│   │   │               │   │   ├── TokenGenerationStrategy.java            # Interface stratégie
│   │   │               │   │   ├── AdaptiveDensityDistribution.java        # Implémentation algorithme
│   │   │               │   │   └── TokenSelectionOptimizer.java          # Algorithme sélection offline
│   │   │               │   ├── 📁 transaction/
│   │   │               │   │   ├── TransactionService.java
│   │   │               │   │   ├── TransactionServiceImpl.java
│   │   │               │   │   ├── LedgerService.java
│   │   │               │   │   ├── LedgerServiceImpl.java
│   │   │               │   │   ├── ReconciliationService.java
│   │   │               │   │   ├── ReconciliationServiceImpl.java
│   │   │               │   │   └── OverpaymentHandler.java
│   │   │               │   ├── 📁 credit/
│   │   │               │   │   ├── CreditService.java
│   │   │               │   │   ├── CreditServiceImpl.java
│   │   │               │   │   ├── CreditRiskEvaluator.java
│   │   │               │   │   └── CreditRepaymentScheduler.java         # @Scheduled ou Quartz
│   │   │               │   ├── 📁 sync/
│   │   │               │   │   ├── SynchronizationService.java
│   │   │               │   │   ├── SynchronizationServiceImpl.java
│   │   │               │   │   ├── SyncValidationService.java
│   │   │               │   │   └── TokenRedemptionService.java
│   │   │               │   ├── 📁 security/
│   │   │               │   │   ├── CryptographicService.java
│   │   │               │   │   ├── CryptographicServiceImpl.java
│   │   │               │   │   ├── CertificateService.java
│   │   │               │   │   ├── CertificateServiceImpl.java
│   │   │               │   │   ├── KeyRotationService.java
│   │   │               │   │   └── SignatureVerificationService.java
│   │   │               │   ├── 📁 risk/
│   │   │               │   │   ├── RiskAssessmentService.java
│   │   │               │   │   ├── RiskAssessmentServiceImpl.java
│   │   │               │   │   └── FraudDetectionEngine.java
│   │   │               │   ├── 📁 notification/
│   │   │               │   │   ├── NotificationService.java
│   │   │               │   │   ├── NotificationServiceImpl.java
│   │   │               │   │   ├── PushNotificationService.java
│   │   │               │   │   └── SmsNotificationService.java
│   │   │               │   └── 📁 audit/
│   │   │               │       ├── AuditService.java
│   │   │               │       ├── AuditServiceImpl.java
│   │   │               │       └── AuditLogChainVerifier.java
│   │   │               │
│   │   │               ├── 📁 controller/                                # Couche REST (Spring MVC)
│   │   │               │   ├── 📁 auth/
│   │   │               │   │   ├── AuthenticationController.java         # @Controller + @ResponseBody
│   │   │               │   │   └── DeviceController.java
│   │   │               │   ├── 📁 wallet/
│   │   │               │   │   └── WalletController.java
│   │   │               │   ├── 📁 token/
│   │   │               │   │   ├── TokenController.java
│   │   │               │   │   └── TokenAllocationController.java
│   │   │               │   ├── 📁 transaction/
│   │   │               │   │   └── TransactionController.java
│   │   │               │   ├── 📁 sync/
│   │   │               │   │   └── SynchronizationController.java
│   │   │               │   ├── 📁 credit/
│   │   │               │   │   └── CreditController.java
│   │   │               │   ├── 📁 admin/
│   │   │               │   │   ├── AdminWalletController.java
│   │   │               │   │   ├── AdminTransactionController.java
│   │   │               │   │   └── AdminRiskController.java
│   │   │               │   └── 📁 webhook/
│   │   │               │       └── WebhookController.java
│   │   │               │
│   │   │               ├── 📁 dto/                                       # Data Transfer Objects
│   │   │               │   ├── 📁 request/
│   │   │               │   │   ├── LoginRequestDto.java
│   │   │               │   │   ├── RegisterRequestDto.java
│   │   │               │   │   ├── TokenAllocationRequestDto.java
│   │   │               │   │   ├── OfflinePaymentRequestDto.java
│   │   │               │   │   ├── SyncRequestDto.java
│   │   │               │   │   └── CreditRequestDto.java
│   │   │               │   ├── 📁 response/
│   │   │               │   │   ├── AuthResponseDto.java
│   │   │               │   │   ├── WalletResponseDto.java
│   │   │               │   │   ├── TokenResponseDto.java
│   │   │               │   │   ├── TransactionResponseDto.java
│   │   │               │   │   ├── SyncResponseDto.java
│   │   │               │   │   └── ApiErrorResponseDto.java
│   │   │               │   └── 📁 kafka/
│   │   │               │       ├── TransactionEventDto.java
│   │   │               │       ├── TokenRedemptionEventDto.java
│   │   │               │       ├── FraudAlertEventDto.java
│   │   │               │       └── AuditEventDto.java
│   │   │               │
│   │   │               ├── 📁 mapper/                                  # MapStruct ou manuel
│   │   │               │   ├── UserMapper.java
│   │   │               │   ├── WalletMapper.java
│   │   │               │   ├── TokenMapper.java
│   │   │               │   └── TransactionMapper.java
│   │   │               │
│   │   │               ├── 📁 exception/                                 # Gestion erreurs
│   │   │               │   ├── GlobalExceptionHandler.java               # @ControllerAdvice
│   │   │               │   ├── BusinessException.java
│   │   │               │   ├── InsufficientFundsException.java
│   │   │               │   ├── TokenExpiredException.java
│   │   │               │   ├── DoubleSpendException.java
│   │   │               │   └── InvalidSignatureException.java
│   │   │               │
│   │   │               ├── 📁 kafka/                                     # Event-Driven Layer
│   │   │               │   ├── 📁 producer/
│   │   │               │   │   ├── TransactionEventProducer.java         # KafkaTemplate
│   │   │               │   │   ├── TokenRedemptionProducer.java
│   │   │               │   │   ├── FraudAlertProducer.java
│   │   │               │   │   ├── NotificationProducer.java
│   │   │               │   │   ├── AuditEventProducer.java
│   │   │               │   │   └── SynchronizationProducer.java
│   │   │               │   ├── 📁 consumer/
│   │   │               │   │   ├── TransactionEventConsumer.java           # @KafkaListener
│   │   │               │   │   ├── TokenRedemptionConsumer.java
│   │   │               │   │   ├── FraudAlertConsumer.java
│   │   │               │   │   ├── NotificationConsumer.java
│   │   │               │   │   ├── AuditEventConsumer.java
│   │   │               │   │   └── SynchronizationConsumer.java
│   │   │               │   ├── 📁 event/
│   │   │               │   │   ├── TransactionCreatedEvent.java
│   │   │               │   │   ├── TokenRedeemedEvent.java
│   │   │               │   │   ├── FraudDetectedEvent.java
│   │   │               │   │   ├── NotificationEvent.java
│   │   │               │   │   ├── AuditEvent.java
│   │   │               │   │   └── SyncCompletedEvent.java
│   │   │               │   └── 📁 handler/
│   │   │               │       ├── TransactionEventHandler.java
│   │   │               │       ├── FraudAlertHandler.java
│   │   │               │       └── NotificationDispatcher.java
│   │   │               │
│   │   │               ├── 📁 security/                                # Sécurité métier
│   │   │               │   ├── 📁 jwt/
│   │   │               │   │   ├── JwtTokenUtil.java
│   │   │               │   │   └── JwtAuthenticationEntryPoint.java
│   │   │               │   ├── 📁 crypto/
│   │   │               │   │   ├── EcdsaSignatureUtil.java
│   │   │               │   │   ├── AesEncryptionUtil.java
│   │   │               │   │   ├── KeyGeneratorUtil.java
│   │   │               │   │   └── HashUtil.java
│   │   │               │   └── 📁 certificate/
│   │   │               │       ├── CertificateValidator.java
│   │   │               │       ├── CertificateRevocationChecker.java
│   │   │               │       └── CertificateChainBuilder.java
│   │   │               │
│   │   │               ├── 📁 util/                                    # Utilitaires
│   │   │               │   ├── DateTimeUtil.java
│   │   │               │   ├── UuidGenerator.java
│   │   │               │   ├── MoneyUtil.java
│   │   │               │   └── ValidationUtil.java
│   │   │               │
│   │   │               └── 📁 validation/                              # Bean Validation
│   │   │                   ├── PhoneNumberValidator.java
│   │   │                   ├── TokenAmountValidator.java
│   │   │                   └── TransactionLimitValidator.java
│   │   │
│   │   └── 📁 resources/
│   │       ├── 📁 spring/
│   │       │   ├── root-context.xml                                    # Contexte racine (services, repos)
│   │       │   ├── servlet-context.xml                                 # Contexte web (controllers)
│   │       │   ├── security-context.xml                                # Spring Security config XML
│   │       │   ├── kafka-context.xml                                   # Kafka beans
│   │       │   ├── jpa-context.xml                                     # JPA/Hibernate beans
│   │       │   └── crypto-context.xml                                  # Beans cryptographiques
│   │       ├── 📁 db/
│   │       │   ├── 📁 migration/
│   │       │   │   ├── V1__init_schema.sql
│   │       │   │   ├── V2__add_token_system.sql
│   │       │   │   ├── V3__add_credit_system.sql
│   │       │   │   └── V4__add_audit_trail.sql
│   │       │   └── 📁 seed/
│   │       │       ├── denominations_seed.sql
│   │       │       └── wallet_configs_seed.sql
│   │       ├── 📁 kafka/
│   │       │   └── kafka-topics.json
│   │       ├── 📁 certs/
│   │       │   └── paylogic-ca.pem
│   │       ├── 📁 properties/
│   │       │   ├── application.properties                            # Propriétés communes
│   │       │   ├── application-dev.properties
│   │       │   ├── application-prod.properties
│   │       │   └── application-test.properties
│   │       ├── log4j2.xml                                            # Logging (pas logback)
│   │       └── 📁 messages/
│   │           ├── messages.properties
│   │           ├── messages_fr.properties
│   │           └── messages_en.properties
│   │
│   └── 📁 test/
│       ├── 📁 java/
│       │   └── 📁 com/paylogic/paywalletlite/
│       │       ├── 📁 unit/
│       │       │   ├── service/
│       │       │   ├── crypto/
│       │       │   └── util/
│       │       ├── 📁 integration/
│       │       │   ├── repository/
│       │       │   ├── kafka/
│       │       │   └── controller/
│       │       ├── 📁 e2e/
│       │       │   └── OfflinePaymentFlowTest.java
│       │       └── 📁 fixtures/
│       │           ├── TestDataFactory.java
│       │           └── MockTokenGenerator.java
│       └── 📁 resources/
│           ├── test-context.xml
│           ├── test-security-context.xml
│           └── 📁 sql/
│               ├── init-test-data.sql
│               └── cleanup.sql
│
├── 📁 webapp/                                                          # Ressources web (Spring MVC)
│   ├── 📁 WEB-INF/
│   │   └── web.xml                                                     # Fallback si pas d'initializer
│   └── 📁 static/
│       └── 📁 docs/
│           └── api-docs.html
│
├── 📁 docs/
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
│   │   ├── cd-deploy.yml
│   │   └── security-scan.yml
│   └── 📁 pull_request_template.md
│
├── .gitignore
├── README.md
├── LICENSE
├── pom.xml                                                             # Maven parent
└── docker-compose.yml                                                  # Stack local (Oracle XE, Kafka, Zookeeper)


paywallet-lite-mobile/
├── 📁 android/                                    # Configuration Android native
│   ├── 📁 app/
│   │   ├── 📁 src/
│   │   │   ├── 📁 main/
│   │   │   │   ├── 📁 kotlin/com/paylogic/paywalletlite/    # Kotlin (recommandé vs Java)
│   │   │   │   │   ├── MainActivity.kt
│   │   │   │   │   └── 📁 plugins/
│   │   │   │   │       ├── SecureStoragePlugin.kt           # Android Keystore integration
│   │   │   │   │       ├── NfcHcePlugin.kt                  # NFC Host Card Emulation
│   │   │   │   │       └── QrCodePlugin.kt                  # Camera/QR native optimizations
│   │   │   │   ├── 📁 res/
│   │   │   │   │   ├── 📁 xml/
│   │   │   │   │   │   └── network_security_config.xml      # Cert pinning config
│   │   │   │   │   └── 📁 values/
│   │   │   │   │       └── strings.xml
│   │   │   │   └── AndroidManifest.xml
│   │   │   └── 📁 profile/
│   │   │       └── AndroidManifest.xml
│   │   ├── build.gradle
│   │   └── 📁 proguard-rules.pro
│   ├── 📁 gradle/wrapper/
│   ├── build.gradle
│   └── settings.gradle
│
├── 📁 ios/                                        # Configuration iOS native
│   ├── 📁 Runner/
│   │   ├── AppDelegate.swift
│   │   ├── Runner-Bridging-Header.h
│   │   ├── Info.plist
│   │   └── 📁 Plugins/
│   │       ├── SecureStoragePlugin.swift          # iOS Keychain/Secure Enclave
│   │       └── QrCodePlugin.swift
│   ├── 📁 Runner.xcworkspace/
│   ├── 📁 Podfile
│   └── 📁 Flutter/
│
├── 📁 lib/                                        # CODE FLUTTER PRINCIPAL
│   │
│   ├── main.dart                                  # Point d'entrée + initialisation
│   │
│   ├── 📁 app/                                    # Configuration globale de l'app
│   │   ├── 📁 config/
│   │   │   ├── app_config.dart                    # Environnements (dev/staging/prod)
│   │   │   ├── api_config.dart                    # URLs backend, timeouts, retries
│   │   │   ├── crypto_config.dart                 # Algorithmes, key sizes, rotation
│   │   │   └── offline_config.dart                # Limites offline, TTL tokens, sync intervals
│   │   ├── 📁 constants/
│   │   │   ├── api_constants.dart                 # Endpoints, versions, headers
│   │   │   ├── storage_constants.dart             # Keys SharedPreferences/SQLCipher
│   │   │   ├── crypto_constants.dart              # Curves, algorithms, key aliases
│   │   │   ├── ui_constants.dart                  # Durations, animations, breakpoints
│   │   │   └── error_constants.dart               # Codes erreur métier
│   │   ├── 📁 theme/
│   │   │   ├── app_theme.dart                     # ThemeData global
│   │   │   ├── app_colors.dart                    # Palette PayLogic
│   │   │   ├── app_typography.dart                # TextStyles
│   │   │   ├── app_dimensions.dart                # Spacing, radius, elevations
│   │   │   └── app_icons.dart                     # Iconography
│   │   └── 📁 localization/
│   │       ├── app_localizations.dart             # Delegate + setup
│   │       ├── intl_fr.arb                        # Français (marchés cibles)
│   │       ├── intl_en.arb                        # Anglais
│   │       └── intl_ar.arb                        # Arabe (Maroc, Moyen-Orient)
│   │
│   ├── 📁 core/                                   # FONDATION TECHNIQUE
│   │   ├── 📁 errors/
│   │   │   ├── app_exception.dart                 # Classe de base
│   │   │   ├── network_exceptions.dart            # NoConnection, Timeout, ServerError
│   │   │   ├── auth_exceptions.dart               # InvalidPin, SessionExpired, BiometricFailed
│   │   │   ├── crypto_exceptions.dart             # SignatureInvalid, KeyCorrupted, TokenExpired
│   │   │   ├── sync_exceptions.dart               # SyncConflict, SyncRejected, CheckpointLost
│   │   │   ├── validation_exceptions.dart         # InsufficientFunds, LimitExceeded
│   │   │   └── error_handler.dart                 # Mapping exception → message utilisateur
│   │   ├── 📁 utils/
│   │   │   ├── date_utils.dart
│   │   │   ├── number_utils.dart                  # Formatage monétaire XOF/MAD
│   │   │   ├── string_utils.dart
│   │   │   ├── uuid_utils.dart
│   │   │   ├── device_utils.dart                  # Info device, OS version
│   │   │   └── connectivity_utils.dart            # État réseau détaillé
│   │   ├── 📁 extensions/
│   │   │   ├── string_extensions.dart
│   │   │   ├── date_extensions.dart
│   │   │   ├── context_extensions.dart
│   │   │   └── either_extensions.dart             # Functional programming
│   │   └── 📁 functional/
│   │       ├── either.dart                        # Result type: Left(erreur)/Right(succès)
│   │       ├── option.dart                        # Some/None pattern
│   │       └── failure.dart                       # Classe abstraite Failure
│   │
│   ├── 📁 data/                                   # COUCHE DONNÉES (Clean Architecture)
│   │   ├── 📁 models/                             # Modèles JSON + Entités SQLCipher
│   │   │   ├── 📁 local/                          # Entités SQLCipher (tables)
│   │   │   │   ├── local_user_model.dart          # @JsonSerializable + drift
│   │   │   │   ├── local_wallet_model.dart
│   │   │   │   ├── local_token_model.dart
│   │   │   │   ├── local_transaction_model.dart
│   │   │   │   ├── local_sync_queue_model.dart
│   │   │   │   ├── local_key_store_model.dart
│   │   │   │   ├── local_audit_log_model.dart
│   │   │   │   └── local_device_sync_state.dart
│   │   │   ├── 📁 remote/                         # DTOs API (JSON ↔ Dart)
│   │   │   │   ├── login_request_dto.dart
│   │   │   │   ├── login_response_dto.dart
│   │   │   │   ├── token_allocation_request_dto.dart
│   │   │   │   ├── token_allocation_response_dto.dart
│   │   │   │   ├── offline_payment_request_dto.dart
│   │   │   │   ├── sync_request_dto.dart
│   │   │   │   ├── sync_response_dto.dart
│   │   │   │   ├── transaction_dto.dart
│   │   │   │   ├── wallet_balance_dto.dart
│   │   │   │   └── api_error_dto.dart
│   │   │   └── 📁 mappers/                        # Conversion local ↔ remote ↔ domain
│   │   │       ├── user_mapper.dart
│   │   │       ├── wallet_mapper.dart
│   │   │       ├── token_mapper.dart
│   │   │       └── transaction_mapper.dart
│   │   │
│   │   ├── 📁 datasources/                        # Sources de données
│   │   │   ├── 📁 local/
│   │   │   │   ├── 📁 database/
│   │   │   │   │   ├── sqlcipher_database.dart    # Singleton SQLCipher
│   │   │   │   │   ├── database_tables.dart       # Définition schéma
│   │   │   │   │   ├── database_migrations.dart   # Migration scripts
│   │   │   │   │   └── database_helper.dart       # CRUD génériques
│   │   │   │   ├── 📁 secure_storage/
│   │   │   │   │   ├── secure_storage_service.dart          # flutter_secure_storage
│   │   │   │   │   ├── keystore_service_android.dart          # Android Keystore
│   │   │   │   │   ├── secure_enclave_service_ios.dart      # iOS Secure Enclave
│   │   │   │   │   └── keychain_service_ios.dart             # iOS Keychain fallback
│   │   │   │   ├── 📁 shared_prefs/
│   │   │   │   │   └── shared_prefs_service.dart            # Settings non-sensibles
│   │   │   │   └── 📁 file_storage/
│   │   │   │       └── encrypted_file_storage.dart            # Fichiers chiffrés
│   │   │   └── 📁 remote/
│   │   │       ├── 📁 api/
│   │   │       │   ├── api_client.dart            # Dio/Http configuré
│   │   │       │   ├── api_interceptor.dart         # JWT refresh, retry, logging
│   │   │       │   ├── api_endpoints.dart           # Centralisation URLs
│   │   │       │   └── api_error_handler.dart       # Traitement erreurs HTTP
│   │   │       ├── 📁 network/
│   │   │       │   ├── network_info.dart            # Connectivity plus
│   │   │       │   ├── network_interceptor.dart     # Offline queue injection
│   │   │       │   └── certificate_pinning.dart     # SSL pinning
│   │   │       └── 📁 websocket/
│   │   │           └── sync_socket.dart             # WebSocket pour sync temps réel
│   │   │
│   │   └── 📁 repositories/                       # Implémentations repositories
│   │       ├── auth_repository_impl.dart
│   │       ├── wallet_repository_impl.dart
│   │       ├── token_repository_impl.dart
│   │       ├── transaction_repository_impl.dart
│   │       ├── sync_repository_impl.dart
│   │       └── audit_repository_impl.dart
│   │
│   ├── 📁 domain/                                 # COUCHE MÉTIER (indépendante)
│   │   ├── 📁 entities/                           # Entités métier pures
│   │   │   ├── user.dart
│   │   │   ├── wallet.dart
│   │   │   ├── token.dart
│   │   │   ├── transaction.dart
│   │   │   ├── device.dart
│   │   │   └── sync_status.dart
│   │   ├── 📁 repositories/                       # Interfaces (contrats)
│   │   │   ├── auth_repository.dart
│   │   │   ├── wallet_repository.dart
│   │   │   ├── token_repository.dart
│   │   │   ├── transaction_repository.dart
│   │   │   ├── sync_repository.dart
│   │   │   └── audit_repository.dart
│   │   └── 📁 usecases/                           # Cas d'utilisation métier
│   │       ├── 📁 auth/
│   │       │   ├── login_usecase.dart
│   │       │   ├── logout_usecase.dart
│   │       │   ├── verify_pin_usecase.dart
│   │       │   ├── set_biometric_usecase.dart
│   │       │   └── refresh_session_usecase.dart
│   │       ├── 📁 wallet/
│   │       │   ├── get_balance_usecase.dart
│   │       │   ├── get_offline_balance_usecase.dart
│   │       │   ├── allocate_tokens_usecase.dart
│   │       │   ├── request_credit_usecase.dart
│   │       │   └── sync_wallet_usecase.dart
│   │       ├── 📁 token/
│   │       │   ├── get_available_tokens_usecase.dart
│   │       │   ├── transfer_token_usecase.dart
│   │       │   ├── receive_token_usecase.dart
│   │       │   ├── validate_token_usecase.dart
│   │       │   └── select_optimal_tokens_usecase.dart   # Algorithme sélection
│   │       ├── 📁 transaction/
│   │       │   ├── get_transaction_history_usecase.dart
│   │       │   ├── execute_offline_payment_usecase.dart
│   │       │   ├── execute_online_payment_usecase.dart
│   │       │   └── refund_overpayment_usecase.dart
│   │       ├── 📁 sync/
│   │       │   ├── check_sync_status_usecase.dart
│   │       │   ├── perform_sync_usecase.dart
│   │       │   ├── resolve_sync_conflict_usecase.dart
│   │       │   └── retry_failed_sync_usecase.dart
│   │       └── 📁 offline/
│   │           ├── enter_offline_mode_usecase.dart
│   │           ├── exit_offline_mode_usecase.dart
│   │           ├── generate_payment_qr_usecase.dart
│   │           ├── scan_payment_qr_usecase.dart
│   │           ├── generate_nfc_payload_usecase.dart
│   │           └── receive_nfc_payment_usecase.dart
│   │
│   ├── 📁 presentation/                           # COUCHE PRÉSENTATION
│   │   ├── 📁 blocs/                              # State Management (BLoC)
│   │   │   ├── 📁 auth/
│   │   │   │   ├── auth_bloc.dart
│   │   │   │   ├── auth_event.dart
│   │   │   │   ├── auth_state.dart
│   │   │   │   └── auth_bloc_observer.dart
│   │   │   ├── 📁 wallet/
│   │   │   │   ├── wallet_bloc.dart
│   │   │   │   ├── wallet_event.dart
│   │   │   │   └── wallet_state.dart
│   │   │   ├── 📁 token/
│   │   │   │   ├── token_bloc.dart
│   │   │   │   ├── token_event.dart
│   │   │   │   └── token_state.dart
│   │   │   ├── 📁 transaction/
│   │   │   │   ├── transaction_bloc.dart
│   │   │   │   ├── transaction_event.dart
│   │   │   │   └── transaction_state.dart
│   │   │   ├── 📁 sync/
│   │   │   │   ├── sync_bloc.dart
│   │   │   │   ├── sync_event.dart
│   │   │   │   └── sync_state.dart
│   │   │   ├── 📁 offline/
│   │   │   │   ├── offline_bloc.dart
│   │   │   │   ├── offline_event.dart
│   │   │   │   └── offline_state.dart
│   │   │   └── 📁 global/
│   │   │       ├── connectivity_bloc.dart         # État réseau global
│   │   │       ├── app_lifecycle_bloc.dart        # Foreground/background
│   │   │       └── error_bloc.dart                # Gestion erreurs globales
│   │   │
│   │   ├── 📁 pages/                              # Écrans (Screens)
│   │   │   ├── 📁 auth/
│   │   │   │   ├── splash_page.dart
│   │   │   │   ├── onboarding_page.dart
│   │   │   │   ├── login_page.dart
│   │   │   │   ├── pin_setup_page.dart
│   │   │   │   ├── biometric_setup_page.dart
│   │   │   │   └── forgot_pin_page.dart
│   │   │   ├── 📁 home/
│   │   │   │   ├── home_page.dart
│   │   │   │   ├── dashboard_page.dart
│   │   │   │   └── quick_actions_page.dart
│   │   │   ├── 📁 wallet/
│   │   │   │   ├── wallet_page.dart
│   │   │   │   ├── balance_detail_page.dart
│   │   │   │   ├── allocate_tokens_page.dart
│   │   │   │   └── credit_request_page.dart
│   │   │   ├── 📁 offline_payment/
│   │   │   │   ├── offline_payment_page.dart
│   │   │   │   ├── qr_display_page.dart           # Afficher QR (payer)
│   │   │   │   ├── qr_scan_page.dart              # Scanner QR (recevoir)
│   │   │   │   ├── nfc_payment_page.dart
│   │   │   │   ├── payment_confirmation_page.dart
│   │   │   │   └── payment_receipt_page.dart
│   │   │   ├── 📁 transaction/
│   │   │   │   ├── transaction_history_page.dart
│   │   │   │   ├── transaction_detail_page.dart
│   │   │   │   └── pending_transactions_page.dart
│   │   │   ├── 📁 sync/
│   │   │   │   ├── sync_status_page.dart
│   │   │   │   ├── sync_progress_page.dart
│   │   │   │   └── sync_conflict_resolution_page.dart
│   │   │   ├── 📁 settings/
│   │   │   │   ├── settings_page.dart
│   │   │   │   ├── security_settings_page.dart
│   │   │   │   ├── notification_settings_page.dart
│   │   │   │   ├── language_settings_page.dart
│   │   │   │   └── about_page.dart
│   │   │   └── 📁 support/
│   │   │       ├── help_center_page.dart
│   │   │       └── contact_support_page.dart
│   │   │
│   │   ├── 📁 widgets/                            # Composants réutilisables
│   │   │   ├── 📁 common/
│   │   │   │   ├── pay_button.dart
│   │   │   │   ├── pay_text_field.dart
│   │   │   │   ├── pay_card.dart
│   │   │   │   ├── pay_loading_indicator.dart
│   │   │   │   ├── pay_error_widget.dart
│   │   │   │   ├── pay_empty_state.dart
│   │   │   │   └── pay_bottom_sheet.dart
│   │   │   ├── 📁 auth/
│   │   │   │   ├── pin_pad.dart
│   │   │   │   ├── biometric_prompt.dart
│   │   │   │   └── session_timeout_dialog.dart
│   │   │   ├── 📁 wallet/
│   │   │   │   ├── balance_card.dart
│   │   │   │   ├── token_chip.dart
│   │   │   │   ├── offline_balance_indicator.dart
│   │   │   │   └── credit_limit_badge.dart
│   │   │   ├── 📁 payment/
│   │   │   │   ├── qr_display_widget.dart
│   │   │   │   ├── qr_scanner_overlay.dart
│   │   │   │   ├── nfc_animation_widget.dart
│   │   │   │   ├── amount_input_field.dart
│   │   │   │   ├── payment_summary_card.dart
│   │   │   │   └── transaction_success_animation.dart
│   │   │   ├── 📁 sync/
│   │   │   │   ├── sync_status_badge.dart
│   │   │   │   ├── sync_progress_bar.dart
│   │   │   │   ├── pending_sync_counter.dart
│   │   │   │   └── last_sync_info.dart
│   │   │   └── 📁 offline/
│   │   │       ├── offline_mode_banner.dart
│   │   │       ├── offline_indicator.dart
│   │   │       └── airplane_mode_hint.dart
│   │   │
│   │   └── 📁 navigation/                         # Routage
│   │       ├── app_router.dart                    # GoRouter configuration
│   │       ├── route_names.dart                   # Constantes routes
│   │       ├── route_guards.dart                  # Auth guards, offline guards
│   │       └── navigation_service.dart            # Navigation programmatique
│   │
│   ├── 📁 services/                               # SERVICES MÉTIER
│   │   ├── 📁 crypto/
│   │   │   ├── local_crypto_service.dart          # Chiffrement local AES-256-GCM
│   │   │   ├── signature_service.dart             # ECDSA P-256 sign/verify
│   │   │   ├── key_management_service.dart        # Import/usage clés server
│   │   │   ├── token_verification_service.dart    # Vérifier signature token
│   │   │   └── secure_random_service.dart         # Génération nonces
│   │   ├── 📁 offline/
│   │   │   ├── offline_manager.dart               # Orchestration mode offline
│   │   │   ├── connectivity_monitor.dart          # Stream état réseau
│   │   │   ├── offline_queue_processor.dart       # Traiter file offline
│   │   │   └── offline_state_persistence.dart     # Sauvegarder état offline
│   │   ├── 📁 nfc/
│   │   │   ├── nfc_service.dart                   # Interface abstraite
│   │   │   ├── nfc_android_service.dart             # Implémentation Android
│   │   │   ├── nfc_ios_service.dart                 # Implémentation iOS (limitée)
│   │   │   └── nfc_payload_builder.dart             # Construire payload NFC
│   │   ├── 📁 qr/
│   │   │   ├── qr_generator_service.dart            # Générer QR dynamique
│   │   │   ├── qr_scanner_service.dart              # Scanner + validation
│   │   │   ├── qr_payload_encoder.dart              # Encoder données QR
│   │   │   └── qr_payload_decoder.dart              # Décoder + vérifier
│   │   ├── 📁 sync/
│   │   │   ├── sync_manager.dart                    # Orchestrateur sync
│   │   │   ├── sync_scheduler.dart                  # Planification sync
│   │   │   ├── sync_executor.dart                   # Exécution sync batch
│   │   │   ├── conflict_resolver.dart               # Résolution conflits
│   │   │   └── sync_retry_policy.dart               # Stratégie retry exponentiel
│   │   ├── 📁 notification/
│   │   │   ├── local_notification_service.dart      # Notifications locales
│   │   │   └── push_notification_service.dart     # FCM/APNs
│   │   └── 📁 background/
│   │       ├── background_sync_service.dart         # WorkManager/BG Fetch
│   │       └── background_crypto_service.dart       # Opérations crypto background
│   │
│   └── 📁 di/                                     # INJECTION DÉPENDANCES
│       ├── injection.dart                         # GetIt setup
│       ├── modules/
│       │   ├── api_module.dart                    # Enregistrement API
│       │   ├── database_module.dart               # SQLCipher singleton
│       │   ├── crypto_module.dart                 # Services crypto
│       │   ├── repository_module.dart             # Repositories
│       │   ├── usecase_module.dart                # Use cases
│       │   └── bloc_module.dart                   # BLoCs
│       └── scopes/
│           ├── auth_scope.dart
│           └── offline_scope.dart
│
├── 📁 test/                                       # TESTS
│   ├── 📁 unit/
│   │   ├── 📁 domain/
│   │   │   ├── usecases/
│   │   │   └── entities/
│   │   ├── 📁 data/
│   │   │   ├── repositories/
│   │   │   └── mappers/
│   │   └── 📁 services/
│   │       ├── crypto/
│   │       └── sync/
│   ├── 📁 widget/
│   │   ├── 📁 pages/
│   │   └── 📁 widgets/
│   ├── 📁 integration/
│   │   ├── 📁 flows/
│   │   │   ├── offline_payment_flow_test.dart
│   │   │   ├── sync_flow_test.dart
│   │   │   └── auth_flow_test.dart
│   │   └── 📁 database/
│   │       └── sqlcipher_test.dart
│   ├── 📁 mocks/
│   │   ├── mock_repositories.dart
│   │   ├── mock_services.dart
│   │   └── fake_data.dart
│   └── 📁 fixtures/
│       ├── tokens_fixture.json
│       ├── transactions_fixture.json
│       └── api_responses_fixture.json
│
├── 📁 assets/                                     # RESSOURCES
│   ├── 📁 images/
│   │   ├── logo_paylogic.png
│   │   ├── logo_paywallet_lite.png
│   │   ├── illustration_offline.png
│   │   ├── illustration_sync.png
│   │   └── icons/
│   ├── 📁 animations/
│   │   ├── payment_success.json                   # Lottie
│   │   ├── sync_complete.json
│   │   └── nfc_wave.json
│   ├── 📁 fonts/
│   │   ├── Inter-Regular.ttf
│   │   └── Inter-Bold.ttf
│   └── 📁 certificates/
│       └── paylogic_ca.pem                        # Certificat CA embarqué
│
├── pubspec.yaml
├── analysis_options.yaml
└── README_MOBILE.md
