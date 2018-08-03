package cr.downloader.service.impl;

import cr.downloader.entity.GlobalConfig;
import cr.downloader.repo.GlobalConfigRepo;
import cr.downloader.service.GlobalConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

/**
 * @author Beldon
 */
@Service
public class GlobalConfigServiceImpl implements GlobalConfigService {

    @Autowired
    private GlobalConfigRepo globalConfigRepo;

    @Override
    public String getConfig(String key) {
        Optional<GlobalConfig> configOptional = globalConfigRepo.findByKey(key);
        return configOptional.map(GlobalConfig::getValue).orElse(null);
    }

    @Override
    public void save(String key, String value) {
        Optional<GlobalConfig> configOptional = globalConfigRepo.findByKey(key);
        GlobalConfig config;
        if (configOptional.isPresent()) {
            config = configOptional.get();
        } else {
            config = new GlobalConfig();
            config.setCreateTime(new Date());
            config.setKey(key);
        }
        config.setUpdateTime(new Date());
        globalConfigRepo.save(config);
    }
}
