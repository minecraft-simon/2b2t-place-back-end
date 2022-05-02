package org.theancients.placebackend.setting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Optional;

@Service
public class SettingService {

    @Autowired
    private SettingRepository settingRepository;

    @PostConstruct
    private void initSettings() {
        initSetting("maintenance_mode", "true");
        initSetting("cooldown_active", "true");
        initSetting("cooldown_seconds", "60");
    }

    private void initSetting(String name, String value) {
        if (!settingRepository.existsById(name)) {
            settingRepository.save(new Setting(name, value));
        }
    }

    public boolean getBoolean(String name, boolean defaultVal) {
        Optional<Setting> optionalSetting = settingRepository.findById(name);
        return optionalSetting.map(setting -> Boolean.parseBoolean(setting.getValue())).orElse(defaultVal);
    }

    public int getInt(String name, int defaultVal) {
        Optional<Setting> optionalSetting = settingRepository.findById(name);
        return optionalSetting.map(setting -> Integer.parseInt(setting.getValue())).orElse(defaultVal);
    }

}
