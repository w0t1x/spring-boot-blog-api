# Blog API — учебный проект на Spring Boot и Spring Data JPA

Учебный REST API-проект «мини-блог», написанный на **Spring Boot 3** и **Spring Data JPA**.

Проект демонстрирует:

* работу с сущностями: `User`, `Post`, `Tag`, `Comment`;
* использование **Spring Data JPA** репозиториев и своих `*DbStorage`-обёрток;
* построение запросов: derived queries, `@Query` (JPQL), агрегации и проекции;
* разделение **Entity** и **DTO** (входные и выходные модели);
* пагинацию и сортировку через `Pageable` / `Page`;
* базовую архитектуру:
  `Controller → Service → DbStorage → Repository → Entity`.

Используется встроенная база **H2**, проект можно запустить и сразу тестировать.

---

## Стек технологий

* **Java 17+**
* **Spring Boot 3**
* **Spring Web** (REST API)
* **Spring Data JPA**
* **H2 Database** (in-memory / file, для разработки)
* **Maven**

---

## Доменные сущности

* `User` — пользователь / автор постов
* `Post` — пост в блоге
* `Tag` — тег (связь «многие-ко-многим» с постами)
* `Comment` — комментарий к посту

В проекте используются связи:

* `OneToMany` / `ManyToOne` (посты пользователя, комментарии поста),
* `ManyToMany` (посты ↔ теги).

---

## Основная функциональность

### Работа с постами

* Создание поста с полями:

  * `authorId` — id автора;
  * `title` — заголовок;
  * `content` — текст;
  * `tagNames` — список имён тегов (строки, например: `["#spring", "#jpa"]`).

При создании поста:

* автор ищется по `authorId`;
* теги ищутся по имени, если не найдены — создаются;
* пост сохраняется и связывается с автором и тегами.

### Получение постов

Реализованы выборки:

* все посты конкретного автора;
* **последние 3 поста** автора (сортивка по дате создания);
* поиск постов по части заголовка (регистронезависимо: `ContainingIgnoreCase`);
* посты по имени тега;
* посты по email автора;
* посты за период по дате создания (`createdAt between from/to`);
* посты без комментариев.

### Статистика по тегам

* Статистика использования тегов:

  * имя тега;
  * количество постов с этим тегом.

Реализовано через `@Query` с `JOIN` и `GROUP BY` и проекцию (`TagUsageProjection` / DTO).

### Удаление постов

* Удаление поста по id;
* удаление комментариев поста — в соответствии с настройками связей и каскадов.

---

## Архитектура проекта

Проект разделён на слои:

* `controller` — REST-контроллеры

  * `BlogController` — точки входа для операций с постами, тегами и т.п.

* `service` — бизнес-логика

  * `BlogService` — создание постов, работа с тегами, выборки постов и статистика.

* `storage.*DbStorage` — обёртки над Spring Data репозиториями

  * инкапсулируют работу с `JpaRepository`, служат прослойкой между сервисом и репозиторием.

* `storage.*Storage` — интерфейсы `JpaRepository<Entity, Long>`

  * `UserStorage`, `PostStorage`, `TagStorage`, `CommentStorage` и др.

* `model` — JPA-сущности

  * `User`, `Post`, `Tag`, `Comment` и их связи.

* `dto` — DTO-классы для запросов/ответов

  * `CreatePostRequestDTO`, `PostResponseDTO`, `AuthorDto`, `TagDto` и др.

* `mapper` — маппинг Entity → DTO

  * `PostMapper` — преобразование `Post` в `PostResponseDTO` (автор, теги, количество комментариев и пр.).

Такой подход позволяет:

* не отдавать JPA-сущности напрямую из API;
* держать бизнес-логику в сервисах, а не в контроллерах;
* удобно расширять слой доступа к данным и DTO.

---

## DTO-модели

### Входящий DTO: создание поста

`CreatePostRequestDTO`:

```json
{
  "authorId": 4,
  "title": "Мой первый пост",
  "content": "Текст поста про Spring Data JPA",
  "tagNames": ["#spring", "#jpa"]
}
```

### Исходящий DTO: пост

`PostResponseDTO` (пример структуры ответа):

```json
{
  "id": 1,
  "title": "Мой первый пост",
  "content": "Текст поста про Spring Data JPA",
  "createdAt": "2025-11-15T18:35:12.345",
  "author": {
    "id": 4,
    "name": "anton",
    "email": "anton@example.com"
  },
  "tags": [
    { "id": 1, "name": "#spring" },
    { "id": 2, "name": "#jpa" }
  ],
  "commentsCount": 0
}
```

