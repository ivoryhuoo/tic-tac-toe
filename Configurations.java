/**
 * This class implements all the methods needed by algorithm computerPlay
 * 
 * @author Ivory Huo
 *
 */
public class Configurations {
	
	//Instance variables to implement 
	private char[][] board;
    private int boardSize;
    private int lengthToWin;
    private int maxLevels;

    /**
     * Constructor that initializes the game board and other settings 
     * 
     * @param boardSize is the size of the board
     * @param lengthToWin is the length of the sequence needed to win the game
     * @param maxLevels is the maximum level of the game tree that will be explored by the program
     */
    public Configurations(int boardSize, int lengthToWin, int maxLevels) {
        this.boardSize = boardSize;
        this.lengthToWin = lengthToWin;
        this.maxLevels = maxLevels;
        this.board = new char[boardSize][boardSize];

        // Initialize board with spaces ' '
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                board[i][j] = ' ';
            }
        }
    }

    /**
     * Constructor that returns an empty HashDictionary with a specified size 
     * 
     * @return empty HashDictionary with a specified size 
     */
    public HashDictionary createDictionary() {
        // Assuming a good size for the dictionary (choose based on  use case)
        int dictionarySize = 9973; // A prime number close to 10,000
        return new HashDictionary(dictionarySize);
    }

    /**
     * Check if current board configuration has been seen before
     * 
     * @param hashTable
     * @return associated score 
     */
    public int repeatedConfiguration(HashDictionary hashTable) {
        String config = boardToString();
        return hashTable.get(config);
    }

    /**
     * Constructor represents the content of the board as a String, then inserts this String & score in the hash Dictionary
     * 
     * @param hashDictionary : where board configuration & score are to be stored
     * @param score : the score asspciated with the current game configuration
     */
    public void addConfiguration(HashDictionary hashDictionary, int score) {
    	
    	// Convert current board configuration to string 
        String config = boardToString();
        
        // Insert the neww board configuration & its associated score into the hashDictionary
        try {
            hashDictionary.put(new Data(config, score));
        } catch (DictionaryException e) { // If already exists, throw exception
            System.out.println("Configuration already exists in the dictionary.");
        }
    }
    
    /**
     * Constructor that stores a symbol in the board[row][col]
     * @param row
     * @param col
     * @param symbol
     */
    public void savePlay(int row, int col, char symbol) {
    	// Store in board[row][col]
        if (row >= 0 && row < boardSize && col >= 0 && col < boardSize) {
            board[row][col] = symbol;
        }
    }

    /**
     * Constructor that checks if specific board square is empty 
     * @param row
     * @param col
     * @return
     */
    public boolean squareIsEmpty(int row, int col) {
        return board[row][col] == ' '; //Empty if equal to ' '
    }

    /**
     * Constructor that returns true if there is a continuous sequence of length at least k fomed by tiles of the kind symbol on the board
     * @param symbol
     * @return true if there is a continuous sequence 
     */
    public boolean wins(char symbol) {
        // Horizontal, vertical, and diagonal check
        return checkHorizontalWin(symbol) || checkVerticalWin(symbol) || checkDiagonalWin(symbol);
    }

    /**
     * Constructor that checks if game is a draw 
     * 
     * @return if game is draw or not 
     */
    public boolean isDraw() {
    	
    	// Iterate through board
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (board[i][j] == ' ') {
                    return false; // Empty space found, not a draw
                }
            }
        }
        return !wins('X') && !wins('O'); // No empty spaces and no winner
    }

    /**
     * Constructor that evaluates the current board state 
     * 
     * @return current board state 
     */
    public int evalBoard() {
        if (wins('O')) {
            return 3; // Computer wins 
        } else if (wins('X')) {
            return 0; // Human wins
        } else if (isDraw()) {
            return 2; // Draw
        } else {
            return 1; // Game still undecided
        }
    }

    /**
     * Constructor that converts the board to a string representation
     * 
     * @return string representation
     */
    private String boardToString() {
    	StringBuilder sb = new StringBuilder(); // Initialize StringBuilder to concatenate cell symbols
        for (int i = 0; i < boardSize; i++) { // Iterate through each row of the board
            for (int j = 0; j < boardSize; j++) { // Within each row, iterate through each column
                sb.append(board[i][j]); // Append the symbol in the current cell to the StringBuilder
            }
            // Note: Depending on the usage, you might want to append a row separator (like a newline) here
        }
        return sb.toString(); // Convert the StringBuilder to a String and return it

    }

    /**
     * Method that checks for a winning condition in any horizontal row
     * 
     * @param symbol The player's symbol ('X' or 'O') to check for a win
     * @return true if the player has a winning sequence horizontally; false otherwise
     */
    private boolean checkHorizontalWin(char symbol) {
        for (int row = 0; row < boardSize; row++) {
            int count = 0; // Counter for consecutive symbols
            for (int col = 0; col < boardSize; col++) {
                if (board[row][col] == symbol) {
                    count++;
                    if (count == lengthToWin) return true; // Win condition met
                } else {
                    count = 0; // Reset counter if sequence is broken
                }
            }
        }
        return false; // No win found
    }
    
    /**
     * Checks for a winning condition in any vertical column
     * 
     * @param symbol The player's symbol ('X' or 'O') to check for a win
     * @return true if the player has a winning sequence vertically; false otherwise
     */
    private boolean checkVerticalWin(char symbol) {
        for (int col = 0; col < boardSize; col++) {
            int count = 0; // Counter for consecutive symbols
            for (int row = 0; row < boardSize; row++) {
                if (board[row][col] == symbol) {
                    count++;
                    if (count == lengthToWin) return true; // Win condition met
                } else {
                    count = 0; // Reset counter if sequence is broken
                }
            }
        }
        return false; // No win found
    }

    /**
     * Checks for a winning condition along both primary and secondary diagonals
     * 
     * @param symbol The player's symbol ('X' or 'O') to check for a win
     * @return true if the player has a winning sequence diagonally; false otherwise
     */
    private boolean checkDiagonalWin(char symbol) {
        // Iterate over each row of the board
        for (int row = 0; row < boardSize; row++) {
            // For each row, iterate over each column
            for (int col = 0; col < boardSize; col++) {
                // Check if a winning diagonal sequence starts from the current position (row, col)
                if (checkDiagonalFromPosition(row, col, symbol)) {
                    return true; // If a winning diagonal is found, return true immediately
                }
            }
        }
        return false; // After checking all positions, if no winning diagonal is found, return false
    }

    /**
     * Checks for winning diagonal position from a specific position
     * 
     * @param row : the starting row index from which to check for a diagonal win
     * @param col : the starting column index from which to check for a diagonal win
     * @param symbol : the player's symbol ('X' or 'O') to check for a win
     * @return true if a winning diagonal sequence of the specified symbol exists; false otherwise
     */
    private boolean checkDiagonalFromPosition(int row, int col, char symbol) {
        int count = 0; // Initialize a counter to track the number of consecutive symbols

        // Check primary diagonal (top-left to bottom-right)
        for (int i = 0; i < lengthToWin; i++) {
            // Ensure we do not go out of bounds
            if (row + i < boardSize && col + i < boardSize && board[row + i][col + i] == symbol) {
                count++; // Increment count if the current symbol matches the player's symbol
                if (count == lengthToWin) return true; // Return true if the count equals the length needed to win
            } else break; // Break the loop if the sequence is broken
        }

        count = 0; // Reset the counter for checking the secondary diagonal

        // Check secondary diagonal (top-right to bottom-left)
        for (int i = 0; i < lengthToWin; i++) {
            // Ensure we do not go out of bounds
            if (row + i < boardSize && col - i >= 0 && board[row + i][col - i] == symbol) {
                count++; // Increment count if the current symbol matches the player's symbol
                if (count == lengthToWin) return true; // Return true if the count equals the length needed to win
            } else break; // Break the loop if the sequence is broken
        }

        // If no winning sequence is found in either diagonal, return false
        return false;
    }


}
