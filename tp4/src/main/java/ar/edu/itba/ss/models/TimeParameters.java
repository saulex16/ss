package ar.edu.itba.ss.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TimeParameters {

    @JsonProperty("dt")
    private double dt;

    @JsonProperty("print_i")
    private int printI;


    @JsonProperty("dt_units")
    private String dtUnits;

    @JsonProperty("stop")
    private double stop;

    @JsonProperty("stop_units")
    private String stopUnits;

    @JsonProperty("start")
    private double start;

    @JsonProperty("start_units")
    private String startUnits;

    public TimeParameters() {
    }

    public double getDt() {
        return TimeUnits.fromString(dtUnits).toSeconds(dt);
    }

    public int getPrintI() {
        return printI;
    }

    public String getDtUnits() {
        return dtUnits;
    }

    public double getStop() {
        double stop = TimeUnits.fromString(stopUnits).toSeconds(this.stop);
        double dt = getDt();
        if(dt > stop)
            throw new RuntimeException("dt must be less than stop");
        return stop;
    }

    public String getStopUnits() {
        return stopUnits;
    }

    public double getStart() {
        return TimeUnits.fromString(startUnits).toSeconds(this.start);
    }

    public String getStartUnits() {
        return startUnits;
    }

    public double getRawStart() {
        return this.start;
    }
}
