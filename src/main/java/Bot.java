import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Bot extends TelegramLongPollingBot {

    public static String type = "";

    public static ArrayList<String> curTypes = new ArrayList<>();
    public static ArrayList<Double> curBalance = new ArrayList<>();
    public boolean condition = false;

    User usr;



    @Override
    public void onUpdateReceived(Update update) {

        if ((update.hasMessage() && update.getMessage().hasText()) && (update.getMessage().getText().equals("/setup"))) {
            try {
                execute(keyBoardExec(update.getMessage().getChatId()));

            } catch (TelegramApiException e) {
                e.printStackTrace();

            }

        } else if (update.hasCallbackQuery()) {
            String call_data = update.getCallbackQuery().getData();
            System.out.println(call_data);

            if (call_data.equals("BTC")) {
                if (curTypes.contains("BTC") == false) {
                    balance(update, call_data);
                } else {
                    already(update);

                }
            }

            if (call_data.equals("ETH")) {
                if (curTypes.contains("ETH") == false) {
                    balance(update, call_data);
                } else {
                    already(update);
                }
            }
            if (call_data.equals("XMR")) {
                if (curTypes.contains("XMR") == false) {
                    balance(update, call_data);
                } else {
                    already(update);
                }
            }
            if (call_data.equals("LTC")) {
                if (curTypes.contains("LTC") == false) {
                    balance(update, call_data);
                } else {
                    already(update);
                }
            }
            if (call_data.equals("XRP")) {
                if (curTypes.contains("XRP") == false) {
                    balance(update, call_data);
                } else {
                    already(update);
                }
            }
            if (call_data.equals("BCH")) {
                if (curTypes.contains("BCH") == false) {
                    balance(update, call_data);
                } else {
                    already(update);
                }
            }
            if (call_data.equals("BNB")) {
                if (curTypes.contains("BNB") == false) {
                    balance(update, call_data);
                } else {
                    already(update);
                }
            }

            if (call_data.equals("EOS")) {
                if (curTypes.contains("BNB") == false) {
                    balance(update, call_data);
                } else {
                    already(update);
                }
            }


            if (call_data.equals("Clear")) {
                textMsgCallBack(update, "Конфигурация очищена");
                type = "";
                curBalance.clear();
                curTypes.clear();

            }

            if (call_data.equals("Save")) {
                textMsgCallBack(update, "Конфигурация сохранена");
                type = "fiat";
                textMsgCallBack(update, "Введите сумму вложений в рублях:");

            }
        } else if (type.equals("BTC")) {
            getBalance(update, type);
        } else if (type.equals("ETH")) {
            getBalance(update, type);
        } else if (type.equals("XMR")) {
            getBalance(update, type);
        } else if (type.equals("LTC")) {
            getBalance(update, type);
        } else if (type.equals("XRP")) {
            getBalance(update, type);
        } else if (type.equals("BCH")) {
            getBalance(update, type);
        } else if (type.equals("BNB")) {
            getBalance(update, type);
        } else if (type.equals("EOS")) {
            getBalance(update, type);
        } else if (type.equals("fiat")) {
            textMsg(update, "<b>Готово!</b>");
            type = "";
            condition = true;
            cycle(update,condition);
        } else {
            textMsg(update, "Введите <b>/setup</b> для начала работы");
            condition = false;
            cycle(update,condition);
            curTypes.clear();
            curBalance.clear();
        }
    }

    public void cycle(Update update,boolean condition) {
        if (condition){
        usr = new User(update.getMessage().getChatId(), Double.parseDouble(update.getMessage().getText()), curTypes, curBalance);
        usr.result();
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(usr.updateData, 0, 100, TimeUnit.SECONDS);}
        else {
            usr.setShutdown();
        }

    }


    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    public static SendMessage keyBoardExec(long chatId) {
        InlineKeyboardMarkup keyboard =new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardButtons = new ArrayList<InlineKeyboardButton>();
        keyboardButtons.add(new InlineKeyboardButton().setText("BTC")
                .setCallbackData("BTC"));
        keyboardButtons.add(new InlineKeyboardButton().setText("ETH")
                .setCallbackData("ETH"));
        keyboardButtons.add(new InlineKeyboardButton().setText("XMR")
                .setCallbackData("XMR"));
        keyboardButtons.add(new InlineKeyboardButton().setText("XRP")
                .setCallbackData("XRP"));

        List<InlineKeyboardButton> keyboardButtons2 = new ArrayList<InlineKeyboardButton>();
        keyboardButtons2.add(new InlineKeyboardButton().setText("LTC")
                .setCallbackData("LTC"));
        keyboardButtons2.add(new InlineKeyboardButton().setText("BCH")
                .setCallbackData("BCH"));
        keyboardButtons2.add(new InlineKeyboardButton().setText("EOS")
                .setCallbackData("EOS"));
        keyboardButtons2.add(new InlineKeyboardButton().setText("BNB")
                .setCallbackData("BNB"));

        List<InlineKeyboardButton> keyboardExit = new ArrayList<InlineKeyboardButton>();
        keyboardExit.add(new InlineKeyboardButton().setText("❌")
                .setCallbackData("Clear"));
        List<InlineKeyboardButton> keyboardSave = new ArrayList<InlineKeyboardButton>();
        keyboardExit.add(new InlineKeyboardButton().setText("✅")
                .setCallbackData("Save"));

        List<List<InlineKeyboardButton>> rowList= new ArrayList<List<InlineKeyboardButton>>();
        rowList.add(keyboardButtons);
        rowList.add(keyboardButtons2);
        rowList.add(keyboardExit);
        rowList.add(keyboardSave);
        keyboard.setKeyboard(rowList);
        System.out.println(keyboard);
        return new SendMessage().setChatId(chatId).setText("<i>Валюта:</i>").setParseMode("HTML").setReplyMarkup(keyboard);
    }
    public  void  already(Update update){

        textMsgCallBack(update,"Баланс уже введен, нажмите ❌ для сброса конфигурации");
    }
    public void  balance(Update update, String coin){
        type = coin;
        textMsgCallBack(update,"Введите ваш баланс "+ coin +":");

    }

    public  void getBalance(Update update, String type) {
        String balance = update.getMessage().getText();
        if (isNumeric(balance)) {
            curTypes.add(type);
            curBalance.add(Double.parseDouble(update.getMessage().getText()));
            System.out.println(curTypes);
            System.out.println(curBalance);
        } else {
            textMsg(update, "<b>Введите число</b>");
        }
    }

    public  void textMsgCallBack(Update update,String text){
        SendMessage message = new SendMessage(); // Create a SendMessage object with mandatory fields
        message.setChatId(update.getCallbackQuery().getMessage().getChatId());
        message.setText(text);
        message.setParseMode("HTML");
        try {
            execute(message); // Call method to send the message
        } catch (TelegramApiException e) {
            e.printStackTrace();

        }
    }
    public void textMsg(Update update,String text){
        SendMessage message = new SendMessage(); // Create a SendMessage object with mandatory fields
        message.setChatId(update.getMessage().getChatId());
        message.setText(text);
        message.setParseMode("HTML");
        try {
            execute(message); // Call method to send the message
        } catch (TelegramApiException e) {
            e.printStackTrace();

        }
    }

    public void textMsgbyId(Long id,String text){
        SendMessage message = new SendMessage(); // Create a SendMessage object with mandatory fields
        message.setChatId(id);
        message.setText(text);
        message.setParseMode("HTML");
        try {
            execute(message); // Call method to send the message
        } catch (TelegramApiException e) {
            e.printStackTrace();

        }
    }


    public void channelMsg(String text){
        SendMessage message = new SendMessage()
                .setChatId("-1001431812014")
                .setText(text)
                .setParseMode("HTML");
        try {
            execute(message); // Call method to send the messag
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

    @Override
    public String getBotUsername() {
        // TODO
        return "BTCTRACKER_BOT";
    }

    @Override
    public String getBotToken() {
        // TODO
        return "";
    }
}