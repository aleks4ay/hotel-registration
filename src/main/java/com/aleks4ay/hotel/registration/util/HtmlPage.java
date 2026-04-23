package com.aleks4ay.hotel.registration.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum HtmlPage {

    DEFAULT(""),
    HOME("home"),
    CSS("css"),
    REALMS("realms"),
    PROFILE("profile"),
    ADMIN("admin"),
    DASHBOARD("dashboard"),
    LOGIN("login"),
    LOGOUT("logout"),
    REGISTER("register");

    private final String pageName;

    public String asPath() {
        return "/%s".formatted(this.getPageName());
    }

    public String asAllPaths() {
        return "/%s/**".formatted(this.getPageName());
    }
}
