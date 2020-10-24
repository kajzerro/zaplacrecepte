package com.hastlin.zaplacrecepte.utils;


import com.hastlin.zaplacrecepte.model.entity.UserEntity;
import org.springframework.security.core.context.SecurityContextHolder;

public class FeatureToggleUtil {
    private static final String SERVICE_BASED = "SERVICE_BASED";
    private static final String PRESCRIPTION_BASED = "PRESCRIPTION_BASED";

    public static boolean isServiceBased() {
        return SERVICE_BASED.equals(((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getClientType());
    }

    public static boolean isPrescriptionBased() {
        return PRESCRIPTION_BASED.equals(((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getClientType());
    }
}
