/**
 * This class represents the records that will be stored in the HashDistionary
 * Each record stored in the dictionary will consist of two parts: a configuration & an integer score
 * 
 * @author Ivory Huo
 *
 */
public class Data {
	
	// Instance variables to store the configurations of the board and the score
	private String config;
	private int score;
	
	/**
	 * Constructor that initializes a new Data object with the specified configuration and score
	 * The string config will be used as a key attribute for every Data object
	 * 
	 * @param config : the configuration of the game board
	 * @param score : the score associated with this configuration
	 */
	public Data (String config, int score) {
		this.config = config;
		this.score = score;
	}
	
	/**
	 * Returns the configuration stored in this Data object
	 * 
	 * @return the configuration of the board game
	 */
	public String getConfiguration() {
		return config;
	}
	
	/**
	 * Return the score in this Data
	 * 
	 * @return the score associated with the configuration
	 */
	public int getScore() {
		return score;
	}

}
