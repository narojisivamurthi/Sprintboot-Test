# 🌐 Centralized Microservices Configuration Repository (`config-repo`)

This repository serves as the single source of truth for all environment configurations, inter-service base URLs (`NOTIFICATION_SERVICE_BASE_URL`, `AUTH_SERVICE_BASE_URL`, etc.), and Kubernetes ConfigMaps.

---

## 📁 Repository Structure

```
config-repo/
├── application.yml                    # Global shared defaults across all microservices
├── api-gateway.yml                    # Gateway routing, circuit breakers, CORS
├── auth-service.yml                   # Auth tokens, JWT secrets, database connection
├── employee-service.yml               # Employee DB, Notification Service endpoints
├── notification-service.yml           # Mail server (Mailtrap/Gmail), SMTP credentials
│
└── environments/
    ├── dev/
    │   └── configurationmap-dev.yaml  # Kubernetes ConfigMap for Development cluster
    └── prod/
        └── configurationmap-prod.yaml # Kubernetes ConfigMap for Production cluster
```

---

## 🔄 How Services Consume These Values

### 1. In Local Development (via `.env` or defaults)
Each microservice reads from environment variables with sensible local fallbacks:
```properties
external.services.notification.base-url=${NOTIFICATION_SERVICE_BASE_URL:http://localhost:8083}
external.services.notification.send-welcome-email=${NOTIFICATION_SERVICE_SEND_WELCOME_EMAIL:/api/notifications/welcome}
```

### 2. In Kubernetes / GitOps Deployments (via `configurationmap.yaml`)
In Kubernetes, the ConfigMap is applied in the target namespace and mounted directly as environment variables in the pod spec:

```yaml
spec:
  containers:
    - name: employee-service
      image: employee-service:latest
      envFrom:
        - configMapRef:
            name: microservices-configmap
```

When the pod starts, Spring Boot's `@ConfigurationProperties(prefix = "external.services.notification")` automatically maps `NOTIFICATION_SERVICE_BASE_URL` to:
```java
apiProperties.getBaseUrl(); // In DEV: http://notification-service.development.svc.cluster.local:8083
```

---

## 🏷️ Standardized Key Reference

| Configuration Key | Local Default | DEV (K8s) | PROD (K8s/Cloud) |
| :--- | :--- | :--- | :--- |
| `GATEWAY_BASE_URL` | `http://localhost:8080` | `http://api-gateway.development.svc.cluster.local:8080` | `https://api.company.com` |
| `AUTH_SERVICE_BASE_URL` | `http://localhost:8081` | `http://auth-service.development.svc.cluster.local:8081` | `http://auth-service.production.svc.cluster.local:8081` |
| `EMPLOYEE_SERVICE_BASE_URL` | `http://localhost:8082` | `http://employee-service.development.svc.cluster.local:8082` | `http://employee-service.production.svc.cluster.local:8082` |
| `NOTIFICATION_SERVICE_BASE_URL` | `http://localhost:8083` | `http://notification-service.development.svc.cluster.local:8083` | `http://notification-service.production.svc.cluster.local:8083` |
