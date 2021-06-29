package com.jibs.neo4j.practice.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jibs.neo4j.practice.domain.Sensor;
import com.jibs.neo4j.practice.domain.SensorType;
import com.jibs.neo4j.practice.repository.SensorRepository;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/sensors")
public class SensorController {

	private final SensorRepository repository;

	@Autowired
	public SensorController(SensorRepository repository) {
		this.repository = repository;
	}

	// User story :: As a user I'd like to create a new sensor
	@ApiOperation(value = "Create a sensor", response = Sensor.class)
	@PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Sensor> createSensor(@RequestBody Sensor sensor) {
		Sensor savedSensor = repository.save(sensor);
		return new ResponseEntity<Sensor>(savedSensor, HttpStatus.CREATED);
	}

	// User story :: As a user I'd like to query for all existing sensors
	// User story :: As a user I'd like to query for sensors of a certain type.
	// Example: sensor_type=ELECTRICITY
	@ApiOperation(value = "Get all sensors", response = Sensor.class, responseContainer = "List")
	@GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Sensor>> getSensors(
			@RequestParam(name = "sensor_type", required = false) SensorType sensorType) {
		List<Sensor> sensors = repository.findAll();

		if (sensorType != null) {
			sensors = sensors.stream().filter(sensor -> sensor.getLastReadings().keySet().contains(sensorType))
					.collect(Collectors.toList());
		}

		return new ResponseEntity<List<Sensor>>(sensors, HttpStatus.OK);
	}
}
