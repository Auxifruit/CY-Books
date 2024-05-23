package abstraction;

/**
 * Enumeration to manage the user' status
 */
public enum Status {
	// If the user has no late borrow
	PUNCTUAL("Punctual"),
	// If the user has at least one late borrow
	LATECOMER("Latecomer");

    private String text;

    /**
     * Constructor for the enumeration
     * @param text the text for each value
     */
    private Status(String text) {
        this.text = text;
    }

    /**
     * Getter to get each value's text
     * @return the text of one value
     */
    public String getText() {
        return text;
    }
}
