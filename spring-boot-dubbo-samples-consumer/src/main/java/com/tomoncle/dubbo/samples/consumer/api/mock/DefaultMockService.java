package com.tomoncle.dubbo.samples.consumer.api.mock;

import com.tomoncle.dubbo.samples.api.service.MockService;


/**
 * 提供默认的mockService接口的Mock接口,
 * 因为 @DubboReference(mock = "fail:true", interfaceClass = DefaultMockService.class) 的interfaceClass要指定接口类
 * 在查找默认值时，会查找 interfaceClass要指定接口的实现类，而且实现类名称必须 = {接口名}Mock 这种命名规则，
 * 而且接口和实现类必须在同一包下，这样就影响了接口定义那一层的结构，
 * 所以在消费者端再定义一个接口，继承 MockService 即可.
 *
 * 这样传入 interfaceClass = com.tomoncle.dubbo.samples.consumer.api.mock.DefaultMockService.class，
 * dubbo 会默认查找 com.tomoncle.dubbo.samples.consumer.api.mock.DefaultMockServiceMock 这个实现类
 *
 * @author tomoncle
 */

public interface DefaultMockService extends MockService {}
