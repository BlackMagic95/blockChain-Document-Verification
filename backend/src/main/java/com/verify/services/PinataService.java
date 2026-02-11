package com.verify.services;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import okhttp3.*;
import com.google.gson.*;

@Service
public class PinataService {

        @Value("${pinata.jwt}")
        private String jwt;

        private final OkHttpClient client = new OkHttpClient();

        /* ================= NORMAL UPLOAD ================= */

        public String upload(MultipartFile file) throws Exception {

                return uploadBytes(file.getBytes());
        }

        /* ================= ENCRYPTED UPLOAD ================= */

        public String uploadBytes(byte[] data) throws Exception {

                RequestBody fileBody = RequestBody.create(data);

                MultipartBody body = new MultipartBody.Builder()
                                .setType(MultipartBody.FORM)
                                .addFormDataPart("file", "encrypted.bin", fileBody)
                                .build();

                Request request = new Request.Builder()
                                .url("https://api.pinata.cloud/pinning/pinFileToIPFS")
                                .addHeader("Authorization", "Bearer " + jwt)
                                .post(body)
                                .build();

                Response response = client.newCall(request).execute();

                String res = response.body().string();

                return JsonParser.parseString(res)
                                .getAsJsonObject()
                                .get("IpfsHash")
                                .getAsString();
        }
}
