package com.cats.android.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by andrey on 15.02.17.
 */
@Parcel
public class AccessToken {

    @SerializedName("expires_in")
    private Long expiresIn;

    private Long expiresAt;

    @SerializedName("token_type")
    private String tokenType;

    @SerializedName("access_token")
    private String accessToken;

    public AccessToken() {
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public Long getExpiresAt() {
        if (expiresAt == null) {
            this.expiresAt = (expiresIn * 1000) + System.currentTimeMillis();
        }
        return expiresAt;
    }

    public String getTokenType() {
        // OAuth requires uppercase Authorization HTTP header value for getAccessToken type
        if (!Character.isUpperCase(tokenType.charAt(0))) {
            tokenType =
                    Character
                            .toString(tokenType.charAt(0))
                            .toUpperCase() + tokenType.substring(1);
        }

        return tokenType;
    }


    public String getAccessToken() {
        return accessToken;
    }

    public boolean isExpired() {
        return (System.currentTimeMillis() >= this.getExpiresAt()) ? true : false;
    }

    @Override
    public String toString() {
        return "AccessToken{" +
                "expiresIn=" + expiresIn +
                ", expiresAt=" + expiresAt +
                ", tokenType='" + tokenType + '\'' +
                ", accessToken='" + accessToken + '\'' +
                '}';
    }
}
