package org.darwin.generic.test;

import org.junit.Test;

/**
 * @author hexiufeng
 * @date 2018/12/28下午7:18
 */
public class TestCase {

  @Test
  public void test() {
    Bird bird = new Bird();
    bird.show();

    Fish fish = new Fish();
    fish.show();

    Pig pig = new Pig();
    pig.show();

    Monkey monkey = new Monkey();
    monkey.show();
  }

}
