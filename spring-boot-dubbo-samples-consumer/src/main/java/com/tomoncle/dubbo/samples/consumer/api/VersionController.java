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

import com.tomoncle.dubbo.samples.api.service.VersionService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/version")
public class VersionController {
    private static Logger logger = LogManager.getLogger(VersionController.class);
    /**
     * 使用version控制，调用的服务版本，只有provide,consumer版本匹配时才能调用成功，可以进行服务滚动升级
     *
     * @see com.alibaba.dubbo.config.annotation.Reference 注解引用服务
     * Load balance strategy, legal values include: random, roundrobin, leastactive
     */
    @DubboReference(loadbalance = "roundrobin", version = "v1.0")
    private VersionService versionService1;

    @DubboReference(loadbalance = "roundrobin", version = "v2.0")
    private VersionService versionService2;

    @GetMapping
    public String version() {
        String version = versionService1.version();
        logger.info(version);
        return version;
    }

    @GetMapping("/v2")
    public String versionV2() {
        String version = versionService2.version();
        logger.info(version);
        return version;
    }
}
