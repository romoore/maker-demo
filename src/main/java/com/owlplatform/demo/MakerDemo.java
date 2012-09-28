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
import com.owlplatform.worldmodel.types.DataConverter;
import com.owlplatform.worldmodel.types.TypeConverter;
import com.thoughtworks.xstream.XStream;

/**
 * @author Robert Moore
 * 
 */
public class MakerDemo extends Thread {

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

    md.init();
  }

  private final MakerDemoConfig config;
  private ClientWorldConnection worldModel = new ClientWorldConnection();

  private JFrame mainFrame = new JFrame("Owl Platform @ MakerFaire NYC 2012");

  private DoorPanel doorPanel = new DoorPanel();
  private StepResponse doorResponse = null;
  private JPanel floodPanel = new JPanel();
  private StepResponse floodResponse = null;
  private JPanel powerPanel = new JPanel();
  private StepResponse powerResponse = null;
  private JPanel walletPanel = new JPanel();
  private StepResponse walletResponse = null;

  private boolean keepRunning = true;

  public MakerDemo(final MakerDemoConfig config) {
    super();
    this.config = config;
    this.worldModel.setHost(this.config.getWmHost());
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

  }

  private void setupWMConnection() {
    this.worldModel.connect(10000);
    log.debug("Connected to {}", this.worldModel);

    this.doorResponse = this.worldModel.getStreamRequest(
        this.config.getWelcomeMatId(), System.currentTimeMillis(), 0, "empty");
  }

  public void run() {
    while (this.keepRunning) {
      try {
        if (this.doorResponse.hasNext()) {
          log.debug("Welcome mat changed state.");
          WorldState state = this.doorResponse.next();
          Collection<Attribute> attributes = state.getState(this.config.getWelcomeMatId());
          for(Attribute attr : attributes){
            if(attr.getAttributeName().equalsIgnoreCase("empty")){
              Boolean newValue = (Boolean)DataConverter.decode(attr.getAttributeName(), attr.getData());
              if(newValue){
                log.info("[Welcome Mat] A wild visitor has appeared!");
              }else{
                log.info("[Welcome Mat] Calm returns to the area.");
              }
              this.doorPanel.setHasVisitor(!newValue.booleanValue());
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
    this.worldModel.disconnect();
  }

  private void buildPanels() {

  }

  private void layoutFrame() {
    this.mainFrame.setLayout(new GridLayout(2, 3));
    // First row
    this.mainFrame.add(this.doorPanel);
    this.mainFrame.add(this.floodPanel);
    this.mainFrame.add(this.powerPanel);

    // Second row
    this.mainFrame.add(this.powerPanel);
    this.mainFrame.add(this.walletPanel);

    this.mainFrame.pack();
    this.mainFrame.setVisible(true);
  }

  public void shutdown() {
    log.debug("Closing world model connection.");
    this.keepRunning = false;
    this.worldModel.disconnect();
  }
}
