package com.phildev.front.mls.utils;

import java.security.Principal;

public class PrincipalTest implements Principal {
    @Override
    public String getName() {
        return "test@mls.com";
    }
}
