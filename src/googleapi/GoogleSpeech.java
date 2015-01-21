package googleapi;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.*;
import java.net.*;

public class GoogleSpeech {
    static final String REQUEST = "https://www.google.com/speech-api/v2/recognize?output=json&lang=ru-RU&key=" +
            "AIzaSyCjgn4qLBs2lGl4s5y5PWfw_kq3mJqP40g";

    static public String getStringFromFlac(String filePath) {
        String res = "";
        try{
            Path path = Paths.get(filePath);

            byte[] data = Files.readAllBytes(path);

            URL url = new URL(REQUEST);

            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "audio/x-flac; rate=16000");
            connection.setRequestProperty("User-Agent", "speech2text");
            connection.setConnectTimeout(250000);
            connection.setUseCaches (false);
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream ());
            wr.write(data);
            wr.flush();
            wr.close();
            connection.disconnect();

            System.out.println("Done");
            System.out.println();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            connection.getInputStream()));
            String decodedString;
            while ((decodedString = in.readLine()) != null) {
                res += decodedString;
            }
        }
        catch(MalformedURLException c){
            c.printStackTrace();
        }
        catch(ProtocolException p){
            p.printStackTrace();
        }
        catch(IOException i){
            i.printStackTrace();
        }

        return res.split("\"")[9];
    }

    static public String getString(byte[] data) {
        String res = "";
        try{

            URL url = new URL(REQUEST);

            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "audio/x-flac; rate=16000");
            connection.setRequestProperty("User-Agent", "speech2text");
            connection.setConnectTimeout(80000);
            connection.setUseCaches (false);
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream ());
            wr.write(data);
            wr.flush();
            wr.close();
            connection.disconnect();

            System.out.println("Done");
            System.out.println();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            connection.getInputStream()));
            String decodedString;
            while ((decodedString = in.readLine()) != null) {
                res += decodedString;
            }
        }
        catch(MalformedURLException c){
            c.printStackTrace();
        }
        catch(ProtocolException p){
            p.printStackTrace();
        }
        catch(IOException i){
            i.printStackTrace();
        }

        if (res.endsWith("{\"result\":[]}")) {
            return "Couldn't recognize";
        }

        try {
            return res.split("\"")[9];
        } catch (Exception e) {
            e.printStackTrace();
            return res;
        }
    }
}
