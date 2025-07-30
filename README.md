# Инструкция по запуску

## Запуск системы

1. **Клонировать репозиторий**

```bash
git clone https://github.com/velshletter/KrainetTA.git
cd KrainetTA
```

2. **Создать `.env` файл**

```bash
cp .env-example .env
```

Заполнить переменные в `.env`:

```env
DB_USERNAME=db_user
DB_PASSWORD=db_password
JWT_SECRET=your_secure_secret

MAIL_USERNAME=example@gmail.com
MAIL_PASSWORD=email_app_password
```

3. **Собрать JAR-файлы (если не используются заранее собранные)**

```bash

mvn clean package -DskipTests

```

4. **Запустить систему**

```bash
docker-compose up --build
```

**Тестовые данные:**
(пароль для админа: admin, пароль для юзера: user)
```json
[
  {
    "id": "11111111-1111-1111-1111-111111111111",
    "username": "admin",
    "password": "$2a$12$3ousmAokL7cagKqtP3lcGOfRWTWhGS3px/LiSSp779duA/vWGZJH2",
    "email": "admin@example.com",
    "first_name": "Admin",
    "last_name": "User",
    "role": "ADMIN"
  },
  {
    "id": "22222222-2222-2222-2222-222222222222",
    "username": "john_doe",
    "password": "$2a$12$SK20UxrEQsGAKuCvDOrTzu9YKXtuJQLzGeFCROfmXh.N9o09Ktxii",
    "email": "user@example.com",
    "first_name": "John",
    "last_name": "Doe",
    "role": "USER"
  }
]
```
