package org.example;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.rmi.server.RemoteObjectInvocationHandler;
import java.util.HashMap;
import java.util.Map;

public class TzviBot extends TelegramLongPollingBot {
    private Map<Long,Reminder >reminderMap;
    public TzviBot(){
        this.reminderMap=new HashMap<>();
        sendNotification();
    }

    public String getBotUsername() {
        return "tzviRemanderBot";
    }
    public String getBotToken() {
        return "6154812753:AAENZ_15VpMwm4AGVnPJX2rjpBMEv7hmTJk";
    }
    public void onUpdateReceived(Update update) {
        long chatId = update.getMessage().getChatId();
        Reminder reminder = reminderMap.get(chatId);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        if (reminder == null) {
            sendMessage.setText("what do you want me to remind you?");
            reminder = new Reminder();
            this.reminderMap.put(chatId, reminder);
        } else{
            if (reminder.getWhat() == null) {
                String text=update.getMessage().getText();
                reminder.setWhat(text);
                sendMessage.setText("when?");
            }else if (reminder.getWhen()==0){
                String text=update.getMessage().getText();
                reminder.setWhen(Integer.valueOf(text));
                reminder.setCompleted(true);
                sendMessage.setText("got it");
            }
        }
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
    private void sendNotification(){
         new Thread(()->{
             while (true){
                 for (long chatId : this.reminderMap.keySet()) {
                     Reminder reminder = this.reminderMap.get(chatId);
                     if (reminder.isCompleted()&&reminder.getWhen()==0){
                         SendMessage sendMessage = new SendMessage();
                         sendMessage.setChatId(chatId);
                         sendMessage.setText(reminder.getWhat());
                         try {
                             execute(sendMessage);
                             reminder.setSent(true);
                         } catch (TelegramApiException e) {
                             throw new RuntimeException(e);
                         }
                     }
                     else {
                         if (reminder.getWhen()>0) {
                             reminder.decrementTime();
                         }
                     }
                 }
                 try {
                     Thread.sleep(1000);
                 } catch (InterruptedException e) {
                     throw new RuntimeException(e);
                 }
             }
         }).start();
    }
}
