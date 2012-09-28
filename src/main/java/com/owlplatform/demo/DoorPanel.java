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

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Robert Moore
 *
 */
public class DoorPanel extends JPanel {
  private static final Logger log = LoggerFactory.getLogger(DoorPanel.class);
  private static BufferedImage doorImage = null;
  private static BufferedImage visitorImage = null;
  
  private boolean hasVisitor = false;
  
  public DoorPanel(){ 
    super();
    if(this.doorImage != null){
      this.setPreferredSize(new Dimension(this.doorImage.getWidth(), this.doorImage.getHeight()));
    }
  }
  
  static {
    try {
    doorImage = ImageIO.read(new File("src/main/resources/img/door.png"));
    visitorImage = ImageIO.read(new File("src/main/resources/img/man.png"));
    }catch(Exception e){
      log.error("Unable to load one or more image resources.",e);
      
    }
  }
  
  public void setHasVisitor(final boolean hasVisitor){
    log.debug("Updating visitor state: {}", hasVisitor);
    this.hasVisitor = hasVisitor;
    this.repaint(100);
    
  }
  
  @Override
  protected void paintComponent(Graphics g){
    super.paintComponent(g);
    log.debug("Redrawing door panel.");
    Graphics2D g2 = (Graphics2D)g;
    int width = this.getWidth();
    int height = this.getHeight();
    if(doorImage != null){
      log.debug("Drawing door image.");
      g2.drawImage(this.doorImage, 0, 0, width, height, 0, 0, this.doorImage.getWidth(), this.doorImage.getHeight(), null);
    }
    
    if(this.hasVisitor && this.visitorImage != null){
      log.debug("Drawing visitor image.");
      g2.drawImage(this.visitorImage, 0, 0, width, height, 0, 0, this.visitorImage.getWidth(), this.visitorImage.getHeight(), null);
    }
  }
  
}
