package org.example.thread.demo;

public class PriorityDemo {

  public static final int SLEEP_GAP = 1000;

  static class PriorityThread extends Thread {

    static int threadNo = 1;

    public PriorityThread() {
      super("thread" + threadNo);
      threadNo++;
    }

    public long opportunities = 0;

    public void run() {
      //处理中断逻辑
      while (!Thread.currentThread().isInterrupted()) {
        opportunities++;
      }
    }
  }

  public static void main(String[] args) throws InterruptedException {
    PriorityThread[] threads = new PriorityThread[10];
    for (int i = 0; i < threads.length; i++) {
      threads[i] = new PriorityThread();
      threads[i].setPriority(i + 1);
    }
    for (PriorityThread thread : threads) {
      thread.start();
    }
    // 等待 1s, 10 个线程的运行时间合计 1s
    Thread.sleep(SLEEP_GAP);
    for (PriorityThread thread : threads) {
      // 此方法本质不是用来中断一个线程，而是将线程设置为中断状态
      thread.interrupt();
    }
    for (PriorityThread thread : threads) {
      System.out.println(thread.getName() + "-优先级为" + thread.getPriority() + "-机会值为"
          + thread.opportunities);
    }
  }
}
