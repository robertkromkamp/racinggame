package nl.kromkamp.racinggame.pojo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import nl.kromkamp.racinggame.exception.RacingGameException;

@Entity
public class Race {

	@Transient
	public final static int DEFAULTNRLAPS = 3;
	
	//wait default 10 seconds to start
	@Transient
	private int secondsWaitToStart = 10;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	private int nrOfLaps = DEFAULTNRLAPS;
	
	@OneToOne (cascade=CascadeType.ALL, mappedBy = "race")
	@JsonManagedReference
	private Lap lap;
	
	@OneToMany(cascade=CascadeType.ALL)
	@JsonManagedReference
	private List<Car> cars;
	
	@Temporal(TemporalType.TIMESTAMP)
	private java.util.Calendar startedTimestamp;
	
	@Temporal(TemporalType.TIMESTAMP)
	private java.util.Calendar endedTimestamp;
	
	
	public void useDefaultLap(){
		lap = new Lap();
		List<TrackSegment> segments = lap.getSegments();
		segments.add(new TrackSegment(Direction.STRAIGHT));
		segments.add(new TrackSegment(Direction.STRAIGHT));
		segments.add(new TrackSegment(Direction.RIGHT));
		segments.add(new TrackSegment(Direction.STRAIGHT));
		segments.add(new TrackSegment(Direction.STRAIGHT));
		segments.add(new TrackSegment(Direction.LEFT));
		segments.add(new TrackSegment(Direction.STRAIGHT));
		segments.add(new TrackSegment(Direction.RIGHT));
		segments.add(new TrackSegment(Direction.STRAIGHT));
		segments.add(new TrackSegment(Direction.RIGHT));
		segments.add(new TrackSegment(Direction.STRAIGHT));
		segments.add(new TrackSegment(Direction.STRAIGHT));
		segments.add(new TrackSegment(Direction.STRAIGHT));
		segments.add(new TrackSegment(Direction.RIGHT));
		segments.add(new TrackSegment(Direction.STRAIGHT));
		segments.add(new TrackSegment(Direction.STRAIGHT));
		segments.add(new TrackSegment(Direction.STRAIGHT));
	}
	
	public void useSmallLap(){
		lap = new Lap();
		List<TrackSegment> segments = lap.getSegments();
		segments.add(new TrackSegment(Direction.STRAIGHT));
		segments.add(new TrackSegment(Direction.RIGHT));
		segments.add(new TrackSegment(Direction.STRAIGHT));
	}
	

	
	public int getSecondsWaitToStart() {
		return secondsWaitToStart;
	}


	public void setSecondsWaitToStart(int secondsWaitToStart) {
		this.secondsWaitToStart = secondsWaitToStart;
	}


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}

	
	public int getNrOfLaps() {
		return nrOfLaps;
	}


	public void setNrOfLaps(int nrOfLaps) {
		this.nrOfLaps = nrOfLaps;
	}


	public Lap getLap() {
		return lap;
	}


	public void setLap(Lap lap) {
		this.lap = lap;
	}


	public List<Car> getCars() {
		return cars;
	}


	public void setCars(List<Car> cars) {
		this.cars = cars;
	}
	
	public java.util.Calendar getStartedTimestamp() {
		return startedTimestamp;
	}


	public void setStartedTimestamp(java.util.Calendar startedTimestamp) {
		this.startedTimestamp = startedTimestamp;
	}


	public java.util.Calendar getEndedTimestamp() {
		return endedTimestamp;
	}


	public void setEndedTimestamp(java.util.Calendar endedTimestamp) {
		this.endedTimestamp = endedTimestamp;
	}


	public void addCar(Car car) throws RacingGameException, RacingGameException {
		if (isRaceActive()){
			throw new RacingGameException("Can't add car race is active");
		}
		
		if (isRaceEnded()){
			throw new RacingGameException("Can't add car race is ended");
		}
		
		//check if color is already taken
		for (Car currentcar : cars) {
			if (currentcar.getColor().equals(car.getColor())){
				throw new RacingGameException("Color already taken by another gamer");
			}
		}
		
		if (car.getCurrentDirection() != null && !car.getCurrentDirection().equals(Direction.NONE)){
			throw new RacingGameException("Invalid state of direction while adding it to race");
		}
		
		if (car.getCurrentSpeed() != null && !car.getCurrentSpeed().equals(Speed.NONE)){
			throw new RacingGameException("Invalid state of speed while adding it to race");
		}
		
		car.setCurrentDirection(Direction.STRAIGHT);
		car.setCurrentSpeed(Speed.NONE);
		car.setCurrentOffset(Offset.ASPHALT);
		car.setNrSegmentsTravelled(0);
		cars.add(car);
	}
	
	public boolean isRaceStarted() {
		return (this.getStartedTimestamp() !=null && this.getStartedTimestamp().compareTo(Calendar.getInstance()) <= 0);
	}
	
	public boolean isRaceEnded(){
		return (this.getEndedTimestamp() !=null);
	}
	
	public boolean isRaceActive(){
		if (isRaceStarted() && !isRaceEnded()){
			for (Car car : getCars()){
				if (!car.isCrashed()){
					//at least 1 car is active
					return true;
				}
			}
		}
		return false;
	}
	
	public void startRace() throws RacingGameException{
		if(isRaceStarted()){
			throw new RacingGameException("Race is already started");
		}
		if(isRaceEnded()){
			throw new RacingGameException("Race is already ended");
		}
		if (this.getCars() == null || this.getCars().size() == 0){
			throw new RacingGameException("Race can't start without cars");
		}
		if (this.getCars().size() < 2){
			throw new RacingGameException("Race can't start without at least 2 cars");
		}
		if (this.getLap() == null || this.getLap().getSegments() == null || this.getLap().getSegments().size() == 0){
			throw new RacingGameException("Race can't start without segments");
		}
		
				
		//Start 10 seconds later
		Calendar currentTime = Calendar.getInstance();
		currentTime.add(Calendar.SECOND, this.getSecondsWaitToStart());
		this.setStartedTimestamp(currentTime);
		
	}
	
	
	public void endRace() throws RacingGameException{
		if(!isRaceStarted()){
			throw new RacingGameException("Race is not started");
		}
		if(isRaceEnded()){
			throw new RacingGameException("Race is already ended");
		}
		
		this.setEndedTimestamp(Calendar.getInstance());
	}
	
	
	
	public List<CarColor> getActiveColors(){
		List<CarColor> colors = new ArrayList<>();
		for (Car car : this.getCars()){
			colors.add(car.getColor());
		}
		return colors;
	}
}
