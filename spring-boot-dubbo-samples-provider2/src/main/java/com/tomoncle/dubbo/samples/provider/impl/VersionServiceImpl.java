package com.tomoncle.dubbo.samples.provider.impl;

import com.tomoncle.dubbo.samples.api.service.VersionService;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 提供 v2.0 版本的服务
 * @author tomoncle
 */
@DubboService(timeout = 5000, version = "v2.0")
public class VersionServiceImpl implements VersionService {
    private static Logger logger = LogManager.getLogger(VersionServiceImpl.class);
    @Override
    public String version() {
        logger.info("provider-2");
        return "provider-2";
    }
}
