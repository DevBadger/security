package com.devbadger.model;

public class AuthUser {
    private String sessionId;
    private String sourceId;
    private String userId;

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getSourceId() {
        return sourceId;
    }

    public String getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "AuthUser{" +
                "sessionId='" + sessionId + '\'' +
                ", sourceId='" + sourceId + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AuthUser user = (AuthUser) o;

        if (sessionId != null ? !sessionId.equals(user.sessionId) : user.sessionId != null) return false;
        if (sourceId != null ? !sourceId.equals(user.sourceId) : user.sourceId != null) return false;
        return userId != null ? userId.equals(user.userId) : user.userId == null;
    }

    @Override
    public int hashCode() {
        int result = sessionId != null ? sessionId.hashCode() : 0;
        result = 31 * result + (sourceId != null ? sourceId.hashCode() : 0);
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        return result;
    }
}