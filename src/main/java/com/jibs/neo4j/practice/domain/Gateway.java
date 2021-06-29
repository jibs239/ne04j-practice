package com.jibs.neo4j.practice.domain;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Node
public class Gateway {

	@Id
	private final long id;

	@Relationship(type = "CONNECTED_TO")
	private final List<Sensor> sensors;

	public Gateway(long id, List<Sensor> sensors) {
		this.id = id;
		if (sensors == null) {
			sensors = new ArrayList<Sensor>();
		}
		this.sensors = sensors;
	}

	public List<Sensor> getSensors() {
		return sensors;
	}

	public void addSensor(Sensor sensor) {
		this.sensors.add(sensor);
	}

	public long getId() {
		return id;
	}
}
