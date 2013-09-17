/*
 * Owl Platform
 * Copyright (C) 2012 Robert Moore and the Owl Platform
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *  
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *  
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package com.owlplatform.demo;

import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.util.Collection;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.owlplatform.worldmodel.Attribute;
import com.owlplatform.worldmodel.client.ClientWorldConnection;
import com.owlplatform.worldmodel.client.StepResponse;
import com.owlplatform.worldmodel.client.WorldState;
import com.owlplatform.worldmodel.types.BooleanConverter;
import com.owlplatform.worldmodel.types.DataConverter;
import com.owlplatform.worldmodel.types.TypeConverter;
import com.thoughtworks.xstream.XStream;

/**
 * @author Robert Moore
 * 
 */
public class MakerDemo extends Thread {
  
  
  private static final String CHAIR_STATE = "closed";
  private static final String LIGHT_STATE = "on";
  private static final String FLOOD_STATE = "wet";
  private static final String MAT_STATE = "closed";
  private static final String MOBILITY_STATE = "closed";
  

  private static final Logger log = LoggerFactory.getLogger(MakerDemo.class);

  public static void main(String[] args) throws Throwable {
    if (args.length < 1) {
      System.err.println("Requires: <Config File>");
      return;
    }

    File configFile = new File(args[0]);
    FileInputStream fin = new FileInputStream(configFile);
    MakerDemoConfig config = (MakerDemoConfig) new XStream().fromXML(fin);
    fin.close();
    MakerDemo md = new MakerDemo(config);

    md.start();

  }

  private final MakerDemoConfig config;
  private ClientWorldConnection worldModel = new ClientWorldConnection();

  private JFrame mainFrame = new JFrame("Owl Platform @ MakerFaire Bay Area 2013");

  private ChestPanel chestPanel = new ChestPanel();
  private StepResponse doorResponse = null;
  private FloodPanel floodPanel = new FloodPanel();
  private StepResponse floodResponse = null;
  private PowerPanel powerPanel = new PowerPanel();
  private StepResponse powerResponse = null;
  private ChairPanel chairPanel = new ChairPanel();
  private StepResponse chairResponse = null;
  private ReminderPanel walletPanel = new ReminderPanel();
  private StepResponse walletResponse = null;
  private LogoPanel logoPanel = new LogoPanel();
  private StepResponse logoResponse = null;

  private boolean keepRunning = true;

  public MakerDemo(final MakerDemoConfig config) {
    super();
    this.config = config;

    Runtime.getRuntime().addShutdownHook(new Thread() {
      public void run() {
        MakerDemo.this.shutdown();
      }
    });
  }

  public void init() {
    this.setupWMConnection();

    this.mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.buildPanels();
    this.layoutFrame();
    this.logoPanel.setMoving(true);

  }

  private void setupWMConnection() {
    this.worldModel.setHost(this.config.getWmHost());
    this.worldModel.setPort(this.config.getWmPort());
    this.worldModel.connect(10000);
    log.debug("Connected to {}", this.worldModel);

    this.doorResponse = this.worldModel.getStreamRequest(
        this.config.getWelcomeMatId(), System.currentTimeMillis(), 0, MAT_STATE);
    this.floodResponse = this.worldModel.getStreamRequest(
        this.config.getWaterId(), System.currentTimeMillis(), 0, FLOOD_STATE);
    this.powerResponse = this.worldModel.getStreamRequest(
        this.config.getPowerId(), System.currentTimeMillis(), 0, LIGHT_STATE);
    this.chairResponse = this.worldModel.getStreamRequest(
        this.config.getChairId(), System.currentTimeMillis(), 0, CHAIR_STATE);
    this.walletResponse = this.worldModel
        .getStreamRequest(
            "^(" + this.config.getKeysId() + "|" + this.config.getWalletId()
                + ")$", System.currentTimeMillis(), 0, "missing");
    this.logoResponse = this.worldModel.getStreamRequest(
        this.config.getLogoId(), System.currentTimeMillis(), 0, MOBILITY_STATE);
  }

