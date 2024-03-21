package com.tsysinfo.oneabove.Utils;
import android.util.Log;
import org.json.JSONArray;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WebService {


public static String URL="http://oneabovefit.ezeeclub.net/MobileAppService.svc/";
    public static String responseString = null;
    private static int TimeOut = 20000;
    public static int timeoutFlag = 0;
    public static JSONArray SaveMessage(String MemberNo, String BranchNo, String Msg, String Time, String empNo, String webMethName, String RequestMethod) {
        return makeHttpRequest(webMethName + "?MemberNo=" + MemberNo + "&BranchNo=" + BranchNo + "&EmployeeNo=" + empNo + "&Message=" + Msg + "&Date_Time=" + Time, RequestMethod);
    }

    public static JSONArray Workoutdetail(String MemberNo, String BranchNo, String webMethName, String RequestMethod) {
        return makeHttpRequest(webMethName + "?MemberNo=" + MemberNo + "&BranchNo=" + BranchNo , RequestMethod);
    }

    public static JSONArray makeHttpRequest(String methodandParameter, String RequestMethod) {
        String myUrl = URL + methodandParameter;
        myUrl = myUrl.replace(" ", "%20");
        Log.w("URL", myUrl);
        JSONArray jsonArray = null;

        try {
            String res = "";
            java.net.URL obj = new URL(myUrl);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod(RequestMethod);
            con.setRequestProperty("User-Agent", "");
            int responseCode = con.getResponseCode();
            System.out.println("GET Response Code :: " + responseCode);
            if (responseCode == HttpURLConnection.HTTP_OK) { // success
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                // print result
                Log.w("Resp : ", response.toString());
                res = "[" + response.toString() + "]";
                //res=response.toString().replace("<string xmlns=\"http://schemas.microsoft.com/2003/10/Serialization/\">","");
                //res=res.replace("</string>","");
                Log.w("Resp1 : ", res);
                if (!res.equals(null))
                    jsonArray = new JSONArray(res);
            } else {
                System.out.println("GET request not worked");
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return jsonArray;
    }

}
