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

/**
 * @author Robert Moore
 *
 */
public class MakerDemoConfig {
  private String wmHost = "localhost";
  private String chairId = "maker.demo.chair";
  private String welcomeMatId = "maker.demo.welcome mat";
  private String waterId = "maker.demo.water sensor";
  private String walletId = "maker.demo.wallet";
  private String keysId = "maker.demo.keys";
  private String powerId = "maker.demo.power";
  public String getWmHost() {
    return wmHost;
  }
  public void setWmHost(String wmHost) {
    this.wmHost = wmHost;
  }
  public String getChairId() {
    return chairId;
  }
  public void setChairId(String chairId) {
    this.chairId = chairId;
  }
  public String getWelcomeMatId() {
    return welcomeMatId;
  }
  public void setWelcomeMatId(String welcomeMatId) {
    this.welcomeMatId = welcomeMatId;
  }
  public String getWaterId() {
    return waterId;
  }
  public void setWaterId(String waterId) {
    this.waterId = waterId;
  }
  public String getWalletId() {
    return walletId;
  }
  public void setWalletId(String walletId) {
    this.walletId = walletId;
  }
  public String getKeysId() {
    return keysId;
  }
  public void setKeysId(String keysId) {
    this.keysId = keysId;
  }
  public String getPowerId() {
    return powerId;
  }
  public void setPowerId(String powerId) {
    this.powerId = powerId;
  }
}
