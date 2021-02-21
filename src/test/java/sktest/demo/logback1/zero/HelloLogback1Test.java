package sktest.demo.logback1.zero;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.shaneking.ling.jackson.databind.OM3;
import org.shaneking.ling.zero.util.List0;
import org.slf4j.MDC;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

@Slf4j
public class HelloLogback1Test {
  @Test
  void testMDC() throws ExecutionException, InterruptedException {
    log.info("BEGIN");

    MDC.put("logMdcFn", "ForkJoinPool");
    IntStream.range(1, 100).boxed().parallel().forEach(i -> {
      log.info(OM3.p(i));
    });

    MDC.put("logMdcFn", "ThreadPool");
    ExecutorService es = Executors.newFixedThreadPool(3);
    List<Future<?>> list = List0.newArrayList();
    for (int i = 0; i < 100; i++) {
      int fi = i;
      list.add(es.submit(() -> log.info(OM3.p(fi))));
    }
    for (Future<?> f : list) {
      f.get();
    }

    log.info("END");
    /*
    ./tstFiles/HelloLogback1Test_testMDC
     */
  }

  //https://github.com/qos-ch/logback/commit/aa7d584ecdb1638bfc4c7223f4a5ff92d5ee6273
  //https://stackoverflow.com/questions/43053482/how-to-get-back-mdc-inheritance-with-modern-logback
  //https://jira.qos.ch/browse/LOGBACK-624

  //https://lihuanghe.github.io/2015/08/15/logback.html
}
