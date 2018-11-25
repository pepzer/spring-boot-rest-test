package org.pepzer.spring_boot_sample;

public class Measurement {

    private final String timestamp;
    private final long value;
    private final String status;
    private final String node;

    public Measurement(String timestamp, long value,
                       String status, String node) {

        this.timestamp = timestamp;
        this.value = value;
        this.status = status;
        this.node = node;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public long getValue() {
        return value;
    }

    public String getStatus() {
        return status;
    }

    public String getNode() {
        return node;
    }
}
