package cr.downloader.service;

/**
 * @author Beldon
 */
public interface GlobalConfigService {

    /**
     * 获取配置
     *
     * @param key
     * @return
     */
    String getConfig(String key);

    /**
     * 添加或更新配置
     *
     * @param key
     * @param value
     */
    void save(String key, String value);
}
