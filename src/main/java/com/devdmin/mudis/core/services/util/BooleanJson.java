package com.devdmin.mudis.core.services.util;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

public class BooleanJson {
    @JsonProperty("isRated")
    private boolean isTrue;

    @JsonCreator
    public BooleanJson(@JsonProperty("isTrue") boolean isTrue) {
        this.isTrue = isTrue;
    }
    @JsonProperty("thumbUp")
    public boolean isTrue() {
        return isTrue;
    }

    public void setTrue(boolean aTrue) {
        isTrue = aTrue;
    }
}
