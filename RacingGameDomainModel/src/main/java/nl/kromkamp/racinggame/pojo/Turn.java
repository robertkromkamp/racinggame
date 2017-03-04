package nl.kromkamp.racinggame.pojo;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import nl.kromkamp.racinggame.exception.RacingGameException;

@Entity
public class Turn {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Enumerated(EnumType.ORDINAL)
	private Direction direction;;
	
	@Enumerated(EnumType.ORDINAL)
	private Speed speed;
	
	@ManyToOne
	private Car car;

	
	public Turn(Direction direction, Speed speed, Car car) throws RacingGameException {
		this.direction = direction;
		this.speed = speed;
		this.car = car;
		
		if (this.getDirection().equals(Direction.NONE) && this.getSpeed().equals(Speed.NONE)){
			throw new RacingGameException("Direction and Speed can't be both NONE");
		}
	}
	
	public long getId() {
		return id;
	}



	public void setId(long id) {
		this.id = id;
	}



	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public Speed getSpeed() {
		return speed;
	}

	public void setSpeed(Speed speed) {
		this.speed = speed;
	}

	public Car getCar() {
		return car;
	}

	public void setCar(Car car) {
		this.car = car;
	}

	public void doTurn() throws RacingGameException {
		if (this.getCar() == null){
			throw new RacingGameException("Can't do turn while there's no car");
		}
		
		if (this.getCar().getRace() == null){
			throw new RacingGameException("Can't do turn while there's no race");
		}
		
		if (!this.getCar().getRace().isRaceStarted()){
			throw new RacingGameException("Can't do turn while race is not started");
		}
		
		if (this.getCar().isCrashed()){
			throw new RacingGameException("Can't do turn while car is crashed");
		}
		
						
		checkTurnAllowed();

		//update 1 turn if car is not stopped
		if (this.getCar().getCurrentSpeed() != Speed.STOPPED){
			Offset.updateOffset(this);
		}
		
		//update current status of car
		updateActualStatus();
		
		//update number of segments traveled
		if (!this.getCar().isCrashed() && this.getDirection() != Direction.NONE && this.getCar().getCurrentSpeed() != Speed.STOPPED){
			TrackSegment.updateSegment(getCar());
		}
		
		//if current speed is fast -> 2 turns, do same again
		if (this.getCar().getCurrentSpeed() == Speed.FAST){
			//if speed is fast do twice
			Offset.updateOffset(this);
			updateActualStatus();
			if (!this.getCar().isCrashed() && this.getDirection() != Direction.NONE){
				TrackSegment.updateSegment(getCar());
			}
		}
	}
		

	
	

	private void updateActualStatus() throws RacingGameException {
		if (!getCar().isFinished() && !getCar().isCrashed() && getCar().getRace().isRaceActive()){ 
			if (!this.getDirection().equals(Direction.NONE)){
				getCar().setCurrentDirection(this.getDirection());
			} else {
				getCar().setCurrentSpeed(this.getSpeed());
			}
			
			//change speed back to slow if not on asphalt
			if (getCar().getCurrentOffset() != Offset.ASPHALT && this.getCar().getCurrentSpeed() == Speed.FAST){
				getCar().setCurrentSpeed(Speed.SLOW);
				this.setSpeed(Speed.SLOW);
			}
		} else {
			if (getCar().getRace().isRaceActive()){
				//check if race is ended
				boolean active = false;
				for (Car thecar : getCar().getRace().getCars()){
					if (!thecar.isCrashed() && !thecar.isFinished()){
						active = true;
						break;
					}
				}
				
				//all cars are crashed or finished after this turn
				if (!active){
					getCar().getRace().endRace();
				}
			}
		}
	}



	private void checkTurnAllowed () throws RacingGameException{
		//check finished
		if (getCar().isFinished()){
			throw new RacingGameException("Car is already finished");
		}
		
		//check crashed
		if (getCar().isFinished()){
			throw new RacingGameException("Car is crashed");
		}
		
		//check active
		if (!getCar().getRace().isRaceActive()){ 
			throw new RacingGameException("Can't do turn because of car is crashed");
		}
		
		//check if direction 
		if (this.getDirection().equals(Direction.NONE) && this.getSpeed().equals(Speed.NONE)){
			throw new RacingGameException("Direction and Speed can't be both NONE");
		}
		
		//check if direction 
		if (!this.getDirection().equals(Direction.NONE) && !this.getSpeed().equals(Speed.NONE)){
			throw new RacingGameException("Can't change both direction and speed");
		}
		
		
		if (this.getCar().getCurrentSpeed().equals(Speed.NONE)){
			//only speed start is allowed
			if (!this.getSpeed().equals(speed.SLOW)){
				throw new RacingGameException("During start you can only change speed to SLOW");
			}
		} else if (this.getSpeed() != Speed.NONE){
			setSpeed();
		} else {
			//direction is changed
			if (Math.abs( (this.getDirection().getValue() - this.getCar().getCurrentDirection().getValue())) > 1) {
				//0 or 1 direction step difference
				throw new RacingGameException("You are making a to big step in the direction change");
			}
		}	
	}

	private void setSpeed() throws RacingGameException {
		//speed is changed
		if (Math.abs( (this.getSpeed().getValue() - this.getCar().getCurrentSpeed().getValue())) == 1) {
			//1 speed step difference
			if (getCar().getCurrentOffset() != Offset.ASPHALT && this.getSpeed().equals(Speed.FAST)) {
				throw new RacingGameException("You can't ride fast besides on the Asphalt");
			}
		} else {
			throw new RacingGameException("You are making a to big or small step in the speed change");
		}
	}
}
