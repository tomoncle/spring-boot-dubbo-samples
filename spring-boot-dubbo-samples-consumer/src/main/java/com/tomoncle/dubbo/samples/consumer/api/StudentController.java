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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * web api
 * <p/>
 * Created by liyuanjun on 18-9-7.
 */
@RestController
@RequestMapping("/students")
public class StudentController {

    private boolean isOk = false;
    private volatile int number = 0;
    /**
     * com.alibaba.dubbo.config.annotation.Reference注解引用服务
     */
    @DubboReference(loadbalance = "roundrobin")
    private StudentService studentService;

    private int getNumber() {
        synchronized (StudentController.class) {
            number++;
        }
        return number;
    }

    @GetMapping("/list")
    public List<Student> getStudents() {
        return studentService.students();
    }

    @GetMapping("/get")
    public Student get() {
        return studentService.save(new Student());
    }

    @GetMapping("/none")
    public List<Student> none() {
        return new ArrayList<Student>() {{
            add(new Student());
        }};
    }

    @SneakyThrows
    @GetMapping("/batch")
    public long batch() {
        Set<String> set = Collections.synchronizedSet(new HashSet<>());
        int size = 100;
        long start = System.currentTimeMillis();
        for (int i = 0; i < size; i++) {
            new Thread(() -> {
                while (true) {
                    if (isOk) {
                        try {
                            Student student;
                            for (int j = 0; j < 100; j++) {
                                student = new Student();
                                student.setAge(j);
                                studentService.save(student);
                            }
                        } catch (RuntimeException e) {
                            e.printStackTrace();
                        } finally {
                            set.add(Thread.currentThread().getName());
                        }
                        break;
                    }
                }
            }).start();
        }
        isOk = true;
        while (set.size() < size) {
            Thread.sleep(1);
        }
        number = 0;
        return System.currentTimeMillis() - start;
    }

}
