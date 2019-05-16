package com.zipsecure.settings;

import lombok.Data;

@Data
public final class Settings {

    private String telegramId;
    private String emailAddress;
    private String credentialsFilePath;
    private int splitSize;
    private int timeCycleForSendArcInMinutes;

}
