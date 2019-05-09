```java
@Test
public void test1() throws ExecutionException, InterruptedException {
    CompletableFuture<String> completableFuture = new CompletableFuture<>(); //step1
    new Thread(new CompleteFutureTask(completableFuture)).start(); //step2
    String result = completableFuture.get(); //step3
    System.out.println(Thread.currentThread().getName() + " get result:" + result);
}

class CompleteFutureTask implements Runnable {
    CompletableFuture<String> completableFuture;

    public CompleteFutureTask(CompletableFuture<String> completableFuture) {
        this.completableFuture = completableFuture;
    }

    @Override
    public void run() {
        MixAll.simulateComputeCost();
        System.out.println(Thread.currentThread().getName() + " will complete future:" + completableFuture);
        completableFuture.complete("hello completableFuture"); //step4
        //completableFuture.complete("hello completableFuture"); //step5
    }
}
```
step1 最简单的方式创建一个CompletableFuture, 如果没有step2, 那么位于step3的get()方法会一直阻塞, 因为计算一直没有完成.     
同时step4 的多次调用是无效的, 即step5是无效的   