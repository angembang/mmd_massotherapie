# ===============================
# Étape 1 : Build de l'application
# ===============================
FROM eclipse-temurin:21-jdk AS build

# Build environment variable
ENV DOCKER_BUILD=true

# Définition du répertoire de travail
WORKDIR /app

# Copier les fichiers Gradle
COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./

# Donner les droits d'exécution au wrapper Gradle
RUN chmod +x gradlew

# Télécharger les dépendances (cache Docker)
RUN ./gradlew dependencies --no-daemon

# Copier le code source
COPY src src

# Builder le JAR
RUN ./gradlew bootJar --no-daemon

# ===============================
# Étape 2 : Image d'exécution
# ===============================
FROM eclipse-temurin:21-jre

WORKDIR /app

# Créer un utilisateur système non-root dédié à l'application
RUN groupadd --system appgroup \
    && useradd --system \
        --gid appgroup \
        --no-create-home \
        --shell /usr/sbin/nologin \
        appuser

# Copier le JAR généré depuis l'étape build avec les bonnes permissions
COPY --from=build --chown=appuser:appgroup /app/build/libs/*.jar app.jar

# Exécuter Spring Boot avec un utilisateur non-root
USER appuser:appgroup

# Exposer le port Spring Boot
EXPOSE 8080

# Lancer l'application
ENTRYPOINT ["java", "-jar", "app.jar"]