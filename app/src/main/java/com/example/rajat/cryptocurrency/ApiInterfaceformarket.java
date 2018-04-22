package com.example.rajat.cryptocurrency;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by chivu on 23/3/18.
 */

public interface ApiInterfaceformarket {

    @GET("data/pricemultifull?fsyms=BTC,ETH,XRP,BCH,LTC,ADA,NEO,XLM,EOS,XMR,DASH,XEM,MIOTA,UDST,TRX,VEN,ETC,LSK,XRB,QTUM,OMG,BTG,BNB,ICX,ZEC&tsyms=USD")
    Call<ResponseBody> getJSON();
}
