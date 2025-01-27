package ar.edu.itba.ss.simulation.algorithms;

import ar.edu.itba.ss.simulation.events.EventListener;
import ar.edu.itba.ss.simulation.events.EventsQueue;

public interface Algorithm<P extends AlgorithmParameters> {

    void calculate(P params, EventListener eventListener);
}