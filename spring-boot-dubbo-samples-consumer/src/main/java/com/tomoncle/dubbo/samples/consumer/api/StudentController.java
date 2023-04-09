/*
MIT License

Copyright (c) 2017 tom.lee

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package com.tomoncle.dubbo.samples.consumer.api;

import com.tomoncle.dubbo.samples.api.service.StudentService;
import com.tomoncle.dubbo.samples.model.Student;
import lombok.SneakyThrows;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.rpc.cluster.loadbalance.RoundRobinLoadBalance;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * web api
 * <p/>
 * Created by liyuanjun on 18-9-7.
 */
@RestController
@RequestMapping("/students")
public class StudentController {
    private static Logger logger = LogManager.getLogger(StudentController.class);
    private volatile boolean enable = false;
    private AtomicInteger atomicSize = new AtomicInteger(0);
    /**
     * @see com.alibaba.dubbo.config.annotation.Reference 注解引用服务
     * Load balance strategy, legal values include: random, roundrobin, leastactive
     *
     * @see org.apache.dubbo.rpc.cluster.loadbalance.RoundRobinLoadBalance #doSelect
     * @see org.apache.dubbo.rpc.cluster.loadbalance.AbstractLoadBalance #getWeight
     * roundrobin：权重，默认1:1, 服务提供者A下线后，未下线的服务B权重会升高, 服务A再次上线后，权重变小
     *             计算方式为服务在线时间
     *
     */
    @DubboReference(loadbalance = RoundRobinLoadBalance.NAME)
    private StudentService studentService;

    @GetMapping("/list")
    public List<Student> getStudents() {
        return studentService.students();
    }

    @GetMapping("/get")
    public Student get() {
        List<Student> students = studentService.students();
        if (students.size() > 0) {
            return studentService.students().get(0);
        } else {
            return null;
        }
    }

    @GetMapping("/none")
    public List<Student> none() {
        return new ArrayList<Student>() {{
            add(new Student());
        }};
    }

    @SneakyThrows
    @GetMapping("/batch")
    public long batch(@RequestParam(defaultValue = "100") int size) {
        long start = System.currentTimeMillis();
        for (int i = 0; i < size; i++) {
            new Thread(new InnerTask(i)).start();
        }
        enable = true;
        while (atomicSize.get() < size) {
            Thread.sleep(1);
        }
        return System.currentTimeMillis() - start;
    }


    class InnerTask implements Runnable{
        private int age;
        InnerTask(int age) {
            this.age = age;
        }
        @Override
        public void run() {
            while (true) {
                if (enable) {
                    try {
                        studentService.save(new Student().setAge(age).setName(Thread.currentThread().getName()));
                    } catch (RuntimeException e) {
                        e.printStackTrace();
                    } finally {
                        atomicSize.getAndIncrement();
                        logger.info("age: "+age+" ; thread name : "+Thread.currentThread().getName());
                    }
                    break;
                }
            }
        }
    }
}
