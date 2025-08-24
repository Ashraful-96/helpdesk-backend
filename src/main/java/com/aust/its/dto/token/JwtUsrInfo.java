package com.aust.its.dto.token;

public record JwtUsrInfo(
    String usrId,
    String role
) {

    public static JwtUsrInfo of(String usrId, String role) {
        return new JwtUsrInfo(usrId, role);
    }
}
