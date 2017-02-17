package com.cats.android.model;

/**
 * Created by andrey on 15.02.17.
 */

public class AccessToken {

    private Long expiresIn;
    private Long expiresAt;
    private String tokenType;
    private String accessToken;

    public AccessToken(Long expiresIn, String tokenType, String accessToken) {
        this.expiresIn = expiresIn;
        this.tokenType = tokenType;
        this.accessToken = accessToken;
        this.expiresAt = (expiresIn * 1000) + System.currentTimeMillis();
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public Long getExpiresAt() {
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
