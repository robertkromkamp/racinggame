package nl.kromkamp.racinggame.server.service;

import java.util.Calendar;
import java.util.List;

import org.springframework.stereotype.Service;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.EbeanServer;

import nl.kromkamp.racinggame.exception.RacingGameException;
import nl.kromkamp.racinggame.pojo.Car;
import nl.kromkamp.racinggame.pojo.CarColor;
import nl.kromkamp.racinggame.pojo.Direction;
import nl.kromkamp.racinggame.pojo.Driveraction;
import nl.kromkamp.racinggame.pojo.Lap;
import nl.kromkamp.racinggame.pojo.Race;
import nl.kromkamp.racinggame.pojo.Speed;
import nl.kromkamp.racinggame.pojo.TrackSegment;
import nl.kromkamp.racinggame.pojo.Turn;

@Service
public class RacingGameService {
	
	private EbeanServer ebeanServer;
	
	public RacingGameService() {
		ebeanServer = Ebean.getDefaultServer();
	}
	
	public EbeanServer getEbeanServer() {
		return ebeanServer;
	}

	public void setEbeanServer(EbeanServer ebeanServer) {
		this.ebeanServer = ebeanServer;
	}
	
	/**
	 * Add a car to the race
	 * 
	 * @param car contains car to add to the race
	 * @return Race
	 * @throws RacingGameException
	 */
	public Race registerToRace(Car car) throws RacingGameException{
		Race race = this.getRace();
		
		if (race == null){
			race = new Race();
			//use for now a default Lap
			race.useSmallLap();
		}
		
		
		//pick a random color
		if(race.getCars().size() == CarColor.values().length){
			throw new RacingGameException("Maximum number of cars already in race, no unique color available");
		}
		boolean uniqueColorFound = false;
		List<CarColor> activeColors = race.getActiveColors();
		while (!uniqueColorFound){
			CarColor color = CarColor.getRandomColor();
			if (!activeColors.contains(color)){
				car.setColor(color);
				uniqueColorFound = true;
			}
		}
		
		//persist car and race
		race.addCar(car);
		ebeanServer.save(race);
		return race;
	}
	
	
	
	/**
	 * Start the race
	 * 
	 * @return Race
	 * @throws RacingGameException
	 */
	public Race startRace() throws RacingGameException{
		Race race = getRace();
		race.startRace();
		ebeanServer.save(race);
		return race;
	}
	
	
	
	/**
	 * Find the persistent race
	 * 
	 * @return Race or null if persistant race is not found
	 */
	public Race getRace() {
		List<Race> races = ebeanServer.find(Race.class).findList();
				
		//find active race, max 1 race active
		for (Race persistantrace : races){
			return persistantrace;
		}	
		return null;
	}
	
	
	
	/**
	 * Delete persistant race
	 */
	public void deleteRace(){
		ebeanServer.deleteAllPermanent(ebeanServer.find(TrackSegment.class).findList()); 
		ebeanServer.deleteAllPermanent(ebeanServer.find(Lap.class).findList());
		ebeanServer.deleteAllPermanent(ebeanServer.find(Turn.class).findList());
		ebeanServer.deleteAllPermanent(ebeanServer.find(Race.class).findList());
		ebeanServer.deleteAllPermanent(ebeanServer.find(Car.class).findList());
	}
	
	
	
	/**
	 * Get persistent cars ordered by lapsCompleted, nrSegments, traveled and finishedTimestamp 
	 * 
	 * @return List<Car>
	 */
	public List<Car> getCars() {
		return ebeanServer.find(Car.class)
				.orderBy().desc("lapsCompleted")
				.orderBy().desc("nrSegmentsTravelled")
				.orderBy().desc("finishedTimestamp")
				.findList();
	}	
	
	
	/**
	 * Do driver action, by example turn left
	 * 
	 * @param driveraction
	 * @return Car
	 * @throws RacingGameException
	 */
	public Car navigate(Driveraction driveraction) throws RacingGameException {
		
		Turn turn = null;
		Car car = ebeanServer.find(Car.class,driveraction.getCarId());
				
		Direction wantedDirection;
		Speed wantedSpeed;
		
		switch(driveraction.getAction()) {
		    case "LEFT": 
		    case "STRAIGHT": 
		    case "RIGHT":
		    	wantedSpeed = Speed.NONE;
		    	wantedDirection = Direction.valueOf(driveraction.getAction());
		        turn = new Turn(wantedDirection, wantedSpeed, car);
	 	        break;
		    case "STOPPED":
		    case "SLOW":
		    case "FAST":
		    	wantedSpeed = Speed.valueOf(driveraction.getAction());;
		    	wantedDirection = Direction.NONE;
		        turn = new Turn(wantedDirection, wantedSpeed, car);
	 	        break;
		    default: 
		        //default
	    }
		
		if (turn == null){
			throw new RacingGameException("Invalid trun, can't create instance of it");
		}
		turn.doTurn();
        ebeanServer.save(turn);
        ebeanServer.save(car);
		
		return ebeanServer.find(Car.class,driveraction.getCarId());
	}
	
	
	/**
	 * Set the current race states
	 * check if race should be ended
	 * check if race data must be deleted after 30 seconds of the race
	 * 
	 * @throws RacingGameException
	 */
	public void setRaceStatus() throws RacingGameException{
		if (getRace() != null && getRace().isRaceActive()){
			//get the leader of the race, an ended race has at least 2 cars
			Car car = getCars().get(0);
			//check if leader is finished and 5 minutes is elapsed after that finish time
			if (car.isFinished()){
				Calendar finishedTimestamp = car.getFinishedTimestamp();
				Calendar currentTimestamp = Calendar.getInstance();
				currentTimestamp.add(Calendar.MINUTE, -5);
				if (currentTimestamp.after(finishedTimestamp)){
					getRace().endRace();
				}
			} else {
				//if 15 minutes after start -> end race
				Calendar currentTimestamp = Calendar.getInstance();
				currentTimestamp.add(Calendar.MINUTE, -15);
				if (currentTimestamp.after(getRace().getStartedTimestamp())){
					getRace().endRace();
				}
			}
		} else if (getRace() != null && getRace().isRaceEnded()){
			Calendar currentTimestamp = Calendar.getInstance();
			currentTimestamp.add(Calendar.SECOND, -30);
			//delete persistence after 30 seconds when the race is ended
			if (currentTimestamp.after(getRace().getEndedTimestamp())){
				this.deleteRace();
			}
		}
	}
}
