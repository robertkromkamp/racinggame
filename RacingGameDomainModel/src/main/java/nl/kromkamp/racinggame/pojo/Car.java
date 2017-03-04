package nl.kromkamp.racinggame.pojo;

import java.util.Calendar;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class Car {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Temporal(TemporalType.TIMESTAMP)
	private java.util.Calendar finishedTimestamp;
	
	@Enumerated(EnumType.ORDINAL)
	private CarColor color;
		
	@Enumerated(EnumType.ORDINAL)
	private Speed currentSpeed;

	@Enumerated(EnumType.ORDINAL)
	private Direction currentDirection;
	
	//position on the road
	@Enumerated(EnumType.ORDINAL)
	private Offset currentOffset;
	
	@ManyToOne (cascade=CascadeType.ALL)
	@JsonBackReference
	private Race race;
	
	private String driverName;
	
	private int nrSegmentsTravelled;
	
	private int lapsCompleted;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public int getLapsCompleted() {
		return lapsCompleted;
	}

	public void setLapsCompleted(int lapsCompleted) {
		this.lapsCompleted = lapsCompleted;
	}

	public java.util.Calendar getFinishedTimestamp() {
		return finishedTimestamp;
	}

	public void setFinishedTimestamp(java.util.Calendar finishedTimestamp) {
		this.finishedTimestamp = finishedTimestamp;
	}
	
	public CarColor getColor() {
		return color;
	}

	public void setColor(CarColor color) {
		this.color = color;
	}

	public String getDrivername() {
		return driverName;
	}

	public void setDrivername(String driverName) {
		this.driverName = driverName;
	}

	public int getNrSegmentsTravelled() {
		return nrSegmentsTravelled;
	}

	public void setNrSegmentsTravelled(int nrSegmentsTravelled) {
		this.nrSegmentsTravelled = nrSegmentsTravelled;
	}

	
	public Speed getCurrentSpeed() {
		return currentSpeed;
	}

	public void setCurrentSpeed(Speed currentSpeed) {
		this.currentSpeed = currentSpeed;
	}
	
	public Direction getCurrentDirection() {
		return currentDirection;
	}

	public void setCurrentDirection(Direction currentDirection) {
		this.currentDirection = currentDirection;
	}

	public Offset getCurrentOffset() {
		return currentOffset;
	}

	public void setCurrentOffset(Offset currentOffset) {
		this.currentOffset = currentOffset;
	}

	public Race getRace() {
		return race;
	}

	public void setRace(Race race) {
		this.race = race;
	}
	
	public boolean isCrashed(){
		return (this.getCurrentOffset() == Offset.LEFTWALL || this.getCurrentOffset() == Offset.RIGHTWALL);
	}
	
	public boolean isFinished(){
		return (this.getFinishedTimestamp() != null);
	}
	
	

	
	
	/**
	 * Set the car states to finished based on the current timestap
	 */
	public void finishCar(){
		this.setFinishedTimestamp(Calendar.getInstance());
	}
	
	
	public Direction getNextDirection(){
		if (this.getRace().getLap().isLastSegmentOfLap(this.getNrSegmentsTravelled())){
			return this.getRace().getLap().getSegments().get(0).getDirection();
		} 
		return this.getRace().getLap().getSegments().get(this.getNrSegmentsTravelled()).getDirection();
	}
	
}
