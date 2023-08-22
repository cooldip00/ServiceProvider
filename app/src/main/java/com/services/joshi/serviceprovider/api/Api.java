package com.services.joshi.serviceprovider.api;

import com.services.joshi.serviceprovider.models.Category;
import com.services.joshi.serviceprovider.models.Provider;
import com.services.joshi.serviceprovider.models.RatingUser;
import com.services.joshi.serviceprovider.models.Services;
import com.services.joshi.serviceprovider.repository.CategoryResponse;
import com.services.joshi.serviceprovider.repository.DefaultResponse;
import com.services.joshi.serviceprovider.repository.GaneratedBillResponse;
import com.services.joshi.serviceprovider.repository.HistoryResponse;
import com.services.joshi.serviceprovider.repository.LastPendingPaymentResponse;
import com.services.joshi.serviceprovider.repository.RatingUserResponse;
import com.services.joshi.serviceprovider.repository.RegisterResponse;
import com.services.joshi.serviceprovider.repository.LoginResponse;
import com.services.joshi.serviceprovider.repository.RequestServiceResponse;
import com.services.joshi.serviceprovider.repository.SPHistoryResponse;
import com.services.joshi.serviceprovider.repository.SPLoginResponse;
import com.services.joshi.serviceprovider.repository.SPRequestServiceResponse;
import com.services.joshi.serviceprovider.repository.SPSignUpResponse;
import com.services.joshi.serviceprovider.repository.SearchProviderResponse;
import com.services.joshi.serviceprovider.repository.ServiceAcceptResponse;
import com.services.joshi.serviceprovider.repository.ServiceRejectResponse;
import com.services.joshi.serviceprovider.repository.SpProfileUpdateResponse;
import com.services.joshi.serviceprovider.repository.StatusResponse;
import com.services.joshi.serviceprovider.repository.UserProfileUpdateResponse;

import java.util.List;

