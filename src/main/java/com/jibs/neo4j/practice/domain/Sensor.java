package com.jibs.neo4j.practice.domain;

import java.util.Map;

import org.springframework.data.neo4j.core.schema.CompositeProperty;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node
public class Sensor {

	@Id
	private final long id;

	@CompositeProperty
	private final Map<SensorType, String> lastReadings;

	public Sensor(long id, Map<SensorType, String> lastReadings) {
		this.id = id;
		this.lastReadings = lastReadings;
	}

	public long getId() {
		return id;
	}

	public Map<SensorType, String> getLastReadings() {
		return lastReadings;
	}
}
