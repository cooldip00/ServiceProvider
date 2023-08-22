package com.services.joshi.serviceprovider.api;

public class Common {

    public static final String BASE_URL = /*"http://jbrother.cf/braintree/"*/ "http://192.168.42.91/braintree/";
//    public static final String BASE_URL = /*"http://jbrother.cf/braintree/"*/ "http://192.168.43.56/braintree/";
    public static final String API_TOKEN_URL = /*"http://jbrother.cf/braintree/main.php"*/"http://192.168.42.91/braintree/main.php";
//    public static final String API_TOKEN_URL = /*"http://jbrother.cf/braintree/main.php"*/"http://192.168.43.56/braintree/main.php";
    public static Api getScalarsApi(){
        return RetrofitScalarsClient.getScalarsClient(BASE_URL).create(Api.class);
    }
}
