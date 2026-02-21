## Rapport de la semaine du 16 février 

Issam :

Premièrement, j'ai supprimé le gros switch/case dans Menu.java en appliquant le pattern Registry. J'ai créé une interface MenuAction et une classe MenuRegistry qui stocke les actions dans une map. Chaque option du menu est devenue sa propre classe (AddGameMenuAction, RemoveGameMenuAction, etc.), ce qui respecte le principe Open/Closed : on peut ajouter une option sans toucher au menu.

Ensuite on a implémenté la feature "tournament" avec le pattern Strategy (ChampionshipStrategy pour le round-robin, KingOfTheHillStrategy pour le roi de la colline). Au départ TournamentAction faisait tout dans une seule méthode, donc j'ai appliqué le SRP en extrayant GameSelector, PlayerCollector, FormatSelector et TournamentRunner. Pareil pour TournamentDisplay que j'ai séparé avec MatchDisplay et RankingDisplay. L'action est enregistrée dynamiquement dans le menu via registerAction(), ce qui prouve que le pattern Registry fonctionne bien.

Et pour les tests j'ai écrit des tests unitaires pour toutes mes classes (actions du menu, tournoi, classes extraites) en utilisant Mockito. On a eu un problème avec Mockito : certains tests bouclaient à l'infini car le mock de readInt() (qui retourne un Optional) renvoyait null quand le prompt ne matchait pas, ce qui empêchait la boucle de validation de sortir. On a résolu ça en centralisant readIntInRange dans InputHandler et en mockant directement cette méthode.

## Rapport de la semaine du 2 février 

Issam :

Cette semaine j'ai séparé GameStorage.java en deux classes spécialisées (GameStorageCSV.java et GameStorageJSON.java) avec un pattern Delegation, du coup GameStorage délègue juste aux bonnes classes selon le format. 

Ensuite j'ai implémenté la feature n°1, j'ai créé une interface Command.java et deux implémentations AddGameCommand.java et RemoveGameCommand.java, plus un CommandManager.java qui gère l'historique avec une Stack. C'était pas évident au début de comprendre comment bien structurer ça. 

J'ai aussi refactorisé GameController qui était trop 'fat' en le séparant en trois services : CommandManager pour l'historique, GameManagementService pour les opérations CRUD, et GameQueryService pour les consultations. Pour les tests j'ai créé CommandManagerTest.java, GameManagementServiceTest.java et GameQueryServiceTest.java, et simplifié GameControllerTest. 

---

Enzo:

Pour cette semaine, j'ai séparé le menu en trois nouvelle classes InputHandler(gestion des entrées utilisateur), GameController(logique métier) et Menu (affichage et navigation).
J'ai supprimé la classe ApplicationContext qui est devenu inutile et j'ai réalisé les tests untaires de mes nouvelles classes. J'ai également ajouté la feature "Game for X Players"
qui affiche tous les jeux compatibles triés alphabétiquement selon un nombre de joueurs donné.

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
