package nl.kromkamp.racinggame.test.pojo;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import com.avaje.ebean.Ebean;
import com.avaje.ebean.EbeanServer;
import nl.kromkamp.racinggame.exception.RacingGameException;
import nl.kromkamp.racinggame.pojo.Car;
import nl.kromkamp.racinggame.pojo.CarColor;
import nl.kromkamp.racinggame.pojo.Direction;
import nl.kromkamp.racinggame.pojo.Lap;
import nl.kromkamp.racinggame.pojo.Offset;
import nl.kromkamp.racinggame.pojo.Race;
import nl.kromkamp.racinggame.pojo.Speed;
import nl.kromkamp.racinggame.pojo.TrackSegment;
import nl.kromkamp.racinggame.pojo.Turn;

public class RaceTest {

	private static Logger logger = Logger.getLogger(RaceTest.class);
	
	@Before
	public void before() {
		server = Ebean.getDefaultServer();
	}
	private EbeanServer server;
	
	@Test
	public void given_a_race_is_saved_when_saved_then_it_should_be_persistend(){
		Race race = new Race();
        server.save(race);
        
        // find by id
        Race race2 = Ebean.find(Race.class, race.getId());
        assertEquals(race2.getId(), race2.getId());        
	}
		
	
	@Test
	public void given_a_race_created_when_the_number_of_laps_is_not_changed_then_the_numvber_of_laps_are_default() throws Exception {
        Race race = new Race();
        assertEquals(race.getNrOfLaps(), Race.DEFAULTNRLAPS);        
    }
	
	
	@Test
	public void given_a_race_is_created_when_1_car_is_added_then_the_persisent_cars_should_be_one() throws RacingGameException {
        Race race = new Race();
        Car car = new Car();
        car.setColor(CarColor.RED);
        car.setDrivername("Robert");
        car.setCurrentDirection(Direction.NONE);
        car.setCurrentSpeed(Speed.NONE);
        race.addCar(car);
        server.save(race);
        
        // find by id
        Race race2 = Ebean.find(Race.class, race.getId());
        assertNotNull(race2);
        assertNotNull(race2.getCars());
        assertTrue(race2.getCars().size() > 0);
        assertTrue(race2.getCars().size() == 1);
    }
	
	
	@Test  (expected = RacingGameException.class)
	public void given_when_2_cars_have_the_same_color_when_they_are_add_to_the_same_race_then_an_exception_is_thrown() throws RacingGameException {
        Race race = new Race();
        Car carRed = new Car();
        carRed.setColor(CarColor.RED);
        carRed.setDrivername("Robert");
        carRed.setCurrentDirection(Direction.NONE);
        carRed.setCurrentSpeed(Speed.NONE);
        race.addCar(carRed);
        server.save(race);
        
        // find by id
        race = Ebean.find(Race.class, race.getId());
        Car carYellow = new Car();
        carYellow.setColor(CarColor.YELLOW);
        carYellow.setDrivername("John");
        carYellow.setCurrentDirection(Direction.NONE);
        carYellow.setCurrentSpeed(Speed.NONE);
        race.addCar(carYellow);
        server.save(race);
        
        race = Ebean.find(Race.class, race.getId());
        Car carYellow2 = new Car();
        carYellow2.setColor(CarColor.YELLOW);
        carYellow2.setDrivername("Bob");
        carYellow2.setCurrentDirection(Direction.NONE);
        carYellow2.setCurrentSpeed(Speed.NONE);
        race.addCar(carYellow2);
	}

	@Test (expected = RacingGameException.class)
	public void give_a_race_is_started_when_no_cars_are_added_then_an_exception_should_be_thrown()  throws RacingGameException {
		Race race = new Race();    
        race.startRace();
	}
	

