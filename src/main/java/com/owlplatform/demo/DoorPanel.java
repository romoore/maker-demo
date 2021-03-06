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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
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
  public void paintComponent(Graphics g){
    super.paintComponent(g);
    log.debug("Redrawing door panel.");
    Graphics2D g2 = (Graphics2D)g;
    
    int width = this.getWidth();
    int height = this.getHeight();
    
    g2.setColor(Color.WHITE);
    
    g2.fillRect(0, 0, width, height);
    
    g2.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
    
    g2.setColor(Color.BLACK);
    
    g2.drawRect(0, 0, width, height);

    width -= 2;
    height -= 2;
    
    if(doorImage != null){
      log.debug("Drawing door image.");
      float imgAR = (this.doorImage.getWidth()*1f)/this.doorImage.getHeight();
      float scaleY = (height *1f) / this.doorImage.getHeight();
      
      int imgWidth = (int)(height*imgAR);
      int offsetX = (width - imgWidth)/2;
      
      g2.drawImage(this.doorImage, offsetX, 1, imgWidth+offsetX, height, 0, 0, this.doorImage.getWidth(), this.doorImage.getHeight(), null);
    }
    
    if(this.hasVisitor) {
      log.debug("Visitor is here!");
    }else{
      log.debug("Nobody loves us...");
    }
    
    if(this.hasVisitor && this.visitorImage != null){
      log.debug("Drawing visitor image.");
      
      float imgAR = (this.visitorImage.getWidth()*1f)/this.visitorImage.getHeight();
      float scaleY = (height *1f) / this.visitorImage.getHeight();
      
      int imgWidth = (int)(height*imgAR);
      int offsetX = (width - imgWidth)/2;
      
      g2.drawImage(this.visitorImage, offsetX, 1, imgWidth+offsetX, height, 0, 0, this.visitorImage.getWidth(), this.visitorImage.getHeight(), null);
    }
  }

  public boolean isHasVisitor() {
    return hasVisitor;
  }
  
}
