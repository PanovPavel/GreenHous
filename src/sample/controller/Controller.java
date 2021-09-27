package sample.controller;

import java.util.*;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.util.Duration;
import sample.GreenhouseControls;

public class Controller {
    @FXML
    Label FXMLlight;
    @FXML
    Label FXMLwater;
    @FXML
    Label FXMLtemperature;
    @FXML
    Label AllOutput;

    String strContains = " ";
    public void initialize(){
        new Thread(()-> {
            GreenhouseControls gc2 = new GreenhouseControls();
            gc2.addEvent(gc2.new Bell(900));
            Event[] eventList = {
                    gc2.new ThermostatNight(0),
                    gc2.new LightOn(200),
                    gc2.new LightOff(400),
                    gc2.new WaterOn(600),
                    gc2.new WaterOff(800),
                    gc2.new ThermostatDay(1400)
            };
            gc2.addEvent(gc2.new Restart(2000, eventList));
            try {
                while (true) {
                    strContains = gc2.runGui();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                            AllOutput.setText(strContains);
                            if(strContains == "Light is on") FXMLlight.setText(strContains);
                                else FXMLlight.setText("Light off");
                            if(strContains == "Greenhouse water is on") FXMLwater.setText(strContains);
                                else FXMLwater.setText("Greenhouse water is off");
                            if(strContains == "Thermostat on day setting") FXMLtemperature.setText(strContains);
                                else FXMLtemperature.setText("Thermostat on night setting");
                    }
                });
            }
        };
        Timer tm = new Timer();
        tm.schedule(task, 200, 200);
    }
    private List<Event> eventList = new ArrayList<Event>();
    public void addEvent(Event c) {
        eventList.add(c);
    }

    public void run() throws InterruptedException {
        while (eventList.size() > 0)
            for (Event e : new ArrayList<Event>(eventList))
                if (e.ready()) {
                    System.out.println(e);
                    Thread.sleep(1000);
                    e.action();
                    eventList.remove(e);
                }
    }
    public String runGui() throws InterruptedException {
        while (eventList.size() > 0)
            for (Event e : new ArrayList<Event>(eventList))
                if (e.ready()) {
                    String str = String.valueOf(e);
                    Thread.sleep(1000);
                    e.action();
                    eventList.remove(e);
                    return str;
                }return "0";
    }
}