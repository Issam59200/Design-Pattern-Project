## Rapport de la semaine du 26 janvier

Issam :

Cette semaine j'ai retravaillé sur la séparation de la classe GameCollection en plusieurs classes. Au début j'avais mal compris, je pensais qu'il fallait juste séparer les méthodes trop longues, mais en relisant les diapos j'ai compris qu'il fallait créer des classes différentes. Du coup j'ai créé GameRepository pour la collection en mémoire, GameStorage pour les fichiers JSON/CSV, et GameDisplay pour l'affichage.

J'ai regroupé les méthodes par groupe comme sur un diagramme de paquetage, créé une classe pour chaque groupe, et adapté le code dans Main.java et Menu.java. J'ai aussi fait des tests unitaires pour chaque classe en suivant le modèle du cours (GameRepositoryTest, GameStorageTest, GameDisplayTest), et adapté MenuTest qui avait été commencé par mon binôme Enzo.

Je dois avouer que c'était pas évident au début, le Java c'est pas un langage que j'ai beaucoup pratiqué. Mais après m'y être mis sérieusement cette semaine, j'ai mieux compris la séparation des responsabilités et l'organisation du code. Le java, c'est pas un langage que j'ai beaucoup pratiqué sérieusement, ce qui explique pourquoi, j'avais du mal à comprendre certaines bases, sachant que y'a encore certaines notions, dont j'ai encore un peu du mal.

J'ai aussi ajouté GameRecommender.java qui recommande des jeux en fonction du nombre de joueurs. J'ai essayé de faire assez simple, et grâce au travail fait en amont avec la séparation de GameCollection, c'était pas trop compliqué à implémenter. A part le recommendGame()dans le Menu.java qui était complexe à crée. Le reste c'était assez simple, juste ajouté recommander sur tous les paramètre des ApplicationContext. Et pour GameRecommenderTest.java, pareil, avec l'aide de la diapo et le travail que j'ai fait avant, créer les différents tests unitaires s'est fait assez facilement.

---

# Board Game Collection

A CLI application to manage a board game collection. This project intentionally demonstrates **bad design patterns** for educational purposes (SRP violations, static state, mixed responsibilities).

## Requirements

- Java 21+
- Maven 3.9+

## How to Run

### Using the run script

```bash
# On Linux/Mac
./run.sh games.json

# On Windows
run.bat games.json
```

### Using Maven directly

```bash
# Compile
./mvnw compile

# Run with JSON storage
./mvnw exec:java -Dexec.mainClass="fr.fges.Main" -Dexec.args="games.json"

# Run with CSV storage
./mvnw exec:java -Dexec.mainClass="fr.fges.Main" -Dexec.args="games.csv"
```

### Using Makefile

```bash
make run ARGS=games.json
```

## Running Tests

```bash
./mvnw test
```

## Storage Formats

The application supports two storage formats:
- **JSON** (`.json` extension)
- **CSV** (`.csv` extension)

The storage file is passed as a command-line argument at startup.

## Documentation

- [Output Examples](docs/output-example.md) - Example CLI sessions
