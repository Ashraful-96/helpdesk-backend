package com.aust.its.dto.token;

public record JwtUsrInfo(
    String username,
    long usrId,
    String role,
    String adminUsrId
) {

    public static JwtUsrInfo of(String username,
                                long usrId,
                                String role) {

        return new JwtUsrInfo(username, usrId, role,null);
    }

    public static JwtUsrInfo withAdminUsrId(String username,
                                            long usrId,
                                            String role,
                                            String adminUsrId) {

        return new JwtUsrInfo(username, usrId, role, adminUsrId);
    }
}
