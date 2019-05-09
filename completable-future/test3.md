#### 构建异步CompletableFuture
上面的get()是阻塞的, 他会一直阻塞直到执行完成,这就是最开始我们说的Future的不足,这不是我们想要的.  
我们需要的是可以设置一个回调,等执行完成后自动进行调用, 下面我们看下这几个方法.  
1.thenApply()方法,**它接受一个Function作为入参,同时返回带参的CompletableFuture** 
示例代码
```java
@Test
public void thenApplyTest() throws ExecutionException, InterruptedException {
    CompletableFuture<String> cf = CompletableFuture
            .supplyAsync(() -> {
                MixAll.simulateComputeCost();
                MixAll.printWithThread(" will return something");
                return "hello supplyAsync";
            })
            .thenApply(info -> {
                MixAll.simulateComputeCost();
                MixAll.printWithThread(" will return something");
                return "thenApply|" + info;
            });


    String data = cf.get();
    MixAll.printWithThread(" get>>" + data);
}
```
supplyAsync执行完成后,自动调用thenApply()方法  
执行thenApply里面的逻辑的线程和执行supplyAsync逻辑的是同一个.   

2.thenAccept(),返回CompletableFuture<Void>, 参数是Consumer   
示例代码,先异步获取数据,完成后进行消费      
```java
@Test
public void thenAcceptTest() {
    CompletableFuture.supplyAsync(() -> UserService.Instance.getUser(12))
            .thenAccept(product -> MixAll.printWithThread(" thenAccept get userName:" + product.getName()));

    //等待执行完成
    MixAll.simulateComputeCost(8);
}
```


3.thenRun(), 返回CompletableFuture<Void>, 参数是Runnable,无法访问上一步的返回结果(如果有返回结果)   
示例代码
```java
@Test
public void thenRunTest() {
    long start = System.currentTimeMillis();
    CompletableFuture
            .runAsync(() -> UserService.Instance.getUser(12))
            .thenRun(() -> MixAll.printWithThread(" thenRun cost:" + (System.currentTimeMillis() - start) + "ms"));
    //等待执行完成
    MixAll.simulateComputeCost(8);
}
``` 

3个方法都有另外2个后缀是Async的重载方法，以thenApply为例  
```java
thenApplyAsync(Function<? super T,? extends U> fn)  //method1
thenApplyAsync(Function<? super T,? extends U> fn, Executor executor)   //method2  
```
method1在方法内部使用了ForkJoinPool.commonPool(),在测试过程中发现执行thenApplyAsync的逻辑和执行supplyAsync的线程依然是同一个,猜测是又在线程池里拿到了同一个,但是好巧啊...   
method2使用了传递的executor进行执行   


