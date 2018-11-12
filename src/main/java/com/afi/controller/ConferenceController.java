package com.afi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.afi.JavaFXController;
import com.afi.model.Conference;

@RestController
@RequestMapping(value="/conf")
public class ConferenceController {
	
	@Autowired
	private JavaFXController mainController;

	@RequestMapping(value="/conference", method=RequestMethod.GET)
	public Conference getConference() {
		return mainController.getSelectedConference();
	}

}