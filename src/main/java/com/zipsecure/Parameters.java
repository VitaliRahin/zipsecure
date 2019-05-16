package com.zipsecure;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public final class Parameters {
    private String emailAddress;
    private int sizeOfSplitArchive;

    //add timers as param
}
