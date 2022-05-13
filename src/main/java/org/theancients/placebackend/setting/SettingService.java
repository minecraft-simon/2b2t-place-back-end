package org.theancients.placebackend.setting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SettingService {

    @Autowired
    private SettingRepository settingRepository;

    private static final Object LOCK = new Object();

    private Map<String, String> cachedSettings = new ConcurrentHashMap<>();

    @PostConstruct
    private void initSettings() {
        initSetting("maintenance_mode", "true");
        initSetting("cooldown_active", "true");
        initSetting("cooldown_seconds", "60");
        initSetting("frontend_polling_delay", "1000");
        initSetting("create_jobs_from_pixel_grid", "false");
    }

    private void initSetting(String name, String value) {
        if (!settingRepository.existsById(name)) {
            settingRepository.save(new Setting(name, value));
            cachedSettings.put(name, value);
        }
    }

    @Scheduled(fixedRate = 1000)
    private void fetchSettings() {
        synchronized (LOCK) {
            List<Setting> settings = settingRepository.findAll();
            for (Setting setting : settings) {
                cachedSettings.put(setting.getId(), setting.getValue());
            }
        }
    }

    public boolean getBoolean(String name, boolean defaultVal) {
        String value = cachedSettings.get(name);
        return (value == null) ? defaultVal : Boolean.parseBoolean(value);
    }

    public void setBoolean(String name, boolean value) {
        synchronized (LOCK) {
            String stringValue = String.valueOf(value);
            cachedSettings.put(name, stringValue);
            settingRepository.save(new Setting(name, stringValue));
            cachedSettings.put(name, stringValue);
        }
    }

    public int getInt(String name, int defaultVal) {
        String value = cachedSettings.get(name);
        return (value == null) ? defaultVal : Integer.parseInt(value);
    }

}
