### build:

```sh
mvn spring-boot:build-image
```

### indítás:

```sh
docker-compose up
```
> Detached mode: `-d` flag hozzáadása a fenti parancshoz

```sh
docker-compose up -d
```

### dokumentáció:

```
http://localhost:8082/swagger-ui/index.html 
```

### felhasználók:

| username     | password  | role                           | 
|--------------|-----------|--------------------------------|
| user         | password  | USER                           |
| journalist   | password  | JOURNALIST (újságíró)          |
| editor       | password  | EDITOR_IN_CHIEF (főszerkesztő) |

### további frissítések:

- Cikk frissítése folyamat kiegészítése (jelenleg külön van):
  - Tagek kezelése
  - Képek kezelése