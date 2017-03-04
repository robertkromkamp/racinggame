package nl.kromkamp.racinggame.pojo;

public enum Direction {

	NONE(-2), LEFT(-1), STRAIGHT(0), RIGHT(1);
	
	private final int value;
		
	//constuctor
	private Direction(int value) { 
		this.value = value; 
	} 
	
    public int getValue() {
        return value;
    }
}
