package com.tomoncle.dubbo.samples.consumer.api;

import com.tomoncle.dubbo.samples.api.service.MockService;
import com.tomoncle.dubbo.samples.api.service.VersionService;
import com.tomoncle.dubbo.samples.consumer.api.mock.DefaultMockService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 模拟服务降级，防止整个调用链瘫痪
 * @author tomoncle
 */
@RestController
@RequestMapping("/mock")
public class MockController {

    /**
     * 不发起远程调用, 执行本地 doMockInvoke, 返回 403 service is unavailable
     * 错误信息可以自定义，格式：mock = `force:return ${your message}`
     * @see org.apache.dubbo.rpc.cluster.support.wrapper.MockClusterInvoker#invoke
     */
    @DubboReference(mock = "force:return 403 service is unavailable")
    private MockService mockService1;

    /**
     * 先尝试远程调用，失败后，返回 interfaceClass **接口**“实现类”的返回值，
     * ***** 注意：接口、实现类必须在同一包下， 实现类名称 = 接口名称+Mock *****
     *
     * 注意：mock只针对 RpcException 异常，即调用异常，业务异常处理不了
     * @see org.apache.dubbo.rpc.cluster.support.wrapper.MockClusterInvoker#invoke
     */
    @DubboReference(mock = "fail:true", interfaceClass = DefaultMockService.class)
    private MockService mockService2;

    /**
     * 默认方式，尝试远程调用，调用失败，抛出异常
     * @see org.apache.dubbo.rpc.cluster.support.wrapper.MockClusterInvoker#invoke
     */
    @DubboReference
    private MockService mockService3;

    @GetMapping("/01")
    public String sayHello1(){
        return mockService1.sayHello();
    }
    @GetMapping("/02")
    public String sayHello2(){
        return mockService2.sayHello();
    }
    @GetMapping("/03")
    public String sayHello3(){
        return mockService3.sayHello();
    }
}

