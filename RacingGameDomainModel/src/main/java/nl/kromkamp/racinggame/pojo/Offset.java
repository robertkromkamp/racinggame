package nl.kromkamp.racinggame.pojo;

public enum Offset {

	LEFTWALL (-2), LEFTGRASS (-1), ASPHALT (0), RIGHTGRASS (1), RIGHTWALL (2);
	
	private final int value;
	
	//constuctor
	private Offset(int value) { 
		this.value = value; 
	} 
	
	
    public int getValue() {
        return value;
    }
    
    
    
    public static void updateOffset(Turn turn) {
		if (!turn.getCar().isFinished() && !turn.getCar().isCrashed() && turn.getCar().getRace().isRaceActive()){ 
			if (turn.getDirection() != Direction.NONE){
				if (turn.getDirection() == Direction.LEFT){
					if (getNextTrackSegment(turn).getDirection() != Direction.LEFT){
						if (getNextTrackSegment(turn).getDirection() == Direction.STRAIGHT){
							switch ( turn.getCar().getCurrentOffset() ) {
								case LEFTGRASS: 
									turn.getCar().setCurrentOffset(Offset.LEFTWALL);
									break;
								case ASPHALT: 
									turn.getCar().setCurrentOffset(Offset.LEFTGRASS);
									break;
								case RIGHTGRASS: 
									turn.getCar().setCurrentOffset(Offset.ASPHALT);
									break;
								default:
									//nothing
							}
						} else if (getNextTrackSegment(turn).getDirection() == Direction.RIGHT){
							switch (turn.getCar().getCurrentOffset() ) {
								case LEFTGRASS: 
									turn.getCar().setCurrentOffset(Offset.LEFTWALL);
									break;
								case ASPHALT: 
									turn.getCar().setCurrentOffset(Offset.LEFTWALL);
									break;
								case RIGHTGRASS: 
									turn.getCar().setCurrentOffset(Offset.LEFTGRASS);
									break;
								default:
									//nothing
							}
						}
					}
				} else if (turn.getDirection() == Direction.STRAIGHT){
					if (getNextTrackSegment(turn).getDirection() == Direction.RIGHT){
						switch ( turn.getCar().getCurrentOffset() ) {
							case LEFTGRASS: 
								turn.getCar().setCurrentOffset(Offset.LEFTWALL);
								break;
							case ASPHALT: 
								turn.getCar().setCurrentOffset(Offset.LEFTGRASS);
								break;
							case RIGHTGRASS: 
								turn.getCar().setCurrentOffset(Offset.ASPHALT);
								break;
							default:
								//nothing
						}
					} else if (getNextTrackSegment(turn).getDirection() == Direction.LEFT){
						switch ( turn.getCar().getCurrentOffset() ) {
							case LEFTGRASS: 
								turn.getCar().setCurrentOffset(Offset.ASPHALT);
								break;
							case ASPHALT: 
								turn.getCar().setCurrentOffset(Offset.RIGHTGRASS);
								break;
							case RIGHTGRASS: 
								turn.getCar().setCurrentOffset(Offset.RIGHTWALL);
								break;
							default:
								//nothing
							}
					}
				} else if (turn.getDirection() == Direction.RIGHT){
					if (getNextTrackSegment(turn).getDirection() != Direction.RIGHT){
						if (getNextTrackSegment(turn).getDirection() == Direction.STRAIGHT){
							switch (turn.getCar().getCurrentOffset() ) {
								case LEFTGRASS: 
									turn.getCar().setCurrentOffset(Offset.ASPHALT);
									break;
								case ASPHALT: 
									turn.getCar().setCurrentOffset(Offset.RIGHTGRASS);
									break;
								case RIGHTGRASS: 
									turn.getCar().setCurrentOffset(Offset.RIGHTWALL);
									break;
								default:
									//nothing
							}
						} else if  (getNextTrackSegment(turn).getDirection() == Direction.LEFT){
							switch (turn.getCar().getCurrentOffset() ) {
								case LEFTGRASS: 
									turn.getCar().setCurrentOffset(Offset.RIGHTGRASS);
									break;
								case ASPHALT: 
									turn.getCar().setCurrentOffset(Offset.RIGHTWALL);
									break;
								case RIGHTGRASS: 
									turn.getCar().setCurrentOffset(Offset.RIGHTWALL);
									break;
								default:
									//nothing
							}
						} 
					}
				}
			}
		}
	}
    
    private static TrackSegment getNextTrackSegment(Turn turn){
		if (turn.getCar().getNrSegmentsTravelled() < turn.getCar().getRace().getLap().getSegments().size()) {
			//last segment of lap not reached
			return turn.getCar().getRace().getLap().getSegments().get(turn.getCar().getNrSegmentsTravelled());
		} else {
			//current segment is last segment of lap, next is first segment of a 'new' lap
			return turn.getCar().getRace().getLap().getSegments().get(0);
		}
		
	}

}
