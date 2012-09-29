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
public class ReminderPanel extends JPanel {
  private static final Logger log = LoggerFactory
      .getLogger(ReminderPanel.class);
  private static BufferedImage wallet_grey = null;
  private static BufferedImage wallet_color = null;
  private static BufferedImage keys_grey = null;
  private static BufferedImage keys_color = null;
  private static BufferedImage check = null;
  private static BufferedImage cross = null;

  public ReminderPanel() {
    super();
    if (this.wallet_color != null) {
      this.setPreferredSize(new Dimension(this.wallet_color.getWidth() * 2,
          this.wallet_color.getHeight() * 2));
    }
  }

  static {
    try {
      wallet_grey = ImageIO.read(new File(
          "src/main/resources/img/wallet-grey.png"));
      wallet_color = ImageIO
          .read(new File("src/main/resources/img/wallet.png"));
      keys_grey = ImageIO.read(new File("src/main/resources/img/key-grey.png"));
      keys_color = ImageIO.read(new File("src/main/resources/img/key.png"));
      check = ImageIO.read(new File("src/main/resources/img/check.png"));
      cross = ImageIO.read(new File("src/main/resources/img/cross.png"));
    } catch (Exception e) {
      log.error("Unable to load one or more image resources.", e);

    }
  }

  private boolean keyIsMissing = true;
  private boolean walletIsMissing = true;
  private boolean needItems = false;

  public void setKeyIsMissing(final boolean keyIsMissing) {
    this.keyIsMissing = keyIsMissing;
    this.repaint(100);
  }

  public void setWalletIsMissing(final boolean walletIsMissing) {
    this.walletIsMissing = walletIsMissing;
    this.repaint(100);
  }

  public void setNeedItems(final boolean needItems) {
    this.needItems = needItems;
    this.repaint(100);
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    log.debug("Redrawing reminder panel.");
    Graphics2D g2 = (Graphics2D) g;

    int width = this.getWidth();
    int height = this.getHeight();

    g2.setColor(Color.WHITE);

    g2.fillRect(0, 0, width, height);

    g2.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND,
        BasicStroke.JOIN_ROUND));

    g2.setColor(Color.BLACK);

    g2.drawRect(0, 0, width, height);

    width -= 2;
    height -= 2;

    // Make 4 quadrants
    int x0 = 0;
    int x1 = width / 2;
    int y0 = 0;
    int y1 = height / 2;

    // Simplest case - don't need anything
    if (!this.needItems) {
      log.debug("Don't need any items.");
      drawImage(g2, keys_grey, 0);
      drawImage(g2, wallet_grey, 2);
    } else {
      if (this.keyIsMissing) {
        log.debug("Key is missing.");
        drawImage(g2, keys_grey, 0);
        drawImage(g2, cross, 1);
      } else {
        log.debug("Key is not missing.");
        drawImage(g2, keys_color, 0);
        drawImage(g2, check, 1);
      }

      if (this.walletIsMissing) {
        log.debug("Wallet is missing.");
        drawImage(g2, wallet_grey, 2);
        drawImage(g2, cross, 3);
      } else {
        log.debug("Wallet is not missing.");
        drawImage(g2, wallet_color, 2);
        drawImage(g2, check, 3);
      }
    }

  }

  private void drawImage(Graphics2D g2, BufferedImage image, int quadrant) {
    float keyImgAR = (image.getWidth() * 1f) / image.getHeight();
    int x1 = this.getWidth() / 2;
    int y1 = this.getHeight() / 2;
    float screenAR = (x1 * 1f) / y1;

    int imgHeight = y1;
    int imgWidth = x1;
    int offsetX = 1;
    int offsetY = 1;

    // Wide screen
    if (screenAR > 1) {
      imgHeight = y1;
      imgWidth = (int) (y1 * keyImgAR);
      offsetX = (x1 - imgWidth) / 2;
    }
    // Tall
    else {
      imgWidth = x1;
      imgHeight = (int) (x1 / keyImgAR);
      offsetY = (y1 - imgHeight) / 2;
    }
    offsetX += ((quadrant & 0x01) == 0 ? 0 : x1);
    offsetY += ((quadrant >> 1) == 0 ? 0 : y1);

    g2.drawImage(image, offsetX, offsetY, imgWidth + offsetX, imgHeight
        + offsetY, 0, 0, image.getWidth(), image.getHeight(), null);
  }

}
