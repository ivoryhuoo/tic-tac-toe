/**
 * This class implements the Dictionary ADT using a hash table
 * 
 * @author Ivory Huo
 */

public class HashDictionary implements DictionaryADT{
	
	//Instance variables 
	private HashNode[] table;
    private int size;

    /**
     * Method that is used by HashDictionary to store individual records
     */
    private static class HashNode {
    	
    	//Data object containing configuration string & associated score
        Data data;
        // Pointer to the necy node in the list to handle collisions using separate chaining
        HashNode next;

        /**
         * Constructor for creating a new HashNode with a specified Data object
         * 
         * @param data : the Data object containing the game configuration and its score
         */
        public HashNode(Data data) {
            this.data = data; 
            this.next = null; //End of the chain in the beginning (no next node) 
        }
    }

    /**
     * Constructor for the HashDictionary class
     * Initializes the hash table with a specified size
     * 
     * @param size : the size of the hash table, determining the number of buckets for storing records
     */
    public HashDictionary(int size) {
        this.size = findNextPrime(size); // Ensure the size is a prime number
        this.table = new HashNode[this.size]; // Initialize table with the adjusted size
    }

    /**
     * A hash function that calculates the index in the hash table array for a given key
     * 
     * @param key : the configuration string to be hashed
     * @return the index in the hash table where the key-value pair should be stored
     */
    private int hashFunction(String key) {
    	int hash = 0; // Initialize hash value to 0
        for (int i = 0; i < key.length(); i++) { // Iterate through each character of the key
            hash = 31 * hash + key.charAt(i); // Multiply the current hash by 31 and add the ASCII value of the char
        }
        return Math.abs(hash) % size; // Ensure the hash index falls within the table bounds
    }

    /**
     * Method that finds the next prime
     * 
     * @param input
     * @return
     */
    private int findNextPrime(int input) {
        int counter;
        input++; // Start with the next number
        while (true) {
            counter = 0;
            for (int i = 2; i <= Math.sqrt(input); i++) {
                if (input % i == 0) counter++;
            }
            if (counter == 0)
                return input; // It's a prime number
            else {
                input++; // Try the next number
            }
        }
    }
    
    //Implement all methods from the DictionaryADT interface
    @Override
    /**
     * Constructor that adds records to the dictionary
     * 
     * @param record The Data object to insert
   	 * @return returns 1 if the insertion caused a collision, 0 otherwise.
	 * @throws DictionaryException if record.getConfiguration() already exists in the dictionary
     */
    public int put(Data record) throws DictionaryException {
    	
    	// Call resizeIfNeeded at the beginning to ensure the hash table is adequately sized
        // before attempting to add a new entry.
        resizeIfNeeded();
        
    	// Calculate the hash index for the given configuration using the hash function
        int hashIndex = hashFunction(record.getConfiguration());
        
        // Retrieve the first node (head of the linked list) at the calculated hash index
        HashNode head = table[hashIndex];

        // Check if the configuration already exists, iterate through the linked list at the calculated hash index
        while (head != null) {
        	// Check if a node with the same configuration as the record exists
            if (head.data.getConfiguration().equals(record.getConfiguration())) {
            	// If node is found, throw a DictionaryException indicating a duplicate configuration
                System.err.println("Duplicate configuration: " + record.getConfiguration());
                throw new DictionaryException();
            }
            // Move to next node in linked list 
            head = head.next;
        }

        HashNode newNode = new HashNode(record); // Insert the new node with given record
        newNode.next = table[hashIndex]; // Point new node's next reference to the current head of the list 
        table[hashIndex] = newNode; // Update head of list at hash index to be new node (insert at beginning)

        // Check for collision
        if (newNode.next != null) {
            return 1; // Indicates a collision occurred
        } else {
            return 0; // Indicates no collision occurred
        }
    }

