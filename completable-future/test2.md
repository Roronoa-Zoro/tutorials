#### 通过runAsync()进行异步计算, 改方法返回CompletableFuture<Void>   
示例代码
```java
@Test
public void runAsyncTest() throws ExecutionException, InterruptedException {
    CompletableFuture<Void> cf = CompletableFuture.runAsync(() -> {
        MixAll.simulateComputeCost();
        System.out.println(Thread.currentThread().getName() + " runAsyncTest...................");
    });
    cf.get();
}
```
该方法有2个重载方法   
```java
public static CompletableFuture<Void> runAsync(Runnable runnable)  
public static CompletableFuture<Void> runAsync(Runnable runnable,Executor executor)  
```
方法1使用的是ForkJoinPool的线程池, 方法2可以传入自定义的线程池  


#### supplyAsync()进行异步计算, 有返回结果   
示例代码
```java
@Test
public void supplyAsyncTest() throws ExecutionException, InterruptedException {
    CompletableFuture<String> cf = CompletableFuture.supplyAsync(() -> {
        MixAll.simulateComputeCost();
        MixAll.printWithThread(" will return something");
        return "hello supplyAsync";
    });

    String data = cf.get();
    MixAll.printWithThread(" get data:" + data);
}
```
依然有2个重载方法,详见api, 线程池说明同上   