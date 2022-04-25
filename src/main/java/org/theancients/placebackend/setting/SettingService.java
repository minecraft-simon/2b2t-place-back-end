package org.theancients.placebackend.setting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SettingService {

    @Autowired
    private SettingRepository settingRepository;

    public void setBoolean(String name, boolean value) {

    }

    public boolean getBoolean(String name, boolean defaultVal) {
        Optional<Setting> optionalSetting = settingRepository.findById(name);
        return optionalSetting.map(setting -> Boolean.parseBoolean(setting.getValue())).orElse(defaultVal);
    }

}
