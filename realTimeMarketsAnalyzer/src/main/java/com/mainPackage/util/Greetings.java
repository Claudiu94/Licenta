package com.mainPackage.util;

/**
 * Created by cozafiu on 13.04.2017.
 */
public class Greetings {

  private final long id;
  private final String content;

  public Greetings(long id, String content) {
    this.id = id;
    this.content = content;
  }

  public long getId() {
    return id;
  }

  public String getContent() {
    return content;
  }
}