	@Test
	public void given_2_cars_are_added_to_a_race_when_they_have_different_colors_then_the_race_should_be_started()  throws Exception {
		Race race = new Race();
		race.useDefaultLap();
        
		//create Red car
		Car carRed = new Car();
        carRed.setColor(CarColor.RED);
        carRed.setDrivername("Robert");
        carRed.setCurrentDirection(Direction.NONE);
        carRed.setCurrentSpeed(Speed.NONE);
        race.addCar(carRed);

        //create Yellow car
        Car carYellow = new Car();
        carYellow.setColor(CarColor.YELLOW);
        carYellow.setDrivername("John");
        carYellow.setCurrentDirection(Direction.NONE);
        carYellow.setCurrentSpeed(Speed.NONE);
        race.addCar(carYellow);
        
        race.startRace();
        
        race.setSecondsWaitToStart(1);
        race.startRace();
        assertFalse(race.isRaceStarted());
        Thread.sleep(1100);
        assertTrue(race.isRaceStarted());
	}
	
	
	@Test
	public void given_a_started_race_when_ended_then_no_exception_is_thrown()  throws Exception {
		Race race = new Race();
		race.useSmallLap();
		
		//create Red car
		Car carRed = new Car();
        carRed.setColor(CarColor.RED);
        carRed.setDrivername("Robert");
        carRed.setCurrentDirection(Direction.NONE);
        carRed.setCurrentSpeed(Speed.NONE);
        race.addCar(carRed);

        //create Yellow car
        Car carYellow = new Car();
        carYellow.setColor(CarColor.YELLOW);
        carYellow.setDrivername("John");
        carYellow.setCurrentDirection(Direction.NONE);
        carYellow.setCurrentSpeed(Speed.NONE);
        race.addCar(carYellow);
         
        race.setSecondsWaitToStart(1);
        race.startRace();
        Thread.sleep(1100);
        assertTrue(race.isRaceStarted());
        race.endRace();
        assertTrue(race.isRaceEnded());
	}
	
	
	@Test
	public void given_a_race_when_assigned_the_default_lap_then_the_number_of_segments_should_be_17()  throws Exception {
		Race race = new Race();
		race.useDefaultLap();
		//int defaultSize = race.getLap().getSegments().size();
		server.save(race);
		
		race = Ebean.find(Race.class, race.getId());
		assertNotNull(race.getLap());
		assertNotNull(race.getLap().getSegments());
		assertTrue(race.getLap().getSegments().size() > 0 );
		assertTrue(race.getLap().getSegments().size() == 17);
	}
	
	
	@Test
	public void given_a_race_is_started_when_all_turns_are_done_then_the_car_is_finished() throws RacingGameException  {
		Race race = new Race();
		race.useSmallLap();
		
		//create Red car
		Car carRed = new Car();
        carRed.setColor(CarColor.RED);
        carRed.setDrivername("Robert");
        carRed.setCurrentDirection(Direction.NONE);
        carRed.setCurrentSpeed(Speed.NONE);
        race.addCar(carRed);

        //create Yellow car
        Car carYellow = new Car();
        carYellow.setColor(CarColor.YELLOW);
        carYellow.setDrivername("John");
        carYellow.setCurrentDirection(Direction.NONE);
        carYellow.setCurrentSpeed(Speed.NONE);
        race.addCar(carYellow);
         
        race.setSecondsWaitToStart(0);
        race.startRace();				
		server.save(race);
		
		race = Ebean.find(Race.class, race.getId());
		assertNotNull(race.getLap());
				
		assertEquals(carYellow.getNrSegmentsTravelled(), 0);
		assertEquals(carYellow.getLapsCompleted(), 0);
		
		Turn turn = new Turn(Direction.NONE, Speed.SLOW, carYellow);
		turn.doTurn();
		server.save(turn);
		
		assertEquals(carYellow.getNrSegmentsTravelled(), 0);
		
		
		try{
			turn = new Turn(Direction.NONE, Speed.SLOW, carYellow);
			turn.doTurn();
			server.save(turn);
			Assert.fail();
		} catch (RacingGameException e){
			assertEquals(carYellow.getNrSegmentsTravelled(), 0);
			assertEquals(carYellow.getLapsCompleted(), 0);
		}
		
		turn = new Turn(Direction.STRAIGHT, Speed.NONE, carYellow);
		turn.doTurn();
		server.save(turn);
		
		assertEquals(carYellow.getNrSegmentsTravelled(), 1);
		assertEquals(carYellow.getLapsCompleted(), 0);
		
		turn = new Turn(Direction.NONE, Speed.FAST, carYellow);
		turn.doTurn();
		server.save(turn);
		
		assertEquals(carYellow.getNrSegmentsTravelled(), 1);
		assertEquals(carYellow.getLapsCompleted(), 0);
		
		turn = new Turn(Direction.RIGHT, Speed.NONE, carYellow);
		turn.doTurn();
		server.save(turn);
		
		assertEquals(carYellow.getNrSegmentsTravelled(), 0);
		assertEquals(carYellow.getLapsCompleted(), 1);
		assertEquals(carYellow.getCurrentOffset(), Offset.RIGHTGRASS);
		assertEquals(carYellow.getCurrentSpeed(), Speed.SLOW); // on the grass
		
		try{ 
			turn = new Turn(Direction.LEFT, Speed.NONE, carYellow);
			turn.doTurn();
			server.save(turn);
			Assert.fail();
		} catch (RacingGameException e){
			assertEquals(carYellow.getNrSegmentsTravelled(), 0);
			assertEquals(carYellow.getLapsCompleted(), 1);
			assertEquals(carYellow.getCurrentOffset(), Offset.RIGHTGRASS);
			assertEquals(carYellow.getCurrentSpeed(), Speed.SLOW); // on the grass
			assertFalse(carYellow.isFinished());
			assertTrue(carYellow.getRace().isRaceActive());
		} 
		
		turn = new Turn(Direction.STRAIGHT, Speed.NONE, carYellow);
		turn.doTurn();
		server.save(turn);
		
		assertEquals(carYellow.getNrSegmentsTravelled(), 1);
		assertEquals(carYellow.getLapsCompleted(), 1);
		assertEquals(carYellow.getCurrentOffset(), Offset.RIGHTGRASS);
		
		turn = new Turn(Direction.NONE, Speed.STOPPED, carYellow);
		turn.doTurn();
		server.save(turn);
		
		assertEquals(carYellow.getNrSegmentsTravelled(), 1);
		assertEquals(carYellow.getLapsCompleted(), 1);
		assertEquals(carYellow.getCurrentOffset(), Offset.RIGHTGRASS);
		
		turn = new Turn(Direction.STRAIGHT, Speed.NONE, carYellow);
		turn.doTurn();
		server.save(turn);
		
		assertEquals(carYellow.getNrSegmentsTravelled(), 1);
		assertEquals(carYellow.getLapsCompleted(), 1);
		assertEquals(carYellow.getCurrentOffset(), Offset.RIGHTGRASS);

		turn = new Turn(Direction.NONE, Speed.SLOW, carYellow);
		turn.doTurn();
		server.save(turn);
		
		turn = new Turn(Direction.STRAIGHT, Speed.NONE, carYellow);
		turn.doTurn();
		server.save(turn);
		
		assertEquals(carYellow.getNrSegmentsTravelled(), 2);
		assertEquals(carYellow.getLapsCompleted(), 1);
		assertEquals(carYellow.getCurrentOffset(), Offset.ASPHALT);
		
		turn = new Turn(Direction.STRAIGHT, Speed.NONE, carYellow);
		turn.doTurn();
		server.save(turn);
		
		assertEquals(carYellow.getNrSegmentsTravelled(), 0);
		assertEquals(carYellow.getLapsCompleted(), 2);
		assertEquals(carYellow.getCurrentOffset(), Offset.ASPHALT);
		
		turn = new Turn(Direction.STRAIGHT, Speed.NONE, carYellow);
		turn.doTurn();
		server.save(turn);
		
		turn = new Turn(Direction.RIGHT, Speed.NONE, carYellow);
		turn.doTurn();
		server.save(turn);
		
		turn = new Turn(Direction.STRAIGHT, Speed.NONE, carYellow);
		turn.doTurn();
		server.save(turn);
		
		//FINISHED
		assertEquals(carYellow.getNrSegmentsTravelled(), 0);
		assertEquals(carYellow.getLapsCompleted(), 3);
		assertTrue(carYellow.isFinished());	
	}
	
	
	@Test
	public void given_persistent_data_when_the_database_contact_is_cleaned_then_all_tables_should_be_empty() {
		//test clean up data
		server.deleteAllPermanent(server.find(TrackSegment.class).findList()); 
		server.deleteAllPermanent(server.find(Lap.class).findList());
		server.deleteAllPermanent(server.find(Turn.class).findList());
		server.deleteAllPermanent(server.find(Race.class).findList());
		server.deleteAllPermanent(server.find(Car.class).findList());
		

		assertTrue(server.find(TrackSegment.class).findList().isEmpty());
		assertTrue(server.find(Lap.class).findList().isEmpty());
		assertTrue(server.find(Turn.class).findList().isEmpty());
		assertTrue(server.find(Race.class).findList().isEmpty());
		assertTrue(server.find(Car.class).findList().isEmpty());		
	}
	
	
	@Test
	public void given_a_race_is_started_when_a_car_hits_the_wall_then_the_car_is_crashed() throws RacingGameException  {
		Race race = new Race();
		race.useSmallLap();
		
		//create Red car
		Car carRed = new Car();
        carRed.setColor(CarColor.RED);
        carRed.setDrivername("Robert");
        carRed.setCurrentDirection(Direction.NONE);
        carRed.setCurrentSpeed(Speed.NONE);
        race.addCar(carRed);

        //create Yellow car
        Car carYellow = new Car();
        carYellow.setColor(CarColor.YELLOW);
        carYellow.setDrivername("John");
        carYellow.setCurrentDirection(Direction.NONE);
        carYellow.setCurrentSpeed(Speed.NONE);
        race.addCar(carYellow);
        
        race.setSecondsWaitToStart(0);
        race.startRace();				
		server.save(race);
		
		assertEquals(carYellow.getNrSegmentsTravelled(), 0);
		assertEquals(carYellow.getLapsCompleted(), 0);
		
		Turn turn = new Turn(Direction.NONE, Speed.SLOW, carYellow);
		turn.doTurn();
		server.save(turn);
		
		turn = new Turn(Direction.LEFT, Speed.NONE, carYellow);
		turn.doTurn();
		server.save(turn);
		
		assertEquals(carYellow.getCurrentOffset(), Offset.LEFTGRASS);
		
		turn = new Turn(Direction.LEFT, Speed.NONE, carYellow);
		turn.doTurn();
		server.save(turn);
		//server.save(carYellow);
		
		assertEquals(carYellow.getCurrentOffset(), Offset.LEFTWALL);
		
		assertTrue(carYellow.isCrashed());
		assertEquals(carYellow.getNrSegmentsTravelled(), 1);
		assertEquals(carYellow.getLapsCompleted(), 0);
		assertTrue(carYellow.getRace().isRaceActive());
		
		
		turn = new Turn(Direction.NONE, Speed.SLOW, carRed);
		turn.doTurn();
		server.save(turn);
		assertTrue(carYellow.isCrashed());
		
		turn = new Turn(Direction.LEFT, Speed.NONE, carRed);
		turn.doTurn();
		server.save(turn);
		
		turn = new Turn(Direction.LEFT, Speed.NONE, carRed);
		turn.doTurn();
		server.save(turn);
				
		assertTrue(carRed.isCrashed());
		assertTrue(carYellow.isCrashed());
		assertFalse(carRed.getRace().isRaceActive());
		
	}

	
	@Test (expected = RacingGameException.class)
	public void testTurnWhileRaceIsNotStarted()  throws Exception {
		Race race = new Race();
		race.useDefaultLap();
		
		//create Red car
		Car carRed = new Car();
        carRed.setColor(CarColor.RED);
        carRed.setDrivername("Robert");
        carRed.setCurrentDirection(Direction.NONE);
        carRed.setCurrentSpeed(Speed.NONE);
        race.addCar(carRed);

        //create Yellow car
        Car carYellow = new Car();
        carYellow.setColor(CarColor.YELLOW);
        carYellow.setDrivername("John");
        carYellow.setCurrentDirection(Direction.NONE);
        carYellow.setCurrentSpeed(Speed.NONE);
        race.addCar(carYellow);
         
        race.setSecondsWaitToStart(0);
		server.save(race);
		
		race = Ebean.find(Race.class, race.getId());
		assertNotNull(race.getLap());
		
		Turn turn = new Turn(Direction.NONE, Speed.SLOW, carYellow);
		turn.doTurn();
		server.save(turn);
	}
	
	
	
	@Test
	public void given_a_race_is_created_when_segments_are_added_then_laps_size_is_equal_to_the_segments_added() throws Exception {	 
    	Race race = new Race();
    	Lap lap = new Lap();
		
    	List<TrackSegment> segments = new ArrayList<TrackSegment>();
		segments.add(new TrackSegment(Direction.STRAIGHT));
		segments.add(new TrackSegment(Direction.RIGHT));
		segments.add(new TrackSegment(Direction.STRAIGHT));
		segments.add(new TrackSegment(Direction.LEFT));
		segments.add(new TrackSegment(Direction.RIGHT));
		
		lap.setSegments(segments);
		race.setLap(lap);
		
		assertNotNull(race.getLap());
		assertNotNull(race.getLap().getSegments());
		assertTrue(race.getLap().getSegments().size() == 5);
    }	
}
