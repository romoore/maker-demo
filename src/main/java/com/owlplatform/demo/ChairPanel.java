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
import java.awt.Image;
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
public class ChairPanel extends JPanel {
  private static final Logger log = LoggerFactory.getLogger(ChairPanel.class);
  private static BufferedImage emptyImage = null;
  private static BufferedImage occupiedImage = null;
  
  private boolean isOccupied = false;
  
  public ChairPanel(){ 
    super();
    if(this.emptyImage != null){
      this.setPreferredSize(new Dimension(this.emptyImage.getWidth(), this.emptyImage.getHeight()));
    }
  }
  
  static {
    try {
    emptyImage = ImageIO.read(new File("src/main/resources/img/chair_empty.png"));
    occupiedImage = ImageIO.read(new File("src/main/resources/img/chair_occupied.png"));
    }catch(Exception e){
      log.error("Unable to load one or more image resources.",e);
      
    }
  }
  
  public void setIsOccupied(final boolean isOccupied){
    log.debug("Updating chair state: {}", isOccupied);
    this.isOccupied = isOccupied;
    this.repaint(100);
    
  }
  
  @Override
  public void paintComponent(Graphics g){
    super.paintComponent(g);
    log.debug("Redrawing chair panel.");
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
    
    BufferedImage drawImage = this.emptyImage;
    
    if(this.isOccupied){
      log.debug("There's an occupant in my chair!");
      drawImage = this.occupiedImage;
    }else{
      log.debug("A lonely chair.");
    }
    
    
    float imgAR = (drawImage.getWidth()*1f)/drawImage.getHeight();
    float scaleY = (height *1f) / drawImage.getHeight();
    
    int imgWidth = (int)(height*imgAR);
    int offsetX = (width - imgWidth)/2;
    
    g2.drawImage(drawImage, offsetX, 1, imgWidth+offsetX, height, 0, 0, drawImage.getWidth(), drawImage.getHeight(), null);
    
    
  }
  
}
