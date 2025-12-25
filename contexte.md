Contexte du projet
Tricol, entreprise spécialisée dans la fabrication de vêtements professionnels, a développé un système de gestion de stock basé sur la méthode FIFO (First-In, First-Out). Ce système gère les réceptions de commandes fournisseurs, les lots de stock, et les sorties de stock via des bons de sortie.

Le module de gestion de stock actuel fonctionne mais manque de tests unitaires pour garantir la fiabilité de la logique métier critique, notamment l'algorithme FIFO. De plus, l'équipe de direction souhaite disposer de fonctionnalités d'audit avancées pour tracer et analyser les mouvements de stock selon plusieurs critères.

Partie 1 : Tests Unitaires de la Gestion de Stock
La gestion de stock, en particulier l'application de la méthode FIFO (First-In, First-Out), est un cœur métier critique. Il est impératif de s'assurer que l'algorithme fonctionne correctement dans tous les scénarios.

Tâche 1.1 : Tests du service de Stock et FIFO
Développer des tests unitaires (JUnit + Mockito) pour la couche Service qui gère :

A. Mécanisme de Sortie de Stock FIFO

Tester l'algorithme qui identifie et consomme les lots par ordre chronologique d'entrée (plus ancien en premier)

Scénario 1 : Sortie simple consommant partiellement un seul lot

Scénario 2 : Sortie nécessitant la consommation de plusieurs lots successifs

Scénario 3 : Sortie avec stock insuffisant (gestion d'erreur)

Scénario 4 : Sortie épuisant exactement le stock disponible

B. Création Automatique de Lot

Vérifier qu'une réception de commande fournisseur validée crée automatiquement un lot de stock traçable

Contrôler la génération du numéro de lot, de la date d'entrée, et l'enregistrement du prix d'achat unitaire

Vérifier le lien entre le lot créé et la réception fournisseur

C. Calcul de Valorisation du Stock

Tester la méthode qui calcule la valeur totale du stock en multipliant les quantités restantes par les prix d'achat unitaires

Vérifier le calcul selon la méthode FIFO (valorisation basée sur les lots les plus anciens)

Tester avec plusieurs lots à prix différents

Tâche 1.2 : Tests des Transitions de Statut
Tester les workflows de validation :

Vérifier que la validation d'un bon de sortie (passage de BROUILLON à VALIDÉ) déclenche automatiquement :

La création des mouvements de stock correspondants

La mise à jour des quantités restantes dans les lots

L'enregistrement des informations de validation (utilisateur, date)

Partie 2 : Recherche Avancée sur les Mouvements de Stock
Afin de permettre des fonctionnalités d'audit robustes, il faut étendre l'API /api/v1/stock/mouvements pour supporter une recherche avancée.

Tâche 2.1 : Implémentation avec Spring Data JPA Specifications
Implémenter la recherche avancée sur l'entité des Mouvements de Stock en utilisant Spring Data JPA Specifications (approche recommandée) ou la Criteria API de JPA.

Cette approche permet de construire dynamiquement des requêtes complexes basées sur la combinaison de plusieurs critères de recherche, ce qui est une pratique très recommandée pour la création de filtres robustes en entreprise.

Tâche 2.2 : Critères de Recherche à Implémenter
Le point de terminaison GET /api/v1/stock/mouvements doit accepter les paramètres de requête suivants (non exhaustifs, mais obligatoires pour l'audit) :

Par Période : Filtrage sur la date du mouvement (intervalle 'Date Début' et 'Date Fin').

Par Type de Produit : Filtrage par la Référence ou l'ID du produit concerné.

Par Type de Mouvement : Filtrage pour distinguer les Entrées (réception de commande) des Sorties (consommation via bon de sortie).

Par Numéro de Lot : Recherche d'un mouvement spécifique lié à un numéro de lot unique.

Tâche 2.3 : Intégration de la Pagination
**Assurer que l'API supporte nativement :**

Pagination : Paramètres page (numéro de page, débute à 0) et size (nombre d'éléments par page)

Exemples d'appels API :

**Recherche par produit et type avec pagination:**

GET /api/v1/stock/mouvements?produitId=123&type=SORTIE&page=0&size=10

**Recherche par période:**

GET /api/v1/stock/mouvements?dateDebut=2025-01-01&dateFin=2025-03-31

**Recherche par numéro de lot:**

GET /api/v1/stock/mouvements?numeroLot=LOT-2025-001

**Recherche combinée multi-critères:**

GET /api/v1/stock/mouvements?reference=PROD001&type=ENTREE&dateDebut=2025-01-01&page=0&size=20

Objectifs d'apprentissage :

Développant une suite de tests unitaires couvrant la logique métier critique (FIFO, création de lots, valorisation du stock).
Implémentant une API de recherche avancée multi-critères pour faciliter l'audit des mouvements de stock.
Modalités pédagogiques
Projet individuel

Début : 10/11/2025

Deadline : 14/11/2025