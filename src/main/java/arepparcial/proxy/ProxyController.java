package arepparcial.proxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Locale.Category;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProxyController {
    private static final String USER_AGENT = "Mozilla/5.0";
    private static final String[] URLS = { "http://localhost:8080","http://localhost2:8080" };
    int indexServer = 0;
    String currentServer = URLS[indexServer];

    public void changeServer (){
        indexServer += 1;
        currentServer = URLS[indexServer];
    }


    @GetMapping("/catalan")
    public String catalan(@RequestParam("value") int number) throws IOException {
        String currentServer = URLS[indexServer] + "/catalan?value=" + number;

        URL obj = new URL(currentServer);
        try{
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);
        
        //The following invocation perform the connection implicitly before getting the code
        int responseCode = con.getResponseCode();
        System.out.println("GET Response Code :: " + responseCode);
        
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // print result
            System.out.println(response.toString());
            return(response.toString());
        } else {
            System.out.println("GET request not worked");
            return("error");
        }
    } catch (Exception e){
        if (indexServer < URLS.length-1){
        changeServer();
    }
        return("fallo pero usa:" + currentServer);
    }
}}

