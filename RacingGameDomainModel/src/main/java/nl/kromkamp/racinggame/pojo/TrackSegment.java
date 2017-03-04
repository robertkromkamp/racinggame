package nl.kromkamp.racinggame.pojo;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import com.fasterxml.jackson.annotation.JsonBackReference;


@Entity
public class TrackSegment {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@ManyToOne (cascade=CascadeType.ALL)
	@JsonBackReference
	private Lap lap;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public Lap getLap() {
		return lap;
	}

	public void setLap(Lap lap) {
		this.lap = lap;
	}

	public TrackSegment(Direction direction) {
		this.direction = direction;
	}
	
	private Direction direction;

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}
	
	
	/**
	 * Update the current segment to next segemet
	 */
	public static void updateSegment(Car car){
		if (!car.isFinished() && !car.isCrashed() && car.getRace().isRaceActive()){
			car.setNrSegmentsTravelled(car.getNrSegmentsTravelled()+1);
			if (car.getNrSegmentsTravelled() == car.getRace().getLap().getSegments().size()){
				car.setNrSegmentsTravelled(0);
				car.setLapsCompleted(car.getLapsCompleted()+1);
				
				if (car.getLapsCompleted() == car.getRace().getNrOfLaps()){
					car.finishCar();
				}
			}
		}
	}
}
