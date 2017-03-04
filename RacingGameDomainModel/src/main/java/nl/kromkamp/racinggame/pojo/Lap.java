package nl.kromkamp.racinggame.pojo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class Lap {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@OneToMany (cascade=CascadeType.ALL)
	@JsonManagedReference
	private List<TrackSegment> segments = new ArrayList<>();
	
	@OneToOne
	@JsonBackReference
	@JoinColumn(name="race")
	private Race race;
	
	private String circuitName;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public Race getRace() {
		return race;
	}

	public void setRace(Race race) {
		this.race = race;
	}

	public String getCircuitName() {
		return circuitName;
	}

	public void setCircuitName(String circuitName) {
		this.circuitName = circuitName;
	}

	public List<TrackSegment> getSegments() {
		return segments;
	}

	public void setSegments(List<TrackSegment> segments) {
		this.segments = segments;
	}
	
	public boolean isLastSegmentOfLap(int nrOfDoneSegments){
		return (nrOfDoneSegments == segments.size());
	}
}
