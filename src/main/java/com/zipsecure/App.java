package com.zipsecure;

import com.beust.jcommander.JCommander;
import com.google.gson.Gson;
import com.zipsecure.control.TelegramBot;
import com.zipsecure.settings.CliArgs;
import com.zipsecure.settings.Settings;
import com.zipsecure.tasks.SendBackupTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

public class App {
 //   private static final Logger LOGGER = LoggerFactory.getLogger(App.class);
    private static CliArgs cliArgs;
    private static Settings settings;


    public static void main(String[] args) throws IOException {
        cliArgs = new CliArgs();
        JCommander.newBuilder().addObject(cliArgs).build().parse(args);
        String pathSettings = cliArgs.getPathSettings();
        String stringJsonSettings =  new String(Files.readAllBytes(Paths.get(pathSettings)), StandardCharsets.UTF_8);
        Gson gson = new Gson();
        settings = gson.fromJson(stringJsonSettings, Settings.class);

        ApiContextInitializer.init();
        TelegramBotsApi botsApi = new TelegramBotsApi();

        try {
            botsApi.registerBot(new TelegramBot(settings.getTelegramId()));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        sendBackupTask();

    }
    private static void sendBackupTask() {
        Timer time = new Timer();
        SendBackupTask sendBackupTask = new  SendBackupTask.Builder(cliArgs.getBackupPath(), cliArgs.getDestinationPath())
                .credentialsFilePath(settings.getCredentialsFilePath()).publicKeyPath(cliArgs.getPublicKeyPath()).splitSize(settings.getSplitSize())
                .email(settings.getEmailAddress()).build();
        time.schedule(sendBackupTask, 0, TimeUnit.SECONDS.toMillis(settings.getTimeCycleForSendArcInMinutes()));
    }
}
