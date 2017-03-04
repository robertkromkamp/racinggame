package nl.kromkamp.racinggame.pojo;

public enum Speed {

	NONE(-1), STOPPED(0), SLOW(1), FAST(2);
	
	private final int value;
		
	//constuctor
	private Speed(int value) { 
		this.value = value; 
	} 
	
    public int getValue() {
        return value;
    }
}
