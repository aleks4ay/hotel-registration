package com.aleks4ay.hotel.registration.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum HtmlPage {

    REGISTER("register");

    private final String pageName;
}
