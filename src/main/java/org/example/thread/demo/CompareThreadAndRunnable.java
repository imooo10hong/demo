package org.example.thread.demo;

import static java.lang.Thread.sleep;

import java.util.concurrent.atomic.AtomicInteger;

//通过实现Runnable接口实现多线程能更好地做到多个线程并发地完成同一个任务，访问同一份数据资源

public class CompareThreadAndRunnable {

  public static final int MAX_AMOUNT = 3; //商品数量

  // 商店商品类(销售线程类),一个商品一个销售线程,每个线程异步销售3次
  static class StoreGoods extends Thread {

    StoreGoods(String name) {
      super(name);
    }

    private int goodsAmount = MAX_AMOUNT;

    public void run() {
      for (int i = 0; i <= MAX_AMOUNT; i++) {
        if (this.goodsAmount > 0) {
          System.out.println(
              Thread.currentThread().getName() + " 卖出一件，还剩：" + (--goodsAmount));
          try {
            sleep(10);
          } catch (InterruptedException e) {
            throw new RuntimeException(e);
          }
        }
      }
      System.out.println(Thread.currentThread().getName() + " 运行结束.");
    }
  }

  //商场商品类（target销售线程的目标类），一个商品最多销售3次，可以多人销售
  static class MallGoods implements Runnable {
    //多人销售可能导致数据出错，使用原子数据类型保障数据安全
    private final AtomicInteger goodsAmount = new AtomicInteger(MAX_AMOUNT);

    public void run() {
      for (int i = 0; i <= MAX_AMOUNT; i++) {
        if (this.goodsAmount.get() > 0) {
          System.out.println(Thread.currentThread().getName() + " 卖出一件，还剩：" + (goodsAmount.decrementAndGet()));
          try {
            sleep(10);
          } catch (InterruptedException e) {
            throw new RuntimeException(e);
          }
        }
      }
      System.out.println(Thread.currentThread().getName() + " 运行结束.");
    }
  }


  public static void main(String[] args) throws InterruptedException {

    System.out.println("商店版本的销售");
    for (int i = 1; i <= 2; i++) {
      Thread thread = null;
      thread = new StoreGoods("店员-" + i);
      thread.start();
    }

    Thread.sleep(1000);

    System.out.println("商场版本的销售");
    MallGoods mallGoods = new MallGoods();
    for (int i = 1; i <= 2; i++) {
      Thread thread = null;
      thread = new Thread(mallGoods, "商场销售员-" + i);
      thread.start();
    }
  }
}

