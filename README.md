# tic-tac-toe
Tic-Tac-Toe Game

# Overview
This project implements a modified version of Tic-Tac-Toe, where the goal is to connect a sequence of length k on an n x n board. It features an AI opponent that uses a game tree to evaluate potential moves and make decisions, based on the minimax algorithm and a custom hash table for board configurations.

# Features
AI Player: Utilizes game tree exploration to compute optimal moves.
Custom Hash Table: Stores board configurations to avoid redundant calculations.
Recursive Move Evaluation: Applies the minimax algorithm to determine the best possible play.

# Classes
HashDictionary.java: Implements a hash table with separate chaining to store game board configurations.
Configurations.java: Manages the game board, checking for winning conditions and storing configurations in the hash table.
Data.java: Represents a board configuration and its associated score.

# Technologies Used
Java

# Author
Ivory Huo - University of Western Ontario