    @Override
    /**
     * Constructor that removes the record with the given config from the dictionary
     * 
     * @param config : the configuration string of the Data object to remove.
     * @throws DictionaryException if the configuration is not found in the dictionary
     */
    public void remove(String config) throws DictionaryException {
    	// Calculate the hash index for the given configuration to find the appropriate bucket
        int hashIndex = hashFunction(config);
        // Retrieve the first node (head of the linked list) at the calculated hash index
        HashNode head = table[hashIndex];
        // Previous node pointer, initialized as null
        HashNode prev = null; //Track the node before the current node in the iteration

        // Iterate through the linked list to find the node with the given configuration
        // Continue looping until either the end of the list is reached (head == null) or the node with the matching configuration is found
        while (head != null && !head.data.getConfiguration().equals(config)) {
            prev = head; // Move prev up to the current head node
            head = head.next; // Advance head to the next node in the list
        }

        // Configuration not found, throw an exception
        if (head == null) { 
        	System.err.println("Configuration not found: " + config);
            throw new DictionaryException();
        }

        // If prev is not null, it means not at beginning of list
        // Remove the node from the chain
        if (prev != null) { 
            prev.next = head.next;
        } else { //If prev is null, head at beginning of list
        	//Set beginning of list to head.next, remove first node
            table[hashIndex] = head.next;
        }
    }

    @Override
    /**
     * Constructor that returns the score stored in the record of the dictionary with key config, or -1 if config is not in the dictionary
     * 
     * @param config : the configuration string whose score is to be retrieved.
     * @return The score associated with the configuration if found, or -1 if the configuration is not found.
     */
    public int get(String config) {
    	// Calculate the hash index for the given configuration to determine where to look in the hash table
        int hashIndex = hashFunction(config);
        // Retrieve the head of the linked list at the calculated hash index
        HashNode head = table[hashIndex];

        // Iterate over linked list at the specific index
        while (head != null) {
        	// If current node configuration matches requested configuration
            if (head.data.getConfiguration().equals(config)) {
            	// Return score associated with the configuration
                return head.data.getScore();
            }
            head = head.next; // Move to next node in the linked list if the current node doesn't match 
        }

        // Return -1 if the configuration is not found in any of the nodes in the list
        return -1; // Not found
    }

    @Override
    /**
     * Constructor that counts the total number of records stored in the hash table
     * 
     * @return the total number of records
     */
    public int numRecords() {
    	//Initialize the counter to keep track of the total number of records
        int count = 0;
        
        //Iterate over each index (bucket) of the hash table
        for (int i = 0; i < size; i++) {
        	// Retrieve the head of the linked list at the current index
            HashNode head = table[i];
            
            // Iterate over linked list at the current index
            while (head != null) {
                count++; // Increment counter for each node encountered in the list
                head = head.next; // Move to next node in list 
            }
        }
        return count; // Return the total count of records found in hash table 
    }
    
    /**
     * Method that resizes if needed
     */
    private void resizeIfNeeded() {
    	double loadFactor = (double) numRecords() / size; // Calculate the current load factor
        if (loadFactor > 0.5) { // Check if the load factor exceeds the threshold for resizing
            int newSize = findNextPrime(size * 2); // Calculate the new size as the next prime number after doubling the current size
            rehash(newSize); // Rehash all entries into a new table of the calculated new size
        }
    }
    
    /**
     * Method that rehashes all entries into a new table of newSize
     * 
     * @param newSize: the size of the new hash table
     */
    private void rehash(int newSize) {
    	HashNode[] newTable = new HashNode[newSize]; // Create a new hash table array of the specified newSize
        int oldSize = size; // Store the current size of the table for iteration purposes
        size = newSize; // Update the instance variable to reflect the new size of the hash table

        // Iterate through each bucket of the old table
        for (int i = 0; i < oldSize; i++) {
            HashNode node = table[i]; // Start with the first node in the current bucket
            while (node != null) { // Iterate through the linked list in the current bucket
                HashNode next = node.next; // Store the next node before rehashing the current one
                int newIndex = hashFunction(node.data.getConfiguration()); // Calculate the new index for the current node
                node.next = newTable[newIndex]; // Insert the node into the new table, maintaining the head of the list
                newTable[newIndex] = node; // Update the head of the list in the new table to the current node
                node = next; // Move to the next node in the list
            }
        }

        table = newTable; // Replace the old table with the new table
    }
    

}
