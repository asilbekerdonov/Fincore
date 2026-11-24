# FinCore — Banking & Money Transfer Service

Backend-сервис для управления банковскими счетами и переводами средств. Проект развивается поэтапно: от Spring Boot монолита до продакшен-системы с PostgreSQL, Redis, безопасностью, Docker и последующим разделением на микросервисы.

## Стек технологий

* **Язык:** Java 21
* **Фреймворк:** Spring Boot, Spring Data JPA, Spring Security (планируется)
* **Базы данных:** H2 (dev), PostgreSQL (prod), Redis (кэширование)
* **Миграции и тесты:** Flyway / Liquibase, JUnit 5, Mockito, Testcontainers
* **Инфраструктура:** Maven, Docker, Docker Compose, GitHub Actions

---

## Roadmap проекта

### Этап 1. Базовая настройка и Spring Boot Core
- [x] Инициализация проекта Spring Boot (Maven, Java 21)
- [x] Настройка Git и публикация репозитория на GitHub
- [ ] Конфигурация через `application.yml` и профили (`dev` / `prod`)
- [ ] Типобезопасные настройки через `@ConfigurationProperties` и валидацию
- [ ] Условная автоконфигурация (`@ConditionalOnProperty`, `@ConditionalOnMissingBean`)
- [ ] Мониторинг через Spring Boot Actuator и кастомный `HealthIndicator`

### Этап 2. REST API, транзакции и безопасность
- [ ] Доменная модель (`Account`, `Transfer`, `AuditLog`) и переводы внутри `@Transactional`
- [ ] Пакетные переводы (`transferAll`) с атомарным откатом и аудитом (`REQUIRES_NEW`)
- [ ] REST API контроллеры, DTO, MapStruct и версионирование (`/api/v1/...`)
- [ ] Валидация запросов (`@Valid`) и глобальная обработка ошибок (`@ControllerAdvice`, `ProblemDetail`)
- [ ] Документация API через OpenAPI / Swagger
- [ ] Пагинация истории переводов и кастомные `@Query` в Spring Data JPA
- [ ] Аутентификация и авторизация: Spring Security + JWT (Access / Refresh токены, роли)
- [ ] Тестирование: Unit-тесты (JUnit 5, Mockito) и интеграционные тесты (Testcontainers)

### Этап 3. PostgreSQL и оптимизация JPA / Hibernate
- [ ] Переход на PostgreSQL и настройка миграций (Flyway / Liquibase)
- [ ] Индексы и анализ производительности запросов (`EXPLAIN ANALYZE`)
- [ ] Защита от параллельных списаний (Optimistic `@Version` / Pessimistic `FOR UPDATE`)
- [ ] Устранение проблемы N+1 (`JOIN FETCH`, `@EntityGraph`)

### Этап 4. Архитектура, DevOps и деплой
- [ ] Упаковка приложения в `Dockerfile` (multi-stage build) и `docker-compose.yml`
- [ ] Кэширование и Rate Limiting через Redis
- [ ] Настройка CI/CD pipeline через GitHub Actions
- [ ] Деплой работающего сервиса на сервер (VPS)

### Этап 5. Переход к микросервисам
- [ ] Разделение монолита на сервисы (`auth`, `account`, `transfer`, `notification`)
- [ ] API Gateway и Service Discovery
- [ ] Асинхронное взаимодействие через Apache Kafka (Outbox pattern / Saga)
- [ ] Отказоустойчивость (Resilience4j: Circuit Breaker, Retry, Timeout)
- [ ] Observability: логирование, метрики (Prometheus + Grafana), трассировка

---

## Локальный запуск

```bash
git clone [https://github.com/asilbekerdonov/Fincore.git](https://github.com/asilbekerdonov/Fincore.git)
cd Fincore
./mvnw spring-boot:run