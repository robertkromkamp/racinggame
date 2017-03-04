package nl.kromkamp.racinggame.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import nl.kromkamp.racinggame.exception.RacingGameException;
import nl.kromkamp.racinggame.server.service.RacingGameService;
@Controller

public class RacingGameController {

	RacingGameService racingGameService;
	 
    @Autowired
    public void setRacingGameService(RacingGameService racingGameService) {
        this.racingGameService = racingGameService;
    }
	
	@RequestMapping("/")
    public String startpage(Model model) {
		model.addAttribute("cars", racingGameService.getCars());
		return "index";
    }
	
	@RequestMapping("/carsandracestatus")
    public String getCarsAndRaceStatus(Model model) throws RacingGameException {
		racingGameService.setRaceStatus();
		model.addAttribute("cars", racingGameService.getCars());
		return "overviewcars :: carsFragment";
    }
	
	@RequestMapping("/deleterace")
    public String deleteRace(Model model) {
		System.out.println("Delete page");
		racingGameService.deleteRace();
		model.addAttribute("cars", racingGameService.getCars());
		return "index";
    }
	
}
