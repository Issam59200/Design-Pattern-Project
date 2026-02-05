## Rapport de la semaine du 2 février 

Issam :

Cette semaine j'ai séparé GameStorage.java en deux classes spécialisées (GameStorageCSV.java et GameStorageJSON.java) avec un pattern Delegation, du coup GameStorage délègue juste aux bonnes classes selon le format. 

Ensuite j'ai implémenté la feature n°1, j'ai créé une interface Command.java et deux implémentations AddGameCommand.java et RemoveGameCommand.java, plus un CommandManager.java qui gère l'historique avec une Stack. C'était pas évident au début de comprendre comment bien structurer ça. 

J'ai aussi refactorisé GameController qui était trop 'fat' en le séparant en trois services : CommandManager pour l'historique, GameManagementService pour les opérations CRUD, et GameQueryService pour les consultations. Pour les tests j'ai créé CommandManagerTest.java, GameManagementServiceTest.java et GameQueryServiceTest.java, et simplifié GameControllerTest. 

## Rapport de la semaine du 26 janvier

Issam :

Cette semaine j'ai retravaillé sur la séparation de la classe GameCollection en plusieurs classes. Au début j'avais mal compris, je pensais qu'il fallait juste séparer les méthodes trop longues, mais en relisant les diapos j'ai compris qu'il fallait créer des classes différentes. Du coup j'ai créé GameRepository pour la collection en mémoire, GameStorage pour les fichiers JSON/CSV, et GameDisplay pour l'affichage.

J'ai regroupé les méthodes par groupe comme sur un diagramme de paquetage, créé une classe pour chaque groupe, et adapté le code dans Main.java et Menu.java. J'ai aussi fait des tests unitaires pour chaque classe en suivant le modèle du cours (GameRepositoryTest, GameStorageTest, GameDisplayTest), et adapté MenuTest qui avait été commencé par mon binôme Enzo.

Je dois avouer que c'était pas évident au début, le Java c'est pas un langage que j'ai beaucoup pratiqué. Mais après m'y être mis sérieusement cette semaine, j'ai mieux compris la séparation des responsabilités et l'organisation du code. Le java, c'est pas un langage que j'ai beaucoup pratiqué sérieusement, ce qui explique pourquoi, j'avais du mal à comprendre certaines bases, sachant que y'a encore certaines notions, dont j'ai encore un peu du mal.

J'ai aussi ajouté GameRecommender.java qui recommande des jeux en fonction du nombre de joueurs. J'ai essayé de faire assez simple, et grâce au travail fait en amont avec la séparation de GameCollection, c'était pas trop compliqué à implémenter. A part le recommendGame()dans le Menu.java qui était complexe à crée. Le reste c'était assez simple, juste ajouté recommander sur tous les paramètre des ApplicationContext. Et pour GameRecommenderTest.java, pareil, avec l'aide de la diapo et le travail que j'ai fait avant, créer les différents tests unitaires s'est fait assez facilement.

---

Enzo :

J'ai implémenté 4 nouvelles méthodes. 3 dans GameRepository dont deux pour de la gestion d'erreur (notamment gestion des doublons feature 1) 
et une pour générer un mélange aléatoire parmi la liste de jeux. J'ai adapté la classe Menu en conséquence et j'y ai implémnté une méthode isWeekend()
pour que l'option Suggest a weekend selection ne soit disponible que pour le weekend. C'était ici la tâche la plus pénible. Je ne suis pas très fort en java,
l'implémentation n'a vraiment pas été simple pour moi, je tenté plusieurs approches pour arriver à ce résultat finalpour la troisième feature.