Дополнительно используются:

* `AuthorDto` — вложенный DTO для автора поста;
* `TagDto` — вложенный DTO для тегов;
* проекция / DTO для статистики по тегам (`tagName`, `postCount`).

---

## Пагинация и сортировка

Для выборок используется стандартный механизм Spring Data:

* в контроллере принимается `Pageable`,
* сервис возвращает `Page<PostResponseDTO>`.

Примеры использования:

* `GET /blog/posts?page=0&size=5&sort=createdAt,desc`
  первая страница, 5 постов на страницу, сортировка по `createdAt` по убыванию;

* `GET /blog/posts/author/4?page=1&size=10`
  посты автора с id `4`, вторая страница по 10 постов;

* `GET /blog/posts/search?text=java&page=0&size=3&sort=title,asc`
  поиск по заголовку (`text=java`) с пагинацией и сортировкой по `title`.

Для установки дефолтной сортировки используется:

```java
@PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC)
Pageable pageable
```

---

### 1. Derived queries (по имени метода)

Примеры метод-имен в репозиториях:

* `findByAuthorId(Long authorId)`
* `findTop3ByAuthorIdOrderByCreatedAtDesc(Long authorId)`
* `findByTitleContainingIgnoreCase(String text)`
* `findByTagsName(String tagName)`
* `findByAuthorEmail(String email)`
* `findPostsBetween(LocalDateTime from, LocalDateTime to)`
* `findPostsWithoutComments()`

Spring Data JPA генерирует соответствующие SQL/JPQL автоматически.

### 2. Явные `@Query` (JPQL)

Для более сложных запросов:

* статистика по тегам:

```java
@Query("""
       select t.name as tagName, count(p) as postCount
       from Post p
       join p.tags t
       group by t.name
       order by postCount desc
       """)
List<TagUsageProjection> findTagUsageStats();
```

* запросы с `JOIN`, `GROUP BY`, фильтрацией по датам и др.

### 3. Проекции

Для выдачи только нужных полей без загрузки всей сущности используются **проекции**:

Пример интерфейсной проекции для статистики по тегам:

```java
public interface TagUsageProjection {
    String getTagName();
    long getPostCount();
}
```

Вместо полной сущности `Post`/`Tag` на выходе получаются «тонкие» объекты с двумя полями, что экономит трафик и ресурсы.

---

## Примеры запросов

### Создание поста

```http
POST /blog
Content-Type: application/json
```

Тело запроса:

```json
{
  "authorId": 4,
  "title": "Мой первый пост",
  "content": "Текст поста про Spring Data JPA",
  "tagNames": ["#spring", "#jpa"]
}
```

Пример ответа — см. выше `PostResponseDTO`.

---

### Получение постов автора (с пагинацией)

```http
GET /blog/posts/author/4?page=0&size=5&sort=createdAt,desc
```

Ответ:

* JSON-структура `Page<PostResponseDTO>`:

  * поле `content` — список постов;
  * поля `totalElements`, `totalPages`, `number`, `size` и др. — метаданные страницы.

---

### Получение постов по тегу

```http
GET /blog/posts/by-tag?tagName=%23spring
```

Возвращает список `PostResponseDTO` с указанным тегом.

---

### Статистика по тегам

```http
GET /blog/tags/stats
```

(путь условный, зависит от реализации контроллера)

Пример ответа:

```json
[
  { "tagName": "#spring", "postCount": 10 },
  { "tagName": "#jpa", "postCount": 7 }
]
```

---

## Как запустить проект

1. Убедитесь, что у вас установлены:

   * **Java 17+**
   * **Maven**

2. Клонируйте репозиторий:

   ```bash
   git clone https://github.com/w0t1x/spring-boot-blog-api.git
   cd spring-boot-blog-api
   ```

3. Соберите и запустите приложение:

   ```bash
   mvn spring-boot:run
   ```

4. По умолчанию приложение доступно по адресу:

   * `http://localhost:8080`

5. (Опционально) H2 Console:

   * `http://localhost:8080/h2-console`
   * JDBC URL и параметры — см. в `application.yml` / `application.properties`.

---

Проект задуман как учебный — его можно свободно дорабатывать, расширять, пробовать разные варианты архитектуры и приёмов Spring Data JPA.
