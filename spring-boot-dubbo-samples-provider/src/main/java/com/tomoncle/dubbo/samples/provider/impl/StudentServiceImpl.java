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

package com.tomoncle.dubbo.samples.provider.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.tomoncle.dubbo.samples.api.service.StudentService;
import com.tomoncle.dubbo.samples.model.Student;

import java.util.ArrayList;
import java.util.List;

/**
 * 使用com.alibaba.dubbo.config.annotation.Service注解注册dubbo服务
 * Created by liyuanjun on 18-9-7.
 */

@Service(timeout = 5000)
public class StudentServiceImpl implements StudentService {
    @Override
    public List<Student> students() {
        return new ArrayList<Student>() {{
            Student s = new Student();
            s.setName("tomoncle");
            s.setAge(25);
            add(s);
        }};
    }
}
