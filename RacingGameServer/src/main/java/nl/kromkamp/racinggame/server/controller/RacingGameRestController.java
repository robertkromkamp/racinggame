package nl.kromkamp.racinggame.server.controller;

import org.springframework.web.bind.annotation.RestController;

import nl.kromkamp.racinggame.exception.RacingGameException;
import nl.kromkamp.racinggame.pojo.Car;
import nl.kromkamp.racinggame.pojo.Driveraction;
import nl.kromkamp.racinggame.server.service.RacingGameService;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class RacingGameRestController {

	RacingGameService racingGameService;
	 
    @Autowired
    public void setRacingGameService(RacingGameService racingGameService) {
        this.racingGameService = racingGameService;
    }
	
	@RequestMapping("/startrace")
    public ResponseEntity<?> startRace(Model model) {
		try {
			racingGameService.startRace();
		} catch (RacingGameException e) {
    		return ResponseEntity.badRequest().body(e.getMessage());
		}
		return ResponseEntity.ok().body("");
    }
        
    
    @PostMapping("/register")
    public ResponseEntity<?> registerByAjax(@Valid @RequestBody Car car, Errors errors, Model model) {
    	model.addAttribute("drivername", car.getDrivername());
    	model.addAttribute("car", car);
    	    	
    	try{
    		racingGameService.registerToRace(car);
    	} catch (Exception e){
    		return ResponseEntity.badRequest().body(e.getMessage());
    	}
    	return ResponseEntity.ok(car);

    }
    
    
    @PostMapping("/navigate")
    public ResponseEntity<?> navigateByAjax(@Valid @RequestBody Driveraction driveraction, Errors errors, Model model) {
    	try{
    		Car car = racingGameService.navigate(driveraction);
    		return ResponseEntity.ok(car);
    	} catch (Exception e){
    		return ResponseEntity.badRequest().body(e.getMessage());
    	}
    }
}	
