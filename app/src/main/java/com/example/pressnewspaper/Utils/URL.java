package com.example.pressnewspaper.Utils;

import android.content.Context;
import android.net.ConnectivityManager;

import java.net.InetAddress;

/**
 * Created by Altayeb on 9/25/2018.
 */
public class URL {

    public static String ROOT_URL = "http://nicehome-realestate.com/code/";
    //public static String ROOT_URL = "http://altayeb20182018.eu5.org/code/";
    //public static String ROOT_URL = "http://192.168.43.209/code/";

    public static String REG_URL = ROOT_URL + "registration.php";
    public static String LOGIN_URL = ROOT_URL + "login.php";
    public static String ADD_PRO_URL = ROOT_URL + "add_pro.php";
    public static String GET_PRO_URL = ROOT_URL + "get_pro.php";
    public static String IMAGE_PATH = ROOT_URL + "../en/uploads/";
    public static String GET_PRO_DETAILS = ROOT_URL + "get_pro_details.php";
    public static String MY_PRO = ROOT_URL + "my_pro.php";
    public static String GET_MY_ADDED_PRO = ROOT_URL + "get_my_added_pro.php";
    public static String UPDATE_MY_PRO = ROOT_URL + "update_my_pro.php";

    public static String GET_CITIES = ROOT_URL + "get_cities.php";
    public static String GET_AREAS = ROOT_URL + "get_areas.php";

    public static String DELETE_POST_URL = ROOT_URL + "delete_row.php";
    //public static String SEARCH_URL = ROOT_URL + "get_search.php";
    public static String SEARCH_URL = ROOT_URL + "search2.php";

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    public static boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("www.google.com");//You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
    }

}
