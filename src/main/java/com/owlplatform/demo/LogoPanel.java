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
public class LogoPanel extends JPanel {
  private static final Logger log = LoggerFactory.getLogger(LogoPanel.class);
  private static BufferedImage logo = null;
  
  
  private boolean hasWater = false;
  
  public LogoPanel(){ 
    super();
    if(this.logo != null){
      this.setPreferredSize(new Dimension(this.logo.getWidth(), this.logo.getHeight()));
    }
  }
  
  static {
    try {
    logo = ImageIO.read(new File("src/main/resources/img/owl.png"));
    }catch(Exception e){
      log.error("Unable to load one or more image resources.",e);
      
    }
  }
  
  
  @Override
  public void paintComponent(Graphics g){
    super.paintComponent(g);
    log.debug("Redrawing flood panel.");
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
    
    BufferedImage drawImage = this.logo;
    
    
    
    float imgAR = (drawImage.getWidth()*1f)/drawImage.getHeight();
    float screenAR = (width*1f)/height;
    
    int imgHeight = 0;
    int imgWidth = 0;
    int offsetX = 1;
    int offsetY = 1;
    
    // Wide screen
    if(screenAR > 1){
      imgHeight = height;
      imgWidth = (int)(height*imgAR);
      offsetX = (width - imgWidth)/2;
    }
    // Tall
    else{
      imgWidth = width;
      imgHeight = (int)(width/imgAR);
      offsetY = (height - imgHeight)/2;
    }
    
    
    
    g2.drawImage(drawImage, offsetX, offsetY, imgWidth + offsetX, imgHeight + offsetY, 0, 0, drawImage.getWidth(), drawImage.getHeight(), null);
    
    
  }
  
}
