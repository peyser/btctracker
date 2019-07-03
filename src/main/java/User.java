import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class User {
    static Long id;
    ArrayList<String> curTypes;
    ArrayList<Double> curBalance;
    Double fiat;

    public Double usd(){
        String req_result = jsonRequest("https://api.exchangerate-api.com/v4/latest/USD").get("rates").getAsJsonObject().get("RUB").toString();
        return (Double.parseDouble(req_result));
    }
    public Double currency(){
        Double sum = 0.0;
        String coin;
        for (String cur: curTypes){
            System.out.println(cur);
            if (cur.equals("XMR")) {
                System.out.println("sssss");
                coin = cur;
            }
            else{
            coin = cur.toLowerCase();
            }
            System.out.println("https://api.cryptonator.com/api/ticker/"+ coin +"-usd");
            String str = jsonRequest("https://api.cryptonator.com/api/ticker/"+ coin +"-usd").get("ticker").getAsJsonObject().get("price").toString();
            Double value = Double.parseDouble(str.substring(1, str.length()-1));
            sum += value*curBalance.get(curTypes.indexOf(cur));
        }
        return sum;
    }

    public ArrayList<Double> result(){
        ArrayList<Double>  res = new ArrayList<>();
        Double balance = currency() * usd();
        Double dif = balance - fiat;
        res.add(balance);
        res.add(dif);
        System.out.println(res);
        return res;
    }



    public JsonObject jsonRequest(String link){
        String url_str = link;
        URL url = null;
        try {
            url = new URL(url_str);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection request = null;
        try {
            request = (HttpURLConnection) url.openConnection();
            request.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            request.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        JsonParser jp = new JsonParser();
        JsonElement root = null;
        try {
            root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        JsonObject jsonobj = root.getAsJsonObject();
        return jsonobj;
    }

    public volatile boolean shutdown = false;
   Runnable  updateData = new Runnable()

    {

        public void run()
        {
            while (!shutdown) {

                Bot bot = new Bot();
                bot.textMsgbyId(id, "➖➖➖➖➖➖➖");
                bot.textMsgbyId(id, "<b>Ваш баланс:</b> " + result().get(0).toString());
                bot.textMsgbyId(id, "<b>Разница с вкладом:</b>  " + result().get(1).toString());
                try {
                    TimeUnit.SECONDS.sleep(600);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    };
    public void setShutdown(){

        shutdown = true;
        System.out.println(shutdown);
    }

    User(Long id, Double fiat, ArrayList<String> curTypes, ArrayList<Double> curBalance){
        this.id = id;
        this.fiat = fiat;
        this.curBalance = curBalance;
        this.curTypes = curTypes;
    }
}
