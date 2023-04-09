package com.tomoncle.dubbo.samples.consumer.api.mock;


/**
 * 提供默认的mockService实现，在服务降级时使用此默认值
 *
 * @see org.apache.dubbo.rpc.support.MockInvoker
 * 注意：此类必须为public访问，否则 dubbo 无法进行反射处理
 *
 * @author tomoncle
 */

public class DefaultMockServiceMock implements DefaultMockService {
    @Override
    public String sayHello() {
        return "[DefaultMockService]: 403 service is unavailable! ";
    }
}