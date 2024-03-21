package com.tsysinfo.oneabove.Utils;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.tsysinfo.oneabove.database.Models;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONArray;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
public class WebServiceSoap {
    // Namespace of the Webservice - can be found in WSDL
    public static String responseString;
    public static int timeoutFlag = 0;
    static SoapSerializationEnvelope envelope;
    static JSONArray jobj = null;
    static Models mod;
    private static String NAMESPACE = "http://tempuri.org/";
    private static int TimeOut = 20000;
    private static String URL = "/AndroidWebService.asmx";// Make
    private static String SOAP_ACTION = "http://tempuri.org/";
    HttpTransportSE androidHttpTransport;
    public static JSONArray registration(String data, String webMethName) {
        Log.w("Web Service", "Form Name : " + webMethName);
        Log.w("Web Service", "Data : " + data);
        // Create request
        SoapObject request = new SoapObject(NAMESPACE, webMethName);
        // Property which holds input parameters
        PropertyInfo dataPI = new PropertyInfo();
        // Set Username
        dataPI.setName("data");
        // Set Value
        dataPI.setValue(data);
        // Set dataType
        dataPI.setType(String.class);
        // Add the property to request object
        request.addProperty(dataPI);
        envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        jobj = new JSONArray();
        jobj = serverConnection(webMethName);
        return jobj;
    }
    @SuppressWarnings("deprecation")
    public static JSONArray serverConnection(String webMethName) {
        mod = new Models();
        Cursor ip = mod.getData("iptable");
        ip.moveToFirst();
        String ipadd = ip.getString(0).trim();
        String port = ip.getString(1).trim();
        String PreFix = "http://" + ipadd + ":" + port;
        ip.close();
        JSONArray jsonObj = null;
        timeoutFlag = 0;
        HttpTransportSE androidHttpTransport = new HttpTransportSE(
                PreFix + URL, TimeOut);
        Log.w("Web Service ", "URL : " + PreFix + URL);
        Log.w("Web Service ", "Method : " + webMethName);
        try {
            // Thread.sleep(20000);
            // Invoke web service
            androidHttpTransport.call(SOAP_ACTION + webMethName, envelope);
            // Get the response
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            Log.w("Web Service ", "Env " + envelope);
            // Assign it to boolean variable variable
            Log.w("Web Service ", "Data " + response.toString());
            // loginStatus = Boolean.parseBoolean(response.toString());
            responseString = response.toString();
            jsonObj = new JSONArray(response.toString());
        } catch (ConnectTimeoutException e) {
            // showAlertDialog();
            timeoutFlag = 1;
            Log.w("Web Service", "ConnectTimeoutException");
        } catch (ConnectException e) {
            // showAlertDialog();
            timeoutFlag = 1;
            Log.w("Web Service", "ConnectException");
        } catch (EOFException e) {
            // showAlertDialog();
            Log.w("Web Service", "EOFException");
        } catch (ClassCastException e) {
            e.printStackTrace();
            // showAlertDialog();
            Log.w("Web Service", "ClassCastException");
        } catch (SocketTimeoutException e) {
            timeoutFlag = 1;
            // showAlertDialog();
            Log.w("Web Service", "Time Out");
        } catch (Exception e) {
            // Assign Error Status true in static variable 'errored'
            // CheckDNLoginActivity.errored = true;
            e.printStackTrace();
        }
        // Return booleam to calling object
        return jsonObj;
    }
    public static JSONArray imeiStatus(String imei, String webMethName) {
        Log.w("Web Service", "Form Name : " + webMethName);
        Log.w("Web Service", "IMEI : " + imei);
        // Create request
        SoapObject request = new SoapObject(NAMESPACE, webMethName);
        // Property which holds input parameters
        PropertyInfo imeiPI = new PropertyInfo();
        // Set Username
        imeiPI.setName("IMEI");
        // Set Value
        imeiPI.setValue(imei);
        // Set dataType
        imeiPI.setType(String.class);
        // Add the property to request object
        request.addProperty(imeiPI);
        envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        jobj = new JSONArray();
        jobj = serverConnection(webMethName);
        return jobj;
    }
    public static JSONArray invokeLoginWS(String userName, String passWord,
                                          String webMethName) {
        Log.w("Web Service", "Form Name : " + webMethName);
        Log.w("Web Service", "User Name : " + userName);
        Log.w("Web Service", "Password : " + passWord);
        // Create request
        SoapObject request = new SoapObject(NAMESPACE, webMethName);
        // Property which holds input parameters
        PropertyInfo unamePI = new PropertyInfo();
        PropertyInfo passPI = new PropertyInfo();
        // Set Username
        unamePI.setName("username");
        // Set Value
        unamePI.setValue(userName);
        // Set dataType
        unamePI.setType(String.class);
        // Add the property to request object
        request.addProperty(unamePI);
        // Set Password
        passPI.setName("password");
        // Set dataType
        passPI.setValue(passWord);
        // Set dataType
        passPI.setType(String.class);
        // Add the property to request object
        request.addProperty(passPI);
        // Create envelope
        envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        jobj = new JSONArray();
        jobj = serverConnection(webMethName);
        return jobj;
    }
    // Methods For Sync
    public static JSONArray Sync(String bno, String webMethName) {
        Log.w("Web Service", "Form Name : " + webMethName);
        Log.w("Web Service", "Branch No : " + bno);
        // Create request
        SoapObject request = new SoapObject(NAMESPACE, webMethName);
        // Property which holds input parameters
        PropertyInfo bnoPI = new PropertyInfo();
        // Set Username
        bnoPI.setName("BranchNo");
        // Set Value
        bnoPI.setValue(bno);
        // Set dataType
        bnoPI.setType(String.class);
        // Add the property to request object
        request.addProperty(bnoPI);
        envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        jobj = new JSONArray();
        jobj = serverConnection(webMethName);
        return jobj;
    }
    // Methods For SyncAuthority
    public static JSONArray SyncAuthority(String UserNo, String webMethName) {
        Log.w("Web Service", "Form Name : " + webMethName);
        Log.w("Web Service", "UserNo : " + UserNo);
        // Create request
        SoapObject request = new SoapObject(NAMESPACE, webMethName);
        // Property which holds input parameters
        PropertyInfo bnoPI = new PropertyInfo();
        // Set Username
        bnoPI.setName("UserNo");
        // Set Value
        bnoPI.setValue(UserNo);
        // Set dataType
        bnoPI.setType(String.class);
        // Add the property to request object
        request.addProperty(bnoPI);
        envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        jobj = new JSONArray();
        jobj = serverConnection(webMethName);
        return jobj;
    }
    public static JSONArray SyncEnquiryType(String UserNo, String webMethName) {
        Log.w("Web Service", "Form Name : " + webMethName);
        Log.w("Web Service", "BNo : " + UserNo);
        // Create request
        SoapObject request = new SoapObject(NAMESPACE, webMethName);
        // Property which holds input parameters
        PropertyInfo bnoPI = new PropertyInfo();
        // Set Username
        bnoPI.setName("BranchNo");
        // Set Value
        bnoPI.setValue(UserNo);
        // Set dataType
        bnoPI.setType(String.class);
        // Add the property to request object
        request.addProperty(bnoPI);
        envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        jobj = new JSONArray();
        jobj = serverConnection(webMethName);
        return jobj;
    }
    public static JSONArray SyncMsg(String userId, String webMethName) {
        Log.w("Web Service", "Form Name : " + webMethName);
        Log.w("Web Service", "User Name : " + userId);
        // Create request
        SoapObject request = new SoapObject(NAMESPACE, webMethName);
        // Property which holds input parameters
        PropertyInfo userNamePI = new PropertyInfo();
        // Set Username
        userNamePI.setName("username");
        // Set Value
        userNamePI.setValue(userId);
        // Set dataType
        userNamePI.setType(String.class);
        // Add the property to request object
        request.addProperty(userNamePI);
        envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        jobj = new JSONArray();
        jobj = serverConnection(webMethName);
        return jobj;
    }
    public static JSONArray attendence(String mobno, String webMethName) {
        Log.w("Web Service", "Form Name : " + webMethName);
        Log.w("Web Service", "Mobile No : " + mobno);
        // Create request
        SoapObject request = new SoapObject(NAMESPACE, webMethName);
        // Property which holds input parameters
        PropertyInfo mobnoPI = new PropertyInfo();
        // Set Username
        mobnoPI.setName("MobileNo");
        // Set Value
        mobnoPI.setValue(mobno);
        // Set dataType
        mobnoPI.setType(String.class);
        // Add the property to request object
        request.addProperty(mobnoPI);
        envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        jobj = new JSONArray();
        jobj = serverConnection(webMethName);
        return jobj;
    }
    public static JSONArray enquiry(String BranchNo, String UserId,
                                    String data, String webMethName) {
        Log.w("Web Service", "Form Name : " + webMethName);
        Log.w("Web Service", "Branch No : " + BranchNo);
        Log.w("Web Service", "User Name : " + UserId);
        Log.w("Web Service", "Data : " + data);
        // Create request
        SoapObject request = new SoapObject(NAMESPACE, webMethName);
        // Property which holds input parameters
        PropertyInfo branchNoPI = new PropertyInfo();
        PropertyInfo userIdPI = new PropertyInfo();
        PropertyInfo dataPI = new PropertyInfo();
        // Set BranchNo
        branchNoPI.setName("BranchNo");
        // Set Value
        branchNoPI.setValue(BranchNo);
        // Set dataType
        branchNoPI.setType(String.class);
        // Add the property to request object
        request.addProperty(branchNoPI);
        // Set UserId
        userIdPI.setName("username");
        // Set dataType
        userIdPI.setValue(UserId);
        // Set dataType
        userIdPI.setType(String.class);
        // Add the property to request object
        request.addProperty(userIdPI);
        // Set UserId
        dataPI.setName("data");
        // Set dataType
        dataPI.setValue(data);
        // Set dataType
        dataPI.setType(String.class);
        // Add the property to request object
        request.addProperty(dataPI);
        // Create envelope
        envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        jobj = new JSONArray();
        jobj = serverConnection(webMethName);
        return jobj;
    }
    public static JSONArray receiptServerPayment(String BranchNo, String UserId,
                                                 String data, String remarks, String imagePath, String webMethName) throws FileNotFoundException {
        String encodedString = ""; //= "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";
        if (imagePath == null) {
        } else {
            if (imagePath.trim().length() > 0) {
                File file = new File(imagePath);
                if (file.exists()) {
                    Bitmap image = BitmapFactory.decodeFile(imagePath);
                    image = Bitmap.createScaledBitmap(image, 190, 130, false);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    image.compress(Bitmap.CompressFormat.PNG, 50, stream);
                    byte[] byteArray = stream.toByteArray();
                    encodedString = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    //		Log.w(".............", "base64 : " + encodedString);
                } else {
                    encodedString = "";
                }
            } else {
                encodedString = "";
            }
        }
        Log.w(".............", encodedString);
        Log.w("Web Service", "Form Name : " + webMethName);
        Log.w("Web Service", "Branch No : " + BranchNo);
        Log.w("Web Service", "User Name : " + UserId);
        Log.w("Web Service", "Data : " + data);
        Log.w("Web Service", "Remarks : " + remarks);
        Log.w("Web Service", "imagepath : " + imagePath);
        Log.w("Web Service", "encoded image : " + encodedString);
        // Create request
        SoapObject request = new SoapObject(NAMESPACE, webMethName);
        // Property which holds input parameters
        PropertyInfo branchNoPI = new PropertyInfo();
        PropertyInfo userIdPI = new PropertyInfo();
        PropertyInfo dataPI = new PropertyInfo();
        // Set BranchNo
        branchNoPI.setName("BranchNo");
        // Set Value
        branchNoPI.setValue(BranchNo);
        // Set dataType
        branchNoPI.setType(String.class);
        // Add the property to request object
        request.addProperty(branchNoPI);
        // Set UserId
        userIdPI.setName("username");
        // Set dataType
        userIdPI.setValue(UserId);
        // Set dataType
        userIdPI.setType(String.class);
        // Add the property to request object
        request.addProperty(userIdPI);
        // Set UserId
        dataPI.setName("data");
        // Set dataType
        dataPI.setValue(data);
        // Set dataType
        dataPI.setType(String.class);
        // Add the property to request object
        request.addProperty(dataPI);
        request.addProperty("Remark", remarks);
        request.addProperty("image", encodedString);
        // Create envelope
        envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        jobj = new JSONArray();
        jobj = serverConnection(webMethName);
        return jobj;
    }
    public static JSONArray planUpgrade(String BranchNo, String UserId,
                                        String data, String PlanSaleNo, String MemberNo, String webMethName) {
        Log.w("Web Service", "Form Name : " + webMethName);
        Log.w("Web Service", "Branch No : " + BranchNo);
        Log.w("Web Service", "Member No : " + MemberNo);
        Log.w("Web Service", "User Name : " + UserId);
        Log.w("Web Service", "Data : " + data);
        Log.w("Web Service", "PlanSaleNo : " + PlanSaleNo);
        // Create request
        SoapObject request = new SoapObject(NAMESPACE, webMethName);
        // Property which holds input parameters
        request.addProperty("BranchNo", BranchNo);
        request.addProperty("UserName", UserId);
        request.addProperty("MemeberNo", MemberNo);
        request.addProperty("data", data);

      /*  <MemeberNo>string</MemeberNo>
        <BranchNo>string</BranchNo>
        <data>string</data>
        <UserName>string</UserName>*/
        // Create envelope
        envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        jobj = new JSONArray();
        jobj = serverConnection(webMethName);
        return jobj;
    }
    public static JSONArray saveMesurments(String BranchNo, String MemberNO, String UserName, String Counsellor, String data, String webMethName) {
        Log.w("Web Service", "Form Name : " + webMethName);
        Log.w("Web Service", "Branch No : " + BranchNo);
        Log.w("Web Service", "User Name : " + UserName);
        Log.w("Web Service", "Member No : " + MemberNO);
        Log.w("Web Service", "Counsellor : " + Counsellor);
        Log.w("Web Service", "Data : " + data);
        // Create request
        SoapObject request = new SoapObject(NAMESPACE, webMethName);
        // Property which holds input parameters
        PropertyInfo branchNoPI = new PropertyInfo();
        PropertyInfo MemberNoPI = new PropertyInfo();
        PropertyInfo userNamePI = new PropertyInfo();
        PropertyInfo dataPI = new PropertyInfo();
        PropertyInfo CounsellorPI = new PropertyInfo();
        // Set BranchNo
        branchNoPI.setName("BranchNo");
        // Set Value
        branchNoPI.setValue(BranchNo);
        // Set dataType
        branchNoPI.setType(String.class);
        // Add the property to request object
        request.addProperty(branchNoPI);
        // Set UserId
        CounsellorPI.setName("Counseller");
        // Set dataType
        CounsellorPI.setValue(Counsellor);
        // Set dataType
        CounsellorPI.setType(String.class);
        // Add the property to request object
        request.addProperty(CounsellorPI);
        // Set UserId
        MemberNoPI.setName("MemberNo");
        // Set dataType
        MemberNoPI.setValue(MemberNO);
        // Set dataType
        MemberNoPI.setType(String.class);
        // Add the property to request object
        request.addProperty(MemberNoPI);
        // Set UserId
        userNamePI.setName("UserName");
        // Set dataType
        userNamePI.setValue(UserName);
        // Set dataType
        userNamePI.setType(String.class);
        // Add the property to request object
        request.addProperty(userNamePI);
        // Set UserId
        dataPI.setName("data");
        // Set dataType
        dataPI.setValue(data);
        // Set dataType
        dataPI.setType(String.class);
        // Add the property to request object
        request.addProperty(dataPI);
        // Create envelope
        envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        jobj = new JSONArray();
        jobj = serverConnection(webMethName);
        return jobj;
    }
    public static JSONArray getMesurmentData(String BranchNo, String Mes, String webMethName) {
        Log.w("Web Service", "Form Name : " + webMethName);
        Log.w("Web Service", "Branch No : " + BranchNo);
        // Create request
        SoapObject request = new SoapObject(NAMESPACE, webMethName);
        request.addProperty("BranchNo", BranchNo);
        request.addProperty("MeasurementNo", Mes);
        // Create envelope
        envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        jobj = new JSONArray();
        jobj = serverConnection(webMethName);
        return jobj;
    }
    public static JSONArray ListMesurments(String BranchNo, String Membername, String webMethName) {
        Log.w("Web Service", "Form Name : " + webMethName);
        Log.w("Web Service", "Branch No : " + BranchNo);
        // Create request
        SoapObject request = new SoapObject(NAMESPACE, webMethName);
        // Property which holds input parameters
        PropertyInfo branchNoPI = new PropertyInfo();
        // Set BranchNo
        branchNoPI.setName("BranchNo");
        // Set Value
        branchNoPI.setValue(BranchNo);
        // Set dataType
        branchNoPI.setType(String.class);
        // Add the property to request object
        request.addProperty(branchNoPI);

       /* PropertyInfo MemberNoPI = new PropertyInfo();
        // Set BranchNo
        MemberNoPI.setName("MemberNo");
        // Set Value
        MemberNoPI.setValue(MemberNo);
        // Set dataType
        MemberNoPI.setType(String.class);
        // Add the property to request object
        request.addProperty(MemberNoPI);

*/
        PropertyInfo MemberNamePI = new PropertyInfo();
        // Set BranchNo
        MemberNamePI.setName("MemberName");
        // Set Value
        MemberNamePI.setValue(Membername);
        // Set dataType
        MemberNamePI.setType(String.class);
        // Add the property to request object
        request.addProperty(MemberNamePI);
        // Create envelope
        envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        jobj = new JSONArray();
        jobj = serverConnection(webMethName);
        return jobj;
    }
    public static JSONArray getDietSugessionList(String Memberno, String BranchNo, String webMethName) {
        Log.w("Web Service", "Form Name : " + webMethName);
        Log.w("Web Service", "Memno No : " + Memberno);
        Log.w("Web Service", "Branch No : " + BranchNo);
        // Create request
        SoapObject request = new SoapObject(NAMESPACE, webMethName);
        request.addProperty("MemberNo", Memberno);
        request.addProperty("BranchNo", BranchNo);
        // Create envelope
        envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        jobj = new JSONArray();
        jobj = serverConnection(webMethName);
        return jobj;
    }
    public static JSONArray sentMessage(String BranchNo, String UserId,
                                        String data, String webMethName) {
        Log.w("Web Service", "Form Name : " + webMethName);
        Log.w("Web Service", "Branch No : " + BranchNo);
        Log.w("Web Service", "User Name : " + UserId);
        Log.w("Web Service", "Data : " + data);
        // Create request
        SoapObject request = new SoapObject(NAMESPACE, webMethName);
        // Property which holds input parameters
        PropertyInfo branchNoPI = new PropertyInfo();
        PropertyInfo userIdPI = new PropertyInfo();
        PropertyInfo dataPI = new PropertyInfo();
        // Set BranchNo
        branchNoPI.setName("BranchNo");
        // Set Value
        branchNoPI.setValue(BranchNo);
        // Set dataType
        branchNoPI.setType(String.class);
        // Add the property to request object
        request.addProperty(branchNoPI);
        // Set UserId
        userIdPI.setName("username");
        // Set dataType
        userIdPI.setValue(UserId);
        // Set dataType
        userIdPI.setType(String.class);
        // Add the property to request object
        request.addProperty(userIdPI);
        // Set UserId
        dataPI.setName("data");
        // Set dataType
        dataPI.setValue(data);
        // Set dataType
        dataPI.setType(String.class);
        // Add the property to request object
        request.addProperty(dataPI);
        // Create envelope
        envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        jobj = new JSONArray();
        jobj = serverConnection(webMethName);
        return jobj;
    }
    // Sales
    public static JSONArray sales(String BranchNo, String FromDate, String ToDate,
                                  String webMethName) {
        Log.w("Web Service", "Form Name : " + webMethName);
        Log.w("Web Service", "Branch No : " + BranchNo);
        Log.w("Web Service", "FromDate : " + FromDate);
        Log.w("Web Service", "ToDate : " + ToDate);
        // Create request
        SoapObject request = new SoapObject(NAMESPACE, webMethName);
        request.addProperty("BranchNo", BranchNo);
        request.addProperty("FromDate", FromDate);
        request.addProperty("ToDate", ToDate);
        // Create envelope
        envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        jobj = new JSONArray();
        jobj = serverConnection(webMethName);
        return jobj;
    }
    // Receipt
    public static JSONArray receipt(String BranchNo, String UserId,
                                    String MemberNo, String data, String Remarks, String webMethName) {
        Log.w("Web Service", "Form Name : " + webMethName);
        Log.w("Web Service", "Branch No : " + BranchNo);
        Log.w("Web Service", "User Name : " + UserId);
        Log.w("Web Service", "Member No : " + MemberNo);
        Log.w("Web Service", "Data : " + data);
        Log.w("Web Service", "Remarks : " + Remarks);
        // Create request
        SoapObject request = new SoapObject(NAMESPACE, webMethName);
        // Property which holds input parameters
        PropertyInfo branchNoPI = new PropertyInfo();
        PropertyInfo userIdPI = new PropertyInfo();
        PropertyInfo memberNoPI = new PropertyInfo();
        PropertyInfo dataPI = new PropertyInfo();
        // Set BranchNo
        branchNoPI.setName("BranchNo");
        // Set Value
        branchNoPI.setValue(BranchNo);
        // Set dataType
        branchNoPI.setType(String.class);
        // Add the property to request object
        request.addProperty(branchNoPI);
        // Set UserId
        userIdPI.setName("username");
        // Set dataType
        userIdPI.setValue(UserId);
        // Set dataType
        userIdPI.setType(String.class);
        // Add the property to request object
        request.addProperty(userIdPI);
        // Set MemberNo
        memberNoPI.setName("MemberNo");
        // Set dataType
        memberNoPI.setValue(MemberNo);
        // Set dataType
        memberNoPI.setType(String.class);
        // Add the property to request object
        request.addProperty(memberNoPI);
        // Set Data
        dataPI.setName("data");
        // Set dataType
        dataPI.setValue(data);
        // Set dataType
        dataPI.setType(String.class);
        // Add the property to request object
        request.addProperty(dataPI);
        request.addProperty("Remark", Remarks);
        // Create envelope
        envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        jobj = new JSONArray();
        jobj = serverConnection(webMethName);
        return jobj;
    }
    // PTRecord Request
    public static JSONArray ptRecordRequest(String BranchNo,
                                            String CounsellorNo, String webMethName) {
        Log.w("Web Service", "Form Name : " + webMethName);
        Log.w("Web Service", "Branch No : " + BranchNo);
        Log.w("Web Service", "Counsellor No : " + CounsellorNo);
        // Create request
        SoapObject request = new SoapObject(NAMESPACE, webMethName);
        // Property which holds input parameters
        PropertyInfo branchNoPI = new PropertyInfo();
        PropertyInfo CounsellorNoPI = new PropertyInfo();
        // Set BranchNo
        branchNoPI.setName("BranchNo");
        // Set Value
        branchNoPI.setValue(BranchNo);
        // Set dataType
        branchNoPI.setType(String.class);
        // Add the property to request object
        request.addProperty(branchNoPI);
        // Set CounsellorNo
        CounsellorNoPI.setName("CounsellerNo");
        // Set dataType
        CounsellorNoPI.setValue(CounsellorNo);
        // Set dataType
        CounsellorNoPI.setType(String.class);
        // Add the property to request object
        request.addProperty(CounsellorNoPI);
        // Create envelope
        envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        jobj = new JSONArray();
        jobj = serverConnection(webMethName);
        return jobj;
    }
    // PTRecord Response
    public static JSONArray ptRecordResponse(String BranchNo, String UserId,
                                             String CounsellorNo, String data, String webMethName) {
        Log.w("Web Service", "Form Name : " + webMethName);
        Log.w("Web Service", "Branch No : " + BranchNo);
        Log.w("Web Service", "User Name : " + UserId);
        Log.w("Web Service", "Counsellor No : " + CounsellorNo);
        Log.w("Web Service", "Data : " + data);
        // Create request
        SoapObject request = new SoapObject(NAMESPACE, webMethName);
        // Property which holds input parameters
        PropertyInfo branchNoPI = new PropertyInfo();
        PropertyInfo userIdPI = new PropertyInfo();
        PropertyInfo CounsellorNoPI = new PropertyInfo();
        PropertyInfo dataPI = new PropertyInfo();
        // Set BranchNo
        branchNoPI.setName("BranchNo");
        // Set Value
        branchNoPI.setValue(BranchNo);
        // Set dataType
        branchNoPI.setType(String.class);
        // Add the property to request object
        request.addProperty(branchNoPI);
        // Set UserId
        userIdPI.setName("username");
        // Set dataType
        userIdPI.setValue(UserId);
        // Set dataType
        userIdPI.setType(String.class);
        // Add the property to request object
        request.addProperty(userIdPI);
        // Set CounsellorNo
        CounsellorNoPI.setName("CounsellerNo");
        // Set dataType
        CounsellorNoPI.setValue(CounsellorNo);
        // Set dataType
        CounsellorNoPI.setType(String.class);
        // Add the property to request object
        request.addProperty(CounsellorNoPI);
        // Set Data
        dataPI.setName("data");
        // Set dataType
        dataPI.setValue(data);
        // Set dataType
        dataPI.setType(String.class);
        // Add the property to request object
        request.addProperty(dataPI);
        // Create envelope
        envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        jobj = new JSONArray();
        jobj = serverConnection(webMethName);
        return jobj;
    }
    // Renew
    public static JSONArray renewRequest(String BranchNo, String MemberNo,
                                         String webMethName) {
        Log.w("Web Service", "Form Name : " + webMethName);
        Log.w("Web Service", "Branch No : " + BranchNo);
        Log.w("Web Service", "Member No : " + MemberNo);
        // Create request
        SoapObject request = new SoapObject(NAMESPACE, webMethName);
        // Property which holds input parameters
        PropertyInfo branchNoPI = new PropertyInfo();
        PropertyInfo memberNoPI = new PropertyInfo();
        // Set BranchNo
        branchNoPI.setName("BranchNo");
        // Set Value
        branchNoPI.setValue(BranchNo);
        // Set dataType
        branchNoPI.setType(String.class);
        // Add the property to request object
        request.addProperty(branchNoPI);
        // Set memberNo
        memberNoPI.setName("MemberNo");
        // Set dataType
        memberNoPI.setValue(MemberNo);
        // Set dataType
        memberNoPI.setType(String.class);
        // Add the property to request object
        request.addProperty(memberNoPI);
        // Create envelope
        envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        jobj = new JSONArray();
        jobj = serverConnection(webMethName);
        return jobj;
    }
    // GlobalSales
    public static JSONArray globalSales(String FromDate, String ToDate,
                                        String webMethName) {
        Log.w("Web Service", "Form Name : " + webMethName);
        Log.w("Web Service", "FromDate : " + FromDate);
        Log.w("Web Service", "ToDate : " + ToDate);
        // Create request
        SoapObject request = new SoapObject(NAMESPACE, webMethName);
        // Property which holds input parameters
        request.addProperty("FromDate", FromDate);
        request.addProperty("ToDate", ToDate);
        // Create envelope
        envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        jobj = new JSONArray();
        jobj = serverConnection(webMethName);
        return jobj;
    }
    // GlobalSales
    public static JSONArray getR2(String BranchNo, String FromDate, String ToDate,
                                  String webMethName) {
        Log.w("Web Service", "Form Name : " + webMethName);
        Log.w("Web Service", "BranchNo : " + BranchNo);
        Log.w("Web Service", "FromDate : " + FromDate);
        Log.w("Web Service", "ToDate : " + ToDate);
        // Create request
        SoapObject request = new SoapObject(NAMESPACE, webMethName);
        // Property which holds input parameters
        request.addProperty("BranchNo", BranchNo);
        request.addProperty("FromDate", FromDate);
        request.addProperty("ToDate", ToDate);
        // Create envelope
        envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        jobj = new JSONArray();
        jobj = serverConnection(webMethName);
        return jobj;
    }
    // FollowUpList
    public static JSONArray getFollowUp(String BranchNo, String search, String FromDate, String ToDate,
                                        String webMethName) {
        Log.w("Web Service", "Form Name : " + webMethName);
        Log.w("Web Service", "BranchNo : " + BranchNo);
        Log.w("Web Service", "Search : " + search);
        Log.w("Web Service", "FromDate : " + FromDate);
        Log.w("Web Service", "ToDate : " + ToDate);
        // Create request
        SoapObject request = new SoapObject(NAMESPACE, webMethName);
        // Property which holds input parameters
        request.addProperty("BranchNo", BranchNo);
        request.addProperty("Search", search);
        request.addProperty("FromDate", FromDate);
        request.addProperty("ToDate", ToDate);
        // Create envelope
        envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        jobj = new JSONArray();
        jobj = serverConnection(webMethName);
        return jobj;
    }
    // getHistory
    public static JSONArray getHistory(String BranchNo, String MemberNo,
                                       String webMethName) {
        Log.w("Web Service", "Form Name : " + webMethName);
        Log.w("Web Service", "BranchNo : " + BranchNo);
        Log.w("Web Service", "MemberNo : " + MemberNo);
        // Create request
        SoapObject request = new SoapObject(NAMESPACE, webMethName);
        // Property which holds input parameters
        request.addProperty("BranchNo", BranchNo);
        request.addProperty("MemberNo", MemberNo);
        // Create envelope
        envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        jobj = new JSONArray();
        jobj = serverConnection(webMethName);
        return jobj;
    }
    public static JSONArray updateMember(String userName, String MemberNo, String BranchNo, String Data, String Image,
                                         String webMethName) {
        String encodedString = Image;
        Log.w("Web Service", "Form Name : " + webMethName);
        Log.w("Web Service", "BranchNo : " + BranchNo);
        Log.w("Web Service", "Data : " + Data);
        Log.w("Web Service", "UserName : " + userName);
        Log.w("Web Service", "MemberNoa : " + MemberNo);
        Log.w("Web Service", "imagePath : " + Image);
        // Create request
        SoapObject request = new SoapObject(NAMESPACE, webMethName);
        // Property which holds input parameters
        request.addProperty("UserName", userName);
        request.addProperty("MemberNo", MemberNo);
        request.addProperty("BranchNo", BranchNo);
        request.addProperty("Data", Data);
        request.addProperty("Image", encodedString);
        // Create envelope
        envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        jobj = new JSONArray();
        jobj = serverConnection(webMethName);
        return jobj;
    }
    // saveR2
    public static JSONArray saveR2(String BranchNo, String Data,
                                   String webMethName) {
        Log.w("Web Service", "Form Name : " + webMethName);
        Log.w("Web Service", "BranchNo : " + BranchNo);
        Log.w("Web Service", "Data : " + Data);
        // Create request
        SoapObject request = new SoapObject(NAMESPACE, webMethName);
        // Property which holds input parameters
        request.addProperty("BranchNo", BranchNo);
        request.addProperty("Data", Data);
        // Create envelope
        envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        jobj = new JSONArray();
        jobj = serverConnection(webMethName);
        return jobj;
    }
    public static JSONArray getMemberInfo(String BranchNo, String memberNo,
                                          String webMethName) {
        Log.w("Web Service", "Form Name : " + webMethName);
        Log.w("Web Service", "BranchNo : " + BranchNo);
        Log.w("Web Service", "memberNo : " + memberNo);
        // Create request
        SoapObject request = new SoapObject(NAMESPACE, webMethName);
        // Property which holds input parameters
        request.addProperty("BranchNo", BranchNo);
        request.addProperty("MemberNo", memberNo);
        // Create envelope
        envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        jobj = new JSONArray();
        jobj = serverConnection(webMethName);
        return jobj;
    }
    // offlineSync
    public static JSONArray offlineSync(String Data,
                                        String webMethName) {
        Log.w("Web Service", "Form Name : " + webMethName);
        Log.w("Web Service", "Data : " + Data);
        // Create request
        SoapObject request = new SoapObject(NAMESPACE, webMethName);
        // Property which holds input parameters
        request.addProperty("Data", Data);
        // Create envelope
        envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        jobj = new JSONArray();
        jobj = serverConnection(webMethName);
        return jobj;
    }
    // getTemplateDetails
    public static JSONArray getTemplateDetails(String BranchNo, String TempName, String webMethName) {
        Log.w("Web Service", "Form Name : " + webMethName);
        Log.w("Web Service", "BranchNo : " + BranchNo);
        Log.w("Web Service", "TempName : " + TempName);
        // Create request
        SoapObject request = new SoapObject(NAMESPACE, webMethName);
        // Property which holds input parameters
        request.addProperty("BranchNo", BranchNo);
        request.addProperty("TempName", TempName);
        // Create envelope
        envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        jobj = new JSONArray();
        jobj = serverConnection(webMethName);
        return jobj;
    }
}