import androidx.cardview.widget.CardView;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface Api {

    @FormUrlEncoded
    @POST("user/register")
    Call<RegisterResponse> createUser(
            @Field("fname") String fname,
            @Field("lname") String lname,
            @Field("email") String email,
            @Field("password") String password,
            @Field("phone") String phone,
            @Field("address") String address,
            @Field("gender") String gender
    );

    @FormUrlEncoded
    @POST("user/login")
    Call<LoginResponse> userLogin(
            @Field("email") String email,
            @Field("password") String password
    );

    @GET("getallprovider")
    Call<CategoryResponse> getAllProviders();

    @GET("user/show-requestlist/{id}")
    Call<RequestServiceResponse> getRequestListProvider(@Path("id") int id);

    @GET("getallcetegory")
    Call<List<Category>> getCategory();

    @GET("user/history/{id}")
    Call<List<Services>> getHistory(@Path("id") int id);

    @FormUrlEncoded
    @POST("provider/register")
    Call<SPSignUpResponse> createProvider(
            @Field("fname") String fname,
            @Field("mname") String mname,
            @Field("lname") String lname,
            @Field("email") String email,
            @Field("password") String password,
            @Field("gender") String gender,
            @Field("phone") String phone,
            @Field("address") String address,
            @Field("category") String category,
            @Field("user_image") String user_image,
            @Field("aadhar_image") String aadhar_image,
            @Field("latitude") String latitude,
            @Field("longtitude") String longtitide
    );

    @FormUrlEncoded
    @PUT("user/updatePassword/{id}")
    Call<RegisterResponse> updatePassword(
            @Path("id") int id,
            @Field("oldpassword") String oldpassword,
            @Field("cpassword") String cpassword,
            @Field("address") String newpassword
    );

    @FormUrlEncoded
    @PUT("user/update/{id}")
    Call<LoginResponse> updateUser(
            @Path("id") int id,
            @Field("fname") String fname,
            @Field("lname") String lname,
            @Field("address") String address,
            @Field("gender") String gender
    );

    @FormUrlEncoded
    @POST("provider/login")
    Call<SPLoginResponse> providerLogin(
            @Field("email") String email,
            @Field("password") String password
    );

    @GET("provider/history/{id}")
    Call<SPHistoryResponse> getProviderHistory(@Path("id") int id);

    @GET("provider/getservicerequest/{id}")
    Call<SPRequestServiceResponse> getProviderServiceRequest(@Path("id") int id);

    @GET("provider/reject-request/{id}")
    Call<ServiceRejectResponse> rejectService(@Path("id") int id);

    @GET("provider/accept-request/{id}")
    Call<ServiceAcceptResponse> acceptService(@Path("id") int id);

    @GET("provider/complete-request/{id}")
    Call<ServiceAcceptResponse> completeService(@Path("id") int id);

    @FormUrlEncoded
    @PUT("user/updateImage/{id}")
    Call<UserProfileUpdateResponse> updateProfileImage(
            @Path("id") int id,
            @Field("user_image") String userImage
    );

    @FormUrlEncoded
    @PUT("user/update/{id}")
    Call<UserProfileUpdateResponse> updateUserData(
            @Path("id") int id,
            @Field("fname") String fname,
            @Field("lname") String lname,
            @Field("address") String address
    );

    @FormUrlEncoded
    @PUT("provider/updateImage/{id}")
    Call<SpProfileUpdateResponse> updateProviderProfileImage(
            @Path("id") int id,
            @Field("user_image") String userImage
    );

    @FormUrlEncoded
    @PUT("provider/update/{id}")
    Call<SpProfileUpdateResponse> updateProviderData(
            @Path("id") int id,
            @Field("fname") String fname,
            @Field("mname") String mname,
            @Field("lname") String lname,
            @Field("address") String address,
            @Field("latitude") String latitude,
            @Field("longtitude") String longtitide
    );

    @FormUrlEncoded
    @PUT("provider/updatePassword/{id}")
    Call<DefaultResponse> changeProviderPassword(
            @Path("id") int id,
            @Field("oldpassword") String fname,
            @Field("newpassword") String mname,
            @Field("cpassword") String lname
    );


    @FormUrlEncoded
    @PUT("user/updatePassword/{id}")
    Call<DefaultResponse> changeUserPassword(
            @Path("id") int id,
            @Field("oldpassword") String fname,
            @Field("newpassword") String mname,
            @Field("cpassword") String lname
    );

    @GET("provider/delete/{id}")
    Call<DefaultResponse> deleteProvider(
            @Path("id") int id
    );

    @GET("user/delete/{id}")
    Call<DefaultResponse> deleteUser(
            @Path("id") int id
    );

    @GET("get-nearby-provider/{latitude}/{longtitude}")
    Call<SearchProviderResponse> getNearbyProvider(
            @Path("latitude") double latitude,
            @Path("longtitude") double longtitude
    );

    @GET("search-provider/{keyword}")
    Call<SearchProviderResponse> getSearchedProvider(
            @Path("keyword") String keyword
    );

    @GET("user/addrequestservice/{user_id}/{provider_id}")
    Call<DefaultResponse> addServiceRequest(
            @Path("user_id") int user_id,
            @Path("provider_id") int provider_id
    );

    @GET("user/cancelrequestservice/{user_id}/{provider_id}")
    Call<DefaultResponse> cancelServiceRequest(
            @Path("user_id") int user_id,
            @Path("provider_id") int provider_id
    );

    @GET("user/checkrequestservice/{user_id}/{provider_id}")
    Call<DefaultResponse> checkServiceRequest(
            @Path("user_id") int user_id,
            @Path("provider_id") int provider_id
    );

    @FormUrlEncoded
    @POST("user/rate-provider")
    Call<DefaultResponse> rateProvider(
            @Field("user_id") int user_id,
            @Field("provider_id") int provider_id,
            @Field("rating") String rating,
            @Field("comment") String comment
    );

    @GET("provider-comment-and-rating/{id}")
    Call<RatingUserResponse> getComments(
            @Path("id") int id
    );

    @FormUrlEncoded
    @POST("provider/generate-bill")
    Call<DefaultResponse> generateBill(
            @Field("service_id") int service_id,
            @Field("description") String description,
            @Field("amount") int amount
    );

    @GET("get-generated-bill/{user_id}/{provider_id}")
    Call<GaneratedBillResponse> getBill(
            @Path("user_id") int user_id,
            @Path("provider_id") int provider_id
    );

    @GET("make-payment/{user_id}/{provider_id}")
    Call<DefaultResponse> makePaymentDone(
            @Path("user_id") int user_id,
            @Path("provider_id") int provider_id
    );

    @GET("get-last-pending-bill/{user_id}")
    Call<LastPendingPaymentResponse> getLastPanddingPaymnetDetail(
            @Path("user_id") int user_id
    );

    @GET("check-pending-bill/{user_id}")
    Call<StatusResponse> checkPendingPaymnet(
            @Path("user_id") int user_id
    );

    //For Payment--------------------------------------------------------------------------------
    @FormUrlEncoded
    @POST("checkout.php")
    Call<String> payment(
            @Field("nonce") String nonce,
            @Field("amount") String amount
    );

}