  public void run() {
    this.init();
    while (this.keepRunning) {
      try {
        // Wallet and Keys sensor
        if (this.walletResponse.hasNext()) {
          log.debug("Reminder changed state.");
          WorldState state = this.walletResponse.next();
          Collection<Attribute> attributes = state.getState(this.config
              .getWalletId());
          if (attributes != null) {
            for (Attribute attr : attributes) {
              if (attr.getAttributeName().equalsIgnoreCase("missing")) {
                Boolean newValue = BooleanConverter.get()
                    .decode(attr.getData());
                if (newValue.booleanValue()) {
                  log.info("[Wallet] Don't forget the wallet!");
                  // this.walletPanel.setNeedItems(true);
                  this.walletPanel.setWalletIsMissing(true);
                  if (this.chestPanel.isOpen()) {
                    this.walletPanel.setNeedItems(true);
                  } else {
                    this.walletPanel.setNeedItems(false);
                  }
                } else {
                  log.info("[Wallet] Picked up your wallet.");
                  this.walletPanel.setWalletIsMissing(false);
                }

              }
            }
          }
          attributes = state.getState(this.config.getKeysId());
          if (attributes != null) {
            for (Attribute attr : attributes) {
              if (attr.getAttributeName().equalsIgnoreCase("missing")) {
                Boolean newValue = BooleanConverter.get()
                    .decode(attr.getData());
                if (newValue.booleanValue()) {
                  log.info("[Keys] Don't forget the keys!");
                  // this.walletPanel.setNeedItems(true);
                  this.walletPanel.setKeyIsMissing(true);
                  if (this.chestPanel.isOpen()) {
                    this.walletPanel.setNeedItems(true);
                  } else {
                    this.walletPanel.setNeedItems(false);
                  }
                } else {
                  log.info("[Keys] Picked up your keys.");
                  this.walletPanel.setKeyIsMissing(false);
                }

              }
            }
          }
        }

        // Welcome mat
        if (this.doorResponse.hasNext()) {
          log.debug("Chest changed state.");
          WorldState state = this.doorResponse.next();
          Collection<Attribute> attributes = state.getState(this.config
              .getWelcomeMatId());
          for (Attribute attr : attributes) {
            if (attr.getAttributeName().equalsIgnoreCase(MAT_STATE)) {
              Boolean newValue = !(Boolean) BooleanConverter.get().decode(
                  attr.getData());
              if (newValue.booleanValue()) {
                log.info("[Chest] Who's taking my things?!");
              } else {
                log.info("[Chest] What's mine is mine.");
              }
              this.chestPanel.setOpen(newValue.booleanValue());
              if (newValue.booleanValue()
                  && (this.walletPanel.isKeyIsMissing() || this.walletPanel
                      .isWalletIsMissing())) {
                this.walletPanel.setNeedItems(true);
              } else {
                this.walletPanel.setNeedItems(false);
              }

            }
          }
        }
        // Flood sensor
        if (this.floodResponse.hasNext()) {
          log.debug("Flood sensor changed state.");
          WorldState state = this.floodResponse.next();
          Collection<Attribute> attributes = state.getState(this.config
              .getWaterId());
          for (Attribute attr : attributes) {
            if (attr.getAttributeName().equalsIgnoreCase(FLOOD_STATE)) {
              Boolean newValue = BooleanConverter.get().decode(attr.getData());
              if (newValue.booleanValue()) {
                log.info("[Flood Sensor] A wild raindrop appeared!!");
              } else {
                log.info("[Flood Sensor] Dry as a desert.");
              }
              this.floodPanel.setHasWater(newValue.booleanValue());
            }
          }
        }

        // Power sensor
        if (this.powerResponse.hasNext()) {
          log.debug("Power sensor changed state.");
          WorldState state = this.powerResponse.next();
          Collection<Attribute> attributes = state.getState(this.config
              .getPowerId());
          for (Attribute attr : attributes) {
            if (attr.getAttributeName().equalsIgnoreCase(LIGHT_STATE)) {
              Boolean newValue = BooleanConverter.get().decode(attr.getData());
              if (newValue.booleanValue()) {
                log.info("[Power Sensor] Flip the switch!");
              } else {
                log.info("[Power Sensor] Conservation is best.");
              }
              this.powerPanel.setHasPower(newValue.booleanValue());
            }
          }
        }

        // Chair Sensor
        if (this.chairResponse.hasNext()) {
          log.debug("Chair sensor changed state.");
          WorldState state = this.chairResponse.next();
          Collection<Attribute> attributes = state.getState(this.config
              .getChairId());
          for (Attribute attr : attributes) {
            if (attr.getAttributeName().equalsIgnoreCase(CHAIR_STATE)) {
              Boolean newValue = BooleanConverter.get().decode(attr.getData());
              if (newValue.booleanValue()) {
                log.info("[Chair Sensor] What a comfy chair!");
              } else {
                log.info("[Chair Sensor] Stretching my legs.");
              }
              this.chairPanel.setIsOccupied(newValue.booleanValue());
            }
          }
        }
        
        // Mascot Sensor
        if (this.logoResponse.hasNext()) {
          log.debug("Mascot sensor changed state.");
          WorldState state = this.logoResponse.next();
          Collection<Attribute> attributes = state.getState(this.config
              .getLogoId());
          for (Attribute attr : attributes) {
            if (attr.getAttributeName().equalsIgnoreCase(MOBILITY_STATE)) {
              Boolean newValue = BooleanConverter.get().decode(attr.getData());
              if (newValue.booleanValue()) {
                log.info("[Mascot Sensor] Hoo-Hoo! I love flying");
              } else {
                log.info("[Mascot Sensor] Let me rest for a moment...");
              }
              this.logoPanel.setMoving(newValue.booleanValue());
            }
          }
        }

        try {
          Thread.sleep(50);
        } catch (InterruptedException ie) {
          // Ignored
        }
      } catch (Exception e) {
        log.error("An exception has occured.", e);
      }
    }
    this.doorResponse.cancel();
    this.floodResponse.cancel();
    this.powerResponse.cancel();
    this.chairResponse.cancel();
    this.walletResponse.cancel();
    this.worldModel.disconnect();
    
  }

  private void buildPanels() {

  }

  private void layoutFrame() {
    this.mainFrame.setLayout(new GridLayout(2, 2));
    // First row
    this.mainFrame.add(this.chestPanel);
    this.mainFrame.add(this.floodPanel);
//    this.mainFrame.add(this.powerPanel);

    // Second row
    this.mainFrame.add(this.chairPanel);
//    this.mainFrame.add(this.walletPanel);
    this.mainFrame.add(this.logoPanel);

    this.mainFrame.setSize(800, 600);
    // this.mainFrame.pack();
    this.mainFrame.setVisible(true);
  }

  public void shutdown() {
    log.debug("Closing world model connection.");
    this.keepRunning = false;
    this.worldModel.disconnect();
  }
}
