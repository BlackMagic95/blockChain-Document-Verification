package com.verify.security;

import java.util.List;

public enum Role {
    SUPER_ADMIN(List.of(Permission.DOC_UPLOAD, Permission.DOC_READ, Permission.DOC_STATS, Permission.ACCESS_APPROVE)),
    COLLEGE_ADMIN(List.of(Permission.DOC_UPLOAD, Permission.DOC_READ, Permission.DOC_STATS));

    private final List<Permission> permissions;

    Role(List<Permission> permissions) {
        this.permissions = permissions;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }
}