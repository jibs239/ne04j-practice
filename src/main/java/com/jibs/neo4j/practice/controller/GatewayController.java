package com.jibs.neo4j.practice.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jibs.neo4j.practice.domain.Gateway;
import com.jibs.neo4j.practice.domain.Sensor;
import com.jibs.neo4j.practice.domain.SensorType;
import com.jibs.neo4j.practice.repository.GatewayRepository;
import com.jibs.neo4j.practice.repository.SensorRepository;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/gateways")
public class GatewayController {
	private final GatewayRepository gatewayRepository;

	private final SensorRepository sensorRepository;

	@Autowired
	public GatewayController(GatewayRepository gatewayRepository, SensorRepository sensorRepository) {
		this.gatewayRepository = gatewayRepository;
		this.sensorRepository = sensorRepository;
	}

	// User story :: As a user I'd like to create a new gateway
	@ApiOperation(value = "Create a gateway", response = Gateway.class)
	@PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Gateway> createGateway(@RequestBody Gateway gateway) {
		Gateway savedGateway = gatewayRepository.save(gateway);
		return new ResponseEntity<Gateway>(savedGateway, HttpStatus.CREATED);
	}

	// User story :: As a user I'd like to query for all gateways
	// User story :: As a user I'd like to query a gateway that has electrical
	// sensors attached to it. Example: sensor_type=ELECTRICITY
	@ApiOperation(value = "Get all gateways", response = Gateway.class, responseContainer = "List")
	@GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Gateway>> getGateways(
			@RequestParam(name = "sensor_type", required = false) SensorType sensorType) {
		List<Gateway> gateways = gatewayRepository.findAll();

		if (sensorType != null) {
			gateways = gateways.stream()
					.filter(gateway -> gateway.getSensors().stream()
							.anyMatch(sensor -> sensor.getLastReadings().keySet().contains(sensorType)))
					.collect(Collectors.toList());
		}

		return new ResponseEntity<List<Gateway>>(gateways, HttpStatus.OK);
	}

	// User story :: As a user I'd like to query all sensors assigned to an existing
	// gateway
	@ApiOperation(value = "Get all sensors connected to gateway", response = Sensor.class, responseContainer = "List")
	@GetMapping(value = "/{id}/sensors", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Sensor>> getSensors(@PathVariable long id) {
		Gateway gatewayNode = gatewayRepository.findById(id).get();
		return new ResponseEntity<List<Sensor>>(gatewayNode.getSensors(), HttpStatus.OK);
	}

	// User story :: As a user I'd like to assign a sensor to a given gateway
	@ApiOperation(value = "Add sensor to gateway")
	@PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Gateway> assignSensor(@PathVariable long id, @RequestParam("sensor_id") long sensorId) {
		Gateway gatewayNode = gatewayRepository.findById(id).get();
		
		Sensor sensorNode = sensorRepository.findById(sensorId).get();
		gatewayNode.addSensor(sensorNode);
		
		Gateway updatedGatewayNode = gatewayRepository.save(gatewayNode);
		return new ResponseEntity<Gateway>(updatedGatewayNode, HttpStatus.OK);
	}
}
