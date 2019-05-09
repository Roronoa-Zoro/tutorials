#### accept和apply系列方法补充
1. acceptEither方法, 先执行完的任务被返回, 但是2个任务都执行了   
```java
@Test
public void thenAcceptEitherTest() {
    CompletableFuture.supplyAsync(() -> ProductService.Instance.getProduct(12))
            .acceptEither(CompletableFuture.supplyAsync(() -> ProductService.Instance.getProduct(15)),
                    p -> MixAll.printWithThread(" product:" + p.getName() + " returned..."));

    //等待执行完成
    MixAll.simulateComputeCost(5);
}
```
该方法同时包括2个带Async后缀的重载方法   
2.applyToEither方法,   

#### 合并2个CompletableFuture  
1.假如要先获取产生信息，拿到数据后在获取对应的商户信息，这种是存在依赖关系的情况   
```java
@Test
public void combineFutureTest() {
    CompletableFuture<Merchant> cf = CompletableFuture
            .supplyAsync(() -> ProductService.Instance.getProduct(12)) //step1
            .thenCompose(product -> CompletableFuture.supplyAsync(() -> MerchantService.Instance.getMerchant(product))); //step2

    
}
```
在step1处返回的是CompletableFuture<Product>, thenCompose的入参是个Function<? super T, ? extends CompletionStage<U>> fn, 注意返回值是CompletionStage的子类,    
所以在step2处转成了CompletableFuture<Merchant>   

2.有2个互相独立的计算, 等他们都完成后在合并做一些计算的情况  
```java
@Test
public void combineFutureTest2() throws ExecutionException, InterruptedException {
    CompletableFuture<Integer> cf = CompletableFuture
            .supplyAsync(() -> RiskService.Instance.getRiskScoreFromA4Product())
            .thenCombine(CompletableFuture.supplyAsync(() -> RiskService.Instance.getRiskScoreFromB4Product()),
                    (ascore, bscore) -> (ascore + bscore) / 2);

    MixAll.printWithThread(" get score=" + cf.get());
}
```
注意看下thenCombine的入参即可   

#### 合并多个CompletableFuture  
1.合并全部Future,等待全部完成, 示例代码和注释如下   
```java
@Test
public void combineAllFutureTest() throws ExecutionException, InterruptedException {
    List<Integer> list = new ArrayList<>(10);
    Random r = new Random();
    for (int i = 0; i < 10; i++) {
        list.add(i + r.nextInt(10));
    }
    //创建10个异步获取产品的任务
    List<CompletableFuture<Product>> proFutures = list.stream()
            .map(in -> CompletableFuture.supplyAsync(() -> ProductService.Instance.getProduct(in)))
            .collect(Collectors.toList());


    //组装到一起
    CompletableFuture<Void> allFutures = CompletableFuture.allOf(proFutures.toArray(new CompletableFuture[proFutures.size()]));

//        // 获取最终数据 方式1
//        CompletableFuture<List<Product>> productList = allFutures.thenApply(v -> {
//            MixAll.printWithThread(" thenApply collect all product info");
//            return proFutures.stream().map(cfp -> cfp.join()).collect(Collectors.toList());
//        });
//
//        List<Product> data = productList.get();
//        MixAll.printWithThread(" productInfo:" + data);

    // 获取最终数据 方式2
    final List<Product> data2 = new ArrayList<>(proFutures.size());
    allFutures.thenRun(() -> {
        MixAll.printWithThread(" thenRun collect all product info");
        data2.addAll(proFutures.stream().map(cfp -> cfp.join()).collect(Collectors.toList()));
    });
    allFutures.get();
    MixAll.printWithThread(" productInfo:" + data2);
    
    //获取最终数据 方式3, 跟上面类似 , 略
    //allFutures.thenAccept()
}
```

2.合并所有Future, 等待任何一个结束即可,示例代码如下
```java
@Test
public void combineAnyFutureTest() throws ExecutionException, InterruptedException {
    List<Integer> list = new ArrayList<>(10);
    Random r = new Random();
    for (int i = 0; i < 10; i++) {
        list.add(i + r.nextInt(10));
    }
    //创建10个异步获取产品的任务
    List<CompletableFuture<Integer>> scoreFutures = list.stream()
            .map(in -> CompletableFuture.supplyAsync(() -> RiskService.Instance.getRiskScoreFromB4Product()))
            .collect(Collectors.toList());

    CompletableFuture<Object> cfAny = CompletableFuture.anyOf(scoreFutures.toArray(new CompletableFuture[scoreFutures.size()]));
    Integer score = (Integer) cfAny.get();
    MixAll.printWithThread(" get score=" + score);
}
```
**Note:**    
anyOf()返回的是CompletableFuture<Object>, 没有具象化泛型类型, 所有如果多个异步任务的返回结果不一样, 那无法/很难知道具体返回的是什么.    
建议执行的任务都是同类型的    

#### 异常处理  
1.使用exceptionally的方式   
```java
@Test
public void exceptionallyTest() throws ExecutionException, InterruptedException {
    final int idx = 2;
    CompletableFuture<String> cf = CompletableFuture
            .supplyAsync(() -> {
                MixAll.printWithThread(" in supplyAsync....");
                if (idx % 2 == 0) {
                    throw new IllegalArgumentException("test illegal argument exception");
                }
                return UUID.randomUUID().toString();
            })
            .thenApply(first -> {
                if (idx % 2 == 1) {
                    throw new IllegalArgumentException("test illegal argument exception");
                }
                MixAll.printWithThread(" in first thenApply....");
                return "thenApply1-" + first;
            })
            .thenApply(second -> {
                MixAll.printWithThread(" in second thenApply....");
                return "thenApply2-" + second;
            })
            .exceptionally(ex -> {
                MixAll.printWithThread(" in exceptionally....");
                return "exceptionally";
            });

    MixAll.printWithThread(" get data:" + cf.get());
}
```
调用链的任何一个地方发生异常,后续的调用都不会继续进行    

2.使用handle()方法**handle方法不管是否有异常,都会执行**    
```java
@Test
public void exceptionHandleTest() throws ExecutionException, InterruptedException {
    final int idx = 1;
    CompletableFuture<String> cf = CompletableFuture
            .supplyAsync(() -> {
                MixAll.printWithThread(" in supplyAsync....");
                if (idx % 2 == 0) {
                    throw new IllegalArgumentException("test illegal argument exception");
                }
                return UUID.randomUUID().toString();
            })
            .thenApply(first -> {
                if (idx % 2 == 1) {
                    throw new IllegalArgumentException("test illegal argument exception");
                }
                MixAll.printWithThread(" in first thenApply....");
                return "thenApply1-" + first;
            })
            .thenApply(second -> {
                MixAll.printWithThread(" in second thenApply....");
                return "thenApply2-" + second;
            })
            .handle((info, ex) -> {
                MixAll.printWithThread(" in exception handle,info=" + info + ",ex=" + ex);
                if (ex != null) {
                    return "exception handle";
                }
                return info;
            });

    MixAll.printWithThread(" get data:" + cf.get());
}
```
