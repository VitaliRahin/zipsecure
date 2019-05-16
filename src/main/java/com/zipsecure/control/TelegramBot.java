package com.zipsecure.control;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.toIntExact;


public class TelegramBot extends TelegramLongPollingBot {
    private String telegramId;
    public TelegramBot(String telegramId){
        this.telegramId = telegramId;
    }
    @Override
    public String getBotUsername() {
        return "Shutdown bot";
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage()) {
            if(update.getMessage().hasText()){
                try {
                    execute(sendInlineKeyBoardMessage(update.getMessage().getChatId()));
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        } else if (update.hasCallbackQuery()) {
            String call = update.getCallbackQuery().getData();
            long message_id = update.getCallbackQuery().getMessage().getMessageId();
            if("shutdown".equals(call)){
                EditMessageText message1 = new EditMessageText()
                        .setChatId(update.getCallbackQuery().getMessage().getChatId())
                        .setMessageId(toIntExact(message_id))
                        .setText("The computer will be turned off");
                try {
                    execute(message1);
                    shutdownPC();
                } catch (TelegramApiException | IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    @Override
    public String getBotToken() {
        return telegramId;
    }


    private static SendMessage sendInlineKeyBoardMessage(long chatId) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Shutdown computer").setCallbackData("shutdown"));
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return new SendMessage().setChatId(chatId).setText("Меню").setReplyMarkup(inlineKeyboardMarkup);
    }

    private void shutdownPC() throws IOException {
        Runtime runtime = Runtime.getRuntime();
        Process proc = runtime.exec("shutdown -s -t 0");
        System.exit(0);
    }
}